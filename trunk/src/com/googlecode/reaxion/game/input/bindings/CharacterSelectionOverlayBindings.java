package com.googlecode.reaxion.game.input.bindings;

import com.googlecode.reaxion.game.overlay.CharacterSelectionOverlay;

public enum CharacterSelectionOverlayBindings implements KeyBindings {
	CHOOSE_1, CHOOSE_2, UNDO_CHOICE;
	
	@Override
	public String toString() {
		return CharacterSelectionOverlay.NAME + "_" + super.toString();
	}
}
