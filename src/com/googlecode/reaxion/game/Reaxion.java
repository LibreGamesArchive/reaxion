package com.googlecode.reaxion.game;

import java.awt.Point;
import java.io.IOException;
import java.util.concurrent.Callable;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.googlecode.reaxion.game.mission.MissionManager;
import com.googlecode.reaxion.game.state.BattleGameState;
import com.googlecode.reaxion.game.state.DialogueGameState;
import com.googlecode.reaxion.game.state.StageGameState;
import com.googlecode.reaxion.game.state.CharacterSelectionState;
import com.googlecode.reaxion.game.state.StageSelectionState;
import com.googlecode.reaxion.game.util.Actor;
import com.googlecode.reaxion.game.util.FontUtils;
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
	 * Initializes the system.
	 * @author Nilay
	 *
	 */
	private class GameInit implements Callable<Void> {

		//@Override
		public Void call() throws Exception {

			FontUtils.loadFonts();
			
//			MissionManager.initMissions();
//			MissionManager.startMission(0);
			
			charState = new CharacterSelectionState();
			GameStateManager.getInstance().attachChild(charState);
			//charState.setActive(true);
			
			int[] times = new int[24];
			for(int i = 0; i < times.length; i++)
				times[i] = i * 20;
			
			// dialogue code
			Actor[] a = {new Actor(), new Actor()};
			a[0].setPortraits(0, new int[]{0}, new String[]{"khoa-test.png"});
			a[0].setPortraits(2, times, 
					new String[]{"cy-test1.png", "cy-test2.png", "cy-test1.png", "cy-test3.png",
					"cy-test1.png", "cy-test2.png", "cy-test1.png", "cy-test3.png",
					"cy-test1.png", "cy-test2.png", "cy-test1.png", "cy-test3.png",
					"cy-test1.png", "cy-test2.png", "cy-test1.png", "cy-test3.png",
					"cy-test1.png", "cy-test2.png", "cy-test1.png", "cy-test3.png",
					"cy-test1.png", "cy-test2.png", "cy-test1.png", "cy-test3.png"});
			a[1].setPortraits(0, new int[]{0}, new String[]{"monica-test.png"});
			a[0].setPositions(0, new int[]{0}, new Point[]{new Point(160, 323)});
			a[1].setPositions(0, new int[]{0}, new Point[]{new Point(520, 323)});
			a[0].setPositions(2, new int[]{0}, new Point[]{new Point(160, 331)});
			a[1].setPositions(1, new int[]{60, 120}, new Point[]{new Point(480, 323), new Point(520, 323)});
			String[] lines = {"Mickey:: Aw, we don't hate it. It's just kinda... scary. But the world's made of light AND darkness. You can't have one without the other, 'cause darkness is half of everything. Sorta makes ya wonder why we are scared of the dark...",
					"You accept darkness, yet choose to live in the light. So why is it that you loathe us who teeter on the edge of nothing? We who were turned away by both light and dark - never given a choice?",
					"I knew. But I was too stubborn to accept it. It's always the same. I try to wrap my mind around things my heart already knows, only to fail.",
					"Xehanort::Students do take after their teachers. Only a fool would be your apprentice. After all, none of this would have happened without you. YOU are the source of all Heartless. It was your research that inspired me to go further than you ever dared.",
					"Thinking of you, wherever you are. We pray for our sorrows to end, and hope that our hearts will blend. Now I will step forward to realize this wish. And who knows: starting a new journey may not be so hard, or maybe it has already begun. There are many worlds, and they share the same sky -\n one sky, one destiny."};
			int[] durations = {0, -1, 230, -2, 600};
			
			DialogueGameState dialogueState = new DialogueGameState(lines, durations, a, "bg_twilight-kingdom.png");
			GameStateManager.getInstance().attachChild(dialogueState);
			dialogueState.setActive(true);
			
			return null;
		}	
	}
}