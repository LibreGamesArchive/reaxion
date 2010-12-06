package com.googlecode.reaxion.game.util;

import com.jme.input.KeyBindingManager;
import com.jme.input.KeyInput;

public class KeyBindingUtils {

	public static void addKeyBinding(KeyBindings kb, int keycode) {
		KeyBindingManager.getKeyBindingManager().set(kb.toString(), keycode);
	}
	
	public static void removeKeyBinding(KeyBindings kb) {
		KeyBindingManager.getKeyBindingManager().remove(kb.toString());
	}
	
	public static boolean checkKeyBinding(KeyBindings kb, boolean repeat) {
		return KeyBindingManager.getKeyBindingManager().isValidCommand(kb.toString(), repeat);
	}
	
	public static void initGlobalBindings() {
		addKeyBinding(GlobalBindings.EXIT, KeyInput.KEY_ESCAPE);
		addKeyBinding(GlobalBindings.MEM_REPORT, KeyInput.KEY_R);
		addKeyBinding(GlobalBindings.SCREENSHOT, KeyInput.KEY_F1);
		addKeyBinding(GlobalBindings.TOGGLE_MOUSE, KeyInput.KEY_M);
	}
	
	private interface KeyBindings {
		
	}
	
	public enum GameBindings implements KeyBindings {
		CAM_UP, CAM_DOWN, CAM_LEFT, CAM_RIGHT,
		TARGET_NEAR, TARGET_FAR, CAMERA_MODE,
		TOGGLE_PAUSE;
	}

	public enum DebugBindings implements KeyBindings {
		TOGGLE_WIRE, TOGGLE_NORMALS, TOGGLE_LIGHTS, 
		TOGGLE_DEPTH, TOGGLE_STATS;
	}
	
	public enum GlobalBindings implements KeyBindings {
		EXIT, MEM_REPORT, SCREENSHOT, TOGGLE_MOUSE;
	}
	
	public enum MenuBindings implements KeyBindings {
		UP, DOWN, LEFT, RIGHT, SELECT_ITEM, SELECT_FINAL, BACK,
		ACCESS_TERMINAL
	}
	
	public enum PlayerBindings implements KeyBindings {
		FORWARD, BACKWARD, LEFT, RIGHT, JUMP, SWITCH,
		ATTACK_1, ATTACK_2, ATTACK_3, HOLD_ATTACK;
	}
	
}
