package com.googlecode.reaxion.game.ability;

import com.googlecode.reaxion.game.model.character.Character;
import com.googlecode.reaxion.game.state.StageGameState;
import com.jme.math.FastMath;

/**
 * Introduces random chance of throwing tracking off Character on each frame.
 * Has no effect of AI targeting.
 */
public class EvasiveStart extends Ability {
	
	private static final float chance = .015f;
	
	public EvasiveStart() {
		super("Evasive Start");
		description = "Allows the User to escape lock-on tracking on occasion.";
	}
	
	@Override
	public boolean act(Character c, StageGameState b) {
		if (b.getTarget() == c && b.cameraMode == "lock" && FastMath.nextRandomFloat() <= chance) {
			System.out.println(c.model+" escaped tracking!");
			b.cameraMode = "free";
			activate(c, b);
		}
		return false;
	}
	
}