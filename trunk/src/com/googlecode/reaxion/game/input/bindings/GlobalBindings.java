package com.googlecode.reaxion.game.input.bindings;

public enum GlobalBindings implements KeyBindings {
	EXIT, MEM_REPORT, SCREENSHOT, TOGGLE_MOUSE;

	@Override
	public String toString() {
		return "GlobalBindings_" + super.toString();
	}
	
}
