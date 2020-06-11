package com.netty.collect.blocking;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;

/**
 * 没有边界，有排序
 * 不能传入往队列中写入null，因为有poll类似的方法，没有元素时会返回null
 * @author 86136
 */
public class PriorityBlockingQueueExmaple {
    public <T>PriorityBlockingQueue<T> create(int size){
        return new PriorityBlockingQueue<>(size);
    }

    public void testOfferMethod (){
        PriorityBlockingQueue<Object> queue = create(10);
        boolean str = queue.offer("str");
    }

    public static void main(String[] args) {
        PriorityBlockingQueueExmaple exmaple = new PriorityBlockingQueueExmaple();
        exmaple.testOfferMethod();
    }
}
