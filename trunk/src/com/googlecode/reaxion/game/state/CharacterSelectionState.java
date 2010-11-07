package com.googlecode.reaxion.game.state;

import com.googlecode.reaxion.game.Reaxion;
import com.googlecode.reaxion.game.mission.MissionManager;
import com.googlecode.reaxion.game.overlay.CharacterSelectionOverlay;
import com.googlecode.reaxion.game.util.Battle;
import com.googlecode.reaxion.game.util.LoadingQueue;
import com.jme.app.AbstractGame;
import com.jme.image.Texture;
import com.jme.input.AbsoluteMouse;
import com.jme.input.InputHandler;
import com.jme.input.KeyBindingManager;
import com.jme.input.KeyInput;
import com.jme.scene.Node;
import com.jme.scene.state.BlendState;
import com.jme.scene.state.TextureState;
import com.jme.system.DisplaySystem;
import com.jme.util.TextureManager;
import com.jmex.game.state.BasicGameState;
import com.jmex.game.state.GameStateManager;

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

	private void init() {
		// Initial charSelect
		rootNode = new Node("RootNode");
		charSelectNode = new CharacterSelectionOverlay();
		rootNode.attachChild(charSelectNode);

		// Initial InputHandler
		input = new InputHandler();
		initKeyBindings();

		// Setup software mouse
		mouse = new AbsoluteMouse("Mouse Input", DisplaySystem
				.getDisplaySystem().getWidth(), DisplaySystem
				.getDisplaySystem().getHeight());
		mouse.registerWithInputHandler(input);
		TextureState cursor = DisplaySystem.getDisplaySystem().getRenderer()
				.createTextureState();
		cursor.setTexture(TextureManager.loadTexture(Reaxion.class
				.getClassLoader().getResource(
						"com/googlecode/reaxion/resources/cursors/cursor.png"),
				Texture.MinificationFilter.NearestNeighborNoMipMaps,
				Texture.MagnificationFilter.NearestNeighbor));
		mouse.setRenderState(cursor);
		BlendState as1 = DisplaySystem.getDisplaySystem().getRenderer()
				.createBlendState();
		as1.setBlendEnabled(true);
		as1.setSourceFunction(BlendState.SourceFunction.SourceAlpha);
		as1
				.setDestinationFunction(BlendState.DestinationFunction.OneMinusSourceAlpha);
		as1.setTestEnabled(true);
		as1.setTestFunction(BlendState.TestFunction.GreaterThan);
		mouse.setRenderState(as1);
		rootNode.attachChild(mouse);

		// Finish up
		rootNode.updateRenderState();
		rootNode.updateWorldBound();
		rootNode.updateGeometricState(0.0f, true);

	}

	// duplicate the functionality of DebugGameState
	// Most of this can be commented out during finalization
	private void initKeyBindings() {
		KeyBindingManager.getKeyBindingManager().set("screen_shot",
				KeyInput.KEY_F1);
		KeyBindingManager.getKeyBindingManager().set("exit",
				KeyInput.KEY_ESCAPE);
		KeyBindingManager.getKeyBindingManager().set("mem_report",
				KeyInput.KEY_R);
		KeyBindingManager.getKeyBindingManager().set("toggle_mouse",
				KeyInput.KEY_M);

		KeyBindingManager.getKeyBindingManager().set("go", KeyInput.KEY_RETURN);

		KeyBindingManager.getKeyBindingManager().set("arrow_up",
				KeyInput.KEY_UP);
		KeyBindingManager.getKeyBindingManager().set("arrow_down",
				KeyInput.KEY_DOWN);
		KeyBindingManager.getKeyBindingManager().set("arrow_left",
				KeyInput.KEY_LEFT);
		KeyBindingManager.getKeyBindingManager().set("arrow_right",
				KeyInput.KEY_RIGHT);
		KeyBindingManager.getKeyBindingManager().set("select",
				KeyInput.KEY_SPACE);

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

		if (KeyBindingManager.getKeyBindingManager().isValidCommand("arrow_up",
				false)) {
			charSelectNode.updateDisplay(1);
		}
		if (KeyBindingManager.getKeyBindingManager().isValidCommand(
				"arrow_right", false)) {
			charSelectNode.updateDisplay(2);
		}
		if (KeyBindingManager.getKeyBindingManager().isValidCommand(
				"arrow_down", false)) {
			charSelectNode.updateDisplay(3);
		}
		if (KeyBindingManager.getKeyBindingManager().isValidCommand(
				"arrow_left", false)) {
			charSelectNode.updateDisplay(4);
		}

		if (KeyBindingManager.getKeyBindingManager().isValidCommand("select",
				false)) {
			charSelectNode.updateSel();
		}

		if (KeyBindingManager.getKeyBindingManager()
				.isValidCommand("go", false)) {
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

	private void goToStageSelectState() {
		// flush LoadingQueue
		LoadingQueue.resetQueue();
		
		String[] chars = charSelectNode.getSelectedChars();
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

	public boolean charSelected() {
		return true;
	}

	public int[] selectedChars() {
		return new int[3];
	}

}