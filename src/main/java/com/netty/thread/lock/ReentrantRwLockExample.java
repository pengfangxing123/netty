package com.netty.thread.lock;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * @author 86136
 */
public class ReentrantRwLockExample {
    public static void main(String[] args) throws InterruptedException {
        ReentrantReadWriteLock readWriteLock = new ReentrantReadWriteLock();
        ReentrantReadWriteLock.WriteLock writeLock = readWriteLock.writeLock();
        ReentrantReadWriteLock.ReadLock readLock = readWriteLock.readLock();

        //读锁
        readLock.lock();
        new Thread(()->{
            readLock.lock();
        }).start();
        TimeUnit.SECONDS.sleep(20);
        readLock.unlock();

        //写锁
        writeLock.lock();
        writeLock.unlock();

        //锁降级
        writeLock.lock();
        readLock.lock();
        writeLock.unlock();
        readLock.unlock();

    }
}
