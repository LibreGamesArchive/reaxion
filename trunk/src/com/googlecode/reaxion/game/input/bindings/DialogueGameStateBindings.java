package com.googlecode.reaxion.game.input.bindings;

import com.googlecode.reaxion.game.state.DialogueGameState;

public enum DialogueGameStateBindings implements KeyBindings {
	CONTINUE;	
	
	@Override
	public String toString() {
		return DialogueGameState.NAME + "_" + super.toString();
	}
	
}
