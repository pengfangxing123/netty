package com.netty.collect.blocking;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * 基于链表，不会扩容，可以指定一个capacity，或者默认的Integer.MAX_VALUE
 * 不能传入往队列中写入null，因为有poll类似的方法，没有元素时会返回null
 * @author 86136
 */
public class LinkedBlockingQueueExample {

    public static void main(String[] args) throws InterruptedException {
        LinkedBlockingQueue<Integer> queue = new LinkedBlockingQueue<>(1);
        new Thread(()->{
            System.out.println("signal");
            try {
                TimeUnit.SECONDS.sleep(3);
                queue.put(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
        queue.take();
    }

}
