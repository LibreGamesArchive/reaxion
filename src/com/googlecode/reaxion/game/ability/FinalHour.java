package com.googlecode.reaxion.game.ability;

import com.googlecode.reaxion.game.model.character.Character;
import com.googlecode.reaxion.game.state.StageGameState;
import com.jme.math.FastMath;

public class FinalHour extends Ability {
	
	private static final int changeRate = 20;
	private int count = 0;
	
	public FinalHour() {
		super("Final Hour");
		description = "Gauge goes crazy when HP reaches critical levels.";
	}
	
	@Override
	public boolean act(Character c, StageGameState b) {
		if (c.hp/c.maxHp <= .1) {
			if (count == changeRate) {
				System.out.println(c.model+"'s gauge is berserking!");
				c.gauge = (2/(FastMath.nextRandomFloat()+1) - 1)*c.maxGauge;
				count = 0;
			} else {
				count++;
			}
		}
		
		return false;
	}
	
}