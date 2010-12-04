package com.googlecode.reaxion.game.ability;

import com.googlecode.reaxion.game.model.character.Character;
import com.googlecode.reaxion.game.state.StageGameState;
import com.jme.math.FastMath;

public class Charity extends Ability {
	
	private final float frac = .25f;
	
	public Charity() {
		super("Charity");
		description = "The user shares a fraction of their gauge returns with their partner.";
	}
	
	@Override
	public boolean act(Character c, StageGameState b) {
		if (b.getPlayer() == c && b.getPartner().hp > 0) {
			b.getPlayer().gauge = Math.max(b.getPlayer().gauge - c.gaugeRate*frac, 0);
			b.getPartner().gauge = Math.min(b.getPartner().gauge + c.gaugeRate*frac, b.getPartner().maxGauge);
		}
		return false;
	}
	
}