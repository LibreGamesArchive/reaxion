package com.googlecode.reaxion.game.ability;

import com.googlecode.reaxion.game.model.character.Character;

/**
 * Increases character's running speed by 1.1x.
 */
public class FleetFooted extends Ability {
	
	public FleetFooted() {
		super("Fleet Footed");
	}
	
	@Override
	public void set(Character c) {
		System.out.println(c.model+" can run faster!");
		c.speed *= 1.1f;
	}
	
}