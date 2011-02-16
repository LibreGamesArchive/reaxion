package com.googlecode.reaxion.game.ability;

import com.googlecode.reaxion.game.model.character.Character;
import com.googlecode.reaxion.game.state.StageGameState;
import com.jme.math.FastMath;

public class Soloist extends Ability {
	
	public Soloist() {
		super("Soloist");
		description = "Sacrifices tag switching for increased gauge recovery.";
	}
	
	@Override
	public void set(Character c) {
		System.out.println(c.model+" ");
		c.tagLock = true;
		c.gaugeRate *= 5f/3f;
	}
	
	@Override
	public boolean act(Character c, StageGameState b) {
		c.tagLock = true;
		return false;
	}
	
}