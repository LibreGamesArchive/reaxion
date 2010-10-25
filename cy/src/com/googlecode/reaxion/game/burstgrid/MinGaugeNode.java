package com.googlecode.reaxion.game.burstgrid;

import java.util.ArrayList;

/** 
 * A simple MinGaugeNode. Will have functionality included to manipulate the augment to the Min Gauge it provides.
 * 
 * @author Cy Neita
 */

public class MinGaugeNode extends BurstNode
{
	public ArrayList<BurstNode> linkedNodes; // the list of all the nodes that this node leads to
	public int minGPlus;
	
	public MinGaugeNode(int id){
		super(id);
		minGPlus = 50;
	}
	
	public MinGaugeNode(int hp, int id){
		super(id);
		minGPlus = hp;
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
}