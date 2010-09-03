package com.googlecode.reaxion.game;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.concurrent.Callable;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.jme.image.Texture;
import com.jme.input.AbsoluteMouse;
import com.jme.input.InputHandler;
import com.jme.scene.Spatial.LightCombineMode;
import com.jme.scene.Spatial.TextureCombineMode;
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

	private static final String GAME_VERSION = "0.5a";
	
	private static final String attackBaseLocation = "com.googlecode.reaxion.game.";

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
			
			// Set the stage
			Stage cb = (Stage)LoadingQueue.push(new Checkerboard());
			
			// Add some characters
	        MajorCharacter mp = (MajorCharacter)LoadingQueue.push(new Monica(false));
	        MajorCharacter t2 = (MajorCharacter)LoadingQueue.push(new Khoa(false)); 
	        MajorCharacter t = (MajorCharacter)LoadingQueue.push(new Khoa());        
	        MajorCharacter c = (MajorCharacter)LoadingQueue.push(new Cy());     
	        MajorCharacter n = (MajorCharacter)LoadingQueue.push(new Nilay());
	        MajorCharacter c2 = (MajorCharacter)LoadingQueue.push(new Cy());
	        
	        // Load everything!
	        LoadingQueue.execute(battleState);
	        
	        // Set up some abilities!
	        mp.setAbilities(new Ability[]{new EvasiveStart()});
	        t2.setAbilities(new Ability[]{new AfterImage()});
	        t.setAbilities(new Ability[]{new AfterImage()});
	        
	        // Set up test attacks!
	        Class[] attacks1 = new Class[6];
	        attacks1[0] = Class.forName(attackBaseLocation+"ShootBullet");
	        attacks1[1] = Class.forName(attackBaseLocation+"ShieldBarrier");
	        Class[] attacks2 = new Class[6];
	        attacks2[0] = Class.forName(attackBaseLocation+"SpinLance");
	        attacks2[1] = Class.forName(attackBaseLocation+"AngelRain");
	        
	        // Set up some AI!
	        t.assignAI(new TestAI(t));
	        
	        // Set stuff in the battleState
	        battleState.assignTeam(mp, attacks1, t2, attacks2);
	        c.model.setLocalTranslation(2, 5, -1);
	        c2.model.setLocalTranslation(6, 5, 3);
	        c2.gravity = 0;
	        n.model.setLocalTranslation(-5, 0, -3);
	        battleState.nextTarget(0);
	        
	        // reupdate due to added changes
	        battleState.getRootNode().updateRenderState();
	        
	        mp.play("stand");
			
			return null;
		}	
	}
}
