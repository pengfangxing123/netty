package com.netty.datastructure.data.tree;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * 顺序存储二叉树
 * 二叉树的顺序存储就是用一组连续的存储单元存放二又树中的结点元素，一般按照二叉树结点自上向下、自左向右的顺序存储。
 * 使用此存储方式，结点的前驱和后继不一定是它们在逻辑上的邻接关系，非常适用于满二又树和完全二又树
 * 以下二叉树可以用{ 1, 2, 3, 4, 5, 6, 7 }存储，但是使用的时候还是以二叉树的形式
 * 就是将数组模拟成二叉数展示
 *      1
 *     /  \
 *    2     3
 *   /  \   /  \  .
 *  4   5   6   7
 *
 * @author 86136
 */
public class ArrBinaryTreeDemo {
    public static void main(String[] args) {
        int[] arr = { 1, 2, 3, 4, 5, 6, 7 };
        //创建一个 ArrBinaryTree
        ArrBinaryTree arrBinaryTree = new ArrBinaryTree(arr);
        //输出 1,2,4,5,3,6,7
        //arrBinaryTree.preOrder();
        Node node = ArrBinaryTree.createBinaryTree(arr, 0);
        ArrBinaryTree.levelPrint(node);
    }

}


class ArrBinaryTree {
    //存储数据结点的数组
    private int[] arr;

    public ArrBinaryTree(int[] arr) {
        this.arr = arr;
    }

    //重载preOrder
    public void preOrder() {
        this.preOrder(0);
    }


    /**
     *
     * @param index 数组的下标
     */
    public void preOrder(int index) {
        //如果数组为空，或者 arr.length = 0
        if(arr == null || arr.length == 0) {
            System.out.println("数组为空，不能按照二叉树的前序遍历");
        }
        //输出当前这个元素
        System.out.println(arr[index]);
        //向左递归遍历
        if((index * 2 + 1) < arr.length) {
            preOrder(2 * index + 1 );
        }
        //向右递归遍历
        if((index * 2 + 2) < arr.length) {
            preOrder(2 * index + 2);
        }
    }

    /**
     * 根据数组创建一个完全二叉树
     * @param array
     * @param index
     * @return
     */
    public static Node createBinaryTree(int[] array, int index) {
        Node node = null;

        if(index < array.length) {
            node = new Node(array[index], null, null);
            node.left = createBinaryTree(array, index * 2 + 1);
            node.right = createBinaryTree(array, index * 2 + 2);
        }

        return node;
    }

    /**
     * 顺序遍历
     * @param root
     * @return
     */
    public static List<Integer> levelPrint(Node root){
        List<Integer> ret = new ArrayList<>();
        if(root == null)
        {
            return ret;
        }
        LinkedList<Node> queue = new LinkedList<Node>();
        Node current = null;
        //将根节点入队
        queue.offer(root);
        while(!queue.isEmpty())
        {
            //出队队头元素并访问
            current = queue.poll();
            //这个地方输出结果
            System.out.println(current.data);
            ret.add(current.data);
            if(current.left != null)//如果当前节点的左节点不为空入队
            {
                queue.offer(current.left);
            }
            if(current.right != null)//如果当前节点的右节点不为空，把右节点入队
            {
                queue.offer(current.right);
            }
        }
        return ret;

    }
}

class Node {

    int data;

    Node left;

    Node right;

    Node(int data, Node left, Node right) {
        this.data = data;
        this.left = left;
        this.left = left;
    }
}