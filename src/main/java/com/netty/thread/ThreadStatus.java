package com.netty.thread;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author Administrator
 */
public class ThreadStatus {

    public static void main(String[] args) throws InterruptedException {
//        Executor threadPool = Executors.newFixedThreadPool(10);
//        //threadPool.execute();
//
//        List<String> integers = Arrays.asList("111", "22");
//        integers.forEach(System.out::println);
//
//        integers.parallelStream().forEach(System.out::println);
//
//        new Thread(()-> {
//            System.out.println("11111");
//            System.out.println("122222");
//        }
//        ).start();
//        integers.stream().filter(p-> p.equals("111")).collect(Collectors.toList());

        //testSource();

        testWait();
    }

    private static void testWait() throws InterruptedException {
        Object o = new Object();
        Object o1 = new Object();

        new Thread(()->{
            synchronized (o){
                System.out.println("睡眠线程");
                try {
                    o.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        }).start();


        new Thread(()->{
            synchronized (o){
                System.out.println("打断线程线程");
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                o.notify();

            }

        }).start();
    }

    /**
     * 测试异常会不会释放锁
     * @throws InterruptedException
     */
    public static void testSource() throws InterruptedException {
        Object o = new Object();

        new Thread(()->{
            synchronized (o){
                System.out.println("新建线程获取到锁");
                try {
                    TimeUnit.SECONDS.sleep(2);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                //异常
                int i=1/0;
            }
        }).start();

        TimeUnit.SECONDS.sleep(1);
        synchronized (o){
            //这个睡眠是分开打印信息
            TimeUnit.SECONDS.sleep(2);
            System.out.println("主线程获取到锁");
        }
    }
}
