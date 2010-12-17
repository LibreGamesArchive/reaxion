package com.googlecode.reaxion.game.util;

import com.googlecode.reaxion.game.input.bindings.BattleGameStateBindings;
import com.googlecode.reaxion.game.input.bindings.CharacterSelectionOverlayBindings;
import com.googlecode.reaxion.game.input.bindings.DebugBindings;
import com.googlecode.reaxion.game.input.bindings.DialogueGameStateBindings;
import com.googlecode.reaxion.game.input.bindings.GameBindings;
import com.googlecode.reaxion.game.input.bindings.GlobalBindings;
import com.googlecode.reaxion.game.input.bindings.HubGameStateBindings;
import com.googlecode.reaxion.game.input.bindings.MenuBindings;
import com.googlecode.reaxion.game.input.bindings.PlayerBindings;
import com.jme.input.KeyBindingManager;
import com.jme.input.KeyInput;

public class KeyBindingUtils {

	private static KeyBindingManager manager = KeyBindingManager.getKeyBindingManager();
	
	public static void initKeyBindings() {
		initGlobalBindings();
		initPlayerBindings();
		initMenuBindings();
		initStageBindings();
		initDebugBindings();
		initHubGameStateBindings();
		initCharacterSelectionOverlayBindings();
		initBattleGameStateBindings();
		initDialogueGameStateBindings();
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
	
	private static void initMenuBindings() {
		manager.set(MenuBindings.LEFT.toString(), KeyInput.KEY_LEFT);
		manager.set(MenuBindings.RIGHT.toString(), KeyInput.KEY_RIGHT);
		manager.set(MenuBindings.UP.toString(), KeyInput.KEY_UP);
		manager.set(MenuBindings.DOWN.toString(), KeyInput.KEY_DOWN);
		manager.set(MenuBindings.SELECT_ITEM.toString(), KeyInput.KEY_SPACE);
		manager.set(MenuBindings.SELECT_FINAL.toString(), KeyInput.KEY_RETURN);
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
		manager.set(BattleGameStateBindings.RETURN_TO_HGS.toString(), KeyInput.KEY_DELETE);
	}
	
	private static void initDialogueGameStateBindings() {
		manager.set(DialogueGameStateBindings.CONTINUE.toString(), KeyInput.KEY_RETURN);
	}
	
}
