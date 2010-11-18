package com.googlecode.reaxion.game.burstgrid.node;

import java.lang.reflect.InvocationTargetException;
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
		at = findAttack(s);
	}
	
	/**
	 * Try to return an empty Attack with info loaded
	 */
	private Attack findAttack(String s) {
		try {
			return (Attack) Class.forName("com.googlecode.reaxion.game.attack."+s).getConstructors()[0].newInstance();
		} catch (Exception e) {
			return new Attack();
		}
	}
	
	public String toString(){
		return "" + id + " Attack ";
	}
}