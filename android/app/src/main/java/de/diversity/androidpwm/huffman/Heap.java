package de.diversity.androidpwm.huffman;

import java.util.NoSuchElementException;

public class Heap <T extends Comparable<T>> extends BinaryTree<T> implements PriorityQueue<T>{

	/** Default constructor for the heap. */
	public Heap(){
		super();
	}
	
	/** 
	 * Specified constructor for the heap with given data
	 * @param data
	 */
	public Heap( T data ){
		super(data);
	}
	
	/**
	  * Gets some data, inserts it to the heap and recoveries the heap
      * @param data the given data
      */
	public void insert(T data){
		BinaryTreeNode<T> newElement = new BinaryTreeNode<T>(data);        // insert a new node with the given data
		BinaryTreeNode<T> father = findFather();
		newElement.parent = father;
		if(father.isLeaf()) father.left = newElement;   // adds a left child to a parent if the parent was a leaf
		else father.right = newElement;                 // adds a right child if the parent already has a left child
		//System.out.println("Es wurde angefuegt." + toString());
		heapifyUp(newElement);                          // recovery the heapproperty
	}
	
	/**
	 * Calculates the number of leafs.
	 * @return the number of leafs
	 */
	public int numberOfLeafs(int size) {
		int leafNumber =  ((size- (int) (Math.pow(2,getHeight())-1)));     // calculate the number of leafs in the last layer of the heap
		if ((int)(Math.pow(2,getHeight())) == leafNumber ) leafNumber = 0;
		return leafNumber;
	}

	/**
	 * Constructs the binary way to a certain node.
	 * @param leafNumber the leaf to which a binary should be found
	 * @return the binary way
	 */
	private String constructBinaryWay2InsPos(int leafNumber) {
		//The way from root to the last Leaf can be seen as a binary number.
		String binaryStr = Integer.toBinaryString(leafNumber);      // converts the leaf number into a binary string
		String binaryWay = "";
		int height = getHeight();
		if(leafNumber == 0) height++;
		//The binary number must consist of as much numbers as levels in the tree.
		for(int i=0; i < (height-binaryStr.length()); i++) {      // calculates the binary way
			binaryWay+= "0";
		}
		binaryWay+= binaryStr;
		return binaryWay;
	}
	
	/**
	 * Finds the parent of the node that's going to be inserted.
	 * @return returns the father node of a certain leaf
	 */
	private BinaryTreeNode<T> findFather() {
		//System.out.println("NumberOfLeafs:" + m);
		String binaryWay = constructBinaryWay2InsPos(numberOfLeafs(getSize()));
		BinaryTreeNode<T> actNode = this.root;
		// Walk the resulting way and return the last node.
		for(int j=0; j < (binaryWay.length()-1); j++) {
			if(binaryWay.charAt(j) == '0') actNode=actNode.left;
			if(binaryWay.charAt(j) == '1') actNode=actNode.right;
		}
		return actNode;
	}

	private String constructBinaryWay2Lst(int leafNumber){
		int height =getHeight();
		return constructBinaryWay2Pos(leafNumber, height);
	}
	public static String constructBinaryWay2Pos(int leafNumber, int height) {
		//The way from root to the last Leaf can be seen as a binary number.
		String binaryStr = Integer.toBinaryString(leafNumber);      // converts the leaf number into a binary string
		String binaryWay = "";
		//The binary number must consist of as much numbers as levels in the tree.
		for(int i=0; i < (height-binaryStr.length()); i++) {      // calculates the binary way
			binaryWay+= "0";
		}
		binaryWay+= binaryStr;
		return binaryWay;

	}

	/**
	 * Finds the last node that was inserted.
	 * @return the last node that was inserted.
	 */
	private BinaryTreeNode<T> findLastNode() {
		String binaryWay = null;
		if(getSize()> 1) binaryWay = constructBinaryWay2Lst(numberOfLeafs(getSize()-1));
		BinaryTreeNode<T> actNode = root;
		// Walk the resulting way and return the last node.
		if(binaryWay!=null) {
			for(int j=0; j < binaryWay.length(); j++) {
				if(binaryWay.charAt(j) == '0') actNode=actNode.left;
				if(binaryWay.charAt(j) == '1') actNode=actNode.right;
			}
		}
		return actNode;
	}
	
	/**
	 * Returns the heap's smallest element without deleting it.
	 * @return returns the data of the smallest element in the heap
	 * @exception throws NoSuchElementException if the heap size is 0.
	 */
	public T peek ()throws NoSuchElementException{
		if(getSize() == 0){
			throw new NoSuchElementException("Kein Element im Heap!");
		}
		return this.root.getData();
	}
		
	/**
	 * Returns the heap's smallest element, deletes it and recoveries the heap.
	 * @return returns the data of the node that is going to be deleted
	 */
	public T extractMin() {
		BinaryTreeNode<T> lastNode = findLastNode();
		T tmpNodeData = root.getData();
		if(lastNode != root) {
			swap(findLastNode(), root);
            lastNode.remove();
            heapifyDown(root);
        }
		else root.remove();
		return tmpNodeData;
	}

	
	/**
	 * Recoveries the heapproperty by moving an element upwards
	 * @param node is the current node that is compared.
	 */
	public void heapifyUp(BinaryTreeNode<T> node){
		if(node.parent == null) return;
		if(node.parent.getData().compareTo(node.getData()) > 0) swap(node.parent, node);
		else return;
		heapifyUp(node.parent);
	}
	
	/**
	 * Recoveries the heapproperty by moving an element downwards.
	 * @param node is the current node that is compared
	 */
	public void heapifyDown(BinaryTreeNode<T> node){
		if(node.isLeaf()) return;
		BinaryTreeNode<T> smallerNode = new BinaryTreeNode<T>();
		if(node.numChildren() == 2) {
			if(node.left.getData().compareTo(node.right.getData()) > 0) smallerNode = node.right;
			else smallerNode = node.left;
		}
		else smallerNode = node.left;
		if(node.getData().compareTo(smallerNode.getData())> 0) swap(smallerNode,node);
		else return;
		heapifyDown(smallerNode);
	}
	
	/**
	 * Checks the heapproperty.
	 * @return {@code true} if it is fulfilled, false otherwise.
	 */
	@SuppressWarnings("unused")
	private boolean _checkHeapProperty(){
		return recCheckHeapProp(root);
	}

	/**
	 * Checks recursively the heapproperty.
	 * @param node is the current node that is compared
	 * @return {@code true} if the property is fulfilled, false otherwise.
	 */
	private boolean recCheckHeapProp(BinaryTreeNode<T> node) {
		if(node.isLeaf()) return true;
		if (!checkNodeProperty(node) || !recCheckHeapProp(node.left) || !recCheckHeapProp(node.right))
			return false;
		else return true;					//Muss hier eigentlich nicht mehr stehen, denke ich.
	}

	/**
	 * Checks if the nodes are in the correct position in the heap.
	 * @param node is the current node that is compared.
	 * @return {@code true} if the property is fulfilled, false otherwise.
	 */
	private boolean checkNodeProperty(BinaryTreeNode<T> node) {
		if(node.isLeaf() && node!=root) 
			return (node.parent.getData().compareTo(node.getData())<0);
		if(node==root && node.isLeaf()) return true;
		if((node.right.getData().compareTo(node.getData())<0)||(node.left.getData().compareTo(node.getData())<0)||(node.parent.getData().compareTo(node.getData())>0)) return false;
		else return true;
	}
			
	/**
	 * Sorts an given array with heapsort.
	 * @param array is a given array that has to be sorted.
	 */
	public static void heapSort(int[] array){
		int length = array.length;
		Heap<Integer> sortHeap = new Heap<Integer>(new Integer(array[0]));
		for(int i=1; i<length; i++) {
			sortHeap.insert(new Integer(array[i]));
		}
		for(int j=0; j<length; j++) {
			array[j]=sortHeap.extractMin();
		}
	}
	
	
	
}
	