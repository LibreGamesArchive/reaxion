package com.googlecode.reaxion.game.state;

import com.googlecode.reaxion.game.Reaxion;
import com.googlecode.reaxion.game.mission.MissionManager;
import com.googlecode.reaxion.game.overlay.CharacterSelectionOverlay;
import com.googlecode.reaxion.game.overlay.MenuOverlay;
import com.googlecode.reaxion.game.util.Battle;
import com.googlecode.reaxion.game.util.LoadingQueue;
import com.jme.app.AbstractGame;
import com.jme.image.Texture;
import com.jme.input.AbsoluteMouse;
import com.jme.input.InputHandler;
import com.jme.input.KeyBindingManager;
import com.jme.input.KeyInput;
import com.jme.input.MouseInput;
import com.jme.scene.Node;
import com.jme.scene.state.BlendState;
import com.jme.scene.state.TextureState;
import com.jme.system.DisplaySystem;
import com.jme.util.TextureManager;
import com.jmex.game.state.BasicGameState;
import com.jmex.game.state.GameStateManager;

/**
 * {@code CharacterSelectionState} is designated to the stage selection menu. It
 * contains a instance of {@code CharacterSelectionOverlay}. This state adds key
 * functionality to the character selection menu. The arrow keys are used to select
 * a character from the table of displayed characters. The number keys are used to 
 * change between Player 1 and Player 2, and the enter key passes the selections and
 * creates a new {@code StageSelectionState}
 * 
 * @author Austin Hou
 * 
 */

public class CharacterSelectionState extends BasicGameState {

	public static final String NAME = "characterSelectionState";

	private CharacterSelectionOverlay charSelectNode;
	protected InputHandler input;

	private AbsoluteMouse mouse;

	public float tpf;

	protected AbstractGame game = null;

	public CharacterSelectionState() {
		super(NAME);
		init();
	}

	/**
	 * Initializes visual and non-visual elements of {@code CharacterSelectionState}.
	 */
	private void init() {
		// Initial charSelect
		rootNode = new Node("RootNode");
		charSelectNode = new CharacterSelectionOverlay();
		rootNode.attachChild(charSelectNode);

		// Initial InputHandler
		input = new InputHandler();
		initKeyBindings();
		
		// Finish up
		rootNode.updateRenderState();
		rootNode.updateWorldBound();
		rootNode.updateGeometricState(0.0f, true);

	}

	/**
	 * Key binding initialization.
	 */
	private void initKeyBindings() {
		KeyBindingManager.getKeyBindingManager().set("screen_shot",
				KeyInput.KEY_F1);
		KeyBindingManager.getKeyBindingManager().set("exit",
				KeyInput.KEY_ESCAPE);
		KeyBindingManager.getKeyBindingManager().set("mem_report",
				KeyInput.KEY_R);
		KeyBindingManager.getKeyBindingManager().set("toggle_mouse",
				KeyInput.KEY_M);
	}
	
	@Override
	public void setActive(boolean active) {
		super.setActive(active);
		if(active)
			initKeyBindings();
	}

	@Override
	public void update(float _tpf) {
		tpf = _tpf;

		// Update the InputHandler
		if (input != null) {
			input.update(tpf);

			/** If exit is a valid command (via key Esc), exit game */
			if (KeyBindingManager.getKeyBindingManager().isValidCommand("exit",
					false)) {
				if (game != null) {
					game.finish();
				} else {
					Reaxion.terminate();
				}
			}
		}

		if (KeyBindingManager.getKeyBindingManager().isValidCommand(MenuOverlay.UP,
				false)) {
			charSelectNode.updateDisplay(KeyInput.KEY_UP);
		}
		if (KeyBindingManager.getKeyBindingManager().isValidCommand(
				MenuOverlay.RIGHT, false)) {
			charSelectNode.updateDisplay(KeyInput.KEY_RIGHT);
		}
		if (KeyBindingManager.getKeyBindingManager().isValidCommand(
				MenuOverlay.DOWN, false)) {
			charSelectNode.updateDisplay(KeyInput.KEY_DOWN);
		}
		if (KeyBindingManager.getKeyBindingManager().isValidCommand(
				MenuOverlay.LEFT, false)) {
			charSelectNode.updateDisplay(KeyInput.KEY_LEFT);
		}

		if (KeyBindingManager.getKeyBindingManager().isValidCommand(MenuOverlay.SELECT,
				false)) {
			charSelectNode.updateDisplay(KeyInput.KEY_SPACE);
		}
		
		if (KeyBindingManager.getKeyBindingManager().isValidCommand(CharacterSelectionOverlay.UNDO_CHOICE,
				false)) {
			charSelectNode.updateDisplay(KeyInput.KEY_BACK);
		}
		
		if (KeyBindingManager.getKeyBindingManager().isValidCommand(CharacterSelectionOverlay.CHOOSE_1,
				false)) {
			charSelectNode.updateDisplay(KeyInput.KEY_1);
		}
		
		if (KeyBindingManager.getKeyBindingManager().isValidCommand(CharacterSelectionOverlay.CHOOSE_2,
				false)) {
			charSelectNode.updateDisplay(KeyInput.KEY_2);
		}
		
		if (KeyBindingManager.getKeyBindingManager()
				.isValidCommand(MenuOverlay.SELECT_FINAL, false)) {
			goToStageSelectState();
		}

		if (input != null) {
			if (KeyBindingManager.getKeyBindingManager().isValidCommand(
					"screen_shot", false)) {
				DisplaySystem.getDisplaySystem().getRenderer().takeScreenShot(
						"SimpleGameScreenShot");
			}

		}
	}

	/**
	 * Switches from character selection menu to stage selection menu.
	 */
	private void goToStageSelectState() {
		// flush LoadingQueue
		LoadingQueue.resetQueue();
		
		String[] chars = charSelectNode.getSelectedChars(true);
		Battle.setDefaultPlayers(chars[0], chars[1]);
		
		/*
		Battle c = Battle.getCurrentBattle();
		c.setPlayers(charSelectNode.getSelectedChars());
		Battle.setCurrentBattle(c);
		 */
		
		if(GameStateManager.getInstance().getChild(StageSelectionState.NAME) == null) {			
			StageSelectionState s = new StageSelectionState();
			GameStateManager.getInstance().attachChild(s);
			s.setActive(true);
		} else {
			GameStateManager.getInstance().getChild(StageSelectionState.NAME).setActive(true);
		}
		
		setActive(false);
	}

}