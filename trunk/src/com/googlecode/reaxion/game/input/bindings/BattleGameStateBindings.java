package com.googlecode.reaxion.game.input.bindings;

import com.googlecode.reaxion.game.state.BattleGameState;

public enum BattleGameStateBindings implements KeyBindings {
	RETURN_TO_HGS;
	
	@Override
	public String toString() {
		return BattleGameState.NAME + "_" + super.toString();
	}
	
}
