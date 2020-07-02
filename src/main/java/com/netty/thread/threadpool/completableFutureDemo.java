package com.netty.thread.threadpool;

import java.net.URL;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * CompletableFuture该类主要是实现CompletionStage接的功能，
 * 也就是某个任务异步任务完成前，主线程可以把该任务执行完，该怎么处理先定义好，异步任务完成后直接按定义好的执行
 * @author 86136
 */
public class completableFutureDemo {
    public static void main(String[] args) throws ExecutionException, InterruptedException {

        //提交一个异步任务
        //就是将任务提交给线程池去处理，里面有ForkJoinPool，
        //如果cpu逻辑核心大于1是ForkJoinPool，否则是一个没次提交任务都创建一个新线程去执行任务的线程池
        //还有重载方法可以自己传入一个线程池
        //supplyAsync和runAsync方法实现的区别就是一个runnable的子类，重写了run方法，
        //在run里面分别条用supply的get方法和runnable的run方法
//        final CompletableFuture<String> futureOne = CompletableFuture.supplyAsync(() -> {
//            try {
//                System.out.println("futureOne："+Thread.currentThread().getName());
//                TimeUnit.SECONDS.sleep(5);
//            } catch (InterruptedException e) {
//                System.out.println("futureOne InterruptedException");
//            }
//            return "futureOneResult";
//        });
//        final CompletableFuture<String> futureTwo = CompletableFuture.supplyAsync(() -> {
//            try {
//                System.out.println("666");
//                TimeUnit.SECONDS.sleep(6);
//            } catch (InterruptedException e) {
//                System.out.println("futureTwo InterruptedException");
//            }
//            return "futureTwoResult";
//        });
//        CompletableFuture<String> stringCompletableFuture = futureOne.thenApply(p ->{
//            //非异步方法由当前线程或线程池方法执行(debug的时候触发过一次main线程，至于什么时候用Main线程没去深究源码)
//            System.out.println("thenApply："+Thread.currentThread().getName());
//            return p + "：thenApply";
//        });
//        System.out.println(stringCompletableFuture.get());
//        CompletableFuture<String> stringCompletableFuture2 = futureOne.thenApplyAsync(p ->{
//            System.out.println("thenApply2："+Thread.currentThread().getName());
//            return p + "：thenApply2";
//        });
//        System.out.println(stringCompletableFuture2.get());
        //CompletableFuture future = CompletableFuture.allOf(futureOne, futureTwo);
        //System.out.println(future.get());
        //CompletableFuture completableFuture = CompletableFuture.anyOf(futureOne, futureTwo);
        //System.out.println(completableFuture.get());
        testForkJoinPool();
    }

    public static void testForkJoinPool() throws InterruptedException {
        for(int i=0;i<2000;i++){
            int j=i;
            CompletableFuture<String> futureOne = CompletableFuture.supplyAsync(() -> {
                try {
                    System.out.println("futureOne："+Thread.currentThread().getName()+"current task："+j);
                    TimeUnit.SECONDS.sleep(5);
                } catch (InterruptedException e) {
                    System.out.println("futureOne InterruptedException");
                }
                return "futureOneResult";
            });
        }

        TimeUnit.SECONDS.sleep(200000);
    }
}
