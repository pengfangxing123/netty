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

            if(current.down!=null){
                current=current.down;
            }else {
                break;
            }
        }
        // current <=element < current.right(if exist)
        return current;
    }

    public boolean contains(Integer element){
        Node node = this.find(element);
        return node.value.equals(element);
    }

    public Integer get(Integer element){
        Node node = this.find(element);
        return node.value.equals(element)?node.value:null;
    }

    public void add(Integer element){
        //底层
        Node node = this.find(element);
        Node newNode = new Node(element);
        newNode.right=node.right;
        newNode.left=node;
        node.right.left=newNode;
        node.right=newNode;

        //跳跃层
        int currentLevel=0;
//        int length=3;
//        if(element==1){
//            length=1;
//        }

//        for(int i=0;i<length;i++){
        while (random.nextDouble()<0.5d){
            if(currentLevel>=height){
                height++;
                Node dumyHead = new Node(null, HEAD_NODE);
                Node dumyTail = new Node(null, TAIL_NODE);
                dumyHead.right=dumyTail;
                dumyHead.down=head;
                head.up=dumyHead;

                dumyTail.left=dumyHead;
                dumyTail.down=tail;
                tail.up=dumyTail;

                head=dumyHead;
                tail=dumyTail;
            }


            while (node!=null&&node.up == null){
                node=node.left;
            }
            node=node.up;
            Node upNode = new Node(element);

            node.right.left=upNode;
            upNode.right=node.right;
            upNode.left=node;
            node.right=upNode;

            upNode.down=newNode;
            newNode.up=upNode;

            //将newNode指向upNode，提升一层
            newNode=upNode;
            currentLevel++;
        }
        //}

        size++;
    }

    public void dumpSkipList(){
        Node temp= head;
        int i=height+1;
        while (temp!=null){
            System.out.print("Total: " + (height + 1) + " height: " + i--);
            Node node=temp.right;
            while (node.bit==DATA_NODE){
                System.out.print("->"+node.value);
                node=node.right;
            }
            System.out.println("");
            temp=temp.down;
        }
        System.out.println("=====================遍历完成");
    }

    public static void main(String[] args) {
        SimpleSkipList simpleSkipList = new SimpleSkipList();
        simpleSkipList.add(10);
        simpleSkipList.dumpSkipList();
        simpleSkipList.add(1);
        simpleSkipList.dumpSkipList();
        simpleSkipList.add(12);
        simpleSkipList.dumpSkipList();
    }
}
