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
	
	public String toString(){
		return "" + id + " HP ";
	}
}