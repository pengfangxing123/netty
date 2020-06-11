package com.netty.thread.threadpool;

import java.time.LocalTime;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 测试ScheduledThreadPoolExecutor
 * @author 86136
 */
public class ScheduledThreadPoolBuild {
    public static void main(String[] args) throws InterruptedException {
        testFixDelay();

        //testFixRate();

    }

    /**
     * 当执行时间小于period时，周期就speriod ，是以上一次任务的开始时间为间隔的
     * 当执行时间大于period时，且执行时间固定时，周期就时执行时间，一个任务执行完后，立即执行下一个
     * 当执行时间大于period时，且执行时间不固定时，好像就有点乱，下面这个例子就是不固定的 在i=4时，
     * 也是立即就进入了下个任务，按理说这个时候周期应该是5
     */
    private static void testFixRate() {
        ScheduledExecutorService executorService = Executors.newScheduledThreadPool(10);
        AtomicInteger atomicInteger = new AtomicInteger(0);
        executorService.scheduleAtFixedRate(()->{
            int i = atomicInteger.incrementAndGet();
            System.out.println(" ##########into"+ LocalTime.now()+"："+Thread.currentThread().getName());
            int sleepTime=i==3||i==5?10:1;
            try {
                TimeUnit.SECONDS.sleep(sleepTime);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("finished"+ LocalTime.now());
        },0,5,TimeUnit.SECONDS);
    }

    /**
     * 固定两个任务之间的延时时间，不管怎样两个任务之间的时间为delay
     */
    private static void testFixDelay() throws InterruptedException {
        ScheduledThreadPoolExecutor executorService = (ScheduledThreadPoolExecutor)Executors.newScheduledThreadPool(2);
        executorService.setContinueExistingPeriodicTasksAfterShutdownPolicy(true);
        executorService.scheduleWithFixedDelay(()->{
            System.out.println("into #####"+ LocalTime.now());
            try {
                TimeUnit.SECONDS.sleep(5);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("finished"+ LocalTime.now());
        },0,2,TimeUnit.SECONDS);

        TimeUnit.SECONDS.sleep(1);
        executorService.shutdown();
    }
}
