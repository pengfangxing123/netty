package com.netty.Lock;

/**
 * @author Administrator
 */
public class Taobao {

    private int count;

    private  final int max_count=10;

    public synchronized void push(){
        if(count>=max_count){
            System.out.println("上限");
            try {
                this.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        count++;
    }

    public void take(){
        count--;
    }
}
