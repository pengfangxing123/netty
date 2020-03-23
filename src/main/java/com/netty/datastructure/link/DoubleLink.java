package com.netty.datastructure.link;

import java.util.Stack;

/**
 * @author 86136
 */
public class DoubleLink {
    public static void main(String[] args) {
        String[] strings = {"福田", "南山", "宝安", "龙华", "龙岗", "盐田"};
        DoubleLinkMg linkMg = new DoubleLinkMg();
//        for(int i =0 ;i<strings.length;i++){
//            linkMg.add(new DoubleLinkNode(i,strings[i]));
//        }
    }
}


class DoubleLinkMg{
    public DoubleLinkNode head=new DoubleLinkNode(1,"头节点");

    public void add(DoubleLinkNode node){
        DoubleLinkNode cur=head;
        while (cur.next!=null){
            cur=cur.next;
        }
        cur.next=node;
        node.pre=cur;
    }

    public void list(){
        if(head.next==null){
            System.out.println("链表为空");
            return;
        }
        DoubleLinkNode cur=head.next;
        while (cur!=null){
            System.out.print(cur.msg);
            cur=cur.next;
        }
        System.out.println("遍历完毕");
    }

    public void deleteNode(int no){
        if(head.next==null){
            System.out.println("链表为空");
            return;
        }

        DoubleLinkNode cur=head;
        boolean flage=false;
        while (true){
            if(cur==null ){
                break;
            }

            if(cur.no==no){
                flage=true;
                break;
            }
            cur=cur.next;
        }
        if(flage){
            cur.pre.next=cur.next;
            if(cur.next!=null){
                cur.next.pre=cur.pre;
            }
            return;
        }
        System.out.println("没有占到节点");
    }

    /**
     * 单向列表，第倒数index个元素
     * @param head
     * @param index
     * @return
     */
    public DoubleLinkNode findLastIndexNode(DoubleLinkNode head ,int index){
        if(head.next==null){
            System.out.println("链表为空");
            return null;
        }

        int size=getLength(head);

        if(index<=0 || index>size){
            return null;
        }

        DoubleLinkNode cur=head.next;
        for(int i=0;i<size-index;i++){
            cur=cur.next;
        }
        return cur;
//        int i=0;
//        int target=size-index;
//        boolean flage=false;
//        while (true){
//            if(cur==null){
//                break;
//            }
//
//            if(i==target){
//                flage=true;
//                break;
//            }
//            cur=cur.next;
//        }
//        if(flage){
//            return cur;
//        }
//        return null;
    }





    public int getLength(DoubleLinkNode head) {
        DoubleLinkNode cur = head.next;
        if(cur ==null){
            System.out.println("链表为空");
            return 0;
        }

        int i=0;
        while (cur!=null){
            i++;
            cur=cur.next;
        }
        return i;
    }
}

class DoubleLinkNode{
    public int no;
    public String msg;
    DoubleLinkNode next;
    DoubleLinkNode pre;

    public DoubleLinkNode(int no, String msg) {
        this.no = no;
        this.msg = msg;
    }
}
