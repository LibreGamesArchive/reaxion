package com.googlecode.reaxion.game.overlay;

import com.jme.input.KeyBindingManager;
import com.jme.input.KeyInput;

/**
 * Contains methods for {@code Overlay} classes that serve as interactive menus.
 * 
 * @author Brian Clanton
 *
 */
public abstract class MenuOverlay extends GridOverlay {
	
	public static final String UP = "menu_up", DOWN = "menu_down",
	LEFT = "menu_left", RIGHT = "menu_right", SELECT = "menu_select", 
	SELECT_FINAL = "menu_select_final", GO_BACK = "menu_go_back";
	
	public MenuOverlay(String name, int baseWidth, int baseHeight, boolean mainOverlay) {
		super(name, baseWidth, baseHeight, mainOverlay);
		
		activate();
	}

	/**
	 * Updates the {@code Overlay} based on key input.
	 * @param key The keycode for the key that was pressed
	 */
	public abstract void updateDisplay(int key);

	/**
	 * Initializes key bindings for this {@code MenuOverlay}.
	 */
	protected void initKeyBindings() {
		KeyBindingManager manager = KeyBindingManager.getKeyBindingManager();
		
		manager.set(UP, KeyInput.KEY_UP);
		manager.set(DOWN, KeyInput.KEY_DOWN);
		manager.set(LEFT, KeyInput.KEY_LEFT);
		manager.set(RIGHT, KeyInput.KEY_RIGHT);
	}
	
	/**
	 * Removes key bindings initialized by this {@code MenuOverlay}.
	 */
	protected void removeKeyBindings() {
		KeyBindingManager manager = KeyBindingManager.getKeyBindingManager();
		
		manager.remove(UP);
		manager.remove(DOWN);
		manager.remove(LEFT);
		manager.remove(RIGHT);
	}
	
	/**
	 * Initializes any key bindings and performs other secondary initalization tasks.
	 */
	public void activate() {
		initKeyBindings();
	}
	
	/**
	 * Removes any key bindings and clears anything created by 
	 * the {@code MenuOveray}.
	 */
	public void deactivate() {
		removeKeyBindings();
	}

}
