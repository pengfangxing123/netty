package com.netty.datastructure.skiplist;

import java.util.Random;

/**
 * 跳表
 * @author 86136
 */
public class SimpleSkipList {
    private final static byte HEAD_NODE = 1;
    private final static byte DATA_NODE =0;
    private final static byte TAIL_NODE = -1;

    private static class Node{
        private Integer value;
        private Node up, down, left, right;
        private byte bit;

        public Node(Integer value ,byte bit) {
            this.value = value;
            this.bit=bit;
        }

        public Node(Integer value){
            this(value,DATA_NODE);
        }
    }

    private Node head;
    private Node tail;
    private int size;
    private int height;
    private Random random;

    public SimpleSkipList() {
        this.head = new Node(null, HEAD_NODE);
        this.tail = new Node(null, TAIL_NODE);
        head.right=tail;
        tail.left=head;
        this.random=new Random(System.currentTimeMillis());
    }

    public boolean isEmpty(){
        return size==0;
    }

    public int getSize(){
        return size;
    }

    private Node find(Integer element){
        Node current = head;
        for(;;){
            while (current.right.bit!=TAIL_NODE&&element>=current.right.value){
                current=current.right;
            }

        }
    }
}
