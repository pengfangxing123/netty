package com.netty.datastructure.queque;


/**
 * @author 86136
 */
public class CircleArrayQueue {
    public static void main(String[] args) throws Exception {
        int length = 5;
        CirycleQuequeEntry quequeEntry = new CirycleQuequeEntry(length);
        for (int i =1;i<length;i++){
            quequeEntry.addEntry(i);
        }
        for (int i =1;i<length;i++){
            quequeEntry.getEntry();
        }

        for (int i =2;i<length;i++){
            quequeEntry.addEntry(i);
        }
        quequeEntry.showQueque();
    }
}

class CirycleQuequeEntry{
    private int maxSize;
    private int head;
    private int tail;
    private int[] arr;

    public CirycleQuequeEntry(int maxSize) {
        this.maxSize=maxSize;
        this.arr =new int[maxSize];
        /**
         * 指向当前队列的第一个元素
         */
        this.head=0;
        /**
         * 指向最后一位的下一位
         * 会空出来一位不放数据，比如maxSize为10，最多可以放9个元素
         */
        this.tail=0;
    }

    public boolean isFull(){
        return (tail+1)%maxSize==head;
    }

    public boolean isEmpty(){
        return head == tail;
    }

    public void addEntry(int n){
        if(isFull()){
            System.out.println("已经满了");
            return;
        }
        arr[tail]=n;
        tail=(tail+1)%maxSize;
    }

    public int getEntry() throws Exception {
        if (isEmpty()) {
            throw new Exception("队列中为空");
        }
        int value = arr[head];
        head = (head + 1) % maxSize;
        return value;
    }

    public int size(){
        return (tail+maxSize-head)%maxSize;
    }


    public void showQueque() throws Exception {
        if(isEmpty()){
            throw new Exception("队列中为空");
        }

        for(int i = head; i< head+size(); i++){
            System.out.printf("arr[%d]=%d\n",i%maxSize, arr[i%maxSize]);
        }
    }
}