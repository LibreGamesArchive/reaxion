package com.googlecode.reaxion.game;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.concurrent.Callable;

import javax.swing.JOptionPane;
import javax.swing.JPanel;

import com.captiveimagination.jgn.JGN;
import com.captiveimagination.jgn.clientserver.JGNClient;
import com.captiveimagination.jgn.clientserver.JGNServer;
import com.captiveimagination.jgn.synchronization.SyncObjectManager;
import com.captiveimagination.jgn.synchronization.SynchronizationManager;
import com.captiveimagination.jgn.synchronization.message.Synchronize3DMessage;
import com.captiveimagination.jgn.synchronization.message.SynchronizeCreateMessage;
import com.captiveimagination.jgn.synchronization.message.SynchronizeRemoveMessage;
import com.captiveimagination.jgn.synchronization.swing.SwingGraphicalController;
import com.captiveimagination.jgn.test.sync.SimpleSynchronization;
import com.googlecode.reaxion.game.audio.AudioPlayer;
import com.googlecode.reaxion.game.mission.MissionManager;
import com.googlecode.reaxion.game.networking.JMEGraphicalController;
import com.googlecode.reaxion.game.networking.chat.ChatClient;
import com.googlecode.reaxion.game.networking.chat.ChatServer;
import com.googlecode.reaxion.game.networking.sync.message.SynchronizeModelMessage;
import com.googlecode.reaxion.game.state.BattleGameState;
import com.googlecode.reaxion.game.state.CharacterSelectionState;
import com.googlecode.reaxion.game.state.StageSelectionState;
import com.googlecode.reaxion.game.util.FontUtils;
import com.googlecode.reaxion.game.util.PlayerInfoManager;
import com.jme.input.MouseInput;
import com.jme.util.GameTaskQueueManager;
import com.jmex.editors.swing.settings.GameSettingsPanel;
import com.jmex.game.StandardGame;
import com.jmex.game.state.GameStateManager;
import com.jmex.game.state.load.LoadingGameState;

/**
 * The main game. This should run everything, but for now it's just a luncher
 * for {@code BattleGameState}.
 * 
 * @author Nilay, Khoa
 */
public class Reaxion {
	private static final long SERVER_OBJECT = 1;
	private static final long CLIENT_OBJECT = 2;

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
	 * GameState that allows basic WASD movement, mouse camera rotation, and
	 * placement of objects, lights, etc.
	 */
	private BattleGameState battleState;

	/**
	 * GameState that allows character selection.
	 */
	private CharacterSelectionState charState;

	// private StageSelectionState stageState;

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
	 * 
	 * @throws IOException
	 *             when StandardGame's settings are unavailable (?)
	 * @throws InterruptedException
	 */
	public void start() throws InterruptedException {
		if (GameSettingsPanel.prompt(game.getSettings()))
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
	 * 
	 * @author Nilay
	 * 
	 */
	private class GameInit implements Callable<Void> {

		// @Override
		public Void call() throws Exception {
			MouseInput.get().setCursorVisible(true);
			AudioPlayer.prepare();
			// SoundEffectManager.initialize();
			FontUtils.loadFonts();
			MissionManager.createMissions();

			setUpNetworkStuff();

			int sv = JOptionPane.showConfirmDialog(null, "Be server?");

			switch (sv) {
			case 0:
				new ChatServer();
			case 1:
				new ChatClient();
				break;
			case 2:
			default:
				terminate();
			}

			PlayerInfoManager.init();

			charState = new CharacterSelectionState();
			GameStateManager.getInstance().attachChild(charState);
			charState.setActive(true);

			return null;
		}

		// TODO (nwk) Make a StageCreateMessage that tells the client which
		// stage was picked (pass an Enum or something) and then have client do
		// that locally.

		// FIXME (nwk) do different behaviors depending on client / server

		private void setUpNetworkStuff() {
			// FIXME (nwk) cp'd from online example, needs to be fix'd for this

			JGN.register(SynchronizeModelMessage.class);

			// Instantiate an instance of a JMEGraphicalController
			JMEGraphicalController controller = new JMEGraphicalController();

			// Start the server
			InetSocketAddress serverReliable = new InetSocketAddress(
					InetAddress.getLocalHost(), 1000);
			InetSocketAddress serverFast = new InetSocketAddress(InetAddress
					.getLocalHost(), 2000);
			JGNServer server = new JGNServer(serverReliable, serverFast);
			SynchronizationManager serverSyncManager = new SynchronizationManager(
					server, controller);
			serverSyncManager.addSyncObjectManager(new SyncObjectManager() {
				public Object create(SynchronizeCreateMessage scm) {
					// TODO (nwk) make it make an object
					// TODO (nwk) figure out how to make this work. the client
					// needs to add a thing
				}

				public boolean remove(SynchronizeRemoveMessage srm,
						Object object) {
					// TODO (nwk) figure out how to reference list of models
					// and call .removeFromParent()
					return true;
				}
			});
			JGN.createThread(server, serverSyncManager).start();

			// Register our server object with the synchronization manager
			// serverSyncManager.register(game.getServerPanel(), new
			// SynchronizeCreateMessage(), 50);

			// Start the client
			JGNClient client = new JGNClient(new InetSocketAddress(InetAddress
					.getLocalHost(), 0), new InetSocketAddress(InetAddress
					.getLocalHost(), 0));
			SynchronizationManager clientSyncManager = new SynchronizationManager(
					client, controller);
			clientSyncManager.addSyncObjectManager(new SyncObjectManager() {
				public Object create(SynchronizeCreateMessage scm) {

				}

				public boolean remove(SynchronizeRemoveMessage srm,
						Object object) {

					return true;
				}
			});
			JGN.createThread(client, clientSyncManager).start();
			client.connectAndWait(serverReliable, serverFast, 5000);

			// Register our client object with the synchronization manager
			clientSyncManager.register(ssClient.getClientPanel(),
					new SynchronizeCreateMessage(), 50);

		}
	}
}