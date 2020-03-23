package com.netty.datastructure.link;

/**
 * 环形链表
 * @author 86136
 */
public class CycleLink {
    public static void main(String[] args) {
        CycleLinkList list = new CycleLinkList();
        list.add(4);
        list.showNode();
    }
}

class CycleLinkList{
    private CycleLinkNode first=new CycleLinkNode(-1);

    /**
     * 构建一个nums长度的环形链表
     * @param nums
     */
    public void add(int nums){
        //要设置启动参数-ea才能有效
        //assert nums < 1 :"nums不正确";
        if(nums < 1){
            System.out.println("nums不正确");
            return ;
        }

        CycleLinkNode cur = null;
        for(int i =1 ;i <=nums; i++){
            CycleLinkNode node = new CycleLinkNode(i);
            if(i==1){
                first=node;
                first.next=node;
                cur=node;
                continue;
            }
            cur.next=node;
            node.next=first;
            cur=node;
        }
    }

    public void showNode(){
        if(first == null){
            System.out.println("列表为空");
            return;
        }

        CycleLinkNode cur = first;
        while (true){
            System.out.println("编号"+cur.no);
            if(cur.next==first){
                break;
            }
            cur=cur.next;
        }
    }
}

class CycleLinkNode{
    int no;
    CycleLinkNode next;

    public CycleLinkNode(int no) {
        this.no = no;
    }

}
