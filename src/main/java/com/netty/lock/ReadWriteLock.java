package com.netty.lock;

import com.google.common.collect.Maps;

import java.util.Map;
import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * @author Administrator
 */
public class ReadWriteLock {
    public static void main(String[] args) throws InterruptedException {
        CountDownLatch count = new CountDownLatch(2);
        ReadWriteLockDemo rwd = new ReadWriteLockDemo(count);
//        //启动100个读线程
//        for (int i = 0; i < 100; i++) {
//            new Thread(new Runnable() {
//                @Override
//                public void run() {
//                    rwd.get();
//                }
//            }).start();
//        }
//        //写线程
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                rwd.set((int) (Math.random() * 101));
//            }
//        }, "Write").start();

        new Thread("sjj"){
            public void run() {
                try {
                    rwd.sjj();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }.start();

        new Thread("getW"){
            public void run() {
                try {
                    rwd.getW();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }.start();

        while (count.getCount()>0){
            count.await();
        }
    }

}

class ReadWriteLockDemo{
    //模拟共享资源--Number
    private int number = 0;
    // 实际实现类--ReentrantReadWriteLock，默认非公平模式
    private ReentrantReadWriteLock readWriteLock = new ReentrantReadWriteLock();

    private Lock r=readWriteLock.readLock();
    private Lock w=readWriteLock.writeLock();
    private volatile boolean isUpdate= true;
    private CountDownLatch count;

    public ReadWriteLockDemo(CountDownLatch count) {
        this.count = count;
    }

    private Map map= Maps.newHashMap();
    public void sjj() throws InterruptedException {
        r.lock();
        if(isUpdate) {
            r.unlock();
            w.lock();
            Thread.sleep(800);
            map.put("xxx","xxx");//6
            r.lock();
            w.unlock();//4
        }
        Object obj= map.get("xxx");//5
        r.unlock();//7
        count.countDown();
    }

    public void getW() throws InterruptedException {
        Thread.sleep(3000);
        w.lock();
        w.unlock();
        count.countDown();
    }

    //读
    public void get(){
        //使用读锁
        readWriteLock.readLock().lock();
        try {
            System.out.println(Thread.currentThread().getName()+" : "+number);
        }finally {
            readWriteLock.readLock().unlock();
        }
    }
    //写
    public void set(int number){
        readWriteLock.writeLock().lock();
        try {
            this.number = number;
            System.out.println(Thread.currentThread().getName()+" : "+number);
        }finally {
            readWriteLock.writeLock().unlock();
        }
    }
}
