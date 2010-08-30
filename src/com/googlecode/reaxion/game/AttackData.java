package com.googlecode.reaxion.game;

/**
 * Data container class that will hold all data needed for all attacks
 * to simplify {@code Attack} parameters
 */
public class AttackData {

	public Character character;
	public Model target;
	public int gc;
	
	public AttackData() {
	
	}
	
	public AttackData(Character c) {
		init(c, null);
	}
	
	public AttackData(Character c, Model t) {
		init(c, t);
	}
	
	private void init(Character c, Model t) {
		character = c;
		target = t;
	}
	
}
