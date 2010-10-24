package com.googlecode.reaxion.game.ability;

import com.googlecode.reaxion.game.model.Model;
import com.googlecode.reaxion.game.model.character.Character;
import com.googlecode.reaxion.game.state.StageGameState;
import com.jme.math.FastMath;
import com.jme.math.Vector3f;

/**
 * Reduces HP over time in exchange for halving when hit.
 */
public class Insurance extends Ability {
	
	private static final float dpf = .005f;
	
	public Insurance() {
		super("Insurance");
	}
	
	@Override
	public boolean act(Character c, StageGameState b) {
		c.hp = Math.max(c.hp - dpf, 0);
		
		return false;
	}
	
	@Override
	public boolean reactHit(Character c, StageGameState b, Model other) {
		System.out.println(c.model+" is insured against hits!");
		
		c.hp += other.damagePerFrame / 2;
		activate(c, b);
		
		return false;
	}
	
}