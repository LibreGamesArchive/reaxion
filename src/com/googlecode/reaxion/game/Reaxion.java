package com.googlecode.reaxion.game;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.Properties;
import java.util.Scanner;
import java.util.concurrent.Callable;

import javax.swing.JOptionPane;

import com.captiveimagination.jgn.JGN;
import com.googlecode.reaxion.game.audio.AudioPlayer;
import com.googlecode.reaxion.game.mission.MissionManager;
import com.googlecode.reaxion.game.networking.NetworkingObjects;
import com.googlecode.reaxion.game.networking.sync.message.CharacterAndStageSelectionsMessage;
import com.googlecode.reaxion.game.networking.sync.message.StartBattleMessage;
import com.googlecode.reaxion.game.networking.sync.message.SynchronizeCreateModelMessage;
import com.googlecode.reaxion.game.networking.sync.message.SynchronizeModelMessage;
import com.googlecode.reaxion.game.state.BattleGameState;
import com.googlecode.reaxion.game.state.CharacterSelectionState;
import com.googlecode.reaxion.game.state.StageSelectionState;
import com.googlecode.reaxion.game.util.FontUtils;
import com.googlecode.reaxion.game.util.PlayerInfoManager;
import com.jme.input.MouseInput;
import com.jmex.editors.swing.settings.GameSettingsPanel;
import com.jmex.game.StandardGame;
import com.jmex.game.StandardGame.GameType;
import com.jmex.game.state.GameStateManager;
import com.jmex.game.state.load.LoadingGameState;

/**
 * The main game. This should run everything, but for now it's just a luncher
 * for {@code BattleGameState}.
 * 
 * @author Nilay, Khoa
 */
public class Reaxion {
	private enum Purpose {
		INITIAL_INITIALIZATION, BACKGROUND_SERVER
	}

	private static final String GAME_VERSION = "0.5a";

	private Purpose purposeInLife;

	/**
	 * Multithreaded game system that shows the state of GameStates
	 */
	private StandardGame game;
	private static Process server;

	private StageSelectionState stageState;

	/**
	 * GameState that shows progress of resource loading
	 */
	private LoadingGameState loadState;

	/**
	 * GameState that allows basic WASD movement, mouse camera rotation, and
	 * placement of objects, lights, etc.
	 */
	private BattleGameState battleState;

	/**
	 * GameState that allows character selection.
	 */
	private CharacterSelectionState charState;

	// private StageSelectionState stageState;

	public int type;

	/**
	 * Initialize the system
	 */
	public Reaxion(Purpose p) {
		purposeInLife = p;
		/* Allow collection and viewing of scene statistics */
		System.setProperty("jme.stats", "set");

		switch (purposeInLife) {
		case INITIAL_INITIALIZATION:
			/*
			 * Create a new StandardGame object with the given title in the
			 * window
			 */
			game = new StandardGame("Reaxion v" + GAME_VERSION,
					GameType.GRAPHICAL);
			break;
		case BACKGROUND_SERVER:
			// Make one that's invisible
			game = new StandardGame("Reaxion v" + GAME_VERSION,
					GameType.GRAPHICAL);

			// game.setConfigShowMode(ConfigShowMode.NeverShow);
		}

	}

	public static void main(String[] args) {

		/*
		 * Properties p=System.getProperties(); for(Object o:
		 * Collections.list(p.keys())) {
		 * System.err.println(o.toString()+"\t"+p.getProperty(o.toString())); }
		 * 
		 * System.out.println(System.getProperty("java.home")+
		 * "/bin/java -Xms32m -Xmx256m \"-D"
		 * +p.getProperty("java.library.path")+"\" -Dfile.encoding="
		 * +p.getProperty("file.encoding") +
		 * " -classpath \""+p.getProperty("java.class.path") +
		 * "\" com.googlecode.reaxion.game.Reaxion BACKGROUND_SERVER");
		 * 
		 * if(true) return;
		 */

		try {
			Reaxion main = new Reaxion(Purpose.valueOf(args[0]));
			main.start();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Start up the system
	 * 
	 * @throws IOException
	 *             when StandardGame's settings are unavailable (?)
	 * @throws InterruptedException
	 */
	public void start() throws InterruptedException, IOException {
		if (GameSettingsPanel.prompt(game.getSettings()))
			game.start();
		// GameTaskQueueManager.getManager().update(new
		// GameInit(INITIAL_INITIALIZATION));

		// MouseInput.get().setCursorVisible(true);
		// FontUtils.loadFonts();
		// MissionManager.createMissions();
		PlayerInfoManager.init();
		MouseInput.get().setCursorVisible(true);
		
		JGN.register(SynchronizeModelMessage.class);
		JGN.register(SynchronizeCreateModelMessage.class);
		JGN.register(CharacterAndStageSelectionsMessage.class);
		JGN.register(StartBattleMessage.class);

		switch (purposeInLife) {
		case INITIAL_INITIALIZATION:
			AudioPlayer.prepare();
			// SoundEffectManager.initialize();
			FontUtils.loadFonts();
			
		//	Runtime.getRuntime().exec("kill -9 `ps aux | grep java.*BACKGROUND_SERVER | cut -c 12-14  -`");
			int sv = JOptionPane.showConfirmDialog(null, "Be server?");
			switch (sv) {
			case 0:
				Properties p = System.getProperties();
				String[] javaCmd = new String[] {
						System.getProperty("java.home") + "/bin/java",
						"-Xms32m",
						"-Xmx256m",
						"-Djava.library.path="
								+ p.getProperty("java.library.path"),
						"-Dfile.encoding=" + p.getProperty("file.encoding"),
						"-classpath", p.getProperty("java.class.path"),
						"com.googlecode.reaxion.game.Reaxion",
						"BACKGROUND_SERVER" };
				server = Runtime.getRuntime().exec(javaCmd, null, null);

				Thread servConsole = new ServerConsole(server);
				servConsole.start();

				// System.out.println(cmdLine);
				// Runtime.getRuntime().exec(cmdLine);
				// bgServer.start();

				// Thread.sleep(10000); // consider replacing with a .wait
				System.out.println("Started?");
				NetworkingObjects.setUpClient(true);
				break;
			case 1:
				NetworkingObjects.setUpClient(false);
				break;
			case 2:
			default:
				terminate();
			}
			charState = new CharacterSelectionState();
			GameStateManager.getInstance().attachChild(charState);
			charState.setActive(true);
			break;
		case BACKGROUND_SERVER:
			NetworkingObjects.setUpServer();
			MouseInput.get().setCursorVisible(true);
			// this.n
			// charState = new CharacterSelectionState();
			// NetworkingObjects.serverSyncManager.register(charState, new
			// CharacterAndStageSelectionsMessage(States.CHARACTER),
			// NetworkingObjects.updateRate);
			break;
		// move to client that recieves the creation message
		}
	}

	/**
	 * Performs necessary cleanup, then closes application.
	 */
	public static void terminate() {
		System.out.println("terminate called");
		AudioPlayer.cleanup();
		if (server != null)
			server.destroy();
		System.exit(0);
	}

	private class ServerConsole extends Thread {
		private Scanner normal, error;

		public ServerConsole(Process p) {
			normal = new Scanner(p.getInputStream());
			error = new Scanner(p.getErrorStream());
		}

		@Override
		public void run() {
			while (true) {
				while (normal.hasNextLine())
					System.out.println("SERVER:  " + normal.nextLine());

				while (error.hasNextLine())
					System.err.println("SERVER:  " + error.nextLine());

				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * Initializes the system.
	 * 
	 * @author Nilay
	 * 
	 */
	/*
	 * private class GameInit implements Callable<Void> {
	 * 
	 * // @Override public Void call() throws Exception { //
	 * MouseInput.get().setCursorVisible(true); AudioPlayer.prepare(); //
	 * SoundEffectManager.initialize(); FontUtils.loadFonts();
	 * MissionManager.createMissions();
	 * 
	 * JGN.register(SynchronizeModelMessage.class);
	 * JGN.register(SynchronizeCreateModelMessage.class);
	 * 
	 * int sv = JOptionPane.showConfirmDialog(null, "Be server?");
	 * 
	 * switch (sv) { case 0: NetworkingObjects.setUpServer(); break; case 1: //
	 * NetworkingObjects.setUpClient(); break; case 2: default: terminate(); }
	 * 
	 * PlayerInfoManager.init();
	 * 
	 * charState = new CharacterSelectionState();
	 * GameStateManager.getInstance().attachChild(charState);
	 * charState.setActive(true);
	 * 
	 * return null; }
	 * 
	 * }
	 */
}