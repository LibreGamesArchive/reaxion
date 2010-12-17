package com.googlecode.reaxion.game.overlay;

import com.googlecode.reaxion.game.input.bindings.KeyBindings;

/**
 * Contains methods for {@code Overlay} classes that serve as interactive menus.
 * 
 * @author Brian Clanton
 *
 */
public abstract class MenuOverlay extends GridOverlay {
	
	public MenuOverlay(String name, int baseWidth, int baseHeight, boolean mainOverlay) {
		super(name, baseWidth, baseHeight, mainOverlay);
	}

	/**
	 * Updates the {@code Overlay} based on key input.
	 * @param key The keycode for the key that was pressed
	 */
	public abstract void updateDisplay(KeyBindings k);

}
