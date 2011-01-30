package com.googlecode.reaxion.game.state;

import com.googlecode.reaxion.game.Reaxion;
import com.googlecode.reaxion.game.input.bindings.GlobalBindings;
import com.googlecode.reaxion.game.input.bindings.MenuBindings;
import com.googlecode.reaxion.game.overlay.TitleScreenOverlay;
import com.jme.app.AbstractGame;
import com.jme.input.InputHandler;
import com.jme.input.KeyBindingManager;
import com.jme.input.KeyInput;
import com.jme.scene.Node;
import com.jmex.game.state.GameStateManager;

/**
 * {@code TitlePreferencesState} is the main menu state for the
 * game, and displays all options for the various game modes.
 * 
 * @author Khoa Ha
 * 
 */

public class TitleScreenState extends BaseGameState {

	private TitleScreenOverlay titleNode;

	protected InputHandler input;
	protected KeyInput keyInput;
	private KeyBindingManager manager;

	protected AbstractGame game = null;
	
	public TitleScreenState() {
		super(true);
	}

	@Override
	protected void init() {
		rootNode = new Node("RootNode");

		titleNode = new TitleScreenOverlay();
		rootNode.attachChild(titleNode);

		input = new InputHandler();
		manager = KeyBindingManager.getKeyBindingManager();
		
		rootNode.updateRenderState();
		rootNode.updateWorldBound();
		rootNode.updateGeometricState(0.0f, true);
	}

	@Override
	public void stateUpdate(float tpf) {
		super.stateUpdate(tpf);
		
		if (input != null) {
			input.update(tpf);

			if (manager.isValidCommand(GlobalBindings.EXIT.toString(), false)) {
				if (game != null)
					game.finish();
				else
					Reaxion.terminate();
			}
			
			if (manager.isValidCommand(MenuBindings.UP.toString(), false)) {
				titleNode.scroll(-1);
			}
			if (manager.isValidCommand(MenuBindings.DOWN.toString(), false)) {
				titleNode.scroll(1);
			}
			if (manager.isValidCommand(MenuBindings.SELECT_FINAL.toString(), false)) {
				// configure keys
				if (titleNode.getOptionIndex() == 3) {
					KeyPreferencesState keyState = new KeyPreferencesState();
					GameStateManager.getInstance().attachChild(keyState);
					keyState.setActive(true);
					
					this.setActive(false);
					GameStateManager.getInstance().detachChild(this);
				}
			}
		}
		
		rootNode.updateGeometricState(tpf, true);
	}

	@Override
	public void cleanup() {
	}

}