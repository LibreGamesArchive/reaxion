package com.googlecode.reaxion.game;

import java.awt.Point;
import java.io.IOException;
import java.util.concurrent.Callable;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.googlecode.reaxion.game.audio.AudioPlayer;
import com.googlecode.reaxion.game.audio.SoundEffectManager;
import com.googlecode.reaxion.game.burstgrid.info.*;
import com.googlecode.reaxion.game.mission.MissionManager;
import com.googlecode.reaxion.game.state.BattleGameState;
import com.googlecode.reaxion.game.state.DialogueGameState;
import com.googlecode.reaxion.game.state.StageGameState;
import com.googlecode.reaxion.game.state.CharacterSelectionState;
import com.googlecode.reaxion.game.state.StageSelectionState;
import com.googlecode.reaxion.game.util.Actor;
import com.googlecode.reaxion.game.util.FontUtils;
import com.googlecode.reaxion.game.util.PlayerInfoManager;
import com.jme.input.MouseInput;
import com.jme.util.GameTaskQueueManager;
import com.jmex.angelfont.BitmapFont;
import com.jmex.angelfont.BitmapFontLoader;
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

	/**
	 * Multithreaded game system that shows the state of GameStates
	 */
	private StandardGame game;
	
	private StageSelectionState stageState;
	
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
	 * GameState that allows character selection.
	 */
	private CharacterSelectionState charState;
	//private StageSelectionState stageState;

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
	 * Performs necessary cleanup, then closes application.
	 */
	public static void terminate() {
		AudioPlayer.cleanup();
		System.exit(0);
	}

	/**
	 * Initializes the system.
	 * @author Nilay
	 *
	 */
	private class GameInit implements Callable<Void> {

		//@Override	
		public Void call() throws Exception {
			MouseInput.get().setCursorVisible(true);
	    	AudioPlayer.prepare();
//	    	SoundEffectManager.initialize();
			FontUtils.loadFonts();
			
			PlayerInfoManager.init();
			
			charState = new CharacterSelectionState();
			GameStateManager.getInstance().attachChild(charState);
			charState.setActive(true);
			
			MissionManager.createMissions();
			
			return null;
		}	
	}
}