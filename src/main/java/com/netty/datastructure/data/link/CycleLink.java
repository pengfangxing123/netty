package com.netty.datastructure.data.link;

/**
 * 环形链表
 * @author 86136
 */
public class CycleLink {
    public static void main(String[] args) {
        CycleLinkList list = new CycleLinkList();
        list.add(5);
        //list.showNode();
        //2->4->1->5->3
        list.countNode(1,2,5);
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

    /**
     *
     * @param startNo 表示第几个节点开始数数
     * @param countNum 表示一次数几个（类似报数，起始位为1，所以循环次数是countNum-1）
     * @param nums 表示最初有多少个节点在圈中
     */
    public void countNode(int startNo,int countNum,int nums){
        if(first == null || startNo<1 || startNo>nums){
            System.out.println("参数输入错误");
            return;
        }

        //找出helper节点->最后一个节点
        CycleLinkNode helper =first;
        while (true){
            if(helper.next==first){
                break;
            }
            helper=helper.next;
        }

        //将first,helper 移动startNo-1次
        for(int j=0;j< startNo-1;j++){
            first=first.next;
            helper=helper.next;
        }

        while (true){
            //圈中只有一个节点
            if(helper==first){
                break;
            }


            for(int i=0;i<countNum-1;i++){
                first=first.next;
                helper=helper.next;
            }

            System.out.printf("编号%d节点需要出圈\n",first.no);

            //编号出圈
            first=first.next;
            helper.next=first;
        }
        //查看最后的节点
        System.out.println("最后节点为"+first.no);
        showNode();
    }
}

class CycleLinkNode{
    int no;
    CycleLinkNode next;

    public CycleLinkNode(int no) {
        this.no = no;
    }

}
