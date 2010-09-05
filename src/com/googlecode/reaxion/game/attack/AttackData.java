package com.googlecode.reaxion.game.attack;

import com.googlecode.reaxion.game.model.Model;
import com.googlecode.reaxion.game.model.character.Character;

/**
 * Data container class that will hold all data needed for all attacks
 * to simplify {@code Attack} parameters
 */
public class AttackData {

	public Character character;
	public Character[] friends;
	public Model target;
	public int gc;
	
	public AttackData() {
	
	}
	
	public AttackData(Character c) {
		init(c, new Character[0], null);
	}
	
	public AttackData(Character c, Model t) {
		init(c, new Character[0], t);
	}
	
	public AttackData(Character c, Character[] f, Model t) {
		init(c, f, t);
	}
	
	private void init(Character c, Character[] f, Model t) {
		character = c;
		friends = f;
		target = t;
	}
	
}
