package com.netty.Lock;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

/**
 * @author fangxing.peng
 */
public class SimpleLock implements Lock {
    private Help help;

    public SimpleLock(){
        this.help=new Help();
    }

    @Override
    public void lock() {
        help.acquire(1);
    }

    /**
     * 获取可中断的锁
     * @throws InterruptedException
     */
    @Override
    public void lockInterruptibly() throws InterruptedException {
        help.acquireInterruptibly(1);
    }

    @Override
    public boolean tryLock() {
        return help.tryAcquire(1);
    }

    @Override
    public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
        return help.tryAcquireNanos(1,unit.toNanos(time));
    }

    @Override
    public void unlock() {
       help.tryRelease(1);
    }

    @Override
    public Condition newCondition() {
        return null;
    }

    static class Help extends AbstractQueuedSynchronizer{
        @Override
        protected boolean tryAcquire(int arg) {
            int state = getState();
            Thread cur = Thread.currentThread();
            if(state==0){
                //保证原子行操作，排除其他线程已经修改set值
                if(compareAndSetState(0,arg)){
                    this.setExclusiveOwnerThread(cur);
                    return true;
                }
            }else{
                if(this.getExclusiveOwnerThread()==cur){
                    //没有线程安全行问题 因为目前state已经不等于0；能进入该方法的只有getExclusiveOwnerThread 线程
                    state+=arg;
                    setState(state);
                    return true;
                }
            }
            return false;
        }

        @Override
        protected boolean tryRelease(int arg) {
            Thread cur = Thread.currentThread();
            boolean isZreo=false;
            if(this.getExclusiveOwnerThread()!=cur){
                throw new RuntimeException("不是同一个线程啦啦啦啦啦啦");
            }
            int state = getState()-arg;
            if(state==0){
                this.setExclusiveOwnerThread((Thread)null);
                isZreo=true;
            }
            setState(state);
            return isZreo;
        }

    }
}
