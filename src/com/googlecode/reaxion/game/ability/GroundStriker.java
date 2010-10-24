package com.googlecode.reaxion.game.ability;

import com.googlecode.reaxion.game.model.character.Character;
import com.googlecode.reaxion.game.state.StageGameState;
import com.jme.math.FastMath;

/**
 * Sacrifices jumping ability for unmatched ground speed.
 */
public class GroundStriker extends Ability {
	
	public GroundStriker() {
		super("Ground Striker");
	}
	
	@Override
	public void set(Character c) {
		System.out.println(c.model+" is a land shark!");
		c.jumpLock = true;
		c.speed *= 1.4f;
	}
	
	@Override
	public boolean act(Character c, StageGameState b) {
		c.jumpLock = true;
		return false;
	}
	
}