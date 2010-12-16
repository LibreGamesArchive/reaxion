package com.googlecode.reaxion.game.input.bindings;

public enum GameBindings implements KeyBindings {
	CAM_UP, CAM_DOWN, CAM_LEFT, CAM_RIGHT,
	TARGET_NEAR, TARGET_FAR, CAM_MODE,
	TOGGLE_PAUSE;

	@Override
	public String toString() {
		return "GameBindings_" + super.toString();
	}
	
}
