package com.netty.datastructure.data.tree;

/**
 * 红黑树的特性:
 * (1) 每个节点或者是黑色，或者是红色。
 * (2) 根节点是黑色。
 * (3) 每个叶子节点是黑色。 [注意：这里叶子节点，是指为空的叶子节点！]
 * (4) 如果一个节点是红色的，则它的子节点必须是黑色的。
 * (5) 从一个节点到该节点的子孙节点的所有路径上包含相同数目的黑节点。
 *
 * 关于它的特性，需要注意的是：
 * 第一，特性(3)中的叶子节点，是只为空(NIL或null)的节点。
 * 第二，特性(5)，确保没有一条路径会比其他路径长出俩倍。因而，红黑树是相对是接近平衡的二叉树。
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
     * 就是x节点沉1位，他的右节点上述1位
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
     * 就是y节点沉1位，他的左节点上述1位
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

    /**
     * 插入一个节点
     * @param key
     */
    public void insert(T key){
        RBTNode<T> node=new RBTNode<T>(key,BLACK,null,null,null);

        // 如果新建结点失败，则返回。
        if (node != null)
            insert(node);
    }

    private void insert(RBTNode<T> node) {
        // 1. 将红黑树当作一颗二叉查找树，将节点添加到二叉查找树中。
        int cmp;
        RBTNode<T> y = null;
        RBTNode<T> x = this.mRoot;
        while(x != null){
            y = x;
            cmp = node.key.compareTo(x.key);
            if(cmp>0){
                x=x.left;
            }else{
                x=x.right;
            }

        }

        node.parent=y;

        node.parent = y;
        if (y!=null) {
            cmp = node.key.compareTo(y.key);
            if (cmp < 0)
                y.left = node;
            else
                y.right = node;
        } else {
            this.mRoot = node;
        }

        // 2. 设置节点的颜色为红色
        node.color = RED;

        // 3. 将它重新修正为一颗二叉查找树
        insertFixUp(node);

    }

    /**
     * 1,原树是空树，此情况会违反根节点是黑色
     *      把此节点涂为黑色
     * 2，当前节点的父节点是红色，且祖父节点的另一个节点（叔叔节点）是红色此时父节点的父节点一定存在的，否则插入前就已经不是红黑树了
     *      将当前节点的父节点和叔叔节点涂黑，祖父节点涂红，把当前节点指向祖父节点，从新的当前节点开始重新计算
     * 3,当前节点的父节点是红色，祖父节点的另一个节点（叔叔节点）是黑色，当前节点的是其父节点的右节点（父节点是祖父右节点时：左节点）
     *      当前节点的父节点作为当前节点，以当前节点为支点左旋（父节点是祖父右节点时：右旋）,把当前节点指向父节点，从新的当前节点开始重新计算
     * 4，当前节点的父节点是红色，祖父节点的另一个节点（叔叔节点）是黑色，当前节点是其父节点的左节点（父节点是祖父右节点时：右节点）
     *      将父节点变为黑色，祖父节点变为红色，再以祖父节点为支点右旋（父节点是祖父右节点时：左旋）
     *
     * 备注：3,4条件左右镜像，
     *          即父节点的是祖父节点的左节点时，节点是父节点的左节点才执行4，节点是父节点的右节点才执行3
     *          父节点是祖父节点的右节点时，节点是父节点的右节点才执行4，节点是父节点的左节点才执行3
     *          (通俗点就是，子，父，祖三个节点连成一条直线时执行4，不是则执行3，)
     *
     * @param node
     */
    private void insertFixUp(RBTNode<T> node) {
        RBTNode<T> parent, gparent;
        //有父节点，父节点且为红色，于红黑树特性4冲突
        while (((parent = parentOf(node))!=null) && isRed(parent)){
            //获取祖父节点，这里为什么不用判断祖父节点是否存在，因为父节点是红色节点，所以父节点肯定不是根节点
            gparent = parentOf(parent);

            //若“父节点”是“祖父节点的左孩子”
            if (parent == gparent.left) {
                RBTNode<T> uncle = gparent.right;
                //Case 2条件
                if(uncle!=null && isRed(uncle)){
                    setBlack(parent);
                    setBlack(uncle);
                    setRed(gparent);
                    node=gparent;
                    continue;
                }
                //Case 3条件
                if(node==parent.right){
                    RBTNode<T> tmp;
                    leftRotate(parent);
                    tmp = parent;
                    parent = node;
                    node = tmp;
                }

                // Case 4条件：叔叔是黑色，且当前节点是左孩子。
                setBlack(parent);
                setRed(gparent);
                rigthRotate(gparent);
            }else{
                RBTNode<T> uncle = gparent.left;
                //Case 2条件
                if(uncle!=null && isRed(uncle)){
                    setBlack(parent);
                    setBlack(uncle);
                    setRed(gparent);
                    node=gparent;
                    continue;
                }
                //Case 3条件 叔叔是黑色，且当前节点是左孩子
                if(node==parent.left){
                    RBTNode<T> tmp;
                    leftRotate(parent);
                    tmp = parent;
                    parent = node;
                    node = tmp;
                }

                // Case 4条件：叔叔是黑色，且当前节点是右孩子。
                setBlack(parent);
                setRed(gparent);
                leftRotate(gparent);
            }

            // 将根节点设为黑色
            setBlack(this.mRoot);
        }

    }

    /*
     * 删除结点(z)，并返回被删除的结点
     *
     * 参数说明：
     *     tree 红黑树的根结点
     *     z 删除的结点
     */
    public void remove(T key) {
        RBTNode<T> node;
        if ((node = search(mRoot, key)) != null)
            remove(node);
    }

    /**
     * 找到T所在的节点
     * @param mRoot
     * @param key
     * @return
     */
    private RBTNode<T> search(RBTNode<T> mRoot, T key) {
        while (mRoot != null && key != mRoot.key) {
            int cmp = mRoot.key.compareTo(key);
            if (cmp>0) {
                mRoot = mRoot.left;
            } else {
                mRoot = mRoot.right;
            }
        }
        return mRoot;
    }

    private void remove(RBTNode<T> node) {
        RBTNode<T> child, parent;
        boolean color;

        // 被删除节点的"左右孩子都不为空"的情况。
        //这时候就可以把被删除元素的右支的最小节点（被删除元素右支的最左边的节点）和被删除元素互换，
        // 我们把被删除元素右支的最左边的节点称之为后继节点（后继元素）
        while(node.left!=null && node.right!=null) {
            RBTNode replace;

            // 获取后继节点
            replace=node.right;
            while (replace.left!=null)
                replace=replace.left;

            // "node节点"不是根节点(只有根节点不存在父节点)
            if ((parent=parentOf(node))!=null){
                if(parent.left==node)
                    parent.left=replace;
                else
                    parent.right=replace;
            }else{
                // "node节点"是根节点，更新根节点。
                this.mRoot=replace;
            }

            // 保存"取代节点"的颜色
            color = colorOf(replace);

            // child是"取代节点"的右孩子，也是需要"调整的节点"。
            // "取代节点"肯定不存在左孩子！因为它是一个后继节点。
            child = replace.right;
            parent=replace.parent;

            if(parent==node){
                parent = replace;
            }else{
                if(child!=null)
                    setParent(child,parent);
                //假如不存在parent的left也是null
                parent.left = child;

                replace.right = node.right;
                setParent(node.right, replace);
            }

            replace.parent = node.parent;
            replace.color = node.color;
            replace.left = node.left;
            node.left.parent = replace;

            if (color == BLACK)
                removeFixUp(child, parent);

            node = null;
            return ;
        }

        if (node.left !=null) {
            child = node.left;
        } else {
            child = node.right;
        }

        parent = node.parent;
        // 保存"取代节点"的颜色
        color = node.color;

        if (child!=null)
            child.parent = parent;

        // "node节点"不是根节点
        if (parent!=null) {
            if (parent.left == node)
                parent.left = child;
            else
                parent.right = child;
        } else {
            this.mRoot = child;
        }

        if (color == BLACK)
            removeFixUp(child, parent);
        node = null;
    }

    private void removeFixUp(RBTNode<T> child, RBTNode<T> parent) {
    }

    private void setParent(RBTNode<T> p, RBTNode parent) {
        p.parent=parent;
    }

    /**
     * 返回节点颜色
     * @param p
     * @return
     */
    private boolean colorOf(RBTNode<T> p) {
        return p.color;
    }

    /**
     * 设置节点为红色
     * @param p
     */
    private void setRed(RBTNode<T> p) {
        p.color=RED;
    }

    /**
     * 设置节点为黑色
     * @param p
     */
    private void setBlack(RBTNode<T> p) {
        p.color=BLACK;
    }

    /**
     * 判断节点颜色是否是红色
     * @param p
     * @return
     */
    private boolean isRed(RBTNode<T> p) {
        return !p.color;
    }

    /**
     * 返回节点的父节点
     * @param p
     * @return
     */
    private RBTNode parentOf(RBTNode p) {
        return (p == null ? null: p.parent);
    }



}
