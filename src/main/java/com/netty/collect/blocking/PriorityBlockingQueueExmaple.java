package com.netty.collect.blocking;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;

/**
 * 没有边界，有排序
 * @author 86136
 */
public class PriorityBlockingQueueExmaple {
    public <T>PriorityBlockingQueue<T> create(int size){
        return new PriorityBlockingQueue<>(size);
    }

    public void testOfferMethod (){
        PriorityBlockingQueue<Object> queue = create(10);
        queue.offer("str");
    }

    public static void main(String[] args) {
        PriorityBlockingQueueExmaple exmaple = new PriorityBlockingQueueExmaple();
        exmaple.testOfferMethod();
    }
}
