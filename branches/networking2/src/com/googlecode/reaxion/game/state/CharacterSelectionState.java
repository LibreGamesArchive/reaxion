package com.googlecode.reaxion.game.state;

import com.googlecode.reaxion.game.Reaxion;
import com.googlecode.reaxion.game.input.bindings.CharacterSelectionOverlayBindings;
import com.googlecode.reaxion.game.input.bindings.GlobalBindings;
import com.googlecode.reaxion.game.input.bindings.MenuBindings;
import com.googlecode.reaxion.game.overlay.CharacterSelectionOverlay;
import com.googlecode.reaxion.game.util.Battle;
import com.googlecode.reaxion.game.util.LoadingQueue;
import com.jme.app.AbstractGame;
import com.jme.input.AbsoluteMouse;
import com.jme.input.InputHandler;
import com.jme.input.KeyBindingManager;
import com.jme.scene.Node;
import com.jme.system.DisplaySystem;
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
		charSelectNode = new CharacterSelectionOverlay(true);
		rootNode.attachChild(charSelectNode);

		// Initial InputHandler
		input = new InputHandler();
		
		// Finish up
		rootNode.updateRenderState();
		rootNode.updateWorldBound();
		rootNode.updateGeometricState(0.0f, true);

	}

	@Override
	public void update(float _tpf) {
		tpf = _tpf;

		// Update the InputHandler
		if (input != null) {
			input.update(tpf);

			/** If exit is a valid command (via key Esc), exit game */
			if (KeyBindingManager.getKeyBindingManager().isValidCommand(GlobalBindings.EXIT.toString(),
					false)) {
				if (game != null) {
					game.finish();
				} else {
					Reaxion.terminate();
				}
			}
		}

		if (KeyBindingManager.getKeyBindingManager().isValidCommand(MenuBindings.UP.toString(),
				false)) {
			charSelectNode.updateDisplay(MenuBindings.UP);
		}
		if (KeyBindingManager.getKeyBindingManager().isValidCommand(MenuBindings.RIGHT.toString(), false)) {
			charSelectNode.updateDisplay(MenuBindings.RIGHT);
		}
		if (KeyBindingManager.getKeyBindingManager().isValidCommand(MenuBindings.DOWN.toString(), false)) {
			charSelectNode.updateDisplay(MenuBindings.DOWN);
		}
		if (KeyBindingManager.getKeyBindingManager().isValidCommand(MenuBindings.LEFT.toString(), false)) {
			charSelectNode.updateDisplay(MenuBindings.LEFT);
		}

		if (KeyBindingManager.getKeyBindingManager().isValidCommand(MenuBindings.SELECT_ITEM.toString(),
				false)) {
			charSelectNode.updateDisplay(MenuBindings.SELECT_ITEM);
		}
		
		if (KeyBindingManager.getKeyBindingManager().isValidCommand(CharacterSelectionOverlayBindings.UNDO_CHOICE.toString(),
				false)) {
			charSelectNode.updateDisplay(CharacterSelectionOverlayBindings.UNDO_CHOICE);
		}
		
		if (KeyBindingManager.getKeyBindingManager().isValidCommand(CharacterSelectionOverlayBindings.CHOOSE_1.toString(),
				false)) {
			charSelectNode.updateDisplay(CharacterSelectionOverlayBindings.CHOOSE_1);
		}
		
		if (KeyBindingManager.getKeyBindingManager().isValidCommand(CharacterSelectionOverlayBindings.CHOOSE_2.toString(),
				false)) {
			charSelectNode.updateDisplay(CharacterSelectionOverlayBindings.CHOOSE_2);
		}
		
		if (KeyBindingManager.getKeyBindingManager()
				.isValidCommand(MenuBindings.SELECT_FINAL.toString(), false)) {
			goToStageSelectState();
		}

		if (input != null) {
			if (KeyBindingManager.getKeyBindingManager().isValidCommand(
					GlobalBindings.SCREENSHOT.toString(), false)) {
				Reaxion.takeScreenshot("CharacterSelection");
			}

		}
		
		charSelectNode.update(this);
	}

	/**
	 * Switches from character selection menu to stage selection menu.
	 */
	private void goToStageSelectState() {
		// flush LoadingQueue
		LoadingQueue.resetQueue();
		
		String[] chars = charSelectNode.getSelectedChars(true);
		
		// check selection
		if (chars == null)
			return;
		
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