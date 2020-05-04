package com.netty.datastructure.data.link;

import java.util.Stack;

/**
 * @author 86136
 */
public class SingleLink {
    public static void main(String[] args) {
        String[] strings = {"福田", "南山", "宝安", "龙华", "龙岗", "盐田"};
        SingleLinkMg linkMg = new SingleLinkMg();
//        for(int i =0 ;i<strings.length;i++){
//            linkMg.add(new SingleLinkNode(i,strings[i]));
//        }
        linkMg.addByOrder(new SingleLinkNode(2,strings[2]));
        linkMg.addByOrder(new SingleLinkNode(4,strings[4]));
        linkMg.addByOrder(new SingleLinkNode(5,strings[5]));
        linkMg.addByOrder(new SingleLinkNode(3,strings[3]));
        linkMg.updateNode(new SingleLinkNode(6,strings[1]));
        linkMg.updateNode(new SingleLinkNode(5,strings[0]));
        linkMg.list();
//        linkMg.deleteNode(2);
//        linkMg.list();
//        linkMg.reverseSingleLink(linkMg.head);
//        linkMg.list();
        linkMg.reversePrint(linkMg.head);
    }
}


class SingleLinkMg{
    public SingleLinkNode head=new SingleLinkNode(1,"头节点");

    public void add(SingleLinkNode node){
        SingleLinkNode cur=head;
        while (cur.next!=null){
            cur=cur.next;
        }
        cur.next=node;
    }

    public void list(){
        if(head.next==null){
            System.out.println("链表为空");
            return;
        }
        SingleLinkNode cur=head.next;
        while (cur!=null){
            System.out.print(cur.msg);
            cur=cur.next;
        }
        System.out.println("遍历完毕");
    }

    public void addByOrder(SingleLinkNode node){
        SingleLinkNode cur=head;
        while (cur.next!=null&&cur.next.no< node.no){
            cur=cur.next;
        }
        if(cur.next!=null&&cur.next.no==node.no){
            System.out.println(node.no+"已经添加过了");
        }
        SingleLinkNode next = cur.next;
        cur.next=node;
        node.next=next;
    }

    /**
     * 更新节点，不然按no大小放在后面
     * @param node
     */
    public void updateNode(SingleLinkNode node){
        if(head.next==null){
            System.out.println("链表为空");
            return;
        }

        SingleLinkNode cur=head;
        while (cur.next!=null&&cur.next.no< node.no){
            cur=cur.next;
        }
        SingleLinkNode  next= cur.next;
        if(cur.next!=null&&next.no==node.no){
            next.msg=node.msg;
        }
        cur.next=node;
        node.next=next;
    }

    public void deleteNode(int no){
        if(head.next==null){
            System.out.println("链表为空");
            return;
        }

        SingleLinkNode cur=head;
        boolean flage=false;
        while (true){
            if(cur.next==null ){
                break;
            }

            if(cur.next.no==no){
                flage=true;
                break;
            }
            cur=cur.next;
        }
        if(flage){
            cur.next=cur.next.next;
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
    public SingleLinkNode findLastIndexNode(SingleLinkNode head ,int index){
        if(head.next==null){
            System.out.println("链表为空");
            return null;
        }

        int size=getLength(head);

        if(index<=0 || index>size){
            return null;
        }

        SingleLinkNode cur=head.next;
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

    /**
     * 反转单项列表
     * @param head
     */
    public void reverseSingleLink(SingleLinkNode head){
        if(head.next==null || head.next.next==null){
            System.out.println("链表为空，或只有一位");
            return ;
        }
        SingleLinkNode temp = new SingleLinkNode(0,"反转头节点");

        SingleLinkNode cur = head.next;
        while (cur!=null){
            //注意这里要先把cur下个节点获取出来，因为下一步要进行cur.next= temp.next操作，
            // 这样就会丢失原有的cur.next;
            SingleLinkNode next=cur.next;
            cur.next= temp.next;
            temp.next=cur;
            cur=next;
        }
        this.head.next=temp.next;
        System.out.println("反转完毕");
    }

    /**
     * 倒叙打印链表
     * 可以先倒叙，再打印，但是会破坏原有结构，所以利用stack的特性来实现
     * @param head
     */
    public void reversePrint(SingleLinkNode head){
        if(head.next==null){
            System.out.println("链表为空");
            return ;
        }

        Stack<SingleLinkNode> stack = new Stack<>();

        SingleLinkNode cur = head.next;
        while(cur!=null){
            stack.push(cur);
            cur=cur.next;
        }

        while (stack.size()>0){
            System.out.println(stack.pop().msg);
        }
    }

    public int getLength(SingleLinkNode head) {
        SingleLinkNode cur = head.next;
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

class SingleLinkNode{
    public int no;
    public String msg;
    SingleLinkNode next;

    public SingleLinkNode(int no, String msg) {
        this.no = no;
        this.msg = msg;
    }
}
