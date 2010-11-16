package com.googlecode.reaxion.game.burstgrid.node;

import java.util.ArrayList;

/** 
 * A simple HPNode. Will have functionality included to manipulate the augment to HP it provides.
 * 
 * @author Cy Neita
 */

public class HPNode extends BurstNode
{
	public ArrayList<BurstNode> linkedNodes; // the list of all the nodes that this node leads to
	public int hpPlus;
	
	public HPNode(int id){
		super(id);
		hpPlus = 50;
	}
	
	public HPNode(int hp, int id){
		super(id);
		hpPlus = hp;
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
	
	@Override
	public void print(){
		System.out.print(id + " HP ");
	}
}