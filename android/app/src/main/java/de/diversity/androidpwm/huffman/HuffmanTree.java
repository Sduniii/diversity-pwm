package de.diversity.androidpwm.huffman;

import java.util.ArrayList;

/**
 * A Huffman Tree
 * @author co2-128
 *
 */

public class HuffmanTree extends BinaryTree<HuffmanToken> implements Comparable<HuffmanTree>{

	public ArrayList<Integer> chars = new ArrayList<Integer>();
	public ArrayList<String> codeWords = new ArrayList<String>();
	
	public HuffmanTree(){
		this.root = new BinaryTreeNode<HuffmanToken>(new HuffmanToken(0,0));	// wurzel mit nem dummytoken initialisieren
	}
	/** Constructor for a node that contains a huffman token.
	 * @param huffmantoken is the data of a given huffman token
	 */
	public HuffmanTree(HuffmanToken huffmantoken){
		this.root = new BinaryTreeNode<HuffmanToken>( huffmantoken );
	}
	
	
	public synchronized int compareTo(HuffmanTree huffmanTree) {
    	if(huffmanTree.root.getData().getFreq() < this.root.getData().getFreq()) return 1;
    	if(huffmanTree.root.getData().getFreq() > this.root.getData().getFreq() ) return -1;
    	else return 0;
	}
	
	public synchronized static HuffmanTree concatTwoHuffmanTrees(HuffmanTree lowest_freq, HuffmanTree lowest2_freq){
		HuffmanTree newFather = new HuffmanTree();	// neuen gemeinsamen Vater erstellen
		newFather.root.getData().freq = (lowest_freq.root.getData().freq) + (lowest2_freq.root.getData().freq);	
		newFather.root.left = lowest2_freq.root;	
		newFather.root.right = lowest_freq.root;	// die geringere haeufigkeit wirds rechts rangehangen
		return newFather;
	}
		
	/** Every inner node must have two children.
	 * 
	 * @param node is the node that is checked.
	 * @return true if everything is ok, otherwise false.
	 */
	public synchronized boolean checkTwoChildren(BinaryTreeNode<HuffmanToken> node){
		if( (getSize() > 2) && (!node.isLeaf()) ){	// mindestens 2 Zeichen wurden kodiert und knoten ist innerer knoten/kein blatt
			if(node.numChildren() != 2){
				return false;
			}
			return true;
		}
		else return true;	// bei nur einem kodierten zeichen kanns eigentlich nur true geben
	}	
			
	/** Checks if the frequency of the father contains the sum of the frequencies of its children */
	public synchronized boolean checkRootFreq(BinaryTreeNode<HuffmanToken> father){
		if( ((father.left.getData().getFreq())+(father.right.getData().getFreq())) == father.getData().getFreq() ){
			return true;
		}
		return false;
	}
	
	public synchronized ArrayList<String> makeCodeTable(){
		chars = new ArrayList<Integer>();
		codeWords = new ArrayList<String>();
		
		// Baum durchlaufen...
		recMakeCodeTable(root, "", "");
		
		ArrayList<String> codes = new ArrayList<String>();
		for(int i = 0; i < codeWords.size(); i++)
			codes.add(codeWords.get(i));
		
		return codes;
	}
	
	/* Claculates the height as int under the assumption that the tree is full*/
	public synchronized int getHeightThroughLeafNum(BinaryTreeNode<HuffmanToken> node){
		if(node.isRoot() && node != null) return 1;
		else
			return getHeightThroughLeafNum(node.parent) + 1;
	}
	
	public synchronized void recMakeCodeTable(BinaryTreeNode<HuffmanToken> node,  String lstStep, String way) {
		way += lstStep;
		if(node.isLeaf()){
			codeWords.add(way);
			chars.add(node.getData().getC());
			return;
		}
		else { recMakeCodeTable(node.left,  "0" ,  way);
			if (node.numChildren()==2) recMakeCodeTable(node.right,  "1" ,  way);
		}
	}

	
	public synchronized void constructWayToLeaf(String way, int cha) {
		if(way.length()<1) {
			root.setData(new HuffmanToken(cha));
			return;
		}
		BinaryTreeNode<HuffmanToken> tmpNode = root;
		
		/*Builds the tree along the way.*/
		for (int i = 0; i < way.length(); i++) {
			
			if(way.charAt(i)=='0') {
				if(tmpNode.left == null) {
					tmpNode.left = new BinaryTreeNode<HuffmanToken>();
					tmpNode.setData(new HuffmanToken());
				}
				tmpNode.left.parent = tmpNode;
				tmpNode = tmpNode.left;
			}
			
			if(way.charAt(i)=='1'){
				if(tmpNode.right == null) {
					tmpNode.right = new BinaryTreeNode<HuffmanToken>();
					tmpNode.setData(new HuffmanToken());
				}
				tmpNode.right.parent = tmpNode;
				tmpNode = tmpNode.right;
			}
		}
		 
		tmpNode.setData(new HuffmanToken(cha));
		
	}

}
