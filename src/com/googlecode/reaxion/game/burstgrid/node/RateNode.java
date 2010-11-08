package com.googlecode.reaxion.game.burstgrid.node;

import java.util.ArrayList;

/** 
 * A simple RateNode. Will have functionality included to manipulate the augment to the Max Gauge it provides.
 * 
 * @author Cy Neita
 */

public class RateNode extends BurstNode
{
	public ArrayList<BurstNode> linkedNodes; // the list of all the nodes that this node leads to
	public int rate;
	
	public RateNode(int id){
		super(id);
		rate = 1;
	}
	
	public RateNode(int r, int id){
		super(id);
		rate = r;
	}
	
	public String toString(){
		return "" + id + " Rate ";
	}
}