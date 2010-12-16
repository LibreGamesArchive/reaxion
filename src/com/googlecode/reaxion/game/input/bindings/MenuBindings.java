package com.googlecode.reaxion.game.input.bindings;

public enum MenuBindings implements KeyBindings {
	UP, DOWN, LEFT, RIGHT, SELECT_ITEM, SELECT_FINAL, BACK;

	@Override
	public String toString() {
		return "MenuBindings_" + super.toString();
	}
	
}
