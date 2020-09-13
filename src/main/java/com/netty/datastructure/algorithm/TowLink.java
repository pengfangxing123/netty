package com.netty.datastructure.algorithm;

import java.util.HashSet;
import java.util.Set;

/**
 * @author 86136
 */
public class TowLink {

    public static void main(String[] args) {

    }

    public void serarch(Node link1,Node link2){
        //用来存放第一个链表数据
        Set<Node>  set=new HashSet<>();
        //存放第二个链表数据，用来判断第二个链表是否时环形链表
        Set<Node>  set2=new HashSet<>();

        Node cur =link1;
        while (cur!=null){
            if(set.add(cur)){
                cur=cur.next;
            }else{
                //解决环形链表
                break;
            }
        }

        cur =link2;
        while (cur!=null){
            if(set.contains(cur)){
                break;
            }else{
                if(set2.add(cur)){
                    cur=cur.next;
                }else{
                    break;
                }

            }
        }

        if(cur!=null&&!set2.contains(cur)){
            System.out.println("交叉节点："+cur);
        }

    }

    class Node{
        private Node next;
    }
}
