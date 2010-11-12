package com.googlecode.reaxion.game;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;

import com.captiveimagination.jgn.JGN;
import com.captiveimagination.jgn.clientserver.JGNClient;
import com.captiveimagination.jgn.clientserver.JGNServer;
import com.captiveimagination.jgn.synchronization.SyncObjectManager;
import com.captiveimagination.jgn.synchronization.SynchronizationManager;
import com.captiveimagination.jgn.synchronization.message.SynchronizeCreateMessage;
import com.captiveimagination.jgn.synchronization.message.SynchronizeRemoveMessage;
import com.googlecode.reaxion.game.networking.JMEGraphicalController;
import com.googlecode.reaxion.game.networking.sync.message.SynchronizeModelMessage;
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

	private static InetSocketAddress serverReliable, serverFast;
	public static boolean isServer;
	public static final long updateRate = 50;

	// TODO (nwk) Make a StageCreateMessage that tells the client which
	// stage was picked (pass an Enum or something) and then have client do
	// that locally.

	// FIXME (nwk) do different behaviors depending on client / server

	public static void setUpServer() throws IOException {
		isServer = true;
		setUpSharedThings();

		// FIXME (nwk) cp'd from online example, needs to be fix'd for this

		server = new JGNServer(serverReliable, serverFast);
		serverSyncManager = new SynchronizationManager(server, controller);

		JGN.createThread(server, serverSyncManager).start();

		// Register our server object with the synchronization manager
		// serverSyncManager.register(game.getServerPanel(), new
		// SynchronizeCreateMessage(), 50);

	}

	private static void setUpSharedThings() throws UnknownHostException {
		JGN.register(SynchronizeModelMessage.class);

		// Instantiate an instance of a JMEGraphicalController
		controller = new JMEGraphicalController();

		// Start the server
		serverReliable = new InetSocketAddress("10.72.11.2", 9001);
		serverFast = new InetSocketAddress("10.72.11.2", 9002);
	}

	public static void setUpClient() throws IOException, InterruptedException {
		isServer = false;

		// Start the client
		client = new JGNClient(serverReliable, serverFast);
		clientSyncManager = new SynchronizationManager(client, controller);
		clientSyncManager.addSyncObjectManager(new SyncObjectManager() {
			public Object create(SynchronizeCreateMessage scm) {
				// TODO (nwk) make it make an object
				// TODO (nwk) figure out how to make this work. the client
				// needs to add a thing

				if (scm instanceof SynchronizeCreateModelMessage) {
					SynchronizeCreateModelMessage scmm = (SynchronizeCreateModelMessage) scm;
					LoadingQueue.quickLoad(scmm.model, null);
					
					return scmm.model;
				}
				
				return null;
			}

			public boolean remove(SynchronizeRemoveMessage srm, Object object) {
				// TODO (nwk) figure out how to reference list of models
				// and call .removeFromParent()
				
				
				
				return true;
			}
		});
		JGN.createThread(client, clientSyncManager).start();
		client.connectAndWait(serverReliable, serverFast, 5000);

		// Register our client object with the synchronization manager
		// / clientSyncManager.register( new SynchronizeCreateMessage(), 50);
	}

}
