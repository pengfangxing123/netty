package com.netty.datastructure.data.tree;

/**
 * 线索二叉树
 * @author 86136
 */
public class ThreadedBinaryTreeDemo {
    public static void main(String[] args) {
        int[] arr = { 1, 2, 3, 4, 5, 6, 7 };
        ThreadedNode node = ThreadedBinaryTree.createBinaryTree(arr, 0);
        ThreadedBinaryTree tree = new ThreadedBinaryTree(node);
        tree.threadedNode(tree.root);
        tree.threadedList();
    }
}

class ThreadedBinaryTree{
    public ThreadedNode root;

    /**
     * 当前节点的前驱节点
     */
    private ThreadedNode preNode;

    public ThreadedBinaryTree(ThreadedNode root) {
        this.root = root;
    }

    /**
     * 中序线索化二叉树
     * @param node
     */
    public void threadedNode(ThreadedNode node){
        if(node == null){
            return ;
        }

        //因为是中序线索化，所以左边的节点先处理，递归调用当node==null时会返回
        threadedNode(node.left);

        //当前节点left 为null时，left指向前驱节点
        if(node.left==null){
            node.left=preNode;
            node.isLeftThread=true;
        }

        //因为右节点为空时，right指向的是后继节点，
        // 所以要在后继节点处理时，判断它的前继节点的rigth是否为空，为空则让前继节点的right指向当前节点
        if(preNode!=null&&preNode.right==null){
            preNode.right=node;
            preNode.isRightThread=true;
        }

        //当前节点已经处理完了，准备处理下一节点，将前继节点指向当前节点
        preNode=node;

        //因为是中序线索化，最后处理右边的节点
        threadedNode(node.right);
    }

    /**
     * 中序线索化二叉树遍历
     * 两个关键点，一是右线索节点执行的一定是下一次要输出的，
     * 二是当右节点不是线索化节点是，那么就要从他的右节点重新开始
     */
    public void threadedList(){
        ThreadedNode node=root;
        while (node!=null){
            //找到当前节点，最左边的且isLeftThread 为true的节点,这里不单单是root节点的最左边，
            // 而是所有isLeftThread 为false的节点最左边，因为当sLeftThread 为false时，需要先遍历的是它最左边的，如笔记图中的C节点
            while(!node.isLeftThread){
                node=node.left;
            }
            //输出当前节点值
            System.out.println(node.data);

            //如果当前节点的右节点指向的是后继节点，那么直接输出,并将Node指向后继节点(后继节点，就是后续的节点嘛，线索话处理就是为了这个)
            while(node.isRightThread){
                node=node.right;
                System.out.println(node.data);
            }

            //右节点不是后继节点，这个时候就右节点当成普通的节点，开始新的循环
            node=node.right;
        }
    }

    /**
     * 根据数组创建一个完全二叉树
     * @param array
     * @param index
     * @return
     */
    public static ThreadedNode createBinaryTree(int[] array, int index) {
        ThreadedNode node = null;

        if(index < array.length) {
            node = new ThreadedNode(array[index]);
            node.left = createBinaryTree(array, index * 2 + 1);
            node.right = createBinaryTree(array, index * 2 + 2);
        }

        return node;
    }

}

class ThreadedNode {
    int data;
    ThreadedNode left;
    ThreadedNode right;
    /**
     * 左指针域类型  false：指向子节点、true：前驱或后继线索
     */
    boolean isLeftThread = false;
    /**
     * 右指针域类型  false：指向子节点、true：前驱或后继线索
     */
    boolean isRightThread = false;

    ThreadedNode(int data) {
        this.data = data;
    }
}