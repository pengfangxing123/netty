package com.netty.redis;

import com.google.common.annotations.Beta;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Preconditions;
import com.google.common.base.Ticker;
import com.google.common.util.concurrent.Uninterruptibles;

import java.util.concurrent.TimeUnit;


/**
 * 把谷歌的令牌桶算法搞出来分析
 */
@Beta
public abstract class GuaRateLimter {


    public static GuaRateLimter create(double permitsPerSecond) {
        return create(SleepingTicker.SYSTEM_TICKER, permitsPerSecond);
    }

    /**
     *
     * @param ticker
     * @param permitsPerSecond  指定每秒钟可以产生多少个 permits
     * @return
     */
    @VisibleForTesting
    static GuaRateLimter create(SleepingTicker ticker, double permitsPerSecond) {
        GuaRateLimter rateLimiter = new Bursty(ticker);
        rateLimiter.setRate(permitsPerSecond);
        return rateLimiter;
    }


    public static GuaRateLimter create(double permitsPerSecond, long warmupPeriod, TimeUnit unit) {
        return create(SleepingTicker.SYSTEM_TICKER, permitsPerSecond, warmupPeriod, unit);
    }

    @VisibleForTesting
    static GuaRateLimter create(
            SleepingTicker ticker, double permitsPerSecond, long warmupPeriod, TimeUnit timeUnit) {
        GuaRateLimter rateLimiter = new WarmingUp(ticker, warmupPeriod, timeUnit);
        rateLimiter.setRate(permitsPerSecond);
        return rateLimiter;
    }

    @VisibleForTesting
    static GuaRateLimter createBursty(
            SleepingTicker ticker, double permitsPerSecond, int maxBurstSize) {
        Bursty rateLimiter = new Bursty(ticker);
        rateLimiter.setRate(permitsPerSecond);
        rateLimiter.maxPermits = maxBurstSize;
        return rateLimiter;
    }

    /**
     * 用来睡眠得ticker
     * The underlying timer; used both to measure elapsed time and sleep as necessary. A separate
     * object to facilitate testing.
     */
    private final SleepingTicker ticker;

    /**
     * 时间偏移量
     * The timestamp when the GuaRateLimter was created; used to avoid possible overflow/time-wrapping
     * errors.
     */
    private final long offsetNanos;

    /**
     * 当前还有多少 permits 没有被使用，被存下来的 permits 数量
     * The currently stored permits.
     */
    double storedPermits;

    /**
     * 最大允许缓存的 permits 数量，也就是 storedPermits 能达到的最大值
     * The maximum number of stored permits.
     */
    double maxPermits;

    /**
     * 每隔多少时间产生一个 permit，
     * 比如我们构造方法中设置每秒 5 个，也就是每隔 200ms 一个，这里单位是微秒，也就是 200,000
     * The interval between two unit requests, at our stable rate. E.g., a stable rate of 5 permits
     * per second has a stable interval of 200ms.
     */
    volatile double stableIntervalMicros;

    private final Object mutex = new Object();

    /**
     * 下一次可以获取 permits 的时间，这个时间是相对 RateLimiter 的构造时间的，是一个相对时间，理解为时间戳吧
     * The time when the next request (no matter its size) will be granted. After granting a request,
     * this is pushed further in the future. Large requests push this further than small requests.
     */
    private long nextFreeTicketMicros = 0L; // could be either in the past or future

    private GuaRateLimter(SleepingTicker ticker) {
        this.ticker = ticker;
        this.offsetNanos = ticker.read();
    }

    /**
     * 用来调整，每秒生成permits的速率
     * Updates the stable rate of this {@code GuaRateLimter}, that is, the
     * {@code permitsPerSecond} argument provided in the factory method that
     * constructed the {@code GuaRateLimter}. Currently throttled threads will <b>not</b>
     * be awakened as a result of this invocation, thus they do not observe the new rate;
     * only subsequent requests will.
     *
     * <p>Note though that, since each request repays (by waiting, if necessary) the cost
     * of the <i>previous</i> request, this means that the very next request
     * after an invocation to {@code setRate} will not be affected by the new rate;
     * it will pay the cost of the previous request, which is in terms of the previous rate.
     *
     * <p>The behavior of the {@code GuaRateLimter} is not modified in any other way,
     * e.g. if the {@code GuaRateLimter} was configured with a warmup period of 20 seconds,
     * it still has a warmup period of 20 seconds after this method invocation.
     *
     * @param permitsPerSecond the new stable rate of this {@code GuaRateLimter}.
     */
    public final void setRate(double permitsPerSecond) {
        Preconditions.checkArgument(permitsPerSecond > 0.0
                && !Double.isNaN(permitsPerSecond), "rate must be positive");
        synchronized (mutex) {
            //重 同步
            resync(readSafeMicros());
            double stableIntervalMicros = TimeUnit.SECONDS.toMicros(1L) / permitsPerSecond;
            this.stableIntervalMicros = stableIntervalMicros;
            doSetRate(permitsPerSecond, stableIntervalMicros);
        }
    }

    abstract void doSetRate(double permitsPerSecond, double stableIntervalMicros);

    /**
     * Returns the stable rate (as {@code permits per seconds}) with which this
     * {@code GuaRateLimter} is configured with. The initial value of this is the same as
     * the {@code permitsPerSecond} argument passed in the factory method that produced
     * this {@code GuaRateLimter}, and it is only updated after invocations
     * to {@linkplain #setRate}.
     */
    public final double getRate() {
        return TimeUnit.SECONDS.toMicros(1L) / stableIntervalMicros;
    }

    /**
     * Acquires a permit from this {@code GuaRateLimter}, blocking until the request can be granted.
     *
     * <p>This method is equivalent to {@code acquire(1)}.
     */
    public void acquire() {
        acquire(1);
    }

    /**
     * 获取许可证的给定数目的从此GuaRateLimter ，阻塞直到该请求被准许
     * Acquires the given number of permits from this {@code GuaRateLimter}, blocking until the
     * request be granted.
     *
     * @param permits the number of permits to acquire
     */
    public void acquire(int permits) {
        //检查permits是否 >0
        checkPermits(permits);
        long microsToWait;
        synchronized (mutex) {
            // 如果 “下次可用时间” 大于当前时间，则需要等待
            microsToWait = reserveNextTicket(permits, readSafeMicros());
        }
        ticker.sleepMicrosUninterruptibly(microsToWait);
    }

    /**
     * Acquires a permit from this {@code GuaRateLimter} if it can be obtained
     * without exceeding the specified {@code timeout}, or returns {@code false}
     * immediately (without waiting) if the permit would not have been granted
     * before the timeout expired.
     *
     * <p>This method is equivalent to {@code tryAcquire(1, timeout, unit)}.
     *
     * @param timeout the maximum time to wait for the permit
     * @param unit the time unit of the timeout argument
     * @return {@code true} if the permit was acquired, {@code false} otherwise
     */
    public boolean tryAcquire(long timeout, TimeUnit unit) {
        return tryAcquire(1, timeout, unit);
    }

    /**
     * Acquires permits from this {@link GuaRateLimter} if it can be acquired immediately without delay.
     *
     * <p>
     * This method is equivalent to {@code tryAcquire(permits, 0, anyUnit)}.
     *
     * @param permits the number of permits to acquire
     * @return {@code true} if the permits were acquired, {@code false} otherwise
     * @since 14.0
     */
    public boolean tryAcquire(int permits) {
        return tryAcquire(permits, 0, TimeUnit.MICROSECONDS);
    }

    /**
     * Acquires a permit from this {@link GuaRateLimter} if it can be acquired immediately without
     * delay.
     *
     * <p>
     * This method is equivalent to {@code tryAcquire(1)}.
     *
     * @return {@code true} if the permit was acquired, {@code false} otherwise
     * @since 14.0
     */
    public boolean tryAcquire() {
        return tryAcquire(1, 0, TimeUnit.MICROSECONDS);
    }

    /**
     * Acquires the given number of permits from this {@code GuaRateLimter} if it can be obtained
     * without exceeding the specified {@code timeout}, or returns {@code false}
     * immediately (without waiting) if the permits would not have been granted
     * before the timeout expired.
     *
     * @param permits the number of permits to acquire
     * @param timeout the maximum time to wait for the permits
     * @param unit the time unit of the timeout argument
     * @return {@code true} if the permits were acquired, {@code false} otherwise
     */
    public boolean tryAcquire(int permits, long timeout, TimeUnit unit) {
        long timeoutMicros = unit.toMicros(timeout);
        checkPermits(permits);
        long microsToWait;
        synchronized (mutex) {
            long nowMicros = readSafeMicros();
            if (nextFreeTicketMicros > nowMicros + timeoutMicros) {
                return false;
            } else {
                microsToWait = reserveNextTicket(permits, nowMicros);
            }
        }
        ticker.sleepMicrosUninterruptibly(microsToWait);
        return true;
    }

    private static void checkPermits(int permits) {
        Preconditions.checkArgument(permits > 0, "Requested permits must be positive");
    }

    /**
     * Reserves next ticket and returns the wait time that the caller must wait for.
     */
    private long reserveNextTicket(double requiredPermits, long nowMicros) {
        // 如果“下次可用时间”小于当前时间，则将它更新到当前时间；同时，“可用令牌数”根据时间增长而增长，最多不超过最大令牌数
        resync(nowMicros);
        // 需要等待的时间即“下次可用时间”减去“当前时间”
        long microsToNextFreeTicket = nextFreeTicketMicros - nowMicros;

        //这里得到freshPermits->超发的令牌数量

        //获取当前有的令牌和需要的令牌的最小值，然后用需要的令牌数减去最小值
        //如果requiredPermits>this.storedPermits ->需要超发得数量
        //如果requiredPermits<=this.storedPermits ->数据是0
        double storedPermitsToSpend = Math.min(requiredPermits, this.storedPermits);
        double freshPermits = requiredPermits - storedPermitsToSpend;

        // 如果最新的“可用令牌数”小于requiredPermits，则可能需要预支一定量的时间，这也将导致“下次可用时间”的推进
        long waitMicros = storedPermitsToWaitTime(this.storedPermits, storedPermitsToSpend)
                + (long) (freshPermits * stableIntervalMicros);

        this.nextFreeTicketMicros = nextFreeTicketMicros + waitMicros;
        this.storedPermits -= storedPermitsToSpend;
        return microsToNextFreeTicket;
    }

    /**
     * Translates a specified portion of our currently stored permits which we want to
     * spend/acquire, into a throttling time. Conceptually, this evaluates the integral
     * of the underlying function we use, for the range of
     * [(storedPermits - permitsToTake), storedPermits].
     *
     * This always holds: {@code 0 <= permitsToTake <= storedPermits}
     */
    abstract long storedPermitsToWaitTime(double storedPermits, double permitsToTake);


    private void resync(long nowMicros) {
        // 如果 nextFreeTicket 已经过掉了，想象一下很长时间都没有再次调用 limiter.acquire() 的场景
        // 需要将 nextFreeTicket 设置为当前时间，重新计算 storedPermits
        // if nextFreeTicket is in the past, resync to now
        if (nowMicros > nextFreeTicketMicros) {
            storedPermits = Math.min(maxPermits,
                    storedPermits + (nowMicros - nextFreeTicketMicros) / stableIntervalMicros);
            //将下次可用时间。改为当前时间。(中间的插值)
            nextFreeTicketMicros = nowMicros;
        }
    }

    private long readSafeMicros() {
        return TimeUnit.NANOSECONDS.toMicros(ticker.read() - offsetNanos);
    }

    @Override
    public String toString() {
        return String.format("GuaRateLimter[stableRate=%3.1fqps]", 1000000.0 / stableIntervalMicros);
    }

    /**
     * 渐距模式
     * This implements the following function:
     *
     *          ^ throttling
     *          |
     * 3*stable +                  /
     * interval |                 /.
     *  (cold)  |                / .
     *          |               /  .   <-- "warmup period" is the area of the trapezoid between
     * 2*stable +              /   .       halfPermits and maxPermits
     * interval |             /    .
     *          |            /     .
     *          |           /      .
     *   stable +----------/  WARM . }
     * interval |          .   UP  . } <-- this rectangle (from 0 to maxPermits, and
     *          |          . PERIOD. }     height == stableInterval) defines the cooldown period,
     *          |          .       . }     and we want cooldownPeriod == warmupPeriod
     *          |---------------------------------> storedPermits
     *              (halfPermits) (maxPermits)
     *
     * Before going into the details of this particular function, let's keep in mind the basics:
     * 1) The state of the GuaRateLimter (storedPermits) is a vertical line in this figure.
     * 2) When the GuaRateLimter is not used, this goes right (up to maxPermits)
     * 3) When the GuaRateLimter is used, this goes left (down to zero), since if we have storedPermits,
     *    we serve from those first
     * 4) When _unused_, we go right at the same speed (rate)! I.e., if our rate is
     *    2 permits per second, and 3 unused seconds pass, we will always save 6 permits
     *    (no matter what our initial position was), up to maxPermits.
     *    If we invert the rate, we get the "stableInterval" (interval between two requests
     *    in a perfectly spaced out sequence of requests of the given rate). Thus, if you
     *    want to see "how much time it will take to go from X storedPermits to X+K storedPermits?",
     *    the answer is always stableInterval * K. In the same example, for 2 permits per second,
     *    stableInterval is 500ms. Thus to go from X storedPermits to X+6 storedPermits, we
     *    require 6 * 500ms = 3 seconds.
     *
     *    In short, the time it takes to move to the right (save K permits) is equal to the
     *    rectangle of width == K and height == stableInterval.
     * 4) When _used_, the time it takes, as explained in the introductory class note, is
     *    equal to the integral of our function, between X permits and X-K permits, assuming
     *    we want to spend K saved permits.
     *
     *    In summary, the time it takes to move to the left (spend K permits), is equal to the
     *    area of the function of width == K.
     *
     * Let's dive into this function now:
     *
     * When we have storedPermits <= halfPermits (the left portion of the function), then
     * we spend them at the exact same rate that
     * fresh permits would be generated anyway (that rate is 1/stableInterval). We size
     * this area to be equal to _half_ the specified warmup period. Why we need this?
     * And why half? We'll explain shortly below (after explaining the second part).
     *
     * Stored permits that are beyond halfPermits, are mapped to an ascending line, that goes
     * from stableInterval to 3 * stableInterval. The average height for that part is
     * 2 * stableInterval, and is sized appropriately to have an area _equal_ to the
     * specified warmup period. Thus, by point (4) above, it takes "warmupPeriod" amount of time
     * to go from maxPermits to halfPermits.
     *
     * BUT, by point (3) above, it only takes "warmupPeriod / 2" amount of time to return back
     * to maxPermits, from halfPermits! (Because the trapezoid has double the area of the rectangle
     * of height stableInterval and equivalent width). We decided that the "cooldown period"
     * time should be equivalent to "warmup period", thus a fully saturated GuaRateLimter
     * (with zero stored permits, serving only fresh ones) can go to a fully unsaturated
     * (with storedPermits == maxPermits) in the same amount of time it takes for a fully
     * unsaturated GuaRateLimter to return to the stableInterval -- which happens in halfPermits,
     * since beyond that point, we use a horizontal line of "stableInterval" height, simulating
     * the regular rate.
     *
     * Thus, we have figured all dimensions of this shape, to give all the desired
     * properties:
     * - the width is warmupPeriod / stableInterval, to make cooldownPeriod == warmupPeriod
     * - the slope starts at the middle, and goes from stableInterval to 3*stableInterval so
     *   to have halfPermits being spend in double the usual time (half the rate), while their
     *   respective rate is steadily ramping up
     */
    private static class WarmingUp extends GuaRateLimter {

        final long warmupPeriodMicros;
        /**
         * The slope of the line from the stable interval (when permits == 0), to the cold interval
         * (when permits == maxPermits)
         */
        private double slope;
        private double halfPermits;

        WarmingUp(SleepingTicker ticker, long warmupPeriod, TimeUnit timeUnit) {
            super(ticker);
            this.warmupPeriodMicros = timeUnit.toMicros(warmupPeriod);
        }

        @Override
        void doSetRate(double permitsPerSecond, double stableIntervalMicros) {
            double oldMaxPermits = maxPermits;
            maxPermits = warmupPeriodMicros / stableIntervalMicros;
            halfPermits = maxPermits / 2.0;
            // Stable interval is x, cold is 3x, so on average it's 2x. Double the time -> halve the rate
            double coldIntervalMicros = stableIntervalMicros * 3.0;
            slope = (coldIntervalMicros - stableIntervalMicros) / halfPermits;
            if (oldMaxPermits == Double.POSITIVE_INFINITY) {
                // if we don't special-case this, we would get storedPermits == NaN, below
                storedPermits = 0.0;
            } else {
                storedPermits = (oldMaxPermits == 0.0)
                        ? maxPermits // initial state is cold
                        : storedPermits * maxPermits / oldMaxPermits;
            }
        }

        @Override
        long storedPermitsToWaitTime(double storedPermits, double permitsToTake) {
            double availablePermitsAboveHalf = storedPermits - halfPermits;
            long micros = 0;
            // measuring the integral on the right part of the function (the climbing line)
            if (availablePermitsAboveHalf > 0.0) {
                double permitsAboveHalfToTake = Math.min(availablePermitsAboveHalf, permitsToTake);
                micros = (long) (permitsAboveHalfToTake * (permitsToTime(availablePermitsAboveHalf)
                        + permitsToTime(availablePermitsAboveHalf - permitsAboveHalfToTake)) / 2.0);
                permitsToTake -= permitsAboveHalfToTake;
            }
            // measuring the integral on the left part of the function (the horizontal line)
            micros += (stableIntervalMicros * permitsToTake);
            return micros;
        }

        private double permitsToTime(double permits) {
            return stableIntervalMicros + permits * slope;
        }
    }

    /**
     * 稳定模式，令牌生成速度恒定
     * This implements a trivial function, where storedPermits are translated to
     * zero throttling - thus, a client gets an infinite speedup for permits acquired out
     * of the storedPermits pool. This is also used for the special case of the "metronome",
     * where the width of the function is also zero; maxStoredPermits is zero, thus
     * storedPermits and permitsToTake are always zero as well. Such a GuaRateLimter can
     * not save permits when unused, thus all permits it serves are fresh, using the
     * designated rate.
     */
    private static class Bursty extends GuaRateLimter {
        Bursty(SleepingTicker ticker) {
            super(ticker);
        }

        /**
         * 用来调整速率
         * @param permitsPerSecond
         * @param stableIntervalMicros
         */
        @Override
        void doSetRate(double permitsPerSecond, double stableIntervalMicros) {
            double oldMaxPermits = this.maxPermits;
            /*
             * We allow the equivalent work of up to one second to be granted with zero waiting, if the
             * rate limiter has been unused for as much. This is to avoid potentially producing tiny
             * wait interval between subsequent requests for sufficiently large rates, which would
             * unnecessarily overconstrain the thread scheduler.
             */
            maxPermits = permitsPerSecond; // one second worth of permits
            storedPermits = (oldMaxPermits == 0.0)
                    ? 0.0 // initial state
                    : storedPermits * maxPermits / oldMaxPermits;
        }

        @Override
        long storedPermitsToWaitTime(double storedPermits, double permitsToTake) {
            return 0L;
        }
    }

    @VisibleForTesting
    static abstract class SleepingTicker extends Ticker {
        abstract void sleepMicrosUninterruptibly(long micros);

        static final SleepingTicker SYSTEM_TICKER = new SleepingTicker() {
            @Override
            public long read() {
                return systemTicker().read();
            }

            @Override
            public void sleepMicrosUninterruptibly(long micros) {
                if (micros > 0) {
                    Uninterruptibles.sleepUninterruptibly(micros, TimeUnit.MICROSECONDS);
                }
            }
        };
    }
}
