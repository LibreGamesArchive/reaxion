package com.googlecode.reaxion.game.ability;

import com.googlecode.reaxion.game.model.character.Character;
import com.googlecode.reaxion.game.state.StageGameState;

/**
 * Attack power increases at low HP levels.
 */
public class CriticalPoint extends Ability {
	
	private static final double multiplier = 2;
	private double lastHp;
	
	public CriticalPoint() {
		super("Critical Point");
	}
	
	@Override
	public void set(Character c) {
		lastHp = c.hp;
	}
	
	@Override
	public boolean act(Character c, StageGameState b) {
		if (c.hp/c.maxHp <= .1 && lastHp/c.maxHp > .1) {
			c.damageMult *= multiplier;
			System.out.println(c.model+" is ready to go all-out!");
			activate(c, b);
		} else if (lastHp/c.maxHp <= .1 && c.hp/c.maxHp > .1) {
			c.damageMult /= multiplier;
		}
		
		lastHp = c.hp;
		
		return false;
	}
	
}