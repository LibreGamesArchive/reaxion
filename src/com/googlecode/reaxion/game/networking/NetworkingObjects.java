package com.googlecode.reaxion.game.networking;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;

import javax.swing.JOptionPane;

import com.captiveimagination.jgn.JGN;
import com.captiveimagination.jgn.clientserver.JGNClient;
import com.captiveimagination.jgn.clientserver.JGNConnection;
import com.captiveimagination.jgn.clientserver.JGNConnectionListener;
import com.captiveimagination.jgn.clientserver.JGNServer;
import com.captiveimagination.jgn.synchronization.SyncObjectManager;
import com.captiveimagination.jgn.synchronization.SynchronizationManager;
import com.captiveimagination.jgn.synchronization.message.SynchronizeCreateMessage;
import com.captiveimagination.jgn.synchronization.message.SynchronizeRemoveMessage;
import com.googlecode.reaxion.game.mission.Mission;
import com.googlecode.reaxion.game.model.Model;
import com.googlecode.reaxion.game.networking.sync.message.SynchronizeCreateModelMessage;
import com.googlecode.reaxion.game.networking.sync.message.SynchronizeModelMessage;
import com.googlecode.reaxion.game.state.ClientBattleGameState;
import com.googlecode.reaxion.game.state.ServerBattleGameState;
import com.googlecode.reaxion.game.util.LoadingQueue;

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

	public static void setUpServer() throws IOException {
		isServer = true;
		setUpSharedThings(true);

		// FIXME (nwk) cp'd from online example, needs to be fix'd for this

		server = new JGNServer(serverReliable, serverFast);
		serverSyncManager = new SynchronizationManager(server, controller);

		JGN.createThread(server, serverSyncManager).start();
		server.addClientConnectionListener(new JGNConnectionListener() {

			@Override
			public void disconnected(JGNConnection connection) {
				// TODO Auto-generated method stub

			}

			@Override
			public void connected(JGNConnection connection) {
				System.out.println("Connectiked");
			}
		});

		// Register our server object with the synchronization manager
		// serverSyncManager.register(game.getServerPanel(), new
		// SynchronizeCreateMessage(), 50);

	}

	private static void setUpSharedThings(boolean serv)
			throws UnknownHostException {
		JGN.register(SynchronizeModelMessage.class);

		// Instantiate an instance of a JMEGraphicalController
		controller = new JMEGraphicalController();

		if (serv) {
			// server IPs
			serverReliable = new InetSocketAddress(InetAddress.getLocalHost(),
					9001);
			serverFast = new InetSocketAddress(InetAddress.getLocalHost(), 9002);
		} else {
			String addr = JOptionPane
					.showInputDialog("Enter server IPv4 address:");
			serverReliable = new InetSocketAddress(addr,
					9001);
			serverFast = new InetSocketAddress(addr, 9002);
		}
	}

	public static void setUpClient() throws IOException, InterruptedException {
		isServer = false;
		setUpSharedThings(false);

		// Start the client
		client = new JGNClient(new InetSocketAddress(
				InetAddress.getLocalHost(), 9001), new InetSocketAddress(
				InetAddress.getLocalHost(), 9002));
		clientSyncManager = new SynchronizationManager(client, controller);
		clientSyncManager.addSyncObjectManager(new SyncObjectManager() {
			public Object create(SynchronizeCreateMessage scm) {
				// TODO (nwk) make it make an object
				// TODO (nwk) figure out how to make this work. the client
				// needs to add a thing

				if (scm instanceof SynchronizeCreateModelMessage) {
					SynchronizeCreateModelMessage scmm = (SynchronizeCreateModelMessage) scm;
					if(scmm.isForPreload())
						return LoadingQueue.push(new Model(scmm.getFilename()));
					return LoadingQueue.quickLoad(new Model(scmm.getFilename()), cbgs);
				}
				return null;
			}

			public boolean remove(SynchronizeRemoveMessage srm, Object object) {
				// FIXME (nwk) figure out how to reference list of models
				// and call .removeFromParent()
				
				if(object instanceof Model) {
					cbgs.removeModel((Model)object);
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
