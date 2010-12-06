package com.googlecode.reaxion.game.state;

import com.googlecode.reaxion.game.Reaxion;
import com.googlecode.reaxion.game.mission.MissionID;
import com.googlecode.reaxion.game.mission.MissionManager;
import com.googlecode.reaxion.game.overlay.StageSelectionOverlay;
import com.googlecode.reaxion.game.util.Battle;
import com.googlecode.reaxion.game.util.KeyBindingUtils;
import com.jme.app.AbstractGame;
import com.jme.input.InputHandler;
import com.jme.input.KeyBindingManager;
import com.jme.input.KeyInput;
import com.jme.scene.Node;
import com.jmex.game.state.BasicGameState;
import com.jmex.game.state.GameStateManager;

/**
 * {@code StageSelectionState} is designated to the stage selection menu. It
 * contains a instance of {@code StageSelectionOverlay}. This state adds key
 * functionality to the stage selection menu. The arrow keys are used to change
 * the selected stage name, and the enter key is used to load that stage and
 * create a new {@code BattleGameState}.
 * 
 * @author Brian Clanton
 * 
 */

public class StageSelectionState extends BasicGameState {

	public static final String NAME = "stageSelectionState";

	public float tpf;

	private StageSelectionOverlay stageSelectionNode;

	protected InputHandler input;

	private KeyBindingManager manager;

	protected AbstractGame game = null;
	
	public StageSelectionState() {
		super(NAME);
		init();
	}

	/**
	 * Initializes visual and non-visual elements of {@code StageSelectionState}.
	 */
	private void init() {
		rootNode = new Node("RootNode");

		stageSelectionNode = new StageSelectionOverlay();
		rootNode.attachChild(stageSelectionNode);

		input = new InputHandler();
		initKeyBindings();

		rootNode.updateRenderState();
		rootNode.updateWorldBound();
		rootNode.updateGeometricState(0.0f, true);
	}

	/**
	 * Key binding initialization.
	 */
	private void initKeyBindings() {
//		KeyBindingUtils.addKeyBinding(KeyBindingUtils.MenuBindings.UP, KeyInput.KEY_UP);
//		KeyBindingUtils.addKeyBinding(KeyBindingUtils.MenuBindings.DOWN, KeyInput.KEY_DOWN);
//		KeyBindingUtils.addKeyBinding(KeyBindingUtils.MenuBindings.LEFT, KeyInput.KEY_LEFT);
//		KeyBindingUtils.addKeyBinding(KeyBindingUtils.MenuBindings.RIGHT, KeyInput.KEY_RIGHT);
//		KeyBindingUtils.addKeyBinding(KeyBindingUtils.MenuBindings.SELECT_FINAL, KeyInput.KEY_RETURN);
//		KeyBindingUtils.addKeyBinding(KeyBindingUtils.MenuBindings.BACK, KeyInput.KEY_BACK);
		
		manager = KeyBindingManager.getKeyBindingManager();
		manager.set("arrow_up", KeyInput.KEY_UP);
		manager.set("arrow_down", KeyInput.KEY_DOWN);
		manager.set("arrow_left", KeyInput.KEY_LEFT);
		manager.set("arrow_right", KeyInput.KEY_RIGHT);
		manager.set("select", KeyInput.KEY_RETURN);
		manager.set("exit", KeyInput.KEY_ESCAPE);
		manager.set("go_back", KeyInput.KEY_BACK);
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
		stageSelectionNode.update(this);
		
		if (input != null) {
			input.update(tpf);

			if (manager.isValidCommand("exit", false)) {
				if (game != null)
					game.finish();
				else
					Reaxion.terminate();
			}
		}

		rootNode.updateGeometricState(tpf, true);

		checkKeyInput();
	}
	
	/**
	 * Checks key input.
	 */
	private void checkKeyInput() {
		if (input != null) {
			if (manager.isValidCommand("arrow_up", false))
				stageSelectionNode.updateDisplay(KeyInput.KEY_UP);
			if (manager.isValidCommand("arrow_down", false))
				stageSelectionNode.updateDisplay(KeyInput.KEY_DOWN);
			if (manager.isValidCommand("arrow_left", false))
				stageSelectionNode.updateDisplay(KeyInput.KEY_LEFT);
			if (manager.isValidCommand("arrow_right", false))
				stageSelectionNode.updateDisplay(KeyInput.KEY_RIGHT);
			if (manager.isValidCommand("select", false)) {
//				switchToLoadingOverlay();
				goToBattleGameState();
			}
			if(manager.isValidCommand("go_back", false)) {
				returnToCharSelectState();
			}
		}

	}

	/**
	 * Switches from stage selection menu to character selection menu.
	 */
	private void returnToCharSelectState() {
		GameStateManager.getInstance().getChild(CharacterSelectionState.NAME).setActive(true);
		setActive(false);
	}

//	private void switchToLoadingOverlay() {
//		Quad cover = new Quad("cover",
//				DisplaySystem.getDisplaySystem().getWidth(),
//				DisplaySystem.getDisplaySystem().getHeight());
//		cover.setSolidColor(new ColorRGBA(0, 0, 0, 1));
//		BitmapText loadingText = new BitmapText(stageSelectionNode.getTextFont(),
//				false);
//		loadingText.setText("Loading " +
//				stageSelectionNode.getSelectedStageName());
//		loadingText.setSize(48);
//		loadingText.setLocalTranslation(cover.getWidth() / 2, cover.getHeight() /
//				2, 0);
//		loadingText.update();
//
//		Overlay loading = new Overlay();
//		loading.attachChild(cover);
//		loading.attachChild(loadingText);
//		loading.updateRenderState();
//
//		rootNode.detachChild(stageSelectionNode);
//		rootNode.attachChild(loading);
//		rootNode.updateRenderState();
//	}

	/**
	 * Switches from {@code StageSelectionState} to {@code BattleGameState} and
	 * passes a {@code Stage} object corresponding to the stage the user selects
	 * in the menu.
	 */
	private void goToBattleGameState() {
		/*
		Battle c = Battle.getCurrentBattle();
		c.setStage(stageSelectionNode.getSelectedStageClass());
		Battle.setCurrentBattle(c);
		
		BattleGameState battleState = Battle.createBattleGameState();
		GameStateManager.getInstance().attachChild(battleState);
		battleState.setActive(true);
		battleState.startBGM();
		*/
		
		/*
		Battle c = Battle.getCurrentBattle();
		c.setStage(stageSelectionNode.getSelectedStageClass());
		Battle.setCurrentBattle(c);
		
		HubGameState hubState = Battle.createHubGameState();
		GameStateManager.getInstance().attachChild(hubState);
		hubState.setActive(true);
		*/
		
		Battle.setDefaultStage(stageSelectionNode.getSelectedStageClass());
//		MissionManager.startMission(MissionID.OPEN_HUBGAMESTATE);
		MissionManager.startHubGameState();
		
		setActive(false);
	}

	@Override
	public void cleanup() {
	}

}