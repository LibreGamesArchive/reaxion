package com.googlecode.reaxion.game.util;

import com.googlecode.reaxion.game.input.bindings.BattleGameStateBindings;
import com.googlecode.reaxion.game.input.bindings.CharacterSelectionOverlayBindings;
import com.googlecode.reaxion.game.input.bindings.DebugBindings;
import com.googlecode.reaxion.game.input.bindings.GameBindings;
import com.googlecode.reaxion.game.input.bindings.GlobalBindings;
import com.googlecode.reaxion.game.input.bindings.HubGameStateBindings;
import com.googlecode.reaxion.game.input.bindings.MenuBindings;
import com.googlecode.reaxion.game.input.bindings.PlayerBindings;
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
		initStageBindings();
		initDebugBindings();
		initHubGameStateBindings();
		initCharacterSelectionOverlayBindings();
		initBattleGameStateBindings();
		
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
		manager.set(GameBindings.CAM_MODE.toString(), KeyInput.KEY_TAB);
		manager.set(GameBindings.TARGET_NEAR.toString(), KeyInput.KEY_1);
		manager.set(GameBindings.TARGET_FAR.toString(), KeyInput.KEY_2);
		manager.set(GameBindings.CAM_LEFT.toString(), KeyInput.KEY_A);
		manager.set(GameBindings.CAM_RIGHT.toString(), KeyInput.KEY_D);
		manager.set(GameBindings.CAM_UP.toString(), KeyInput.KEY_W);
		manager.set(GameBindings.CAM_DOWN.toString(), KeyInput.KEY_S);
		manager.set(GameBindings.TOGGLE_PAUSE.toString(), KeyInput.KEY_P);
	}
	
	private static void initDebugBindings() {
		manager.set(DebugBindings.TOGGLE_WIRE.toString(), KeyInput.KEY_T);
		manager.set(DebugBindings.TOGGLE_DEPTH.toString(), KeyInput.KEY_F3);
		manager.set(DebugBindings.TOGGLE_LIGHTS.toString(), KeyInput.KEY_L);
		manager.set(DebugBindings.TOGGLE_STATS.toString(), KeyInput.KEY_F4);
		manager.set(DebugBindings.TOGGLE_NORMALS.toString(), KeyInput.KEY_N);
		manager.set(DebugBindings.TOGGLE_BOUNDS.toString(), KeyInput.KEY_B);
	}
	
	private static void initHubGameStateBindings() {
		manager.set(HubGameStateBindings.ACCESS_TERMINAL.toString(), KeyInput.KEY_RETURN);
		manager.set(HubGameStateBindings.CLOSE_TERMINAL.toString(), KeyInput.KEY_BACK);
	}
	
	private static void initCharacterSelectionOverlayBindings() {
		manager.set(CharacterSelectionOverlayBindings.CHOOSE_1.toString(), KeyInput.KEY_1);
		manager.set(CharacterSelectionOverlayBindings.CHOOSE_2.toString(), KeyInput.KEY_2);
		manager.set(CharacterSelectionOverlayBindings.UNDO_CHOICE.toString(), KeyInput.KEY_DELETE);
	}
	
	private static void initBattleGameStateBindings() {
		manager.set(BattleGameStateBindings.RETURN_TO_HGS.toString(), KeyInput.KEY_BACK);
	}
	
}
