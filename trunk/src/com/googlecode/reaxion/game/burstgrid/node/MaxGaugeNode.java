package com.googlecode.reaxion.game.burstgrid.node;

import java.util.ArrayList;

/** 
 * A simple MaxGaugeNode. Will have functionality included to manipulate the augment to the Max Gauge it provides.
 * 
 * @author Cy Neita
 */

public class MaxGaugeNode extends BurstNode
{
	public int maxGPlus;
	
	public MaxGaugeNode(int id){
		super(id);
		maxGPlus = 50;
	}
	
	public MaxGaugeNode(int mg, int id){
		super(id);
		maxGPlus = mg;
	}
	
	public String toString(){
		return "" + id + " MaxGauge ";
	}
}