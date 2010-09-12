package com.googlecode.reaxion.game.ability;

import com.googlecode.reaxion.game.model.character.Character;
import com.googlecode.reaxion.game.state.BattleGameState;
import com.jme.math.FastMath;

/**
 * Heals a small amount of HP on every frame
 */
public class PassiveHealer extends Ability {
	
	private static final float hpf = .01f;
	
	public PassiveHealer() {
		super("Passive Healer");
	}
	
	@Override
	public boolean act(Character c, BattleGameState b) {
		c.heal(b, hpf);
		return false;
	}
	
}