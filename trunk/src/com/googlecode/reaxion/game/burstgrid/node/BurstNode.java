package com.googlecode.reaxion.game.burstgrid.node;

import java.util.ArrayList;

import com.jme.math.Vector3f;

/** 
 * The superclass of a node in the BurstGrid [to be renamed, perhaps]. Each BurstNode has some number 
 * of other nodes it links to. Each of these nodes is either an HPBoost, Gauge Boost, or a new Ability.
 * New types of nodes may be added in the future
 * 
 * @author Cy Neita
 */

public class BurstNode
{
	public ArrayList<BurstNode> nodes;
	public ArrayList<Integer> costs;
	public Vector3f vect;
	public int id; //ID number of the node; used to find nodes in the tree
	public boolean activated; //checks to see if this node has been activated by the player
	
	public BurstNode(int idNo){
		id = idNo;
		nodes = new ArrayList<BurstNode>();
		costs = new ArrayList<Integer>();
	}
	
	public void addConnection(BurstNode b, int cost){
		nodes.add(b);
		costs.add(cost);
	}
	
	public String toString(){
		return "" + id;
	}

	public void setVect(Vector3f vect) {
		this.vect = vect;
	}
}