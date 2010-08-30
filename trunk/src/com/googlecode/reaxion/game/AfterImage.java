package com.googlecode.reaxion.game;

import com.jme.math.FastMath;
import com.jme.math.Vector3f;

/**
 * Random chance of teleporting when about to get hit. Rises with HP loss.
 */
public class AfterImage extends Ability {
	
	private static final float lowerThreshold = 0;
	private static final float upperThreshold = .8f;
	
	private Vector3f prevVel = new Vector3f();
	private Vector3f jumpSpot;
	private int stepCount = 1;
	
	public AfterImage() {
		super("After Image");
	}
	
	@Override
	public boolean act(Character c, BattleGameState b) {
		if (stepCount == 1) {
			// store first position as possible jumpSpot
			jumpSpot = c.model.getLocalTranslation().clone();
		} else {
			// if character was moving, store location as future jump point at random
			if (prevVel.x != 0 || prevVel.z != 0) {
				if (FastMath.nextRandomFloat() < 1/Math.sqrt(stepCount)) {
					jumpSpot = c.model.getLocalTranslation().clone();
				}
			}
		}
		prevVel = c.getVelocity().normalize();
		stepCount++;
		return false;
	}
	
	@Override
	public boolean hit(Character c, BattleGameState b, Model other) {
		// calculate chance of activation
		float chance = (float)(c.maxHp - c.hp) / c.maxHp * (upperThreshold - lowerThreshold) + lowerThreshold;
		if (FastMath.nextRandomFloat() < chance) {
			System.out.println(c.model+" teleported!");
			// teleport away
			 c.model.setLocalTranslation(jumpSpot);
			 b.getStage().contain(c);
			return true;
		} else {
			// do nothing
			return false;
		}
	}
	
}