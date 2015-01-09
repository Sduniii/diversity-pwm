package de.diversity.androidpwm.huffman;

/**
 * Compiling Version 1.0 A binary tree.
 * 
 * @param <T>
 *            the data type that is stored in the nodes of the tree
 * @author CoMaTeam
 */
public class BinaryTree<T> {
	/**
	 * Inner class for tree nodes. Each node can have at most two children.
	 * 
	 * @param <T>
	 *            the data type that is stored in the node
	 */
	@SuppressWarnings("hiding")
	public class BinaryTreeNode<T> {
		/** Data stored in the node (as reference). */
		private T data;
		/** The parent of the node. */
		public BinaryTreeNode<T> parent;
		/** The left child of the node. */
		public BinaryTreeNode<T> left;
		/** The right child of the node. */
		public BinaryTreeNode<T> right;

		/*****************************************************************************
		 * * Get methods * *
		 ****************************************************************************/

		/**
		 * Method for removing a node.
		 */
		public void remove() {
			if (this.parent != null) {
				if (this.isLeftChild())
					this.parent.left = null;
				else
					this.parent.right = null;
			}
			this.left = null;
			this.right = null;
			this.parent = null;
			// System.out.println("Es wurde ein Knoten entfernt." + toString());
		}

		/**
		 * A default constructor that initializes the node with {@code null} as
		 * data
		 */
		public BinaryTreeNode() {
			this.setData(null);
			this.parent = this.left = this.right = null;
		}

		/**
		 * Constructs a new node in the tree with the specified data.
		 * 
		 * @param data
		 *            the data of the node
		 */
		public BinaryTreeNode(T data) {
			this();
			this.setData(data);
		}

		/**
		 * Returns the number of children of this node. 0 if it is a leaf.
		 * 
		 * @return the number of children of this node
		 */
		public int numChildren() {
			final int number = this.left != null ? this.right != null ? 2 : 1
					: this.right != null ? 1 : 0;

			int rev = 0;
			if (this.left != null)
				++rev;
			if (this.right != null)
				++rev;
			if (rev != number)
				throw new IllegalStateException("counter falsch implementiert");

			return number;
		}

		/**
		 * Decides if the node is a leaf, that means, it has no children.
		 * 
		 * @return {@code true} if the node is a leaf, {@code false} otherwise
		 */
		public boolean isLeaf() {
			return (this.left == null && this.right == null);
		}

		/**
		 * Decides if the node is the tree node of a tree, or not.
		 * 
		 * @return {@code true} if the node is root, {@code false} otherwise
		 */
		public boolean isRoot() {
			return (this == getRoot());
		}

		/**
		 * Decides wheather the node is the left child of the parent, or not.
		 * 
		 * @return {@code true} if the node is the left node of the parent,
		 *         {@code false} otherwise
		 */
		public boolean isLeftChild() {
			return (this.parent != null && this.parent.left == this);
		}

		/**
		 * @return the data
		 */
		public T getData() {
			return data;
		}

		/**
		 * @param data the data to set
		 */
		public void setData(T data) {
			this.data = data;
		}

		/**
		 * Converts the node to a string representation.
		 * 
		 * @return a string representation of the node
		 */
		@Override
		public String toString() {
			return (this.getData() != null ? this.getData().toString() : "*");
		}

		/**
		 * @return the parent
		 */
		public BinaryTreeNode<T> getParent() {
			return parent;
		}

		/**
		 * @param parent the parent to set
		 */
		public void setParent(BinaryTreeNode<T> parent) {
			this.parent = parent;
		}

		/**
		 * @return the left
		 */
		public BinaryTreeNode<T> getLeft() {
			return left;
		}

		/**
		 * @param left the left to set
		 */
		public void setLeft(BinaryTreeNode<T> left) {
			this.left = left;
		}

		/**
		 * @return the right
		 */
		public BinaryTreeNode<T> getRight() {
			return right;
		}

		/**
		 * @param right the right to set
		 */
		public void setRight(BinaryTreeNode<T> right) {
			this.right = right;
		}
	}

	/** flag for debugging */
	protected static boolean _checkMode = false;
	/** Dummy node referencing root of tree */
	protected BinaryTreeNode<T> root;

	/*****************************************************************************
	 * * Constructors * *
	 ****************************************************************************/
	/**
	 * Default constructor, initializes an empty tree.
	 */
	public BinaryTree() {
		this.root = null;
	}

	/**
	 * Constructs a tree of only one node and stores the given data in the root.
	 * 
	 * @param data
	 *            the given data
	 */
	public BinaryTree(T data) {
		this.root = new BinaryTreeNode<T>(data);
	}

	/**
	 * Decides wheather the tree is empty, or not.
	 * 
	 * @return {@code true} if the tree is empty, {@code false} otherwise
	 */
	public boolean isEmpty() {
		return (getRoot() == null);
	}

	/**
	 * Returns the root node of the tree.
	 * 
	 * @return the root node of the tree
	 */
	public BinaryTreeNode<T> getRoot() {
		return this.root;
	}

	/**
	 * Returns the current number of nodes in the tree.
	 * 
	 * @return the current number of nodes in the tree
	 */
	public int getSize() {
		int a = recSize(this.root);
		return a;
	}

	/**
	 * Calculates the size of the tree recursively
	 * 
	 * @param node
	 *            is the current node
	 * @return returns the current size of the tree
	 */
	private int recSize(BinaryTreeNode<T> node) {
		if (node.isLeaf())
			return 1;
		if (node.numChildren() == 2)
			return (recSize(node.left) + recSize(node.right) + 1);
		else
			return (recSize(node.left) + 1);
	}

	/**
	 * Returns the height of the tree.
	 * 
	 * @return the height of the tree
	 */
	public int getHeight() {
		if (getRoot() == null)
			return 0;
		if (getRoot().isLeaf())
			return 0;
		return Math.max(getHeightRec(getRoot().left),
				getHeightRec(getRoot().right)) + 1;
	}

	/**
	 * Calculates the height of the tree recursively
	 * 
	 * @param node
	 *            is the current node
	 * @return returns the current height of the tree
	 */
	public int getHeightRec(BinaryTreeNode<T> node) {
		// System.out.println("start getHeightRecursive");
		if (node == null)
			return 0;
		if (node.isLeaf())
			return 0;
		return Math.max(getHeightRec(node.left), getHeightRec(node.right)) + 1;
	}

	/**
	 * Method for swapping two tree elements.
	 * 
	 * @param nodeA
	 *            is the current node.
	 */
	public void swap(BinaryTreeNode<T> nodeA, BinaryTreeNode<T> nodeB) {
		T tmpNodeData = nodeA.getData();
		nodeA.setData(nodeB.getData());
		nodeB.setData(tmpNodeData);
		// System.out.println("Es wurde getauscht." + toString());
	}

	/*****************************************************************************
	 * * Set methods * *
	 ****************************************************************************/
	/**
	 * Switches to debugging mode and between normal mode.
	 * 
	 * @param mode
	 *            decides if debugging mode is active, or not
	 */
	public static void setCheckMode(boolean mode) {
		_checkMode = mode;
	}

	/*****************************************************************************
	 * * Conversion methods * *
	 ****************************************************************************/
	/**
	 * Recursively traverses the tree, appends output of level to given
	 * StringBuffer
	 * 
	 * @param strbuf
	 *            StringBuffer to append to
	 * @param level
	 *            current level in tree (for indentation)
	 * @param node
	 *            current tree node
	 */
	private void _tree2string(StringBuffer strbuf, int level,
			BinaryTreeNode<T> node) {
		if (node.right != null)
			_tree2string(strbuf, level + 1, node.right);
		for (int i = 0; i < level; ++i)
			strbuf.append("    ");
		if (!node.isRoot())
			strbuf.append(node.isLeftChild() ? "/" : "\\");
		strbuf.append(node + "\n");
		if (node.left != null)
			_tree2string(strbuf, level + 1, node.left);
	}

	/**
	 * Returns a string representation of the tree.
	 * 
	 * @return string representation of tree
	 */
	@Override
	public String toString() {
		StringBuffer strbuf = new StringBuffer(this.getClass() + ": ");
		if (isEmpty())
			strbuf.append("EMPTY\n");
		else {
			strbuf.append("\n");
			_tree2string(strbuf, 0, getRoot());
		}
		return strbuf.toString();
	}

	/*****************************************************************************
	 * * Debugging methods * *
	 ****************************************************************************/
	/**
	 * Checks the consistency of links in the entire tree.
	 * 
	 * @return {@code true} if the links consistent in tree, {@code false}
	 *         otherwise
	 */
	protected boolean _checkTree() {
		if (_checkMode) {
			// check parent-child-consistency...
			if (isEmpty())
				return true;
			boolean ret = true;
			if (this.root.left != null)
				ret = ret && checkNode(this.root.left);
			if (this.root.right != null)
				ret = ret && checkNode(this.root.right);
			return ret && this.root.parent == null;
		} else
			return true;
	}

	/**
	 * Checks the validity of a node and its child nodes. It is checked that the
	 * parents of the child nodes point back to the father node.
	 * 
	 * @param node
	 *            the node from where the check is started
	 * @return {@code true} if the subtree at {@code node} is valid, {@code
	 *         false} otherwise
	 */
	private boolean checkNode(BinaryTreeNode<T> node) {
		boolean ret = true;
		if (node.isLeftChild() && node.parent.left != node)
			ret = false;
		else if (node.parent.right != node)
			ret = false;
		if (node.left != null)
			ret = ret && checkNode(node.left);
		if (node.right != null)
			ret = ret && checkNode(node.right);
		return ret;
	}
	
	public int getAmountOfLeafs(){
		return this.calculateLeafs(root);
	}
	
	/**
	 * a recursive method to calculate the amount of leafs - might be incorrect
	 * 
	 * @param node
	 * @return height of the subTree
	 */
	public int calculateLeafs(BinaryTreeNode<T> node){
		if (!node.left.isLeaf() && !node.right.isLeaf()){
			return ( calculateLeafs(node.left) + calculateLeafs(node.right) );
		}
		if (!node.left.isLeaf()){
			return ( calculateLeafs(node.left));
		}
		
		if (!node.right.isLeaf()){
			return (calculateLeafs(node.right));
		}
		
		return 1;
		
	}
}
