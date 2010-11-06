package com.googlecode.reaxion.game.burstgrid;

import java.util.ArrayList;

/** 
 * A simple MinGaugeNode. Will have functionality included to manipulate the augment to the Min Gauge it provides.
 * 
 * @author Cy Neita
 */

public class MinGaugeNode extends BurstNode
{
	public int minGPlus;
	
	public MinGaugeNode(int id){
		super(id);
		minGPlus = 50;
	}
	
	public MinGaugeNode(int mg, int id){
		super(id);
		minGPlus = mg;
	}
	
	public void print(){
		System.out.print(id + " MinGauge ");
	}
}