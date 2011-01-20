package com.googlecode.reaxion.game.state;

import java.io.IOException;

import com.googlecode.reaxion.game.Reaxion;
import com.googlecode.reaxion.game.audio.AudioPlayer;
import com.googlecode.reaxion.game.audio.SfxPlayer;
import com.googlecode.reaxion.game.input.ClientPlayerInput;
import com.googlecode.reaxion.game.input.PlayerInput;
import com.googlecode.reaxion.game.model.Model;
import com.googlecode.reaxion.game.model.character.MajorCharacter;
import com.googlecode.reaxion.game.networking.NetworkingObjects;
import com.googlecode.reaxion.game.util.Battle;
import com.jme.input.KeyBindingManager;
import com.jme.input.MouseInput;
import com.jme.math.FastMath;
import com.jme.math.Vector3f;
import com.jme.system.DisplaySystem;
import com.jmex.audio.AudioSystem;
import com.jmex.game.state.GameStateManager;
import com.jmex.game.state.StatisticsGameState;

/**
 * {@code ClientGameState} extends {@code BattleGameState} with functionality
 * dedicated to receiving network data.
 * 
 * @author Khoa
 */
public class ClientBattleGameState extends BattleGameState {

	// FIXME future bug: Tell client which one it is so it knows whose HUD to
	// display

	public static final String NAME = "clientBattleGameState";

	protected PlayerInput opPlayerInput;
	protected MajorCharacter opPlayer;
	protected Class[] opPlayerAttacks;
	protected MajorCharacter opPartner;
	protected Class[] opPartnerAttacks;
	
//	protected ClientPlayerInput clientInput;  

	public ClientBattleGameState() {
		super();
	}

	public ClientBattleGameState(Battle b) {
		super(b);
		
		playerInput = new ClientPlayerInput(this);
		try {
			NetworkingObjects.clientSyncManager.register(playerInput, new SynchronizeCreatePlayerInputMessage(), NetworkingObjects.updateRate);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void stateUpdate(float _tpf) {
		try {
			Thread.sleep(10);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

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

			// Update the InputHandler
			if (input != null) {
				/**
				 * If toggle_pause is a valid command (via key P), change pause.
				 */
				if (KeyBindingManager.getKeyBindingManager().isValidCommand(
						"toggle_pause", false)
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
		if (playerInput != null) {
			playerInput.checkKeys();
		}

		// Update time
		tpf = _tpf;
		if (timing)
			totalTime += tpf;

		
		// Make the stage act
//		if(stage!=null) stage.act(this);
		// Client can't do this otherwise bad shit will happen D:

		// Update the camera
		if (cameraMode == "lock" && player != null && models.size() > 0
				&& models.indexOf(currentTargets) != -1) {
			Vector3f p = player.getTrackPoint();
			// System.out.println(models+" "+currentTargets);
			Vector3f t = getCurrentTarget().getTrackPoint();
			if (!p.equals(t)) {
				// Vector3f camOffset = new Vector3f(t.x-p.x, t.y-p.y, t.z-p.z);
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

		// Update the HUD
		hudNode.update(this);

		// Update the geometric state of the rootNode
		rootNode.updateGeometricState(tpf, true);

		// Update the pass manager
		pManager.updatePasses(tpf);

		if (input != null) {
			/**
			 * If camera_mode is a valid command (via key TAB), switch camera
			 * modes.
			 */
			if (KeyBindingManager.getKeyBindingManager().isValidCommand(
					"camera_mode", false)) {
				swapCameraMode();
			}
			/**
			 * If camera controls are valid commands (via WASD keys), change
			 * camera angle.
			 */
			if (KeyBindingManager.getKeyBindingManager().isValidCommand(
					"cam_left", true)) {
				camRotXZ -= camRotXZSpd;
				if (cameraMode != "free")
					camRotXZ = Math.max(camRotXZ, camRotXZLimit[0]);
			}
			if (KeyBindingManager.getKeyBindingManager().isValidCommand(
					"cam_right", true)) {
				camRotXZ += camRotXZSpd;
				if (cameraMode != "free")
					camRotXZ = Math.min(camRotXZ, camRotXZLimit[1]);
			}
			if (KeyBindingManager.getKeyBindingManager().isValidCommand(
					"cam_up", true)) {
				if (cameraMode == "free")
					camRotY = Math.min(camRotY + camRotYSpd, camRotYLimit[1]);
				else
					camRotY = Math.min(camRotY + camRotYSpd, camRotYLimit[2]);
			}
			if (KeyBindingManager.getKeyBindingManager().isValidCommand(
					"cam_down", true)) {
				camRotY = Math.max(camRotY - camRotYSpd, camRotYLimit[0]);
			}
			/**
			 * If target_near is a valid command (via key 1), switch to next
			 * closest target.
			 */
			if (KeyBindingManager.getKeyBindingManager().isValidCommand(
					"target_near", false)
					&& cameraMode == "lock") {
//				nextTargets(-1);
				rootNode.updateRenderState();
			}
			/**
			 * If target_far is a valid command (via key 2), switch to next
			 * furthest target.
			 */
			if (KeyBindingManager.getKeyBindingManager().isValidCommand(
					"target_far", false)
					&& cameraMode == "lock") {
	//			nextTargets(1);
				rootNode.updateRenderState();
			}
			/**
			 * If toggle_wire is a valid command (via key T), change wirestates.
			 */
			if (KeyBindingManager.getKeyBindingManager().isValidCommand(
					"toggle_wire", false)) {
				wireState.setEnabled(!wireState.isEnabled());
				rootNode.updateRenderState();
			}
			/**
			 * If toggle_lights is a valid command (via key L), change
			 * lightstate.
			 */
			if (KeyBindingManager.getKeyBindingManager().isValidCommand(
					"toggle_lights", false)) {
				lightState.setEnabled(!lightState.isEnabled());
				rootNode.updateRenderState();
			}
			/** If toggle_bounds is a valid command (via key B), change bounds. */
			if (KeyBindingManager.getKeyBindingManager().isValidCommand(
					"toggle_bounds", false)) {
				showBounds = !showBounds;
			}
			/** If toggle_depth is a valid command (via key F3), change depth. */
			if (KeyBindingManager.getKeyBindingManager().isValidCommand(
					"toggle_depth", false)) {
				showDepth = !showDepth;
			}
			/** If toggle_stats is a valid command (via key F4), change depth. */
			if (KeyBindingManager.getKeyBindingManager().isValidCommand(
					"toggle_stats", false)) {
				if (statisticsCreated == false) {
					// create a statistics game state
					GameStateManager.getInstance().attachChild(
							new StatisticsGameState("stats", 1f, 0.25f, 0.75f,
									true));
					statisticsCreated = true;
				}
				GameStateManager.getInstance().getChild("stats").setActive(
						!GameStateManager.getInstance().getChild("stats")
								.isActive());
			}

			if (KeyBindingManager.getKeyBindingManager().isValidCommand(
					"toggle_normals", false)) {
				showNormals = !showNormals;
			}
			/**
			 * If camera_out is a valid command (via key O), show camera
			 * location.
			 */
			if (KeyBindingManager.getKeyBindingManager().isValidCommand(
					"camera_out", false)) {
				logger.info("Camera at: "
						+ DisplaySystem.getDisplaySystem().getRenderer()
								.getCamera().getLocation());
			}
			if (KeyBindingManager.getKeyBindingManager().isValidCommand(
					"screen_shot", false)) {
				DisplaySystem.getDisplaySystem().getRenderer().takeScreenShot(
						"SimpleGameScreenShot");
			}
			if (KeyBindingManager.getKeyBindingManager().isValidCommand(
					"mem_report", false)) {
				long totMem = Runtime.getRuntime().totalMemory();
				long freeMem = Runtime.getRuntime().freeMemory();
				long maxMem = Runtime.getRuntime().maxMemory();

				logger.info("|*|*|  Memory Stats  |*|*|");
				logger.info("Total memory: " + (totMem >> 10) + " kb");
				logger.info("Free memory: " + (freeMem >> 10) + " kb");
				logger.info("Max memory: " + (maxMem >> 10) + " kb");
			}
			if (KeyBindingManager.getKeyBindingManager().isValidCommand(
					"toggle_mouse", false)) {
				MouseInput.get().setCursorVisible(
						!MouseInput.get().isCursorVisible());
				logger.info("Cursor Visibility set to "
						+ MouseInput.get().isCursorVisible());
			}
		}

		try {
		rootNode.updateRenderState();
		} catch(Exception e) {
			e.printStackTrace();
			System.out.println("Thread: "+Thread.currentThread());
		} // consider making removeMessage put in a request for removal in opengl thread. that makes the most sense really.
	}

	@Override
	public synchronized void addModel(Model m) {
		models.add(m);
		containerNode.attachChild(m.model);
	}

	@Override
	public synchronized boolean removeModel(Model m) {
		if (models.contains(m)) {
		//	NetworkingObjects.clientSyncManager.unregister(m);
			containerNode.detachChild(m.model);
			return models.remove(m);
		}
		System.out
				.println("Not removed, not in the model list, not a good comic");
		return false;
	}

}