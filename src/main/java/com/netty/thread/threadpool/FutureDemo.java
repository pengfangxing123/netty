package com.netty.thread.threadpool;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * @author 86136
 */
public class FutureDemo {
    public static void main(String[] args) throws InterruptedException, ExecutionException {
        Map<Thread, StackTraceElement[]> allStackTraces = Thread.getAllStackTraces();

        //simpleFuture();
        testCancel();
        //testIsDone();
    }

    /**
     * 测试cancel
     * task is completed ,task is cancled 这两中情况会调用cancel方法会返回false
     * @throws InterruptedException
     * @throws ExecutionException
     */
    private static void testCancel() throws InterruptedException, ExecutionException {
        ExecutorService threadPool = Executors.newFixedThreadPool(10);
        List<Future<String>> collect = IntStream.range(0, 1).boxed().map(p -> {
            return threadPool.submit(() -> {
                try {
                    System.out.println("into run method");
                    TimeUnit.SECONDS.sleep(10);
                    System.out.println("finished");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return "success";
            });

        }).collect(Collectors.toList());
        TimeUnit.SECONDS.sleep(2);
        Future<String> future = collect.get(0);
        //这个参数表示是否调用工作线程的interrupt方法
        boolean cancel = future.cancel(true);
        System.out.println(cancel);
        System.out.println(future.isDone());
        System.out.println(future.get());
    }

    /**
     * isDone ，任务已经完成，调用了cancel(尽管任务该在精选)，会返回True
     * return state != NEW
     * @throws InterruptedException
     * @throws ExecutionException
     */
    public void testIsDone() throws InterruptedException, ExecutionException {
        ExecutorService threadPool = Executors.newFixedThreadPool(10);
        List<Future<String>> collect = IntStream.range(0, 1).boxed().map(p -> {
            return threadPool.submit((Callable<String>) () -> {
                while (true){

                }
                //return "success";
            });

        }).collect(Collectors.toList());
        TimeUnit.SECONDS.sleep(2);
        Future<String> future = collect.get(0);
        boolean cancel = future.cancel(true);
        System.out.println(cancel);
        System.out.println("是否已经完成"+future.isDone());
        System.out.println(future.get());
    }

    private static void simpleFuture() throws ExecutionException, InterruptedException {
        Callable callable = (Callable<Integer>) () -> {
            TimeUnit.SECONDS.sleep(2);
            System.out.println("2222222211");
            return 1111;
        };

        FutureTask<Integer> futureTask = new FutureTask<>(callable);
        Thread thread = new Thread(futureTask);
        thread.start();

        System.out.println("1111111111111");

        new Thread(()->{
            try {
                System.out.println("新建线程开始获取结果");
                System.out.println(futureTask.get());
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }).start();
        System.out.println("main线程开始获取结果");
        Integer o = futureTask.get();
        System.out.println(o);
    }
}
