package com.googlecode.reaxion.game.input.bindings;

import com.googlecode.reaxion.game.state.BurstGridGameState;

public enum BurstGridStateBindings implements KeyBindings {
	TRAVERSE_COUNTERCLOCKWISE, TRAVERSE_CLOCKWISE, 
	TRAVERSE_NEXT, TRAVERSE_BACK,
	BUY_NODE, ZOOM,
	RETURN_TO_HGS;
	
	@Override
	public String toString() {
		return BurstGridGameState.NAME + "_" + super.toString();
	}
}
