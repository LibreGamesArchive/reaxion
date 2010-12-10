package com.googlecode.reaxion.game.ability;

import com.googlecode.reaxion.game.model.Model;
import com.googlecode.reaxion.game.model.character.Character;
import com.googlecode.reaxion.game.model.character.MajorCharacter;
import com.googlecode.reaxion.game.state.StageGameState;
import com.jme.math.FastMath;

/**
 * Access a comrade's abilities even after they have fallen.
 */
public class Inheritor extends Ability {
	
	public Inheritor() {
		super("Inheritor");
	}
	
	@Override
	public void set(Character c) {
		System.out.println(c.model+" is ready to inherit its partner's will!");
	}
	
	@Override
	public boolean act(Character c, StageGameState b) {
		if (c == b.getPlayer()) {
			Character partner =  b.getPartner();
			if (partner.hp <= 0 && partner.abilities != null) {
				boolean flag = false;
				for (int i=0; i<partner.abilities.length; i++) {
					if (!(partner.abilities[i] instanceof Inheritor) && !(partner.abilities[i] instanceof Synchronize))
						flag = flag || partner.abilities[i].act(c, b);
				}
				if (flag)
					return flag;
			}
		}
		
		return false;
	}
	
	public boolean hit(Character c, StageGameState b, Model other) {
		if (c == b.getPlayer()) {
			Character partner =  b.getPartner();
			if (partner.hp <= 0 && partner.abilities != null) {
				boolean flag = false;
				for (int i=0; i<partner.abilities.length; i++) {
					if (!(partner.abilities[i] instanceof Inheritor) && !(partner.abilities[i] instanceof Synchronize))
						flag = flag || partner.abilities[i].hit(c, b, other);
				}
				if (flag)
					return flag;
			}
		}
		
		return false;
	}
	
	public boolean reactHit(Character c, StageGameState b, Model other) {
		if (c == b.getPlayer()) {
			Character partner =  b.getPartner();
			if (partner.hp <= 0 && partner.abilities != null) {
				boolean flag = false;
				for (int i=0; i<partner.abilities.length; i++) {
					if (!(partner.abilities[i] instanceof Inheritor) && !(partner.abilities[i] instanceof Synchronize))
						flag = flag || partner.abilities[i].reactHit(c, b, other);
				}
				if (flag)
					return flag;
			}
		}
		
		return false;
	}
	
	public boolean heal(Character c, StageGameState b, double hpf) {
		if (c == b.getPlayer()) {
			Character partner =  b.getPartner();
			if (partner.hp <= 0 && partner.abilities != null) {
				boolean flag = false;
				for (int i=0; i<partner.abilities.length; i++) {
					if (!(partner.abilities[i] instanceof Inheritor) && !(partner.abilities[i] instanceof Synchronize))
						flag = flag || partner.abilities[i].heal(c, b, hpf);
				}
				if (flag)
					return flag;
			}
		}
		
		return false;
	}
	
}