package com.netty.thread.lock;

import java.util.concurrent.locks.ReentrantLock;

/**
 * @author Administrator
 */
public class ReenTrantLockTest {
    private String name;

    public static void main(String[] args) throws InterruptedException {
        ReentrantLock lock = new ReentrantLock(true);
        new Thread(() -> {
            System.out.println("第一个线程启动");
            lock.lock();
            try {
                Thread.sleep(60);
                lock.unlock();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();

        new Thread(() -> {
            System.out.println("第二个线程启动");
            lock.lock();
        }).start();

        Thread.sleep(60*60);
    }


    public void joinTest() throws InterruptedException {
        Thread thread = new Thread(() -> {
            System.out.println("thread 1");
        });
        thread.start();
        thread.join();
        System.out.println("6666");
    }


}
