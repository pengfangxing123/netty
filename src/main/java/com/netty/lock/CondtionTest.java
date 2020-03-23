package com.netty.lock;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author Administrator
 */
public class CondtionTest {

    private Lock lock = new ReentrantLock();
    public Condition condition = lock.newCondition();

    public static void main(String[] args) throws InterruptedException {
        CondtionTest test = new CondtionTest();
        ThreadA a = new ThreadA(test);
        a.start();
        Thread.sleep(3000);
        test.signal();
    }
    //等待
    public void await() {
        try {
            lock.lock();    //调用lock.lock()方法的线程就持有了"对象监视器"，其他线程只有等待锁被释放时再次争抢
            System.out.println("await()时间为：" + System.currentTimeMillis());
            condition.await();
            System.out.println("666666666");
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }
    //通知
    public void signal() {
        try {
            lock.lock();
            System.out.println("signal()时间为：" + System.currentTimeMillis());
            condition.signal();
        } finally {
            lock.unlock();
        }
    }
}

//线程类
class ThreadA extends Thread {
    private CondtionTest test;
    public ThreadA(CondtionTest test) {
        super();
        this.test = test;
    }
    @Override
    public void run() {
        test.await();
    }
}


