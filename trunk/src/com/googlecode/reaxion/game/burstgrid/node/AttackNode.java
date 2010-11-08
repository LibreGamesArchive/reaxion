package com.googlecode.reaxion.game.burstgrid.node;

import java.util.ArrayList;

import com.googlecode.reaxion.game.attack.Attack;

/** 
 * A simple AttackNode. Simply stores an Attack in a node.
 * 
 * @author Cy Neita
 */

public class AttackNode extends BurstNode
{
	public Attack at; //The Attack
	
	public AttackNode(Attack a, int id){
		super(id);
		at = a;
	}
	
	public AttackNode(Attack a, int id, ArrayList<BurstNode> nextNodes){
		super(id);
		at = a;
	}
	
	public AttackNode(String s, int id){
		super(id);
		/**
		 * Not yet implemented, but findAttack will return an Attack based on the name of the Attack
		 */
		//ab = findAttack(s);
	}
	
	public String toString(){
		return "" + id + " Attack ";
	}
}