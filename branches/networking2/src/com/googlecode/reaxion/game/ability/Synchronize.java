package com.googlecode.reaxion.game.ability;

import com.googlecode.reaxion.game.model.Model;
import com.googlecode.reaxion.game.model.attackobject.AngelSword;
import com.googlecode.reaxion.game.model.character.Character;
import com.googlecode.reaxion.game.model.character.MajorCharacter;
import com.googlecode.reaxion.game.model.prop.GlowRing;
import com.googlecode.reaxion.game.state.StageGameState;
import com.googlecode.reaxion.game.util.LoadingQueue;

public class Synchronize extends Ability {
	
	
	public Synchronize() {
		super("Synchronize");
		description = "Copies current target's ability powers.";
	}
	
	@Override
	public void set(Character c) {
		System.out.println(c.model+" can mimic its foe's abilities!");
	}
	
	public boolean act(Character c, StageGameState b) {
		if (b.getTarget() instanceof MajorCharacter &&
				((MajorCharacter)(b.getTarget())).abilities[0] != null)
			return ((MajorCharacter)(b.getTarget())).abilities[0].act(c, b);
		return false;
	}
	
	public boolean hit(Character c, StageGameState b, Model other) {
		if (b.getTarget() instanceof MajorCharacter &&
				((MajorCharacter)(b.getTarget())).abilities[0] != null)
			return ((MajorCharacter)(b.getTarget())).abilities[0].hit(c, b, other);
		return false;
	}
	
	public boolean reactHit(Character c, StageGameState b, Model other) {
		if (b.getTarget() instanceof MajorCharacter &&
				((MajorCharacter)(b.getTarget())).abilities[0] != null)
			return ((MajorCharacter)(b.getTarget())).abilities[0].reactHit(c, b, other);
		return false;
	}
	
	public boolean heal(Character c, StageGameState b, double hpf) {
		if (b.getTarget() instanceof MajorCharacter &&
				((MajorCharacter)(b.getTarget())).abilities[0] != null)
			return ((MajorCharacter)(b.getTarget())).abilities[0].heal(c, b, hpf);
		return false;
	}
	
}