package com.netty.thread.lock;

import java.util.Random;
import java.util.SplittableRandom;
import java.util.concurrent.Phaser;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

/**
 * 分别实现countDownLunch 和cyclicBarrier
 * @author 86136
 */
public class PhaseExample2 {
    private static Random random=new Random();

    public static void main(String[] args) throws InterruptedException {
        //testCountDownLunch();
        //testCyclicBarrier();
        testDynamic();
    }

    /**
     * 重用及动态减少
     * @throws InterruptedException
     */
    private static void testDynamic() throws InterruptedException {
        Phaser phaser = new Phaser();
        phaser.register();

        IntStream.rangeClosed(1,5).boxed().map(i->phaser).forEach(p->{
            p.register();
            new Thread(()->{
                System.out.println("开始任务1"+Thread.currentThread().getName());
                try {
                    TimeUnit.SECONDS.sleep(random.nextInt(3));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("任务结束1"+Thread.currentThread().getName());
                p.arriveAndAwaitAdvance();
                System.out.println("越过栅栏1"+p.getPhase());
                p.arriveAndDeregister();
            }).start();
        });
        phaser.arriveAndAwaitAdvance();


        IntStream.rangeClosed(1,3).boxed().map(i->phaser).forEach(p->{
            p.register();
            new Thread(()->{
                System.out.println("开始任务2"+Thread.currentThread().getName());
                try {
                    TimeUnit.SECONDS.sleep(random.nextInt(3));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("任务结束2"+Thread.currentThread().getName());
                p.arriveAndAwaitAdvance();
                System.out.println("越过栅栏2"+p.getPhase());
                p.arriveAndDeregister();
            }).start();
        });

        phaser.arriveAndAwaitAdvance();
        //因为这里也是栅栏，防止主线程结束，不能打印"越过栅栏"
        TimeUnit.SECONDS.sleep(1);
        System.out.println("任务已经完成");
    }

    private static void testCyclicBarrier() throws InterruptedException {
        Phaser phaser = new Phaser();
        phaser.register();
        IntStream.rangeClosed(1,5).boxed().map(i->phaser).forEach(p->{
            p.register();
            new Thread(()->{
                System.out.println("开始任务"+Thread.currentThread().getName());
                try {
                    TimeUnit.SECONDS.sleep(random.nextInt(3));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("任务结束"+Thread.currentThread().getName());
                p.arriveAndAwaitAdvance();
                System.out.println("越过栅栏"+p.getPhase());
            }).start();
        });
        phaser.arriveAndAwaitAdvance();
        //因为这里也是栅栏，防止主线程结束，不能打印"越过栅栏"
        TimeUnit.SECONDS.sleep(1);
        System.out.println("任务已经完成");
    }

    private static void testCountDownLunch() {
        Phaser phaser = new Phaser();
        phaser.register();
        IntStream.rangeClosed(1,5).boxed().map(i->phaser).forEach(p->{
            p.register();
           new Thread(()->{
               System.out.println("开始任务"+Thread.currentThread().getName());
               try {
                   TimeUnit.SECONDS.sleep(random.nextInt(3));
               } catch (InterruptedException e) {
                   e.printStackTrace();
               }
               System.out.println("任务结束"+Thread.currentThread().getName());
               p.arrive();
           }).start();
        });

        phaser.arriveAndAwaitAdvance();
        System.out.println("任务已经完成");
    }
}
