package com.googlecode.reaxion.game.attack;

import java.io.Serializable;

public class AttackDisplayInfo implements Serializable {

	public String name;
	public int gaugeCost;
	
	public AttackDisplayInfo() {
		name = "";
		gaugeCost = 0;
	}

}
