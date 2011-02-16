package com.googlecode.reaxion.game.input.bindings;

public enum MenuBindings implements KeyBindings {
	UP, DOWN, LEFT, RIGHT, SELECT_ITEM, SELECT_FINAL, BACK,
	CHOOSE_1, CHOOSE_2, CHOOSE_3, CHOOSE_4, CHOOSE_5, CHOOSE_6;

	@Override
	public String toString() {
		return "MenuBindings_" + super.toString();
	}
	
}
