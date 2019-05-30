package com.netty.lock;

/**
 * @author Administrator
 */
public class ClassSock {

    public void test(){
        System.out.println("111111111");
            Object obj=new Object();
            synchronized (this){
                try {
                    notify();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            };


    }

    public static void main(String[] args) {
        ClassSock classSock = new ClassSock();
        classSock.test();
    }
}
