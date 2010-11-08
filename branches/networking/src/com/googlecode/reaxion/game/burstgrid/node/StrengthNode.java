package com.googlecode.reaxion.game.burstgrid.node;

import java.util.ArrayList;

/** 
 * A simple HPNode. Will have functionality included to manipulate the augment to HP it provides.
 * 
 * @author Cy Neita
 */

public class StrengthNode extends BurstNode
{
	public int strengthPlus;
	
	public StrengthNode(int id){
		super(id);
		strengthPlus = 1;
	}
	
	public StrengthNode(int str, int id){
		super(id);
		strengthPlus = str;
	}
	
	public void print(){
		System.out.print(id + " Strength ");
	}
}