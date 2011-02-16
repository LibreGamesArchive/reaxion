package com.googlecode.reaxion.game.ability;

import com.googlecode.reaxion.game.model.Model;
import com.googlecode.reaxion.game.model.character.Character;
import com.googlecode.reaxion.game.state.StageGameState;

public class Masochist extends Ability {
	
	private static float ratio = 1/3f;
	
	public Masochist() {
		super("Masochist");
		description = "Increases gauge drive proportional to damage received.";
	}
	
	@Override
	public boolean reactHit(Character c, StageGameState b, Model other) {
		// increase gauge proportional to damage proportion
		c.gauge = Math.min(c.gauge + ratio * other.getDamage()/c.maxHp * c.maxGauge, c.maxGauge);
		System.out.println(c.model+" is growing stronger from the pain!");
		activate(c, b);
		return false;
	}
	
}