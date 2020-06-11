package com.netty.datastructure.data.tree;

/**
 * @author 86136
 */
public class BinarySortTreeDemo {
    public static void main(String[] args) {
        int[] arr={7,3,4,5,8,9};
        BsTree bsTree = new BsTree();
        for(int ele:arr){
            bsTree.add(new BsNode(ele));
        }
        bsTree.infixOrder();
        bsTree.deleNode(8);
        System.out.println("删除后");
        bsTree.infixOrder();
    }

}

class BsTree{
    private BsNode root;

    /**
     * 添加节点
     * @param bsNode
     */
    public void add(BsNode bsNode){
        if(root==null){
            root=bsNode;
        }else{
            root.add(bsNode);
        }
    }

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
        BsNode node = this.search(value);
        if(node==null){
            System.out.println("节点未找到或已被删除");
            return;
        }
        //找到该节点的父节点
        BsNode parent =this.searchParent(value);

        //删除逻辑
        if(node.left==null&&node.right==null){
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
            BsNode leftMax = node.searchLeftMax();

            //删除左子树最大节点
            //这里很重要，因为是递归删除，所以不用在这个if语句中考虑这个leftmax 的左子节点问题，因为在删除该leftmax的时候会考虑
            deleNode(leftMax.value);

            //左子树最大节点代替node
            //注意这个判断，左子树最大节点的父节点如果是要删除的节点，那么久不需要这一步，不然久会leftMax.left=leftMax
            if(node.left!=leftMax){
                leftMax.left=node.left;
            }

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
     * 删除节点
     * 将要删除的节点有一个子节点，和没有子节点的情况放到一起考虑
     * @param value
     */
    public void deleNode2(int value){
        if(root==null){
            System.out.println("空树..");
            return;
        }

        //找到该节点
        BsNode node = this.search(value);
        if(node==null){
            System.out.println("节点未找到或已被删除..");
            return;
        }
        //找到该节点的父节点
        BsNode parent =this.searchParent(value);

        //删除逻辑
        if(node.left!=null && node.right!=null){
            //节点有两个子节点
            BsNode leftMax = node.searchLeftMax();

            //删除左子树最大节点
            //这里很重要，因为是递归删除，所以不用在这个if语句中考虑这个leftmax 的左子节点问题，因为在删除该leftmax的时候会考虑
            deleNode(leftMax.value);

            //左子树最大节点代替node
            //注意这个判断，左子树最大节点的父节点如果是要删除的节点，那么久不需要这一步，不然久会leftMax.left=leftMax
            if(node.left!=leftMax){
                leftMax.left=node.left;
            }

            leftMax.right=node.right;

            //parent为空node为root,也要替换
            if(parent==null){
                root=leftMax;
            }
        }else{
            //有一个子节点，或者没有子节点
            BsNode child;
            if (node.left !=null) {
                //这个时候left !=null，那么right 肯定为null
                child = node.left;
            } else {
                //这个时候left==null,right不确定，
                // 假如 right!=null ,parent.right=node.node,为null的话也可以这样处理，
                // 两个子节点为Null的话parent指向node的点也为null
                child = node.right;
            }

            if(parent!=null){
                if(parent.left==node){
                    parent.left=child;
                }else{
                    parent.right=child;
                }
            }else{
                root=child;
            }
        }
    }

    /**
     * 查找节点
     * @param value
     * @return
     */
    public BsNode search(int value){
        if(root==null){
            System.out.println("空树");
            return null;
        }
        return root.search(value);
    }

    /**
     * 查找节点
     * @param value
     * @return
     */
    public BsNode searchParent(int value){
        if(root==null){
            System.out.println("空树");
            return null;
        }
        return root.searchParent(value);
    }

    /**
     * 中序遍历
     */
    public void infixOrder(){
        if(root==null){
            return;
        }
        root.infixOrder();
    }
}

class BsNode{
    int value;
    BsNode left;
    BsNode right;

    public BsNode(int value) {
        this.value = value;
    }

    /**
     * 添加节点
     * @param node
     */
    public void add(BsNode node){
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
    }

    /**
     * 添加节点
     * 非递归
     * @param node
     */
    public void add2(BsNode node){
        if(node==null){
            return;
        }
        BsNode x=this;
        BsNode y=null;
        while (x!=null){
            y=x;
            if(node.value<x.value){
                x=x.left;
            }else{
                x=x.right;
            }
        }
        //while循环后 y 就是node的父节点
        if(node.value<y.value){
            y.left=node;
        }else{
            y.right=node;
        }
    }

    /**
     * 查找
     * @param value
     * @return
     */
    public BsNode search(int value){
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
     * 查找
     * 非递归
     * @param value
     * @return
     */
    public BsNode search2(int value){
        BsNode curNode=this;
        while (curNode!=null && curNode.value!=value){
            if(curNode.value>value){
                curNode=curNode.left;
            }else {
                curNode=curNode.right;
            }
        }
        return curNode;
    }

    /**
     * 找指的节点的父节点
     * @param value
     * @return
     */
    public BsNode searchParent(int value){
        if((this.left!=null && this.left.value==value)||
                (this.right!=null && this.right.value==value)){
            return this;
        }
        if(this.value>value && this.left!=null){
            return this.left.searchParent(value);
        }else if (this.value<value && this.right!=null){
            //这里是不能用小于等于的，假如是value是root节点，用小于等于的，
            // 假如右子树中有值和root节点相同的节点，这个时候找到的父节点不为null，而实际应该为Null
            return  this.right.searchParent(value);
        }else{
            return null;
        }
    }

    /**
     * 找指的节点的父节点
     * 非递归
     * @param value
     * @return
     */
    public BsNode searchParent2(int value){
        BsNode x=this;
        BsNode y=null;

        while (x!=null && x.value!=value){
            y=x;
            if(x.value>value){
                x=x.left;
            }else {
                x=x.right;
            }
        }
        return y;
    }

    /**
     * 找到指的节点 左子树中的最大值节点
     * @return
     */
    public BsNode searchLeftMax(){
        BsNode x=left;
        while (x!=null&& x.right!=null){
            x=x.right;
        }
        return x;
    }

    /**
     * 找到指的节点 右子树中的最小值节点
     * @return
     */
    public BsNode searchRightMax(){
        BsNode x=right;
        while (x!=null&& x.left!=null){
            x=x.left;
        }
        return x;
    }

    public void infixOrder(){
        if(this.left!=null){
            this.left.infixOrder();
        }

        System.out.println(value);

        if(this.right!=null){
            this.right.infixOrder();
        }

    }
}
