package com.googlecode.reaxion.game.util;

import com.googlecode.reaxion.game.overlay.CharacterSelectionOverlay;
import com.googlecode.reaxion.game.overlay.MissionOverlay;
import com.googlecode.reaxion.game.overlay.StageSelectionOverlay;
import com.googlecode.reaxion.game.overlay.TerminalOverlay;
import com.jme.input.KeyBindingManager;
import com.jme.input.KeyInput;

public class KeyBindingUtils {

	private static final String[] menus = {TerminalOverlay.NAME, StageSelectionOverlay.NAME, CharacterSelectionOverlay.NAME, MissionOverlay.NAME};
	
	private static KeyBindingManager manager = KeyBindingManager.getKeyBindingManager();
	
	public static void initKeyBindings() {
		initGlobalBindings();
		initPlayerBindings();
		
		for (String s : menus)
			initMenuBindings(s);
	}
	
	private static void initGlobalBindings() {
		manager.set(GlobalBindings.EXIT.toString(), KeyInput.KEY_ESCAPE);
		manager.set(GlobalBindings.MEM_REPORT.toString(), KeyInput.KEY_R);
		manager.set(GlobalBindings.SCREENSHOT.toString(), KeyInput.KEY_F1);
		manager.set(GlobalBindings.TOGGLE_MOUSE.toString(), KeyInput.KEY_M);
	}
	
	private static void initPlayerBindings() {
		manager.set(PlayerBindings.FORWARD.toString(), KeyInput.KEY_NUMPAD8);
		manager.set(PlayerBindings.BACKWARD.toString(), KeyInput.KEY_NUMPAD2);
		manager.set(PlayerBindings.RIGHT.toString(), KeyInput.KEY_NUMPAD6);
		manager.set(PlayerBindings.LEFT.toString(), KeyInput.KEY_NUMPAD4);
		manager.set(PlayerBindings.JUMP.toString(), KeyInput.KEY_NUMPAD0);
		manager.set(PlayerBindings.HOLD_ATTACK.toString(), KeyInput.KEY_Z);
		manager.set(PlayerBindings.ATTACK_1.toString(), KeyInput.KEY_X);
		manager.set(PlayerBindings.ATTACK_2.toString(), KeyInput.KEY_C);
		manager.set(PlayerBindings.ATTACK_3.toString(), KeyInput.KEY_V);
		manager.set(PlayerBindings.SWITCH.toString(), KeyInput.KEY_SPACE);
	}
	
	private static void initMenuBindings(String name) {
		manager.set(name + MenuBindings.LEFT, KeyInput.KEY_LEFT);
		manager.set(name + MenuBindings.RIGHT, KeyInput.KEY_RIGHT);
		manager.set(name + MenuBindings.UP, KeyInput.KEY_UP);
		manager.set(name + MenuBindings.DOWN, KeyInput.KEY_DOWN);
		manager.set(name + MenuBindings.SELECT_ITEM, KeyInput.KEY_SPACE);
		manager.set(name + MenuBindings.SELECT_FINAL, KeyInput.KEY_RETURN);
	}
	
	private static void initStageBindings() {
		manager.set("camera_mode", KeyInput.KEY_TAB);
		manager.set("target_near", KeyInput.KEY_1);
		manager.set("target_far", KeyInput.KEY_2);
		manager.set("cam_left", KeyInput.KEY_A);
		manager.set("cam_right", KeyInput.KEY_D);
		manager.set("cam_up", KeyInput.KEY_W);
		manager.set("cam_down", KeyInput.KEY_S);
		manager.set("toggle_pause", KeyInput.KEY_P);
	}
	
	private interface KeyBindings {
		
	}
	
	public enum GameBindings implements KeyBindings {
		CAM_UP, CAM_DOWN, CAM_LEFT, CAM_RIGHT,
		TARGET_NEAR, TARGET_FAR, CAMERA_MODE,
		TOGGLE_PAUSE;

		@Override
		public String toString() {
			return "GameBindings_" + super.toString();
		}
		
	}

	public enum DebugBindings implements KeyBindings {
		TOGGLE_WIRE, TOGGLE_NORMALS, TOGGLE_LIGHTS, 
		TOGGLE_DEPTH, TOGGLE_STATS;

		@Override
		public String toString() {
			return "DebugBindings_" + super.toString();
		}
		
	}
	
	public enum GlobalBindings implements KeyBindings {
		EXIT, MEM_REPORT, SCREENSHOT, TOGGLE_MOUSE;

		@Override
		public String toString() {
			return "GlobalBindings_" + super.toString();
		}
		
	}
	
	public enum MenuBindings implements KeyBindings {
		UP, DOWN, LEFT, RIGHT, SELECT_ITEM, SELECT_FINAL, BACK,
		ACCESS_TERMINAL;

		@Override
		public String toString() {
			return "MenuBindings_" + super.toString();
		}
		
	}
	
	public enum PlayerBindings implements KeyBindings {
		FORWARD, BACKWARD, LEFT, RIGHT, JUMP, SWITCH,
		ATTACK_1, ATTACK_2, ATTACK_3, HOLD_ATTACK;

		@Override
		public String toString() {
			return "PlayerBindings_" + super.toString();
		}
		
	}
	
}
