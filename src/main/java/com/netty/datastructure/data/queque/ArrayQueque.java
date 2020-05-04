package com.netty.datastructure.data.queque;

/**
 * @author 86136
 */
public class ArrayQueque {
    public static void main(String[] args) throws Exception {
        int length = 5;
        ArrayQuequeEntry quequeEntry = new ArrayQuequeEntry(length);
        for (int i =0;i<length;i++){
            quequeEntry.addEntry(i);
        }
        quequeEntry.showQueque();
    }
}

class ArrayQuequeEntry{
    private int maxSize;
    private int head;
    private int tail;
    private int[] arry;

    public ArrayQuequeEntry(int maxSize){
        this.maxSize=maxSize;
        this.arry =new int[maxSize];
        this.head=-1;
        this.tail=-1;
    }

    public boolean isFull(){
        return tail == maxSize -1;
    }

    public boolean isEmpty(){
        return head == tail && tail== -1;
    }

    public void addEntry(int n){
        if(isFull()){
            System.out.println("已经满了");
            return;
        }
        tail++;
        arry[tail]=n;
    }

    public int getEntry() throws Exception {
        if(isEmpty()){
            throw new Exception("队列中为空");
        }
        head++;
        return arry[head];
    }

    public void showQueque() throws Exception {
        if(isEmpty()){
            throw new Exception("队列中为空");
        }

        for(int i=0 ;i<arry.length;i++){
            System.out.printf("arr[%d]=%d\n",i,arry[i]);
        }
    }
}