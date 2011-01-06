package com.googlecode.reaxion.game.input.bindings;

public enum PlayerBindings implements KeyBindings {
	FORWARD, BACKWARD, LEFT, RIGHT, JUMP, SWITCH,
	ATTACK_1, ATTACK_2, ATTACK_3, HOLD_ATTACK, DASH;

	@Override
	public String toString() {
		return "PlayerBindings_" + super.toString();
	}
	
}