package com.netty.collect.blocking;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;

/**
 * 没有边界，有排序
 * 不能传入往队列中写入null，因为有poll类似的方法，没有元素时会返回null
 * 用小顶堆存储元素
 * @author 86136
 */
public class PriorityBlockingQueueExmaple {
    public <T>PriorityBlockingQueue<T> create(int size){
        return new PriorityBlockingQueue<>(size);
    }

    public void testOfferMethod (){
        PriorityBlockingQueue<Object> queue = create(10);
        boolean str = queue.offer(3);
        boolean str1 = queue.offer(2);
        queue.offer(1);
        queue.offer(5);
        queue.offer(6);
        queue.offer(0);
    }

    public static void main(String[] args) {
        PriorityBlockingQueueExmaple exmaple = new PriorityBlockingQueueExmaple();
        exmaple.testOfferMethod();
    }
}
