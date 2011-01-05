package com.googlecode.reaxion.game.networking;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.Arrays;

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
import com.captiveimagination.jgn.test.chat.NamedChatMessage;
import com.googlecode.reaxion.game.model.Model;
import com.googlecode.reaxion.game.networking.sync.message.CharacterAndStageSelectionsMessage;
import com.googlecode.reaxion.game.networking.sync.message.SynchronizeCreateModelMessage;
import com.googlecode.reaxion.game.networking.sync.message.SynchronizeModelMessage;
import com.googlecode.reaxion.game.state.BattleGameState;
import com.googlecode.reaxion.game.state.ClientBattleGameState;
import com.googlecode.reaxion.game.state.ServerBattleGameState;
import com.googlecode.reaxion.game.util.Battle;
import com.googlecode.reaxion.game.util.LoadingQueue;
import com.jme.util.GameTaskQueueManager;
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

	private static int creationMessagesRecieved = 0;

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

		System.out.println("RELIABLE SERVER:" + serverReliable);

		// do weird things w/ same computer
		serverReliable = new InetSocketAddress(InetAddress.getLocalHost(), 9001);
	//	serverFast = new InetSocketAddress(InetAddress.getLocalHost(), 9002);
		serverFast = null;

		server = new JGNServer(serverReliable, serverFast);
		serverSyncManager = new SynchronizationManager(server, controller);

		server.addMessageListener(new MessageListener() {
			private String[] chars = new String[4];
			private String stage1, stage2, stageChoice;

			public void messageCertified(Message message) {
			}

			public void messageFailed(Message message) {
			}

			public void messageReceived(Message message) {
				if(message instanceof NamedChatMessage) {
					System.out.println("Client sent me a message successfully =/");
					return;
				}
			//	System.out.println("Server thread: " + isServer);

				if (message instanceof CharacterAndStageSelectionsMessage) {
					CharacterAndStageSelectionsMessage cassm = (CharacterAndStageSelectionsMessage) message;
					// TODO handle creation properly

					if (creationMessagesRecieved == 0) {
						System.out.print("1 message recieved! ");
						chars[0] = cassm.getCharacters()[0];
						chars[1] = cassm.getCharacters()[1];
						stage1 = cassm.getStage();
						creationMessagesRecieved++;
						System.out.println("Characters: "+Arrays.toString(cassm.getCharacters())+" Stage: "+stage1);
					} else if (creationMessagesRecieved == 1) {
						System.out.print("2 message recieved! ");
						chars[2] = cassm.getCharacters()[0];
						chars[3] = cassm.getCharacters()[1];
						stage2 = cassm.getStage();
						System.out.println("Characters: "+Arrays.toString(cassm.getCharacters())+" Stage: "+stage2);
						stageChoice = Math.random() > .5 ? stage1 : stage2;
						System.out.println("Stage choice: " + stageChoice);
						Battle c = Battle.getCurrentBattle();
						
						c.setPlayers(chars);
						c.setStage(stageChoice);
						Battle.setCurrentBattle(c);

						// for the lulz, since nothing happens
						server
								.sendToAll(new CharacterAndStageSelectionsMessage(
										chars, stageChoice));

						System.out.println("Making a battle");

						ServerBattleGameState sbgs = (ServerBattleGameState) (Battle
								.createNetworkedBattleGameState());

						// I'm not sure this is how you do it
			//			GameStateManager.getInstance().attachChild(sbgs);
						try {
							System.out.println("setactive pre");
					//		sbgs.setActive(true);
							System.out.println("setactive post");
						} catch (NullPointerException e) {
							e.printStackTrace();
							System.out
									.println("Server doesn't like it when it's invisible and we setActive a gamestate.");
						}
						// Creation of objects when the server makes objects
						// will make clients load the right things. it's not the
						// most elegant way, but that's okay.

					}

					// FIXME cause this to send info to clients when all choices
					// have been recieved, and make clients create it, and make
					// sure the server will update the game without displaying
					// it, and fix up all the bs you did to othe things
				}
			}

			public void messageSent(Message message) {
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
		serverFast = null;

		// Start the client
		client = new JGNClient(new InetSocketAddress(
				InetAddress.getLocalHost(), 9003), null);
		client.addMessageListener(new MessageListener() {
			public void messageCertified(Message message) {
			}

			public void messageFailed(Message message) {
			}

			public void messageReceived(Message message) {
		//		System.out.println("I'm in client's listener, and isServer = "
			//			+ isServer);
				if (message instanceof CharacterAndStageSelectionsMessage) {
					CharacterAndStageSelectionsMessage cassm = (CharacterAndStageSelectionsMessage) message;
					
					System.out.println("Stage recieved!");
					
					Battle c = Battle.getCurrentBattle();
					c.setStage(cassm.getStage());
					
					ClientBattleGameState nbgs = (ClientBattleGameState)Battle.createNetworkedBattleGameState();
					
					GameStateManager.getInstance().attachChild(nbgs);
					nbgs.setActive(true);
					
					cbgs = nbgs;
				}
			}

			public void messageSent(Message message) {
			}
		});
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
		boolean retry = true;
		while (true) {
			try {
				// System.out.print("Trying to connect... ");
				if (retry) {
					retry = false;
					client.connectAndWait(serverReliable, null, 2500);
				}
			} catch (IOException e) {
				client.close();
				System.out.println("Connection failed. Retrying...");
				Thread.sleep(2500);
				retry = true;
				continue;
			}
			// System.out.println("Connection successful.");
			break;
		}
		
		client.sendToServer(new NamedChatMessage());
		System.out.println(client.getServerConnection().getReliableClient().getStatus());
	//	System.out.println(client.getServerConnection().getFastClient().getStatus());
		

		// Register our client object with the synchronization manager
		// / clientSyncManager.register( new SynchronizeCreateMessage(), 50);
	}
}