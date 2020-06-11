package com.netty.collect.blocking;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;

/**
 * 基于环形数组，需要初始值，不能扩容
 * 并且有一个可选的参数来指定是否需 要公平性。若设置了公平参数， 则那么等待了最长时间的线程会优先得到处理
 * 不能传入往队列中写入null，因为有poll类似的方法，没有元素时会返回null
 * @author 86136
 */
public class ArrayBlockingQueueExample {

    /**
     * FIFO
     * Once created, the capacity cannot be changed.
     * @param size
     * @param <T>
     * @return
     */
    public <T> ArrayBlockingQueue<T> create(int size){
        return new ArrayBlockingQueue<T>(size);
    }

    /**
     * 没有超过容量，会立即加入
     * 超过容量会抛出IllegalStateException
     */
    public void testAddMethod(){
        ArrayBlockingQueue<Object> queue = this.create(3);
        queue.add("hello");
        queue.add("hello");
        queue.add("hello");
        System.out.println("that queue is fulled ");
        queue.add("hello");
        System.out.println("that queue add next ");
    }

    /**
     * 没有超过容量，会立即加入
     * 超过容量会失败，返回结果为false
     */
    public void testOfferMehod(){
        ArrayBlockingQueue<Object> queue = this.create(3);
        queue.offer("hello");
        queue.offer("hello");
        queue.offer("hello");
        System.out.println("that queue is fulled ");
        boolean hello = queue.offer("hello");
        System.out.println("that queue continue add result is "+hello);
    }

    /**
     * 没有超过容量，会立即加入
     * 超过容量阻塞
     */
    public void testPutMehod() throws InterruptedException {
        ArrayBlockingQueue<Object> queue = this.create(3);
        queue.put("hello");
        queue.put("hello");
        queue.put("hello");
        System.out.println("that queue is fulled ");
        queue.put("hello");
        System.out.println("that queue continue add ");
    }

    /**
     * 有元素之间拿出,并且会唤醒因为队列满而休眠的线程
     * 没有元素会返回Null
     */
    public void testPollMethos(){
        ArrayBlockingQueue<Object> queue = this.create(3);
        queue.add("hello");
        System.out.println("that queue add is over ");
        Object poll = queue.poll();
        System.out.println("that queue poll result "+poll);
        Object poll1 = queue.poll();
        System.out.println("that queue poll result "+poll1);
    }

    /**
     * 拿第一个元素
     * 没有元素会返回Null
     */
    public void testPeekMethos(){
        ArrayBlockingQueue<Object> queue = this.create(3);
        queue.add("hello");
        queue.add("hello2");
        System.out.println("that queue add is over ");
        Object poll = queue.peek();
        System.out.println("that queue peek result "+poll);
        Object poll1 = queue.peek();
        System.out.println("that queue peek result "+poll1);
    }

    /**
     * 拿第一个元素
     * 没有元素会抛NoSuchElementException异常
     */
    public void testElementMethos(){
        ArrayBlockingQueue<Object> queue = this.create(3);
        queue.add("hello");
        queue.add("hello2");
        System.out.println("that queue add is over ");
        Object poll = queue.element();
        System.out.println("that queue peek result "+poll);
        Object poll1 = queue.element();
        System.out.println("that queue peek result "+poll1);

    }

    /**
     * 删除第一个元素，并返回
     * 没有元素会抛NoSuchElementException异常
     */
    public void testRemoveMethos(){
        ArrayBlockingQueue<Object> queue = this.create(3);
        queue.add("hello");
        queue.add("hello2");
        System.out.println("that queue add is over ");
        Object poll = queue.remove();
        System.out.println("that queue remove result "+poll);
        Object poll1 = queue.remove();
        System.out.println("that queue remove result "+poll1);
    }


    public void testDrainToMethos(){
        ArrayBlockingQueue<Object> queue = this.create(3);
        queue.add("hello");
        queue.add("hello2");
        queue.add("hello3");
        System.out.println("that queue add is over and size ="+queue.size());
        List<Object> list= new ArrayList<>();
        //int i = queue.drainTo(list);
        int i = queue.drainTo(list, 2);
        System.out.println("after drail to method that queue size ="+queue.size());
        System.out.println("after drail to method that list size ="+list.size());
        System.out.println("after drail to method that queue peek element ="+queue.peek());
    }

    public static void main(String[] args) throws InterruptedException {
        ArrayBlockingQueueExample example = new ArrayBlockingQueueExample();
        //example.testAddMethod();
        //example.testOfferMehod();
        //example.testPutMehod();
        //example.testPollMethos();
        //example.testPeekMethos();
        example.testElementMethos();
        //example.testRemoveMethos();
        //example.testDrainToMethos();
    }
}
