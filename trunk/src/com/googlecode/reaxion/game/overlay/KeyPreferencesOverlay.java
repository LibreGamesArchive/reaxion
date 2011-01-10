package com.googlecode.reaxion.game.overlay;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.googlecode.reaxion.game.input.bindings.BurstGridStateBindings;
import com.googlecode.reaxion.game.input.bindings.DialogueGameStateBindings;
import com.googlecode.reaxion.game.input.bindings.GameBindings;
import com.googlecode.reaxion.game.input.bindings.HubGameStateBindings;
import com.googlecode.reaxion.game.input.bindings.KeyBindings;
import com.googlecode.reaxion.game.input.bindings.MenuBindings;
import com.googlecode.reaxion.game.input.bindings.PlayerBindings;
import com.jme.input.KeyBindingManager;
import com.jme.input.KeyInput;
import com.jme.renderer.ColorRGBA;
import com.jme.scene.Node;
import com.jme.scene.shape.Quad;

/**
 * Displays a scroll menu to select bindings and prompts for new keys.
 * @author Khoa Ha
 *
 */
public class KeyPreferencesOverlay extends Overlay {
	
	public static final String NAME = "keyPreferencesOverlay";	
	private static final String baseURL = "com/googlecode/reaxion/resources/gui/";
	
	private String[] bindingText = {
			"Menu - Up",
			"Menu - Down",
			"Menu - Left",
			"Menu - Right",
			"Menu - Select Item",
			"Menu - Finalize",
			"Menu - Cancel",
			"Game - Forward",
			"Game - Back",
			"Game - Right",
			"Game - Left",
			"Game - Jump",
			"Game - Dash",
			"Game - Attack Toggle",
			"Game - Attack 1/4",
			"Game - Attack 2/5",
			"Game - Attack 3/6",
			"Game - Tag Switch",
			"Game - Pause",
			"Camera - Pivot Up",
			"Camera - Pivot Down",
			"Camera - Pivot Left",
			"Camera - Pivot Right",
			"Camera - Lock-On Toggle",
			"Camera - Target Closer",
			"Camera - Target Further",
			"Hub - Open Terminal",
			"Hub - Close Terminal",
			"Dialogue - Advance Text",
			"BurstGrid - Cycle Forward",
			"BurstGrid - Cycle Back",
			"BurstGrid - Step Forward",
			"BurstGrid - Step Back",
			"BurstGrid - Buy Node",
			"BurstGrid - Toogle Zoom",
			"BurstGrid - Exit",
	};
	
	private KeyBindings[] bindings;
	private KeyBindings[] menuBindings = {
			MenuBindings.UP,
			MenuBindings.DOWN,
			MenuBindings.LEFT,
			MenuBindings.RIGHT,
			MenuBindings.SELECT_ITEM,
			MenuBindings.SELECT_FINAL,
			MenuBindings.BACK
	};
	private KeyBindings[] gameBindings = {
			PlayerBindings.FORWARD,
			PlayerBindings.BACKWARD,
			PlayerBindings.RIGHT,
			PlayerBindings.LEFT,
			PlayerBindings.JUMP,
			PlayerBindings.DASH,
			PlayerBindings.HOLD_ATTACK,
			PlayerBindings.ATTACK_1,
			PlayerBindings.ATTACK_2,
			PlayerBindings.ATTACK_3,
			PlayerBindings.SWITCH,
			GameBindings.TOGGLE_PAUSE,
			GameBindings.CAM_UP,
			GameBindings.CAM_DOWN,
			GameBindings.CAM_LEFT,
			GameBindings.CAM_RIGHT,
			GameBindings.CAM_MODE,
			GameBindings.TARGET_NEAR,
			GameBindings.TARGET_FAR,
			HubGameStateBindings.ACCESS_TERMINAL,
			HubGameStateBindings.CLOSE_TERMINAL
	};
	private KeyBindings[] dialogueBindings = {
			DialogueGameStateBindings.CONTINUE
	};
	private KeyBindings[] burstGridBindings = {
			BurstGridStateBindings.TRAVERSE_CLOCKWISE,
			BurstGridStateBindings.TRAVERSE_COUNTERCLOCKWISE,
			BurstGridStateBindings.TRAVERSE_NEXT,
			BurstGridStateBindings.TRAVERSE_BACK,
			BurstGridStateBindings.BUY_NODE,
			BurstGridStateBindings.ZOOM,
			BurstGridStateBindings.RETURN_TO_HGS
	};
	
	private String[] keys;
	
	private Node container;
	
	public ScrollMenu menu;
	public ScrollMenu keysMenu;
	
	public KeyPreferencesOverlay() {
		super(NAME);
		
		// create a container Node for scaling
        container = new Node("container");
        
        // combine all bindings
        List<KeyBindings> all = new ArrayList<KeyBindings>(menuBindings.length + gameBindings.length + dialogueBindings.length + burstGridBindings.length);
        Collections.addAll(all, menuBindings);
        Collections.addAll(all, gameBindings);
        Collections.addAll(all, dialogueBindings);
        Collections.addAll(all, burstGridBindings);
        bindings = all.toArray(new KeyBindings[] {});
        
        // grab keys
        keys = new String[bindings.length];
        retrieveKeys();
        
        // create scroll menu for names of bindings
        menu = new ScrollMenu(400, 32, 12, bindingText);
        menu.setLocalTranslation(240, 120, 0);
        menu.update();
        
        // create scroll menu for current keys
        keysMenu = new ScrollMenu(128, 32, 12, keys);
        keysMenu.enableScrollBar();
        keysMenu.setLocalTranslation(512, 120, 0);
        keysMenu.update();
        
        Quad bg = drawRect(800, 600, new ColorRGBA(1, .5f, .5f, 1));
        bg.setLocalTranslation(400, 300, 0);
        
        // attach children
        attachChild(container);
        container.attachChild(bg);
        container.attachChild(menu);
        container.attachChild(keysMenu);
        
        container.setLocalScale((float) height/600);
    }
	
	/**
	 * Scroll the scroll bars by a specified amount.
	 */
	public void scroll(int amount) {
		menu.changeIndex(amount, true);
		menu.remainVisible();
		menu.update();
		
		keysMenu.changeIndex(amount, true);
		keysMenu.remainVisible();
		keysMenu.update();
	}
	
	/**
	 * Updates all keys according to their bindings.
	 */
	private void retrieveKeys() {
		KeyInput input = KeyInput.get();
		KeyBindingManager manager = KeyBindingManager.getKeyBindingManager();
		for (int i=0; i<keys.length; i++) {
			String cmd = input.getKeyName(manager.getKeyCodesForCommand(bindings[i].toString()).get(0).keys[0]);
			keys[i] = cmd;
		}
	}
	
	/**
	 * Refresh the keys list.
	 */
	public void refreshKeys() {
		retrieveKeys();
		keysMenu.setEntries(keys);
		keysMenu.update();
	}
	
	/**
	 * Check whether a key binding will be unique within its state.
	 * @param k Key binding to check
	 * @param keyCode New key code to assign
	 * @return True if unique, false if otherwise
	 */
	public boolean verifyUnqiue(KeyBindings k, int keyCode) {
		KeyBindingManager manager = KeyBindingManager.getKeyBindingManager();
		
		KeyBindings[] check = new KeyBindings[0];
		if (k.toString().contains("MenuBindings"))
			check = menuBindings;
		else if (k.toString().contains("PlayerBindings") || k.toString().contains("GameBindings") || k.toString().contains("hubGameState"))
			check = gameBindings;
		else if (k.toString().contains("dialogueGameState"))
			check = dialogueBindings;
		else if (k.toString().contains("burstGridGameState"))
			check = burstGridBindings;
		for (int i=0; i<check.length; i++)
			if (keyCode == manager.getKeyCodesForCommand(check[i].toString()).get(0).keys[0])
				return false;
		
		return true;
	}
	
	/**
	 * Get binding at index {@code ind}.
	 */
	public KeyBindings getBinding(int ind) {
		return bindings[ind];
	}
	
}