package com.googlecode.reaxion.game;

import java.io.IOException;

import com.googlecode.reaxion.game.model.character.Cy;
import com.googlecode.reaxion.game.model.character.Khoa;
import com.googlecode.reaxion.game.model.character.MajorCharacter;
import com.googlecode.reaxion.game.model.character.Monica;
import com.googlecode.reaxion.game.model.character.Nilay;
import com.googlecode.reaxion.game.model.stage.FlowerField;
import com.googlecode.reaxion.game.model.stage.Stage;
import com.googlecode.reaxion.game.state.BattleGameState;
import com.googlecode.reaxion.game.state.CharSelectState;
import com.googlecode.reaxion.game.state.StageSelectionState;
import com.googlecode.reaxion.game.util.LoadingQueue;
import com.googlecode.reaxion.game.state.StageSelectionState;
import com.jme.util.GameTaskQueueManager;
import com.jmex.editors.swing.settings.GameSettingsPanel;
import com.jmex.game.StandardGame;
import com.jmex.game.state.GameStateManager;
import com.jmex.game.state.load.LoadingGameState;
import java.util.concurrent.Callable;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import com.googlecode.reaxion.game.model.*;

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
	private static final String attackBaseLocation = "com.googlecode.reaxion.game.attack.";
	
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
	private CharSelectState charState;
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
	 * Initializes the system.
	 * @author Nilay
	 *
	 */
	private class GameInit implements Callable<Void> {

		//@Override
		public Void call() throws Exception {
			/*
			loadState = new LoadingGameState();
			GameStateManager.getInstance().attachChild(loadState);
			loadState.setActive(true);
			*/

			
			
			//battleState = new BattleGameState();
			//GameStateManager.getInstance().attachChild(battleState);
			//battleState.setActive(true);
			//stageState = new StageSelectionState();
			//GameStateManager.getInstance().attachChild(stageState);
			//stageState.setActive(true);
			charState = new CharSelectState(battleState);
			GameStateManager.getInstance().attachChild(charState);
			charState.setActive(true);
			
			// Set the stage
			Stage cb = (Stage)LoadingQueue.push(new FlowerField());
			
			// Add some characters
	        MajorCharacter mp = (MajorCharacter)LoadingQueue.push(new Monica());
	        MajorCharacter t2 = (MajorCharacter)LoadingQueue.push(new Khoa(false)); 
	        MajorCharacter t = (MajorCharacter)LoadingQueue.push(new Khoa());        
	        MajorCharacter c = (MajorCharacter)LoadingQueue.push(new Cy());     
	        MajorCharacter n = (MajorCharacter)LoadingQueue.push(new Nilay());
	        MajorCharacter c2 = (MajorCharacter)LoadingQueue.push(new Cy());
	        //MajorCharacter a = (MajorCharacter)LoadingQueue.push(new Austin());
	        
	        //Creates a list of characters for use with character selection
	        ArrayList <MajorCharacter> charList = new ArrayList<MajorCharacter>();
	        charList.add(t);
	        charList.add(c);
	        charList.add(n);
	        charList.add(mp);
	        //charList.add(a);
	        
	        
	        //boolean x = true;
	        //Character Select
	        //CharSelectForm CS = new CharSelectForm();
	        //waits till start button is pressed
	        //while(CS.beginGame())
	       // {
	        //}
	       // int[]chars = CS.getChoices();
	        
	        
	        
	        // Load everything!
	        LoadingQueue.execute(battleState);
	        
	        
	        // Set up some abilities!

	        //charList.get(chars[0]).setAbilities(new Ability[]{new EvasiveStart()});
	        //charList.get(chars[1]).setAbilities(new Ability[]{new AfterImage()});

	        
	        // Set up test attacks!
	        Class[] attacks1 = new Class[6];
	        attacks1[0] = Class.forName(attackBaseLocation+"ShootBullet");
	        attacks1[1] = Class.forName(attackBaseLocation+"ShieldBarrier");
	        Class[] attacks2 = new Class[6];
	        attacks2[0] = Class.forName(attackBaseLocation+"SpinLance");
	        attacks2[1] = Class.forName(attackBaseLocation+"AngelRain");
	        
	        // Set up some AI!
	        //t.assignAI(new TestAI(t));
	        //c2.assignAI(new TestAI(c2));
	        //charList.get(chars[1]).assignAI(new TestAI(charList.get(chars[1])));
	        
	        // Set the opponent!
	        com.googlecode.reaxion.game.model.character.Character[] opponents = new com.googlecode.reaxion.game.model.character.Character[1];
	        opponents[0] = t;
	        battleState.assignOpponents(opponents);
	        
	        // Set stuff in the battleState

	        //battleState.assignPlayer(charList.get(chars[0]), attacks);

	        battleState.assignTeam(mp, attacks1, t2, attacks2);

	        c.model.setLocalTranslation(2, 5, -1);
	        c2.model.setLocalTranslation(6, 5, 3);
	        c2.gravity = 0;
	        n.model.setLocalTranslation(-5, 0, -3);
	        battleState.nextTarget(0);
	        
	        // reupdate due to added changes
	        battleState.getRootNode().updateRenderState();
	        
	        //mp.play("stand");
	        //charList.get(chars[0]).play("stand");
			



			return null;
		}	
	}
}
