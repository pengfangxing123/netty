package com.netty.rbTree;

/**
 * @author fangxing.peng
 */
public class RBTree<T extends Comparable<T>> {
    private RBTNode<T> mRoot;    // 根结点

    private static final boolean RED   = false;
    private static final boolean BLACK = true;

    public class RBTNode<T extends Comparable<T>> {
        boolean color;        // 颜色
        T key;                // 关键字(键值)
        RBTNode<T> left;    // 左孩子
        RBTNode<T> right;    // 右孩子
        RBTNode<T> parent;    // 父结点

        public RBTNode(T key, boolean color, RBTNode<T> parent, RBTNode<T> left, RBTNode<T> right) {
            this.key = key;
            this.color = color;
            this.parent = parent;
            this.left = left;
            this.right = right;
        }

    }

    /*
     * 对红黑树的节点(x)进行左旋转
     *
     * 左旋示意图(对节点x进行左旋)：
     *      px                              px
     *     /                               /
     *    x                               y
     *   /  \      --(左旋)-.           / \
     *  lx   y                          x  ry
     *     /   \                       /  \
     *    ly   ry                     lx  ly
     *
     *
     */
    private void leftRotate(RBTNode<T> x) {
        //获取y节点
        RBTNode<T> y = x.right;

        //因为y.left存在，左旋会与x位置冲突，所以将y.left挂在x下，因为大于x，所以是x.rihgt
        x.right=y.left;
        if(y.left!=null){
            y.left.parent=x;
        }

        // 将 “x的父亲” 设为 “y的父亲”
        y.parent=x.parent;

        if(x.parent==null){
           this.mRoot=y;
        }else{
            //判断x是 “x的父亲” 的左节点还是右节点
            if(x.parent.left==x){
                x.parent.left=y;
            }else{
                x.parent.right=y;
            }
        }
        // 将 “x的父节点” 设为 “y”
        x.parent=y;
        // 将 “x” 设为 “y的左孩子”
        y.left=x;
    }

    /*
     * 对红黑树的节点(y)进行右旋转
     *
     * 右旋示意图(对节点y进行左旋)：
     *            py                               py
     *           /                                /
     *          y                                x
     *         /  \      --(右旋)-.            /  \                     #
     *        x   ry                           lx   y
     *       / \                                   / \                   #
     *      lx  rx                                rx  ry
     *
     */
    private void rigthRotate(RBTNode<T> y){
        RBTNode<T> x = y.left;

        //因为x.right存在，左旋会与y位置冲突，所以将x.right挂在y下，因为小于y，所以是y.left
        y.left=x.right;
        if(x.right!=null){
            x.right.parent=y;
        }

        x.parent=y.parent;
        if(y.parent==null){
            this.mRoot=x;
        }else{
            if(y.parent.left==y){
                y.parent.left=x;
            }else{
                y.parent.right=y;
            }
        }

        y.parent=x;
        x.right=y;
    }
}
