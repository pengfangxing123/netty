package com.netty.datastructure.stack;

import java.lang.reflect.Array;

/**
 * @author 86136
 */
public class ArrayStack<T>{
    /**
     * 栈大小
     */
    private int maxSize;

    /**
     * 数据存放数组
     */
    private T[] stackArray;

    /**
     * 栈顶
     */
    private int top =-1;

    public ArrayStack(int maxSize, Class<T> clz) {
        this.maxSize = maxSize;
        this.stackArray = (T[]) Array.newInstance(clz,maxSize);
    }

    public boolean isFull(){
        return top==maxSize-1;
    }

    public boolean isEmpty(){
        return top==-1;
    }

    public void push(T value){
        if(isFull()){
            System.out.println("栈已经满了");
            return ;
        }
        top++;
        stackArray[top]=value;
    }

    public T poll(){
        if(isEmpty()){
            System.out.println("栈是空的");
            throw new RuntimeException("栈是空的");
        }
        T t = stackArray[top];
        top--;
        return t;
        
    }

    /**
     * 遍历要从栈顶开始
     */
    public void list(){
        if(isEmpty()){
            System.out.println("栈是空的");
            return;
        }

        for(int i=top;i>-1;i--){
            System.out.println(stackArray[i]);
        }
    }

    public static void main(String[] args) {
        ArrayStack<Integer> stack = new ArrayStack<>(3,Integer.class);
        stack.push(1);
        stack.push(2);
        stack.list();
        System.out.println(stack.poll());
        System.out.println(stack.poll());
        stack.list();
    }
}

