package com.netty.thread.lock;

/**
 * @author Administrator
 */
public class TakeTargert implements Runnable{
    private Taobao taobao;

    public TakeTargert(Taobao taobao){
        this.taobao=taobao;
    }

    @Override
    public void run() {

    }
}
