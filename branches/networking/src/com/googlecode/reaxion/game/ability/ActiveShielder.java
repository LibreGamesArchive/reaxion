package com.googlecode.reaxion.game.ability;

import com.googlecode.reaxion.game.attack.Attack;
import com.googlecode.reaxion.game.attack.ShieldBarrier;
import com.googlecode.reaxion.game.attack.ShieldMediguard;
import com.googlecode.reaxion.game.attack.ShieldReflega;
import com.googlecode.reaxion.game.model.character.Character;
import com.googlecode.reaxion.game.state.StageGameState;

/**
 * Recovers some gauge points when a Barrier type attack is used.
 */
public class ActiveShielder extends Ability {
	
	private Attack lastAttack;
	
	public ActiveShielder() {
		super("Active Shielder");
	}
	
	@Override
	public boolean act(Character c, StageGameState b) {
		// check if attack was used since last time
		if (lastAttack != c.currentAttack) {
			// recover gauge if a barrier attack was used
			if (c.currentAttack instanceof ShieldBarrier) {
				c.gauge = Math.min(c.gauge+1, c.maxGauge);
				activate(c, b);
			} else if (c.currentAttack instanceof ShieldMediguard) {
				c.gauge = Math.min(c.gauge+4, c.maxGauge);
				activate(c, b);
			} else if (c.currentAttack instanceof ShieldReflega) {
				c.gauge = Math.min(c.gauge+6, c.maxGauge);
				activate(c, b);
			}
		}
		lastAttack = c.currentAttack;
		return false;
	}
	
}