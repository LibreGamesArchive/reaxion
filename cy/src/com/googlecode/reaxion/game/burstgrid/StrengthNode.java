package com.googlecode.reaxion.game.burstgrid;

import java.util.ArrayList;

/** 
 * A simple HPNode. Will have functionality included to manipulate the augment to HP it provides.
 * 
 * @author Cy Neita
 */

public class StrengthNode extends BurstNode
{
	public ArrayList<BurstNode> linkedNodes; // the list of all the nodes that this node leads to
	public int strengthPlus;
	
	public StrengthNode(int id){
		super(id);
		strengthPlus = 1;
	}
	
	public StrengthNode(int str, int id){
		super(id);
		strengthPlus = str;
	}
	
	/**
	 * Returns an ArrayList of all the BurstNodes attached to this Node
	 * @return
	 */
	public ArrayList<BurstNode> getLinkedNodes(){
		return linkedNodes;
	}
	
	/**
	 * Sets the ArrayList of all the BurstNodes attached to this Node
	 * @return
	 */
	public void setLinkedNodes(ArrayList<BurstNode> newNodes){
		linkedNodes = newNodes;
	}
	
	/**
	 * Adds additional linked nodes to this node
	 * @return
	 */
	public void addLinkedNode(ArrayList<BurstNode> newNodes){
		for(BurstNode bn : newNodes)
			linkedNodes.add(bn);
	}
	
	public void print(){
		System.out.print(id + " Strength");
	}
}