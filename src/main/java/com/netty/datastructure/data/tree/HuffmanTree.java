package com.netty.datastructure.data.tree;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 赫夫曼树
 * @author 86136
 */
public class HuffmanTree {
    public static void main(String[] args) {
        int arr[] = {13, 7, 8, 3, 6, 1, 29};
        HfNode tree = createTree(arr);
        preOrder(tree);

    }

    /**
     * 创建赫夫曼树
     * @param arr
     * @return
     */
    public static HfNode createTree(int arr[]){
        List<HfNode> list = new ArrayList<>();

        //创建数节点
        for(int val : arr){
            list.add(new HfNode(val));
        }

        //只有一个元素，就是拼接过后得赫夫曼树,也就是size==2会创建一个新的Node
        while (list.size()>1){
            //将所有节点顺序排序
            Collections.sort(list);

            //取出最小得两个节点构建树
            HfNode newNode = new HfNode(list.get(0).value + list.get(1).value);
            newNode.left=list.get(0);
            newNode.right=list.get(1);

            //删除这两个节点,这里要先删除index为1的，删除一个元素后长度会变化，最后一次删除的，先删除一个list长度会变为1
            list.remove(1);
            list.remove(0);


            //将构建得树加入到集合中
            list.add(newNode);
        }
        return list.get(0);
    }

    public static void preOrder(HfNode node){
        node.preOrder();
    }
}

class HfNode implements Comparable<HfNode>{
    int value;
    HfNode left;
    HfNode right;

    public HfNode(int value) {
        this.value = value;
    }

    public void preOrder(){
        System.out.println(toString());
        if(left!=null){
            left.preOrder();
        }
        if(right!=null){
            right.preOrder();
        }
    }

    @Override
    public String toString() {
        return "HfNode{" +
                "value=" + value +
                '}';
    }

    @Override
    public int compareTo(HfNode o) {
        return this.value-o.value;
    }
}
