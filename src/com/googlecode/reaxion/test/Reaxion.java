package com.googlecode.reaxion.test;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.Callable;

import com.jme.animation.Bone;
import com.jme.animation.SkinNode;
import com.jme.math.Vector3f;
import com.jme.scene.shape.Box;
import com.jme.system.DisplaySystem;
import com.jme.util.GameTaskQueueManager;
import com.jmex.editors.swing.settings.GameSettingsPanel;
import com.jmex.game.StandardGame;
import com.jmex.game.state.DebugGameState;
import com.jmex.game.state.GameStateManager;
import com.jmex.game.state.load.LoadingGameState;
import com.jmex.model.collada.ColladaImporter;


public class Reaxion {

	private static final float GAME_VERSION = 0.1f;

	/**
	 * Multithreaded game system that shows the state of GameStates
	 */
	private StandardGame game;
	
	/**
	 * GameState that shows progress of resource loading
	 */
	private LoadingGameState loadState;
	
	/**
	 * GameState that allows basic WASD movement, mouse camera rotation,
	 * and placement of objects, lights, etc.
	 */
	private DebugGameState debugState;

	/**
	 * Initialize the system
	 */
	public Reaxion() {
		/* Allow collection and viewing of scene statistics */
		System.setProperty("jme.stats", "set");
		/* Create a new StandardGame object with the given title in the window */
		game = new StandardGame("Reaxion v" + GAME_VERSION);
	}

	public static void main(String[] args) {
		try {
			Reaxion main = new Reaxion();
			main.start();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Start up the system
	 * @throws IOException when StandardGame's settings are unavailable (?)	
	 * @throws InterruptedException 
	 */
	public void start() throws InterruptedException {
		if(GameSettingsPanel.prompt(game.getSettings()))
			game.start();
		GameTaskQueueManager.getManager().update(new GameInit());
	}

	/**
	 * Initializes the system.
	 * @author Nilay
	 *
	 */
	private class GameInit implements Callable<Void> {

		@Override
		public Void call() throws Exception {
			loadState = new LoadingGameState();
			GameStateManager.getInstance().attachChild(loadState);
			loadState.setActive(true);
			debugState = new DebugGameState();
			GameStateManager.getInstance().attachChild(debugState);
			debugState.setActive(true);
			GameTaskQueueManager.getManager().update(new LoadingTask());
			return null;
		}	
	}

	/**
	 * Loads all resources necessary to the system
	 * and updates the loading progress bar.
	 * @author Nilay
	 *
	 */
	private class LoadingTask implements Callable<Void> {
		@Override
		public Void call() throws Exception {
			InputStream model = Reaxion.class.getClassLoader().getResourceAsStream("com/googlecode/reaxion/resources/sphere.dae");
			if(model == null) {
				System.err.println("Unable to find model!");
				System.exit(0);
			}
			loadState.setProgress(0.0f, "Loading resources/models");
			ColladaImporter.load(model, "sphere");
			//SkinNode skinNode = ColladaImporter.getSkinNode(ColladaImporter.getSkinNodeNames().get(0));
			//Bone skeleton = ColladaImporter.getSkeleton(ColladaImporter.getSkeletonNames().get(0));
			//skeleton.getChild("femChild13_fem2Base4_femBase5_l_leg1").setLocalTranslation(new Vector3f(1.0f, 1.0f, 1.0f));
			ColladaImporter.cleanUp();
			loadState.setProgress(0.8f, "Loading scene...");
			DisplaySystem.getDisplaySystem().getRenderer().getCamera().setAxes(new Vector3f(-1.0f, 0.0f, 0.0f), new Vector3f(0.0f, 0.0f, 1.0f), new Vector3f(0.0f, 1.0f, 0.0f));
			DisplaySystem.getDisplaySystem().getRenderer().getCamera().setLocation(new Vector3f(0.0f, -100.0f, 20.0f));
			//debugState.getRootNode().attachChild(skinNode);
			//debugState.getRootNode().attachChild(skeleton);
			debugState.getRootNode().updateRenderState();
			debugState.getRootNode().updateGeometricState(0, true);
			loadState.setProgress(1.0f, "Finished loading!");
			return null;
		}
	}

}
