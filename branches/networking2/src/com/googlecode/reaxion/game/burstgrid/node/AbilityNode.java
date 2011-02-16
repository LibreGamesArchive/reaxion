package com.googlecode.reaxion.game.burstgrid.node;

import java.util.ArrayList;

import com.googlecode.reaxion.game.ability.Ability;
import com.googlecode.reaxion.game.attack.Attack;

/** 
 * A simple AbilityNode. Simply stores an Ability in a node.
 * 
 * @author Cy Neita
 */

public class AbilityNode extends BurstNode
{
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
		ab = findAbility(s);
	}
	
	/**
	 * Try to return an empty Ability with info loaded
	 */
	private Ability findAbility(String s) {
		try {
			return (Ability) Class.forName("com.googlecode.reaxion.game.ability."+s).getConstructors()[0].newInstance();
		} catch (Exception e) {
			return new Ability("");
		}
	}
	
	public String toString(){
		return "" + id + " Ability ";
	}
}