package com.googlecode.reaxion.game.state;

import com.googlecode.reaxion.game.ability.Ability;
import com.googlecode.reaxion.game.ability.AfterImage;
import com.googlecode.reaxion.game.ability.EvasiveStart;
import com.googlecode.reaxion.game.audio.BgmPlayer;
import com.googlecode.reaxion.game.input.ai.TestAI;
import com.googlecode.reaxion.game.model.character.Character;
import com.googlecode.reaxion.game.model.character.Cy;
import com.googlecode.reaxion.game.model.character.Khoa;
import com.googlecode.reaxion.game.model.character.MajorCharacter;
import com.googlecode.reaxion.game.model.character.Monica;
import com.googlecode.reaxion.game.model.character.Nilay;
import com.googlecode.reaxion.game.model.stage.Stage;
import com.googlecode.reaxion.game.overlay.StageSelectionOverlay;
import com.googlecode.reaxion.game.util.LoadingQueue;
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
 * @author Brian
 * 
 */

public class StageSelectionState extends BasicGameState {

	public static final String NAME = "stageSelectionState";

	private static final String stageClassURL = "com.googlecode.reaxion.game.model.stage.";
	private static final String attackBaseLocation = "com.googlecode.reaxion.game.attack.";

	public float tpf;

	private StageSelectionOverlay stageSelectionNode;

	protected InputHandler input;

	private KeyBindingManager manager;

	protected AbstractGame game = null;

	public StageSelectionState() {
		super(NAME);
		init();
	}

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

	private void initKeyBindings() {
		manager = KeyBindingManager.getKeyBindingManager();
		manager.set("arrow_up", KeyInput.KEY_UP);
		manager.set("arrow_down", KeyInput.KEY_DOWN);
		manager.set("select", KeyInput.KEY_RETURN);
		manager.set("exit", KeyInput.KEY_ESCAPE);
	}

	@Override
	public void update(float _tpf) {
		tpf = _tpf;

		if (input != null) {
			input.update(tpf);

			if (manager.isValidCommand("exit", false)) {
				if (game != null)
					game.finish();
				else
					System.exit(0);
			}
		}

		rootNode.updateGeometricState(tpf, true);

		checkKeyInput();
	}

	private void checkKeyInput() {
		if (input != null) {
			if (manager.isValidCommand("arrow_up", false))
				stageSelectionNode.updateDisplay(true);
			if (manager.isValidCommand("arrow_down", false))
				stageSelectionNode.updateDisplay(false);
			if (manager.isValidCommand("select", false)) {
				// switchToLoadingOverlay();
				gotoBattleState();
			}
		}

	}

	// private void switchToLoadingOverlay() {
	// Quad cover = new Quad("cover",
	// DisplaySystem.getDisplaySystem().getWidth(),
	// DisplaySystem.getDisplaySystem().getHeight());
	// cover.setSolidColor(new ColorRGBA(0, 0, 0, 1));
	// BitmapText loadingText = new BitmapText(stageSelectionNode.getTextFont(),
	// false);
	// loadingText.setText("Loading " +
	// stageSelectionNode.getSelectedStageName());
	// loadingText.setSize(48);
	// loadingText.setLocalTranslation(cover.getWidth() / 2, cover.getHeight() /
	// 2, 0);
	//		
	// Overlay loading = new Overlay();
	// loading.attachChild(cover);
	// loading.attachChild(loadingText);
	// loading.updateRenderState();
	//		
	// rootNode.detachChild(stageSelectionNode);
	// rootNode.attachChild(loading);
	// }

	/**
	 * Switches from {@code StageSelectionState} to {@code BattleGameState} and
	 * passes a {@code Stage} object corresponding to the stage the user selects
	 * in the menu.
	 */
	private void gotoBattleState() {
		String className = stageSelectionNode.getSelectedStageClass();

		BattleGameState battleState = new BattleGameState();

		// Set the stage
		try {
			Class cl;
			cl = Class.forName(stageClassURL + className);
			Stage temp = (Stage) cl.getConstructors()[0].newInstance();
			Stage cb = (Stage) LoadingQueue.push(temp);
		} catch (Exception e) {
			e.printStackTrace();
		}

		// Add some characters
		MajorCharacter mp = (MajorCharacter) LoadingQueue
				.push(new Monica(false));
		MajorCharacter t2 = (MajorCharacter) LoadingQueue.push(new Khoa(false));
		MajorCharacter t = (MajorCharacter) LoadingQueue.push(new Khoa());
		MajorCharacter c = (MajorCharacter) LoadingQueue.push(new Cy());
		MajorCharacter n = (MajorCharacter) LoadingQueue.push(new Nilay());
		MajorCharacter c2 = (MajorCharacter) LoadingQueue.push(new Cy());

		// Load everything!
		LoadingQueue.execute(battleState);

		// Set up some abilities!
		mp.setAbilities(new Ability[] { new EvasiveStart() });
		t2.setAbilities(new Ability[] { new AfterImage() });
		t.setAbilities(new Ability[] { new AfterImage() });

		// Set up test attacks!
		Class[] attacks1 = new Class[6];
		Class[] attacks2 = new Class[6];
		try {
			attacks1[0] = Class.forName(attackBaseLocation + "ShootBullet");
			attacks1[1] = Class.forName(attackBaseLocation + "ShieldBarrier");

			attacks2[0] = Class.forName(attackBaseLocation + "SpinLance");
			attacks2[1] = Class.forName(attackBaseLocation + "AngelRain");
		} catch (Exception e) {
			e.printStackTrace();
		}

		// Set up some AI!
		t.assignAI(new TestAI(t));
		//t.hp = 5;

		// Set the opponent!
		Character[] opponents = new Character[1];
		opponents[0] = t;
		battleState.assignOpponents(opponents);

		// Set stuff in the battleState
		battleState.assignTeam(mp, attacks1, t2, attacks2);
		c.model.setLocalTranslation(2, 5, -1);
		c2.model.setLocalTranslation(6, 5, 3);
		c2.gravity = 0;
		n.model.setLocalTranslation(-5, 0, -3);
		battleState.nextTarget(0);

		// Set up BGM
		try {
			BgmPlayer.prepare();
			BgmPlayer.play(battleState.getStage().bgm[0]);
		} catch (NullPointerException e) {
			System.out.println("No BGM for " + className);
		}

		// test sounds, uncomment to test
		// SfxPlayer.addSfx("test3.ogg", t, 50, true);

		// Set some game conditions...
		battleState.targetTime = 60;
		battleState.expYield = 1000;

		// reupdate due to added changes
		battleState.getRootNode().updateRenderState();

		mp.play("stand");

		GameStateManager.getInstance().attachChild(battleState);
		battleState.setActive(true);
		setActive(false);
	}

	@Override
	public void cleanup() {
	}

}
