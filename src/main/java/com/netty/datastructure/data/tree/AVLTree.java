package com.netty.datastructure.data.tree;

/**
 * 网上别人写的平衡树，
 * 有删除方法，以递归的方式找到要删除的节点，然后可以判断寻找改节点的链路上所有节点，来判断是否失衡
 */
public class AVLTree {
 
	private class Node {
		private Node left;
		private int value;
		private Node right;
		private int height = -1;
 
		private Node(int value) {
			this.value = value;
		}
 
		private Node() {
 
		}
	}
	
	Node head;
	public AVLTree() {
		
	}
 
	private int Height(Node root) {
		if(root == null) {
			return -1;
		}else {
			return root.height;
		}
	}
	
	//LL旋转（右旋）
	//x为失衡点，有元素插入至x的左子树w的左孩子
	private Node singleRotateRight(Node x) {
		Node w = x.left;
		x.left = w.right;
		w.right = x;
		x.height = Math.max(Height(x.left), Height(x.right))+1;
		w.height = Math.max(Height(w.left), Height(w.right))+1;
		return w;
	}
	
	//RR旋转（左旋）
	//有元素插入至x的右子树w的右孩子
	private Node singleRotateLeft(Node x) {
		Node w = x.right;
		x.right = w.left;
		w.left = x;
		x.height = Math.max(Height(x.left), Height(x.right))+1;
		w.height = Math.max(Height(w.left), Height(w.right))+1;
		return w;
	}
	
	//LR旋转，先左旋x，再右旋z
	//失衡点z，有元素插入其左子树x的右子树w
	private Node doubleRotateRight(Node z) {
		z.left = singleRotateLeft(z.left);
		return singleRotateRight(z);
	}
	
	//RL旋转，先右旋x，再左旋z
	//失衡点z，有元素插入其右子树x的左子树w
	private Node doubleRotateLeft(Node z) {
		z.right = singleRotateRight(z.right);
		return singleRotateLeft(z);
	}
	
	//插入
	private Node insert(Node root,int value) {
		if(root == null) {
			root = new Node(value);
		}else if (value < root.value) {
			//插入将发生在左子树
			root.left = insert(root.left, value);
			//失衡
			if(Height(root.left)-Height(root.right) == 2) {
				if(value < root.left.value) {
					//插入发生在root的左子树
					//LL插入，右旋
					root = singleRotateRight(root);
				}else {
					//LR插入，先左旋再右旋
					root = doubleRotateRight(root);
				}
			}
		}else if(value > root.value) {
			//插入将发生在右子树
			root.right = insert(root.right, value);
			if(Height(root.left)-Height(root.right)==2) {
				//失衡
				if(value>root.right.value) {
					//失衡发生在root的右子树
					//RR插入，左旋
					root = singleRotateLeft(root);
				}else {
					//RL插入，先右旋再左旋
					root = doubleRotateLeft(root);
				}
			}
		}
		root.height = Math.max(Height(root.left), Height(root.right))+1;
		return root;
	}
	
	private Node deleteNode(Node root,int value) {
		if(root == null) {
			//没找到说明不存在，不存在不做任何操作
			return root;
		}
		if(value < root.value) {
			//删除可能发生在左子树
			root.left = deleteNode(root.left, value);
		}else if(value > root.value) {
			//删除可能发生在右子树
			root.right = deleteNode(root.right, value);
		}else {
			//找到值为value的结点
			if(root.left!=null && root.right!=null) {
				//如果当前结点左右子树不为空，
				//找到右（左）子树中最小（大）值，赋值给当前结点
				//将问题转换为删除右（左）子树中值最小（大）的结点
				Node pre = root.right;
				while(pre.left != null) {
					pre = pre.left;
				}
				root.value = pre.value;
				root.right = deleteNode(root.right, pre.value);
			}else {
				//左右结点仅有一个或者都为空
				//实际的删除就发送在下面这条语句中，
				//当执行root.right = deleteNode(root.right, pre.value)再次进入方法至此
				//因为该节点是叶子节点，左右子树为空
				//拿左右子树将其覆盖即为删除该节点
				root = (root.left != null) ? root.left : root.right;
			}
		}
		//删除完成，调整平衡性
		if(root == null) {
			return root;
		}
		root.height = Math.max(Height(root.left), Height(root.right))+1;
		//失衡
		if(Height(root.left)-Height(root.right)>=2) {
			//删除发生在右子树，模拟插入发生在左子树
			if(Height(root.left.left) > Height(root.left.right)) {
				//插入发生在左子树，LL旋转
				return singleRotateRight(root);
			}else {
				//LR旋转
				return doubleRotateRight(root);
			}
		}else if(Height(root.left)-Height(root.right)<=-2){
			//删除发生在左子树，模拟插入发生在右子树
			if(Height(root.right.left) > Height(root.right.right)) {
				//RL旋转
				return doubleRotateLeft(root);
			}else {
				//RR旋转
				return singleRotateLeft(root);
			}
		}
		//未失衡，不做操作
		return root;
	}
	
	public void delete(int value) {
		head = deleteNode(head, value);
	}
	
	public void add(int value) {
		head = insert(head, value);
	}
	
	// 中序遍历
	private void print(Node node) {
		if (node == null) {
			return;
		}
		print(node.left);
		System.out.print(node.value+" ");
		print(node.right);
	}
 
	public void showTree() {
		print(head);
		System.out.println();
	}
}