package com.googlecode.reaxion.game.ability;

import com.googlecode.reaxion.game.model.character.Character;
import com.googlecode.reaxion.game.state.StageGameState;
import com.jme.math.FastMath;

/**
 * Poisons the current target, sapping their HP over time.
 */
public class MedusaEyes extends Ability {
	
	private static final float dpf = .002f;
	
	public MedusaEyes() {
		super("Medusa Eyes");
	}
	
	@Override
	public void set(Character c) {
		System.out.println(c.model+" has a deadly gaze!");
	}
	
	@Override
	public boolean act(Character c, StageGameState b) {
		// if the user is the player
		if (c == b.getPlayer())
			((Character)b.getTarget()).hp = Math.max(((Character)b.getTarget()).hp - dpf, 0);
		else
			// if the user is an opponent
			b.getPlayer().hp = Math.max(b.getPlayer().hp - dpf, 0);
		
		return false;
	}
	
}