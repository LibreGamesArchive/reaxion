package com.googlecode.reaxion.game.ability;

import com.googlecode.reaxion.game.model.Model;
import com.googlecode.reaxion.game.model.character.Character;
import com.googlecode.reaxion.game.state.StageGameState;

public class Insurance extends Ability {
	
	private static final float dpf = .005f;
	
	public Insurance() {
		super("Insurance");
		description = "Reduces HP over time in exchange for halving damage when hit.";
	}
	
	@Override
	public boolean act(Character c, StageGameState b) {
		if (c.hp > 1)
			c.hp = Math.max(c.hp - dpf, 0);
		
		return false;
	}
	
	@Override
	public boolean reactHit(Character c, StageGameState b, Model other) {
		System.out.println(c.model+" is insured against hits!");
		
		c.hp += other.getDamage() / 2;
		activate(c, b);
		
		return false;
	}
	
}