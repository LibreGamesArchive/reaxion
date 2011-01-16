package com.googlecode.reaxion.game.state;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.logging.Logger;

import com.googlecode.reaxion.game.Reaxion;
import com.googlecode.reaxion.game.audio.AudioPlayer;
import com.googlecode.reaxion.game.audio.BgmPlayer;
import com.googlecode.reaxion.game.audio.SfxPlayer;
import com.googlecode.reaxion.game.input.ClientPlayerInput;
import com.googlecode.reaxion.game.input.PlayerInput;
import com.googlecode.reaxion.game.model.Model;
import com.googlecode.reaxion.game.model.character.MajorCharacter;
import com.googlecode.reaxion.game.model.stage.Stage;
import com.googlecode.reaxion.game.networking.NetworkingObjects;
import com.googlecode.reaxion.game.networking.NetworkingObjects.PlayerNum;
import com.googlecode.reaxion.game.overlay.HudOverlay;
import com.googlecode.reaxion.game.overlay.PauseOverlay;
import com.googlecode.reaxion.game.util.Battle;
import com.googlecode.reaxion.game.util.LoadingQueue;
import com.jme.app.AbstractGame;
import com.jme.image.Texture;
import com.jme.input.AbsoluteMouse;
import com.jme.input.InputHandler;
import com.jme.input.KeyBindingManager;
import com.jme.input.KeyInput;
import com.jme.input.MouseInput;
import com.jme.math.FastMath;
import com.jme.math.Vector3f;
import com.jme.renderer.pass.BasicPassManager;
import com.jme.scene.Node;
import com.jme.scene.state.LightState;
import com.jme.scene.state.WireframeState;
import com.jme.scene.state.ZBufferState;
import com.jme.system.DisplaySystem;
import com.jme.util.geom.Debugger;
import com.jmex.audio.AudioSystem;
import com.jmex.game.state.CameraGameState;
import com.jmex.game.state.GameStateManager;
import com.jmex.game.state.StatisticsGameState;

/**
 * {@code StageGameState} is a heavily modified version {@code DebugGameState},
 * with added functionality built around {@code Models}, movement, and the
 * camera system.
 * 
 * @author Khoa
 */
public class StageGameState extends CameraGameState {

	public static final String NAME = "stageGameState";

	protected static final Logger logger = Logger.getLogger(StageGameState.class.getName());

	public float tpf;
	protected double totalTime = 0;
	protected boolean timing = true;

	protected HudOverlay hudNode;
	protected PauseOverlay pauseNode;

	protected Node containerNode;

	protected InputHandler input;
	protected WireframeState wireState;
	protected LightState lightState;

	protected BasicPassManager pManager;

	protected boolean pause;
	protected boolean showBounds = false;
	protected boolean showDepth = false;
	protected boolean showNormals = false;
	protected boolean statisticsCreated = false;
	protected AbstractGame game = null;

	/**
	 * Contains all scene model elements
	 */
	protected ArrayList<Model> models;

	/**
	 * Contains the stage used for the battle
	 */
	protected Stage stage;

	protected AbsoluteMouse mouse;
	public String cameraMode = "free"; // options are "lock" and "free"
	public static int cameraDistance = 15;
	protected Model currentTarget;
	protected float camRotXZ;
	protected final static float camRotXZSpd = (float) Math.PI / 12;
	protected float camRotY;
	protected final static float camRotYSpd = (float) Math.PI / 24;
	protected final static float[] camRotXZLimit = { -0.5236f, 0.5236f }; // -pi/6
	// and
	// pi/6
	protected final static float[] camRotYLimit = { -0.1325f, 1.5394f, 0.5236f }; // arctan(2/15),
	// 49pi/100
	// (close
	// to
	// pi/2)
	// and
	// pi/6

	/*
	 * // test axes converge at point private Cylinder[] cyl = new Cylinder[3];
	 */

	protected PlayerInput playerInput;
	protected MajorCharacter player;
	protected Class[] playerAttacks;
	protected MajorCharacter partner;
	protected Class[] partnerAttacks;

	public StageGameState() {
		super(NAME);
		init();
	}

	public StageGameState(Battle b) {
		super(NAME);
		init();

		LoadingQueue.execute(this);

		if (!NetworkingObjects.isServer) {
			BgmPlayer.prepare();
			if (getStage() != null)
				AudioPlayer.queueBGM(getStage().getBgm(-1));
			// fixed, problem was getStage() returned null since it starts w/o a
			// stage
		}

		b.assignPositions();
		assignTeam(b.getP1(), b.getP1Attacks(), b.getP2(), b.getP2Attacks());
		if(NetworkingObjects.isServer)
			((ServerBattleGameState)this).assignOpTeam(b.getOp1(), b.getOp1Attacks(), b.getOp2(), b.getOp2Attacks());
		nextTarget(0);

		load();

		LoadingQueue.execute(this);

		if (!NetworkingObjects.isServer)
			rootNode.updateRenderState();
	}

	public void startBGM() {
		// try {
		// String str = getStage().getBgm(-1);
		// BgmPlayer.play(str);
		// System.out.println("BGM loaded: "+str);
		// } catch (NullPointerException e) {
		// System.out.println("No BGM for " + getStage().name);
		// }
		AudioPlayer.startBGM();
	}

	protected void init() {
		rootNode = new Node("RootNode");
		models = new ArrayList<Model>();

		// Prepare HUD node
		if (!NetworkingObjects.isServer) {
			hudNode = new HudOverlay();
			rootNode.attachChild(hudNode);

			// Prepare pause node
			pauseNode = new PauseOverlay();
			rootNode.attachChild(pauseNode);
		}

		// Prepare container node (must contain anything being reflected)
		containerNode = new Node("ReflectionNode");
		rootNode.attachChild(containerNode);

		// Prepare the pass manager
		pManager = new BasicPassManager();

		if (!NetworkingObjects.isServer) {
			// Create a wirestate to toggle on and off. Starts disabled with
			// default
			// width of 1 pixel.
			wireState = DisplaySystem.getDisplaySystem().getRenderer().createWireframeState();
			wireState.setEnabled(false);
			rootNode.setRenderState(wireState);

			// Create ZBuffer for depth
			ZBufferState zbs = DisplaySystem.getDisplaySystem().getRenderer().createZBufferState();
			zbs.setEnabled(true);
			zbs.setFunction(ZBufferState.TestFunction.LessThanOrEqualTo);
			rootNode.setRenderState(zbs);

			// Fix up the camera, will not be needed for final camera controls
			Vector3f loc = new Vector3f(0.0f, 2.5f, 10.0f);
			Vector3f left = new Vector3f(-1.0f, 0.0f, 0.0f);
			Vector3f up = new Vector3f(0.0f, 1.0f, 0.0f);
			Vector3f dir = new Vector3f(0.0f, 0f, -1.0f);
			cam.setFrame(loc, left, up, dir);
			cam.setFrustumPerspective(45f, (float) DisplaySystem.getDisplaySystem().getWidth()
					/ DisplaySystem.getDisplaySystem().getHeight(), .01f, 1500);
			cam.update();

			// Initial InputHandler
			// input = new FirstPersonHandler(cam, 15.0f, 0.5f);
			input = new InputHandler();
			initKeyBindings();

			// Setup software mouse
			// Commented out to get rid of that annoying bs where it would nom
			// the
			// mouse
			MouseInput.get().setCursorVisible(true);
			/*
			 * mouse = new AbsoluteMouse("Mouse Input",
			 * DisplaySystem.getDisplaySystem().getWidth(),
			 * DisplaySystem.getDisplaySystem().getHeight());
			 * mouse.registerWithInputHandler(input); TextureState cursor =
			 * DisplaySystem
			 * .getDisplaySystem().getRenderer().createTextureState();
			 * cursor.setTexture(TextureManager.loadTexture(
			 * Reaxion.class.getClassLoader
			 * ().getResource("com/googlecode/reaxion/resources/cursors/cursor.png"
			 * ), Texture.MinificationFilter.NearestNeighborNoMipMaps,
			 * Texture.MagnificationFilter.NearestNeighbor));
			 * mouse.setRenderState(cursor); BlendState as1 =
			 * DisplaySystem.getDisplaySystem
			 * ().getRenderer().createBlendState(); as1.setBlendEnabled(true);
			 * as1.setSourceFunction(BlendState.SourceFunction.SourceAlpha);
			 * as1.setDestinationFunction
			 * (BlendState.DestinationFunction.OneMinusSourceAlpha);
			 * as1.setTestEnabled(true);
			 * as1.setTestFunction(BlendState.TestFunction.GreaterThan);
			 * mouse.setRenderState(as1); rootNode.attachChild(mouse);
			 */

			/*
			 * // make test cylinders for (int i=0; i<cyl.length; i++) { cyl[i]
			 * = new Cylinder("cyl"+i, 3, 3, .025f, 100f);
			 * rootNode.attachChild(cyl[i]); cyl[i].setLocalRotation(new
			 * Quaternion().fromAngleAxis((float)Math.PI/2, new
			 * Vector3f((i==0)?1:0,(i==1)?1:0,(i==2)?1:0))); }
			 */

			// Finish up
			rootNode.updateRenderState();
		}
		rootNode.updateWorldBound();
		rootNode.updateGeometricState(0.0f, true);

	}

	/**
	 * Preloads all elements at start to reduce on-the-fly loading.
	 */
	protected void load() {
		try {
			// try to preload player characters' attacks
			for (int i = 0; i < playerAttacks.length; i++) {
				if (playerAttacks[i] != null)
					playerAttacks[i].getMethod("load").invoke(null);
				if (partnerAttacks[i] != null)
					partnerAttacks[i].getMethod("load").invoke(null);
			}

			// try to preload common resources
			LoadingQueue.push(new Model("glow-ring"));

			// why is this line here...? it makes the state null. that is rather
			// odd.
			// possibly because it preloads into memory? hmm
			LoadingQueue.execute(null);
		} catch (Exception e) {
			System.out.println("Error occured during preloading.");
			e.printStackTrace();
		}
	}

	/**
	 * Hides the mouse and HUD.
	 */
	protected void hideOverlays() {
		rootNode.detachChild(hudNode);
		rootNode.detachChild(mouse);
	}

	/**
	 * Returns the {@code BasicPassManager}.
	 * 
	 * @author Khoa
	 * 
	 */
	public BasicPassManager getPassManager() {
		return pManager;
	}

	/**
	 * Specifies the stage for this game state.
	 * 
	 * @param s
	 *            Stage to be designated
	 * @author Khoa
	 * 
	 */
	public void setStage(Stage s) {
		stage = s;
		stage.loadComponents(this);
		containerNode.attachChild(s.model);
		// attach stage's lighting to rootNode
		lightState = stage.createLights();
		rootNode.setRenderState(lightState);
	}

	/**
	 * Returns pointer to the stage.
	 * 
	 * @author Khoa
	 * 
	 */
	public Stage getStage() {
		return stage;
	}

	/**
	 * Specifies the tag team for this game state.
	 * 
	 * @param p1
	 *            Character to be designated as the player
	 * @param q1
	 *            Array of the attack classes for the player
	 * @param p2
	 *            Character to be designated as the partner
	 * @param q2
	 *            Array of the attack classes for the partner
	 * @author Khoa
	 * 
	 */
	public void assignTeam(MajorCharacter p1, Class[] q1, MajorCharacter p2, Class[] q2) {
		player = p1;
		playerAttacks = q1;
		partner = p2;
		partnerAttacks = q2;
		// Create input system
		// playerInput = new PlayerInput(this);
		// Pass attack reference to HUD
		if (!NetworkingObjects.isServer)
			hudNode.passCharacterInfo(playerAttacks, player.minGauge);
		// Remove the inactive character
		removeModel(partner);
	}

	/**
	 * Specifies the player character for this game state.
	 * 
	 * @param p
	 *            Character to be designated as the player
	 * @param q
	 *            Array of the attack classes for the character
	 * @author Khoa
	 * 
	 */
	public void assignPlayer(MajorCharacter p, Class[] q) {
		player = p;
		playerAttacks = q;
		// Create input system
		playerInput = new PlayerInput(this);
		// Pass attack reference to HUD
		hudNode.passCharacterInfo(playerAttacks, player.minGauge);
	}

	/**
	 * Switches player with partner
	 * 
	 * @author Khoa
	 * 
	 */
	public void tagSwitch() {
		if (partner != null && partner.hp > 0) {
			MajorCharacter p = player;
			player = partner;
			partner = p;
			Class[] a = playerAttacks;
			playerAttacks = partnerAttacks;
			partnerAttacks = a;
			// Pass attack reference to HUD
			hudNode.passCharacterInfo(playerAttacks, player.minGauge);
			// Attach the active character
			addModel(player);
			// Synchronize position
			player.model.setLocalTranslation(partner.model.getLocalTranslation().clone());
			player.model.setLocalRotation(partner.model.getLocalRotation().clone());
			player.rotationVector = partner.rotationVector;
			player.gravVel = partner.gravVel;
			// Remove the inactive character
			removeModel(partner);

			rootNode.updateRenderState();
		}
	}

	/**
	 * Returns total time in {@code BattleGameState}.
	 * 
	 * @author Khoa
	 * 
	 */
	public double getTotalTime() {
		return totalTime;
	}

	/**
	 * Returns pointer to main character.
	 * 
	 * @author Khoa
	 * 
	 */
	public MajorCharacter getPlayer() {
		return player;
	}

	/**
	 * Returns pointer to partner character.
	 * 
	 * @author Khoa
	 * 
	 */
	public MajorCharacter getPartner() {
		return partner;
	}

	/**
	 * Returns pointer to current target.
	 * 
	 * @author Khoa
	 * 
	 */
	public Model getTarget() {
		if (currentTarget == null)
			nextTarget(0);
		return currentTarget;
	}

	/**
	 * Returns player's attacks
	 * 
	 * @author Khoa
	 * 
	 */
	public Class[] getPlayerAttacks() {
		return playerAttacks;
	}

	/**
	 * Returns an {@code ArrayList} of models.
	 */
	public ArrayList<Model> getModels() {
		return models;
	}

	@Override
	protected void onActivate() {
		if (!NetworkingObjects.isServer) {
			// System.out.println("Lolwut, why is this activating: " +
			// NetworkingObjects.isServer);
			super.onActivate();
		}
	}

	public LightState getLightState() {
		return lightState;
	}

	// duplicate the functionality of DebugGameState
	// Most of this can be commented out during finalization
	protected void initKeyBindings() {
		/** Assign key TAB to action "camera_mode". */
		KeyBindingManager.getKeyBindingManager().set("camera_mode", KeyInput.KEY_TAB);
		/** Assign key 1 to action "target_near". */
		KeyBindingManager.getKeyBindingManager().set("target_near", KeyInput.KEY_1);
		/** Assign key 2 to action "target_far". */
		KeyBindingManager.getKeyBindingManager().set("target_far", KeyInput.KEY_2);
		/** Assign WASD keys to free camera controls. */
		KeyBindingManager.getKeyBindingManager().set("cam_left", KeyInput.KEY_A);
		KeyBindingManager.getKeyBindingManager().set("cam_right", KeyInput.KEY_D);
		KeyBindingManager.getKeyBindingManager().set("cam_up", KeyInput.KEY_W);
		KeyBindingManager.getKeyBindingManager().set("cam_down", KeyInput.KEY_S);
		/** Assign key P to action "toggle_pause". */
		KeyBindingManager.getKeyBindingManager().set("toggle_pause", KeyInput.KEY_P);
		// These actions are holdovers from DebugGameState and are not fully
		// "supported"
		/** Assign key T to action "toggle_wire". */
		KeyBindingManager.getKeyBindingManager().set("toggle_wire", KeyInput.KEY_T);
		/** Assign key L to action "toggle_lights". */
		KeyBindingManager.getKeyBindingManager().set("toggle_lights", KeyInput.KEY_L);
		/** Assign key B to action "toggle_bounds". */
		KeyBindingManager.getKeyBindingManager().set("toggle_bounds", KeyInput.KEY_B);
		/** Assign key N to action "toggle_normals". */
		KeyBindingManager.getKeyBindingManager().set("toggle_normals", KeyInput.KEY_N);
		/** Assign key O to action "camera_out". */
		KeyBindingManager.getKeyBindingManager().set("camera_out", KeyInput.KEY_O);
		KeyBindingManager.getKeyBindingManager().set("screen_shot", KeyInput.KEY_F1);
		KeyBindingManager.getKeyBindingManager().set("exit", KeyInput.KEY_ESCAPE);
		KeyBindingManager.getKeyBindingManager().set("toggle_depth", KeyInput.KEY_F3);
		KeyBindingManager.getKeyBindingManager().set("toggle_stats", KeyInput.KEY_F4);
		KeyBindingManager.getKeyBindingManager().set("mem_report", KeyInput.KEY_R);
		KeyBindingManager.getKeyBindingManager().set("toggle_mouse", KeyInput.KEY_M);
	}

	@Override
	public void stateUpdate(float _tpf) {

		// Update the InputHandler
		if (input != null) {
			input.update(tpf);

			/** If exit is a valid command (via key Esc), exit game */
			if (KeyBindingManager.getKeyBindingManager().isValidCommand("exit", false)) {
				if (game != null) {
					game.finish();
				} else {
					Reaxion.terminate();
				}
			}

			// Update the InputHandler
			if (input != null) {
				/**
				 * If toggle_pause is a valid command (via key P), change pause.
				 */
				if (KeyBindingManager.getKeyBindingManager().isValidCommand("toggle_pause", false)
						&& timing) {
					pause = !pause;
					// toggle the overlay
					if (pause) {
						AudioPlayer.gamePaused();
						pauseNode.pause();
						// BgmPlayer.gamePaused();
					} else {
						AudioPlayer.gameUnpaused();
						pauseNode.unpause();
						// BgmPlayer.gameUnpaused();
					}
					System.out.println("Paused: " + pause);
				}

				if (pause)
					return;

			}
		}

		// Update the PlayerInput
		if (NetworkingObjects.isServer) {
			// consider having if both at the same time so that it's not unfair?
	//		System.out.println("p1input="+NetworkingObjects.p1input+"\t\tp2input="+NetworkingObjects.p2input);
			if (NetworkingObjects.p1input != null)
				synchronized (NetworkingObjects.p1input) {
					((ServerBattleGameState) this).checkKeys(NetworkingObjects.p1input,
							NetworkingObjects.PlayerNum.P1);
				}
			if (NetworkingObjects.p2input != null)
				synchronized (NetworkingObjects.p2input) {
					((ServerBattleGameState) this).checkKeys(NetworkingObjects.p2input,
							NetworkingObjects.PlayerNum.P2);
				}
		} else {
			if (playerInput != null) {
				playerInput.checkKeys();
			}
		}

		// Update time
		tpf = _tpf;
		if (timing)
			totalTime += tpf;

		// Make the stage act
		stage.act(this);

		// Traverse list of models and call act() method
		for (int i = 0; i < models.size(); i++)
			models.get(i).act(this);

		if (!NetworkingObjects.isServer) {
			// Update the camera
			if (cameraMode == "lock" && player != null && models.size() > 0
					&& models.indexOf(currentTarget) != -1) {
				Vector3f p = player.getTrackPoint();
				// System.out.println(models+" "+currentTarget);
				Vector3f t = currentTarget.getTrackPoint();
				if (!p.equals(t)) {
					// Vector3f camOffset = new Vector3f(t.x-p.x, t.y-p.y,
					// t.z-p.z);
					float angle = FastMath.atan2(p.z - t.z, p.x - t.x);
					// camOffset = camOffset.normalize().mult(cameraDistance);
					// System.out.println((p.x-camOffset.x)+", "+(p.y-camOffset.y)+", "+(p.z-camOffset.z));
					cam.setLocation(new Vector3f(p.x + cameraDistance
							* FastMath.cos(angle - camRotXZ), p.y + cameraDistance
							* FastMath.sin(camRotY), p.z + cameraDistance
							* FastMath.sin(angle - camRotXZ)));
					cam.lookAt(t, new Vector3f(0, 1, 0));
				}
			} else if (cameraMode == "free" && player != null) {
				Vector3f p = player.getTrackPoint();
				float x, y, z, minor;
				y = cameraDistance * (float) Math.sin(camRotY);
				minor = cameraDistance * (float) Math.cos(camRotY);
				x = minor * (float) Math.sin(camRotXZ);
				z = minor * (float) Math.cos(camRotXZ);
				cam.setLocation(new Vector3f(p.x + x, p.y + y, p.z + z));
				cam.lookAt(p, new Vector3f(0, 1, 0));
			}

			// Update the audio system
			AudioSystem.getSystem().update();
			SfxPlayer.update(this);
		}

		if (!NetworkingObjects.isServer) {
			// Update the HUD
			hudNode.update(this);
		}

		// Update the geometric state of the rootNode
		rootNode.updateGeometricState(tpf, true);

		// Update the pass manager
		pManager.updatePasses(tpf);

		if (input != null) {
			/**
			 * If camera_mode is a valid command (via key TAB), switch camera
			 * modes.
			 */
			if (KeyBindingManager.getKeyBindingManager().isValidCommand("camera_mode", false)) {
				swapCameraMode();
			}
			/**
			 * If camera controls are valid commands (via WASD keys), change
			 * camera angle.
			 */
			if (KeyBindingManager.getKeyBindingManager().isValidCommand("cam_left", true)) {
				camRotXZ -= camRotXZSpd;
				if (cameraMode != "free")
					camRotXZ = Math.max(camRotXZ, camRotXZLimit[0]);
			}
			if (KeyBindingManager.getKeyBindingManager().isValidCommand("cam_right", true)) {
				camRotXZ += camRotXZSpd;
				if (cameraMode != "free")
					camRotXZ = Math.min(camRotXZ, camRotXZLimit[1]);
			}
			if (KeyBindingManager.getKeyBindingManager().isValidCommand("cam_up", true)) {
				if (cameraMode == "free")
					camRotY = Math.min(camRotY + camRotYSpd, camRotYLimit[1]);
				else
					camRotY = Math.min(camRotY + camRotYSpd, camRotYLimit[2]);
			}
			if (KeyBindingManager.getKeyBindingManager().isValidCommand("cam_down", true)) {
				camRotY = Math.max(camRotY - camRotYSpd, camRotYLimit[0]);
			}
			/**
			 * If target_near is a valid command (via key 1), switch to next
			 * closest target.
			 */
			if (KeyBindingManager.getKeyBindingManager().isValidCommand("target_near", false)
					&& cameraMode == "lock") {
				nextTarget(-1);
				rootNode.updateRenderState();
			}
			/**
			 * If target_far is a valid command (via key 2), switch to next
			 * furthest target.
			 */
			if (KeyBindingManager.getKeyBindingManager().isValidCommand("target_far", false)
					&& cameraMode == "lock") {
				nextTarget(1);
				rootNode.updateRenderState();
			}
			/**
			 * If toggle_wire is a valid command (via key T), change wirestates.
			 */
			if (KeyBindingManager.getKeyBindingManager().isValidCommand("toggle_wire", false)) {
				wireState.setEnabled(!wireState.isEnabled());
				rootNode.updateRenderState();
			}
			/**
			 * If toggle_lights is a valid command (via key L), change
			 * lightstate.
			 */
			if (KeyBindingManager.getKeyBindingManager().isValidCommand("toggle_lights", false)) {
				lightState.setEnabled(!lightState.isEnabled());
				rootNode.updateRenderState();
			}
			/** If toggle_bounds is a valid command (via key B), change bounds. */
			if (KeyBindingManager.getKeyBindingManager().isValidCommand("toggle_bounds", false)) {
				showBounds = !showBounds;
			}
			/** If toggle_depth is a valid command (via key F3), change depth. */
			if (KeyBindingManager.getKeyBindingManager().isValidCommand("toggle_depth", false)) {
				showDepth = !showDepth;
			}
			/** If toggle_stats is a valid command (via key F4), change depth. */
			if (KeyBindingManager.getKeyBindingManager().isValidCommand("toggle_stats", false)) {
				if (statisticsCreated == false) {
					// create a statistics game state
					GameStateManager.getInstance().attachChild(
							new StatisticsGameState("stats", 1f, 0.25f, 0.75f, true));
					statisticsCreated = true;
				}
				GameStateManager.getInstance().getChild("stats")
						.setActive(!GameStateManager.getInstance().getChild("stats").isActive());
			}

			if (KeyBindingManager.getKeyBindingManager().isValidCommand("toggle_normals", false)) {
				showNormals = !showNormals;
			}
			/**
			 * If camera_out is a valid command (via key O), show camera
			 * location.
			 */
			if (KeyBindingManager.getKeyBindingManager().isValidCommand("camera_out", false)) {
				logger.info("Camera at: "
						+ DisplaySystem.getDisplaySystem().getRenderer().getCamera().getLocation());
			}
			if (KeyBindingManager.getKeyBindingManager().isValidCommand("screen_shot", false)) {
				DisplaySystem.getDisplaySystem().getRenderer()
						.takeScreenShot("SimpleGameScreenShot");
			}
			if (KeyBindingManager.getKeyBindingManager().isValidCommand("mem_report", false)) {
				long totMem = Runtime.getRuntime().totalMemory();
				long freeMem = Runtime.getRuntime().freeMemory();
				long maxMem = Runtime.getRuntime().maxMemory();

				logger.info("|*|*|  Memory Stats  |*|*|");
				logger.info("Total memory: " + (totMem >> 10) + " kb");
				logger.info("Free memory: " + (freeMem >> 10) + " kb");
				logger.info("Max memory: " + (maxMem >> 10) + " kb");
			}
			if (KeyBindingManager.getKeyBindingManager().isValidCommand("toggle_mouse", false)) {
				MouseInput.get().setCursorVisible(!MouseInput.get().isCursorVisible());
				logger.info("Cursor Visibility set to " + MouseInput.get().isCursorVisible());
			}
		}

		act();
	}

	/**
	 * Called by {@code stateUpdate()}. Override to add extra functionality.
	 */
	protected void act() {

	}

	/**
	 * Toggles camera mode, maintaining viewpoint when switching to free, and
	 * auto-locking closest target when switching to lock
	 */
	protected void swapCameraMode() {
		if (cameraMode == "lock") {
			cameraMode = "free";
			Vector3f c = cam.getLocation();
			Vector3f p = player.getTrackPoint();
			// camRotY = (float)Math.asin((double)((c.y-p.y)/cameraDistance));
			camRotXZ = (float) Math.atan2((c.x - p.x), (c.z - p.z));
		} else if (cameraMode == "free") {
			// make sure there are trackable objects
			boolean noTargets = true;
			for (int i = 0; i < models.size(); i++) {
				if (models.get(i) != player && models.get(i).trackable) {
					noTargets = false;
					break;
				}
			}
			if (!noTargets) {
				cameraMode = "lock";
				camRotXZ = 0;
				nextTarget(0);
			}
		}
		System.out.println("Camera switch to " + cameraMode);
	}

	/**
	 * Sets the target to the specified model and returns whether it was in the
	 * model list
	 */
	public Boolean setTarget(Model m) {
		int i = models.indexOf(m);
		if (i != -1)
			currentTarget = m;
		cam.update();
		return (i != -1);
	}

	/**
	 * Sets the currentTarget to another model, according to the value of
	 * {@code k}
	 * 
	 * @param k
	 *            -1 for next closest from current target, 1 for next further,
	 *            and 0 for closest to player
	 */
	public void nextTarget(int k) {
		// do nothing if there are no models
		if (models.size() == 0)
			return;

		ArrayList<Object[]> o = new ArrayList<Object[]>();

		// Add models and distances to 2D ArrayList
		for (int i = 0; i < models.size(); i++) {
			Model m = models.get(i);
			if (m != player && m.trackable) {
				Object[] a = new Object[2];
				a[0] = new Float(player.model.getWorldTranslation().distance(
						m.model.getWorldTranslation()));
				a[1] = m;
				o.add(a);
			}
		}

		// do nothing if there's nothing to sort
		if (o.size() == 0)
			return;

		// Make it an array
		Object[] t = o.toArray();

		// Sort 2D array by distances
		Arrays.sort(t, new Comparator<Object>() {
			public int compare(Object one, Object two) {
				Object[] first = (Object[]) one;
				Object[] secnd = (Object[]) two;
				// System.out.println((Float)(first[0])+" - "+(Float)(secnd[0]));
				return (int) ((Float) (first[0]) - (Float) (secnd[0]));
			}
		});
		/*
		 * System.out.print("[ "); for (Object f : t)
		 * {System.out.print(Arrays.toString((Object[])f));}
		 * System.out.println(" ]");
		 */

		// Locate the currentTarget's index
		int ind = -1;
		for (int i = 0; i < t.length; i++) {
			if (((Object[]) t[i])[1] == currentTarget) {
				ind = i;
				break;
			}
		}

		// Set the new target
		switch (k) {
		case 0:
			currentTarget = (Model) (((Object[]) t[0])[1]);
			break;
		case -1:
			currentTarget = (Model) (((Object[]) t[(t.length + ind - 1) % t.length])[1]);
			break;
		case 1:
			currentTarget = (Model) (((Object[]) t[(ind + 1) % t.length])[1]);
		}

		/*
		 * // move test cylinders to lock point Vector3f tp =
		 * currentTarget.getTrackPoint(); for (int i=0; i<cyl.length; i++) {
		 * cyl[i].setLocalTranslation(tp.x, tp.y, tp.z); }
		 */

		// Update camera
		cam.update();
	}

	/**
	 * Adds m to the child node that is reflected by water in this stage.
	 * 
	 * @param m
	 */
	public void addModel(Model m) {
		models.add(m);
		containerNode.attachChild(m.model);
	}

	public boolean removeModel(Model m) {
		containerNode.detachChild(m.model);
		return models.remove(m);
	}

	public Node getContainerNode() {
		return containerNode;
	}

	public void toggleZPressed(boolean b) {
		hudNode.zPressed = b;
	}

	@Override
	public void stateRender(float tpf) {
		if (!NetworkingObjects.isServer) {
			// Render the rootNode
			DisplaySystem.getDisplaySystem().getRenderer().draw(rootNode);

			if (showBounds) {
				Debugger.drawBounds(rootNode, DisplaySystem.getDisplaySystem().getRenderer(), true);
			}

			if (showNormals) {
				Debugger.drawNormals(rootNode, DisplaySystem.getDisplaySystem().getRenderer());
			}

			if (showDepth) {
				DisplaySystem.getDisplaySystem().getRenderer().renderQueue();
				Debugger.drawBuffer(Texture.RenderToTextureType.Depth, Debugger.NORTHEAST,
						DisplaySystem.getDisplaySystem().getRenderer());
			}

			// Have the PassManager render
			pManager.renderPasses(DisplaySystem.getDisplaySystem().getRenderer());
		}
	}

	@Override
	public void cleanup() {
	}
}