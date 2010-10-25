package com.googlecode.reaxion.game.ability;

import com.googlecode.reaxion.game.model.Model;
import com.googlecode.reaxion.game.model.character.Character;
import com.googlecode.reaxion.game.state.StageGameState;

/**
 * Steps in to save partner from a fatal hit.
 */
public class Chivalry extends Ability {
	
	
	public Chivalry() {
		super("Chivalry");
	}
	
	@Override
	public boolean hit(Character c, StageGameState b, Model other) {
		
		if (c.hp - other.damagePerFrame <= 0 && b.getPartner().hp - other.damagePerFrame > 0) {
			activate(c, b);
			b.tagSwitch();
			b.getPlayer().hit(b, other);
			System.out.println(b.getPlayer().model+" is being chivalrous!");
			return true;
		}
		
		return false;
	}
	
}