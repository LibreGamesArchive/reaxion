package com.googlecode.reaxion.game.model.character;

import java.io.Serializable;

public enum CharacterName implements Serializable {

	ANDREW("i_andrew3"), AUSTIN("i_austin2"), BRIAN("i_brian2"), CY("i_cy2"), KHOA(
			"i_khoa6"), MONICA("i_monica7"), NILAY("i_nilay4"), SHINE(
			"i_shine6");

	public String filename, name;
	public float speed;

	private CharacterName() {
		type();
	}
	
	private CharacterName(String filename) {
		this.filename = filename;
		type();
	}
	
	private void type() {
		speed = .5f;
	}
}
