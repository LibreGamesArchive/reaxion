package com.googlecode.reaxion.game.ability;

import com.googlecode.reaxion.game.model.Model;
import com.googlecode.reaxion.game.model.attackobject.AngelSword;
import com.googlecode.reaxion.game.model.character.Character;
import com.googlecode.reaxion.game.model.prop.GlowRing;
import com.googlecode.reaxion.game.state.StageGameState;
import com.googlecode.reaxion.game.util.LoadingQueue;

/**
 * Passive ability that adds extra functionality to {@code Character} functions.
 * All functions return {@code true} if they replace and override normal 
 * {@code Character} function and return {@code false} if they supplement or do
 * not modify the default {@code Character} functionality. All specialized
 * abilities should extend this base class.
 * @author Khoa Ha
 *
 */
public class Ability {
	
	private String name;
	
	public Ability(String n) {
		name = n;
	}
	
	/**
	 * Called by Character only when ability is first set up. Thus, it has no
	 * return value since it does not conflict with any default functions.
	 */
	public void set(Character c) {	
	}
	
	/**
	 * Called by Character at the start of the {@code act()} function, returns
	 * true if the function is to be interrupted, false if otherwise.
	 */
	public boolean act(Character c, StageGameState b) {
		return false;
	}
	
	/**
	 * Called by Character at the start of the {@code hit()} function, returns
	 * true if the function is to be interrupted, false if otherwise.
	 */
	public boolean hit(Character c, StageGameState b, Model other) {
		return false;
	}
	
	/**
	 * Called by Character at the start of the {@code reactHit()} function,
	 * returns true if the function is to be interrupted, false if otherwise.
	 */
	public boolean reactHit(Character c, StageGameState b, Model other) {
		return false;
	}
	
	/**
	 * Called by Character at the start of the {@code heal()} function,
	 * returns true if the function is to be interrupted, false if otherwise.
	 */
	public boolean heal(Character c, StageGameState b, double hpf) {
		return false;
	}
	
	/**
	 * When an ability is set off, create a ring around the character to show it.
	 */
	public void activate(Character c, StageGameState b) {
		LoadingQueue.quickLoad(new GlowRing(c), b);
		b.getRootNode().updateRenderState();
	}
	
	public String toString() {
		return name;
	}
	
}