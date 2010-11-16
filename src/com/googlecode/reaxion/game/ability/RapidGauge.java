package com.googlecode.reaxion.game.ability;

import com.googlecode.reaxion.game.model.character.Character;

/**
 * Increases character's gauge rate by a factor of 1.1x.
 */
public class RapidGauge extends Ability {
	
	public RapidGauge() {
		super("Evasive Start");
	}
	
	@Override
	public void set(Character c) {
		System.out.println(c.model+" is charging the gauge fast!");
		c.gaugeRate *= 1.1;
	}
	
}