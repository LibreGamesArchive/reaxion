package com.googlecode.reaxion.game.burstgrid;

import java.util.ArrayList;

import com.googlecode.reaxion.game.ability.Ability;

/** 
 * A simple AbilityNode. Simply stores an Ability in a node.
 * 
 * @author Cy Neita
 */

public class AbilityNode extends BurstNode
{
	public ArrayList<BurstNode> linkedNodes; // the list of all the nodes that this node leads to
	public Ability ab;
	
	public AbilityNode(Ability a, int id){
		super(id);
		ab = a;
	}
	
	public AbilityNode(String s, int id){
		super(id);
		/**
		 * Not yet implemented, but findAbility will return an Ability based on then name of the Ability
		 */
		//ab = findAbility(s);
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