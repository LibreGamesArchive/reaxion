package com.googlecode.reaxion.game.ability;

import com.googlecode.reaxion.game.model.character.Character;
import com.googlecode.reaxion.game.state.StageGameState;

/**
 * Introduces amount of HP recovered when a healing move is used.
 */
public class HealingFactor extends Ability {
	
	private static final double factor = 1.5;
	
	public HealingFactor() {
		super("Healing Factor");
	}
	
	@Override
	public boolean heal(Character c, StageGameState b, double hpf) {
		c.hp = Math.min(c.hp+hpf*factor, c.maxHp);
		return true;
	}
	
}