package com.netty.thread.threadpool;

import java.util.concurrent.*;
import java.util.stream.IntStream;

/**
 * ThreadPoolExecutor创建
 * @author 86136
 */
public class ThreadPoolExecutorBuild {
    public static void main(String[] args) throws InterruptedException {
        //testCoreSize();
        //testException();
        //testCachePool();

        //直接往queque加入到队列
        //testAddRunable2Queue();

        //testShutDown();
        testShutDown2();

        //线程回收
        //testThreadRecycle();

    }

    private static void testShutDown2() {
        ThreadPoolExecutor executor = new ThreadPoolExecutor(1, 1, 1,
                TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>());
        IntStream.range(0,3).boxed().forEach(p->{
            executor.execute(()->{
                System.out.println("第"+p+"个任务开始"+Thread.currentThread().getName());
                System.out.println("第"+p+"个任务结束"+Thread.currentThread().getName());
            });
        });
        executor.shutdown();
    }

    /**
     * 测试线程回收
     * @throws InterruptedException
     */
    private static void testThreadRecycle() throws InterruptedException {
        //这种线程maximumPookSize虽然是2，但是永远只有一个线程去处理任务corePoolSize是0，队列容量(int的最大值)没有限制，所以创建线程
        //的第一种情况wc<corePoolSize不满足，而提交给队列一直满足，所以一直等那个唯一的线程去处理
        ThreadPoolExecutor executor = new ThreadPoolExecutor(0, 2, 1,
                TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>());
        IntStream.range(0,3).boxed().forEach(p->{
            executor.execute(()->{
                System.out.println("第"+p+"个任务开始"+Thread.currentThread().getName());
                System.out.println("第"+p+"个任务结束"+Thread.currentThread().getName());
            });
         });
        //模拟特殊情况最后一个线程回收时，ThreadPoolExecutor第1065行 超时获取任务，没有获取到，将标识timedOut置为true
        //然后再提交一个任务
        //这种情况这里限制的来防止，就时最后一个线程回收时，要判断下任务queue是否为空。wc > 1 || workQueue.isEmpty()
        executor.execute(()->{
            System.out.println("第"+3+"个任务开始"+Thread.currentThread().getName());
            System.out.println("第"+3+"个任务结束"+Thread.currentThread().getName());
        });
    }

    /**
     * 测试
     * @throws InterruptedException
     */
    private static void testShutDown() throws InterruptedException {
        ThreadPoolExecutor executorService = (ThreadPoolExecutor) Executors.newFixedThreadPool(5);
        IntStream.range(1,10).boxed().forEach(p->executorService.execute(()->{
            System.out.println("第"+p+"个任务开始"+Thread.currentThread().getName());
            try {
                TimeUnit.SECONDS.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("第"+p+"个任务结束"+Thread.currentThread().getName());
        }));
        TimeUnit.SECONDS.sleep(2);
        executorService.shutdown();
    }


    /**
     * 测试往queque加入到队列
     */
    private static void testAddRunable2Queue() {
        ThreadPoolExecutor executorService = (ThreadPoolExecutor) Executors.newFixedThreadPool(5);
        //注释掉该段任务就不会执行，因为没有线程在take队列，等待执行任务
        //开启coreSize数量的线程去等待执行任务
        executorService.prestartAllCoreThreads();
        BlockingQueue<Runnable> queue = executorService.getQueue();
        queue.add(()->System.out.println("6666"));
    }

    /**
     * 测试newCachedThreadPool的用法
     * @throws InterruptedException
     */
    private static void testCachePool() throws InterruptedException {
        ThreadPoolExecutor executorService = (ThreadPoolExecutor)Executors.newCachedThreadPool();
        executorService.execute(()->{
            try {
                System.out.println(Thread.currentThread().getName());
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("666");
        });
        executorService.execute(()->{
            System.out.println(Thread.currentThread().getName());
            System.out.println("666");
        });

        TimeUnit.SECONDS.sleep(2);
        System.out.println(executorService.getActiveCount());
        executorService.execute(()->{
            try {
                System.out.println(Thread.currentThread().getName());
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("666");
        });
    }

    /**
     * 测试coreSize参数的作用
     * 当线程池活跃线程数量小于corePoolSize时，新提交任务将创建一个新线程执行任务，即使此时线程池中存在空闲线程。
     */
    private static void testCoreSize() throws InterruptedException {
        ThreadPoolExecutor executorService = (ThreadPoolExecutor) Executors.newFixedThreadPool(2);
        //这里getActiveCount是获取正在进行任务的线程数量，不是线程数，是debug进去看work数组的数量才是真正的活跃线程数量
        System.out.println(executorService.getActiveCount());
        executorService.submit(()->{
            try {
                System.out.println(Thread.currentThread().getName());
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("666");
            int i=1/0;
        });

//        executorService.execute(()->{
//            try {
//                System.out.println(Thread.currentThread().getName());
//                TimeUnit.SECONDS.sleep(2);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//            System.out.println("666");
//            //int i=2/0;
//        });
//
//        executorService.execute(()->{
//            try {
//                System.out.println(executorService.getActiveCount());
//                System.out.println(Thread.currentThread().getName());
//                TimeUnit.SECONDS.sleep(2);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//            System.out.println("666");
//        });

        TimeUnit.SECONDS.sleep(5);
        System.out.println(executorService.getActiveCount());
    }

    /**
     *
     * @throws InterruptedException
     */
    public static void testException() throws InterruptedException {
        ThreadPoolExecutor executorService = (ThreadPoolExecutor) Executors.newFixedThreadPool(2);

        executorService.execute(() -> {
            try {
                System.out.println(Thread.currentThread().getName());
                TimeUnit.SECONDS.sleep(3);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("66666");
            int i = 1 / 0;
        });
        TimeUnit.SECONDS.sleep(1);

    }
}
