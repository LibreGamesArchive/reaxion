package com.googlecode.reaxion.game.input.bindings;

import com.googlecode.reaxion.game.state.HubGameState;

public enum HubGameStateBindings implements KeyBindings {
	ACCESS_TERMINAL, CLOSE_TERMINAL;
	
	@Override
	public String toString() {
		return HubGameState.NAME + "_" + super.toString();
	}
}
