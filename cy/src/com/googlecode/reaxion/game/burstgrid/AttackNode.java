package com.googlecode.reaxion.game.burstgrid;

import java.util.ArrayList;

import com.googlecode.reaxion.game.attack.Attack;

/** 
 * A simple AttackNode. Simply stores an Attack in a node.
 * 
 * @author Cy Neita
 */

public class AttackNode extends BurstNode
{
	public ArrayList<BurstNode> linkedNodes; // the list of all the nodes that this node leads to
	public Attack ab;
	
	public AttackNode(Attack a, int id){
		super(id);
		ab = a;
	}
	
	public AttackNode(Attack a, int id, ArrayList<BurstNode> nextNodes){
		super(id);
		ab = a;
		linkedNodes = nextNodes;
	}
	
	public AttackNode(String s, int id){
		super(id);
		/**
		 * Not yet implemented, but findAttack will return an Attack based on the name of the Attack
		 */
		//ab = findAttack(s);
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
		System.out.print(id + " Attack");
	}
}