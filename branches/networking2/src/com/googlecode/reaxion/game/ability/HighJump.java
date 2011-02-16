package com.googlecode.reaxion.game.ability;

import com.googlecode.reaxion.game.model.character.Character;
import com.googlecode.reaxion.game.state.StageGameState;
import com.jme.math.FastMath;

/**
 * Increases character's jumping height by double.
 */
public class HighJump extends Ability {
	
	public HighJump() {
		super("High Jump");
		description = "Doubles user's jumping height.";
	}
	
	@Override
	public void set(Character c) {
		System.out.println(c.model+" can jump higher!");
		c.gravity /= 2;
	}
	
}