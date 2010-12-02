package com.googlecode.reaxion.game.networking;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;

import javax.swing.JOptionPane;

import com.captiveimagination.jgn.JGN;
import com.captiveimagination.jgn.clientserver.JGNClient;
import com.captiveimagination.jgn.clientserver.JGNServer;
import com.captiveimagination.jgn.event.MessageListener;
import com.captiveimagination.jgn.message.Message;
import com.captiveimagination.jgn.synchronization.SyncObjectManager;
import com.captiveimagination.jgn.synchronization.SynchronizationManager;
import com.captiveimagination.jgn.synchronization.message.SynchronizeCreateMessage;
import com.captiveimagination.jgn.synchronization.message.SynchronizeRemoveMessage;
import com.googlecode.reaxion.game.model.Model;
import com.googlecode.reaxion.game.networking.sync.message.SynchronizeCreateModelMessage;
import com.googlecode.reaxion.game.networking.sync.message.SynchronizeCreateBattleMessage;
import com.googlecode.reaxion.game.networking.sync.message.SynchronizeModelMessage;
import com.googlecode.reaxion.game.state.CharacterSelectionState;
import com.googlecode.reaxion.game.state.ClientBattleGameState;
import com.googlecode.reaxion.game.util.Battle;
import com.googlecode.reaxion.game.util.LoadingQueue;
import com.jmex.game.state.GameStateManager;

public abstract class NetworkingObjects {

	public static JGNServer server;
	public static JGNClient client;

	/**
	 * Probably don't need to have separate ones for both, since it'll never be
	 * both a server and a client. Too lazy to condense, also it'd be confusing.
	 */
	public static SynchronizationManager serverSyncManager, clientSyncManager;

	public static JMEGraphicalController controller;

	public static ClientBattleGameState cbgs;

	private static InetSocketAddress serverReliable, serverFast;
	public static boolean isServer;
	public static final long updateRate = 50;

	// TODO (nwk) Make a StageCreateMessage that tells the client which
	// stage was picked (pass an Enum or something) and then have client do
	// that locally.

	// FIXME (nwk) do different behaviors depending on client / server

	/**
	 * Sets up the server, which involves setting up a whole new fancy thread
	 * thingamajig.
	 */
	public static void setUpServer() throws IOException {
		isServer = true;

		server = new JGNServer(serverReliable, serverFast);
		serverSyncManager = new SynchronizationManager(server, controller);

		server.addMessageListener(new MessageListener() {
			@Override
			public void messageCertified(Message message) {
				// TODO Auto-generated method stub

			}

			@Override
			public void messageFailed(Message message) {
				// TODO Auto-generated method stub

			}

			@Override
			public void messageReceived(Message message) {
				if (message instanceof SynchronizeCreateBattleMessage) {
					SynchronizeCreateBattleMessage scbm = (SynchronizeCreateBattleMessage) scm;
					// TODO handle creation properly

					Battle c = Battle.getCurrentBattle();
					c.setPlayers(scbm.getCharacters());
					c.setStage(scbm.getStage());
					Battle.setCurrentBattle(c);

					Battle.createNetworkedBattleGameState();

					// FIXME cause this to send info to clients when all choices
					// have been recieved, and make clients create it, and make
					// sure the server will update the game without displaying
					// it, and fix up all the bs you did to othe things

					// I'm not sure this is how you do it
					GameStateManager.getInstance().attachChild(
							Battle.createNetworkedBattleGameState());
				}
			}

			@Override
			public void messageSent(Message message) {
				// TODO Auto-generated method stub

			}
		});

		JGN.createThread(server, serverSyncManager).start();
	}

	public static void setUpClient(boolean local) throws IOException,
			InterruptedException {
		isServer = false;
		JGN.register(SynchronizeModelMessage.class);

		// Instantiate an instance of a JMEGraphicalController
		controller = new JMEGraphicalController();

		if (local) {
			// server IPs
			serverReliable = new InetSocketAddress(InetAddress.getLocalHost(),
					9001);
			serverFast = new InetSocketAddress(InetAddress.getLocalHost(), 9002);
		} else {
			String addr = JOptionPane
					.showInputDialog("Enter server IPv4 address:");
			serverReliable = new InetSocketAddress(addr, 9001);
			serverFast = new InetSocketAddress(addr, 9002);
		}

		// Start the client
		client = new JGNClient(new InetSocketAddress(
				InetAddress.getLocalHost(), 9001), new InetSocketAddress(
				InetAddress.getLocalHost(), 9002));
		clientSyncManager = new SynchronizationManager(client, controller);
		clientSyncManager.addSyncObjectManager(new SyncObjectManager() {
			public Object create(SynchronizeCreateMessage scm) {
				if (scm instanceof SynchronizeCreateModelMessage) {
					SynchronizeCreateModelMessage scmm = (SynchronizeCreateModelMessage) scm;
					if (scmm.isForPreload())
						return LoadingQueue.push(new Model(scmm.getFilename()));
					return LoadingQueue.quickLoad(
							new Model(scmm.getFilename()), cbgs);
				}

				return null;
			}

			public boolean remove(SynchronizeRemoveMessage srm, Object object) {
				// FIXME (nwk) figure out how to reference list of models
				// and call .removeFromParent()

				if (object instanceof Model) {
					cbgs.removeModel((Model) object);
					return true;
				}

				return false;
			}
		});
		JGN.createThread(client, clientSyncManager).start();
		client.connectAndWait(serverReliable, serverFast, 5000);

		// Register our client object with the synchronization manager
		// / clientSyncManager.register( new SynchronizeCreateMessage(), 50);
	}

}
