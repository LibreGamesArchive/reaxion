package com.googlecode.reaxion.game.burstgrid;

import java.util.ArrayList;

/** 
 * A simple MaxGaugeNode. Will have functionality included to manipulate the augment to the Max Gauge it provides.
 * 
 * @author Cy Neita
 */

public class MaxGaugeNode extends BurstNode
{
	public ArrayList<BurstNode> linkedNodes; // the list of all the nodes that this node leads to
	public int maxGPlus;
	
	public MaxGaugeNode(int id){
		super(id);
		maxGPlus = 50;
	}
	
	public MaxGaugeNode(int mg, int id){
		super(id);
		maxGPlus = mg;
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
		System.out.print(id + " MaxGauge");
	}
}