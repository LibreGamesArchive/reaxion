package com.googlecode.reaxion.game.input.bindings;

public enum DebugBindings implements KeyBindings {
	TOGGLE_WIRE, TOGGLE_NORMALS, TOGGLE_LIGHTS, 
	TOGGLE_DEPTH, TOGGLE_STATS, TOGGLE_BOUNDS;

	@Override
	public String toString() {
		return "DebugBindings_" + super.toString();
	}
	
}
