package com.googlecode.reaxion.game;

import com.radakan.jme.mxml.anim.MeshAnimationController;

/**
 * Temporarily acts upon the attacking {@code Character} for the duration
 * of the attack, controlling animations and creating {@code AttackObjects}.
 * All attacks should extend this class.
 * 
 * @author Khoa Ha
 *
 */
public class Attack {
	
	public static String name;
	
	public int gaugeCost;
	
	// Some useful variables
	protected int frameCount = 0;
	protected int phase = 0;
	
	/**
	 * The one that initiated the attack
	 */
	protected Character character;
	
	// creating an Attack with these constructors is useless, as they only
	// exist to facilitate backend stuff
	public Attack() {}
	public Attack(AttackData ad) {}
	
	public Attack(AttackData ad, int gc) {
		character = ad.character;
		character.currentAttack = this;
		gaugeCost = gc;
		checkGauge();
	}
	
	/**
	 * Contains all the commands to load required AttackObjects, override to
	 * add functionality.
	 */
	public void load() {
	}
	
	/**
	 * Events to be executed on each act() call for character
	 */
	public void enterFrame(BattleGameState b) {
		if (frameCount == 0) {
			firstFrame(b);
		} else {
			nextFrame(b);
		}
		frameCount++;
	}
	
	/**
	 * Events to be executed on the first frame of the attack, override to add
	 * functionality.
	 */
	public void firstFrame(BattleGameState b) {
		
	}
	
	/**
	 * Events to be executed on subsequent frames of the attack, override to add
	 * functionality.
	 */
	public void nextFrame(BattleGameState b) {
		
	}
	
	/**
	 * Called when the attacking character is interrupted by Model m, override to
	 * add functionality. Default functions as if character was hit as normal.
	 */
	public void interrupt(BattleGameState b, Model m) {
		character.reactHit(b, m);
	}
	
	/**
	 * Called to dismiss the attack, usually only done by the attack itself.
	 * Override to add functionality.
	 */
	public void finish() {
		character.currentAttack = null;
	}
	
	/**
	 * Checks if the character's gauge is high enough to use the attack. If so,
	 * deduct it accordingly. Otherwise, finish the attack before it starts. Return
	 * whether deduction was successful or not.
	 */
	protected boolean checkGauge() {
		if (character.gauge >= gaugeCost) {
			character.gauge = Math.max(Math.min(character.minGauge, character.gauge) - gaugeCost, 0);
			return true;
		} else {
			finish();
			return false;
		}
	}
	
}
