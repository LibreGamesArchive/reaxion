package com.googlecode.reaxion.game.util;

import java.io.Serializable;
import java.util.ArrayList;

import com.googlecode.reaxion.game.burstgrid.node.BurstNode;


public class BurstGridSerializer implements Serializable{

	ArrayList<BurstNode> burstGrid = new ArrayList<BurstNode>();
	
	public BurstGridSerializer(){
		
	}
}