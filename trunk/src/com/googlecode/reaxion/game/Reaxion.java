package com.googlecode.reaxion.game;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.concurrent.Callable;
import java.util.logging.Level;
import java.util.logging.Logger;

import jmetest.input.TestHardwareMouse;

import com.googlecode.reaxion.test.ModelTest;
import com.jme.image.Texture;
import com.jme.input.AbsoluteMouse;
import com.jme.input.InputHandler;
import com.jme.scene.state.BlendState;
import com.jme.scene.state.TextureState;
import com.jme.system.DisplaySystem;
import com.jme.util.GameTaskQueueManager;
import com.jme.util.TextureManager;
import com.jme.util.resource.ResourceLocatorTool;
import com.jme.util.resource.SimpleResourceLocator;
import com.jmex.editors.swing.settings.GameSettingsPanel;
import com.jmex.game.StandardGame;
import com.jmex.game.state.GameStateManager;
import com.jmex.game.state.load.LoadingGameState;

/**
 * The main game. This should run everything, but for now it's just a luncher for {@code BattleGameState}.
 * @author Nilay, Khoa
 */
public class Reaxion {

	private static final float GAME_VERSION = 0.2f;

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
	private BattleGameState battleState;
	
	/**
	 * Log response messages from jME
	 */
	private static final Logger logger = Logger.getLogger(ModelTest.class.getName());

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
			/*
			loadState = new LoadingGameState();
			GameStateManager.getInstance().attachChild(loadState);
			loadState.setActive(true);
			*/
			battleState = new BattleGameState();
			GameStateManager.getInstance().attachChild(battleState);
			battleState.setActive(true);
			
			locateTextures();
			
			// Add some characters
	        MajorCharacter mp = (MajorCharacter)LoadingQueue.push(new Monica(false));
	        MajorCharacter t = (MajorCharacter)LoadingQueue.push(new Khoa());        
	        MajorCharacter c = (MajorCharacter)LoadingQueue.push(new Cy());     
	        MajorCharacter n = (MajorCharacter)LoadingQueue.push(new Nilay());
	        
	        // Add checker plane
	        Model cb = LoadingQueue.push(new Model("checkerPlane"));
	        
	        
	        // Load everything!
	        LoadingQueue.execute(battleState);
	        
	        // Set stuff in the battleState
	        battleState.assignPlayer(mp);
	        c.model.setLocalTranslation(2, 0, -1);
	        n.model.setLocalTranslation(-5, 0, -3);
	        battleState.setTarget(t);
	        //battleState.getRootNode().attachChild(cb.model);
	        
	        // reupdate due to added changes
	        battleState.getRootNode().updateRenderState();
	        
	        mp.play("stand");
			
			return null;
		}	
	}
	
	/**
	 * Points to the location of texture files to be loaded on the system
	 * @author Khoa
	 */
	public void locateTextures() {
		try {
            SimpleResourceLocator locator = new SimpleResourceLocator(ModelTest.class
                                                    .getClassLoader()
                                                    .getResource("com/googlecode/reaxion/resources/"));
            ResourceLocatorTool.addResourceLocator(
                    ResourceLocatorTool.TYPE_TEXTURE, locator);
            ResourceLocatorTool.addResourceLocator(
                    ResourceLocatorTool.TYPE_MODEL, locator);
        } catch (URISyntaxException e1) {
            logger.log(Level.WARNING, "unable to setup texture directory.", e1);
        }
	}
}
