package com.netty.datastructure.data.tree;

/**
 * 平衡二叉数
 * @author 86136
 */
public class AvlTreeDemo{

}

class AvlTree{
    AvlNode root;

    /**
     * 删除节点
     * @param value
     */
    public void deleNode(int value){
        if(root==null){
            System.out.println("空树");
            return;
        }

        //找到该节点
        AvlNode node = this.search(value);
        if(node==null){
            System.out.println("节点未找到或已被删除");
            return;
        }
        //找到该节点的父节点
        AvlNode parent =this.searchParent(value);

        //删除逻辑
        if(node.left==null&&node.right==null){
            //叶子节点直接删除
            if(parent!=null ){
                if(parent.left==node){
                    //左节点
                    parent.left=null;
                }else{
                    //右节点
                    parent.right=null;
                }
            }else{
                //node 就是root节点
                root=null;
            }
        }else if(node.left!=null && node.right!=null){
            //节点有两个子节点
            AvlNode leftMax = node.searchLeftMax();

            //删除左子树最大节点
            //这里很重要，因为是递归删除，所以不用在这个if语句中考虑这个leftmax 的左子节点问题，因为在删除该leftmax的时候会考虑
            deleNode(leftMax.value);

            //左子树最大节点代替node
            //注意这个判断，左子树最大节点的父节点如果是要删除的节点，那么久不需要这一步，不然久会leftMax.left=leftMax
            if(node.left!=leftMax){
                leftMax.left=node.left;
            }

            //下面的操作时错误的，因为没有考虑父节点指向问题
            leftMax.right=node.right;

            //parent为空node为root,也要替换
            if(parent==null){
                root=leftMax;
            }
        }else{
            //节点只有一个子节点
            if(parent!=null){
                if(parent.left==node){
                    parent.left=node.left ==null?node.right:node.left;
                }else{
                    parent.right=node.left ==null?node.right:node.left;
                }
            }else{
                root=node.left ==null?node.right:node.left;
            }

        }

    }


    /**
     * 查找节点
     * @param value
     * @return
     */
    public AvlNode search(int value){
        if(root==null){
            System.out.println("空树");
            return null;
        }
        return root.search(value);
    }

    /**
     * 非递归查找
     * @param value
     * @return
     */
    public AvlNode searchNoRecurse(int value){
        AvlNode cur =root;
        AvlNode dest=null;
        while(cur!=null){
            if(cur.value==value){
                dest=cur;
                break;
            }else if(value>cur.value){
                cur=cur.right;
            }else {
                cur=cur.left;
            }
        }
        return dest;
    }

    /**
     * 查找节点
     * @param value
     * @return
     */
    public AvlNode searchParent(int value){
        if(root==null){
            System.out.println("空树");
            return null;
        }
        return root.searchParent(value);
    }
}

class AvlNode{
    int value;
    AvlNode left;
    AvlNode right;

    public AvlNode(int value) {
        this.value = value;
    }

    /**
     * 以当前节点为根节点的高度
     * @return
     */
    public int height(){
        return Math.max(left==null?0:left.height(),right==null?0:right.height())+1;
    }

    /**
     * 左子节点的高度
     * @return
     */
    public int leftHeihht(){
        if(left==null){
            return 0;
        }
        return left.height();
    }

    /**
     * 右子节点的高度
     * @return
     */
    public int rightHeihht(){
        if(right==null){
            return 0;
        }
        return right.height();
    }

    /**
     * 添加节点
     * @param node
     */
    public void add(AvlNode node){
        if(node==null){
            return;
        }
        if(node.value>this.value){
            if(this.right==null){
                this.right=node;
            }else{
                this.right.add(node);
            }
        }else{
            if(this.left==null){
                this.left=node;
            }else{
                this.left.add(node);
            }
        }

        //当添加完一个结点后，如果 (左子树的高度 - 右子树的高度) > 1, 右旋转
        if(leftHeihht()-rightHeihht()>1){
            //如果左子节点的右子树大于左子树的高度，子节点左旋
            //这个说明路径长的再右子树，而右子树是要添加到右旋节点的左节点的，如果是长的话久会导致，不满足平衡树，
            // 所以左子节点要左旋先预处理
            if(left.rightHeihht()>left.leftHeihht()){
                left.leftRotate();
            }
            rightHeihht();
        }

        //当添加完一个结点后，如果 (右子树的高度 - 左子树的高度) > 1, 左旋转
        if(rightHeihht()-leftHeihht()>1){
            /**
             *
             *      3                 6
             *     / \               / \
             *    2   6             3  7
             *       / \           / \
             *      5   7         2   5
             *     /                 /
             *    4                 4
             */
            /**
             *
             *      3                 3               5
             *     / \               / \             / \
             *    2   6             2  5            3   6
             *       / \              / \          / \   \
             *      5   7            4   6        2   4   7
             *     /                      \
             *    4                        7
             */
            //如果右子节点的左子树大于右子树的高度，子节点右旋
            //这个说明路径长的在左子树，而左子树是要添加到右旋节点的右节点的，如果是长的话久会导致，不满足平衡树，
            // 所以右子节点要右旋先预处理
            if(right.leftHeihht()>right.rightHeihht()){
                right.rightRotate();
            }
            leftRotate();
        }
    }



    /**
     * 左旋
     * 这里的处理方式是创建一个值和当前节点一样的，重新处理后左旋后的子节点
     * 将当前节点值替换为右节点的值(这样的话就不用处理parent指向的问题)，
     * 这个时候就要处理新节点和替换后节点，当前节点右节点（这个节点要从树中删除）的指向关系
     * 所以就是left=newNode, right=right.right(把right从树种删除);
     *
     *      4                 6
     *     / \               / \
     *    3   6             4   7
     *       / \           / \   \
     *      5   7         3   5   8
     *          \
     *           8
     */
    public void leftRotate(){
        //创建一个当前值 节点，改节点用于替换新树中的当前节点
        AvlNode newNode = new AvlNode(value);
        //将节点的左节点指向当前节点的左节点
        newNode.left=left;
        //将节点的右节点指向当前节点的右节点的左节点
        newNode.right=right.left;
        //右节点替换当前节点
        value=right.value;
        //替换后节点左节点，指向newNode
        left=newNode;
        //替换后节点右节点指向 原节点的right
        right=right.right;
    }


    /**
     * 处理父节点的 左旋
     */
    public void leftRotate2(){
        AvlNode parent = searchParent(value);

        AvlNode up=this.right;
        this.right=right.left;
        right.left=this;

        if(parent!=null&&parent.left==this){
            parent.left=up;
        }
        if(parent!=null&&parent.right==this){
            parent.right=up;
        }

    }

    /**
     * 右旋
     *
     *       10                 8
     *      / \               /   \
     *     8   12            7     10
     *    /  \              /    /  \
     *   7    9            6     9   12
     *  /
     * 6
     */
    public void rightRotate(){
        AvlNode newNode = new AvlNode(value);
        newNode.right=right;
        newNode.left=newNode.left.right;
        //改的是值，所以不用处理parent的指向
        value=left.value;
        right=newNode;
        left=left.left;

    }

    /**
     * 查找
     * @param value
     * @return
     */
    public AvlNode search(int value){
        if(this.value==value){
            return this;
        }else if (this.value>value){
            if(this.left==null){
                return null;
            }
            return this.left.search(value);
        }else{
            if(this.right==null){
                return null;
            }
            return this.right.search(value);
        }
    }

    /**
     * 找指定节点的父节点
     * @param value
     * @return
     */
    public AvlNode searchParent(int value){
        if((this.left!=null && this.left.value==value)||
                (this.right!=null && this.right.value==value)){
            return this;
        }
        if(this.value>value && this.left!=null){
            return this.left.searchParent(value);
        }else if (this.value<value && this.right!=null){
            //这里是不能用小于等于的，假如是value是root节点，用小于等于的话，
            // 假如右子树中有值和root节点相同的节点，这个时候找到的父节点不为null，而实际应该为Null
            return  this.right.searchParent(value);
        }else{
            return null;
        }
    }

    /**
     * 找到指的节点 左子树中的最大值节点
     * @return
     */
    public AvlNode searchLeftMax(){
        AvlNode x=left;
        while (x!=null&& x.right!=null){
            x=x.right;
        }
        return x;
    }
}