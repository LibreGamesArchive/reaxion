package com.googlecode.reaxion.game.networking;

import java.io.Serializable;

public class HudInfoContainer implements Serializable {
	public String name;
	public double hp, maxHp;
	
	public HudInfoContainer() {
		name = "";
		hp = maxHp = 0;
	}
}