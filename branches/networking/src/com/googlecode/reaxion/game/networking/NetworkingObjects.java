package com.googlecode.reaxion.game.networking;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.Callable;

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
import com.googlecode.reaxion.game.input.ClientPlayerInput;
import com.googlecode.reaxion.game.model.Model;
import com.googlecode.reaxion.game.networking.sync.message.CharacterAndStageSelectionsMessage;
import com.googlecode.reaxion.game.networking.sync.message.SynchronizeCreateModelMessage;
import com.googlecode.reaxion.game.networking.sync.message.SynchronizeModelMessage;
import com.googlecode.reaxion.game.state.ClientBattleGameState;
import com.googlecode.reaxion.game.state.ServerBattleGameState;
import com.googlecode.reaxion.game.state.SynchronizeCreatePlayerInputMessage;
import com.googlecode.reaxion.game.util.Battle;
import com.googlecode.reaxion.game.util.LoadingQueue;
import com.jme.math.Vector3f;
import com.jme.util.GameTaskQueueManager;
import com.jmex.game.state.GameStateManager;

public abstract class NetworkingObjects {

	public static enum PlayerNum{P1, P2};
	
	public static JGNServer server;
	public static JGNClient client;

	/**
	 * Probably don't need to have separate ones for both, since it'll never be
	 * both a server and a client. Too lazy to condense, also it'd be confusing.
	 */
	public static SynchronizationManager serverSyncManager, clientSyncManager;

	public static JMEGraphicalController controller;

	public static ClientBattleGameState cbgs;
	private static ServerBattleGameState sbgs;

	private static InetSocketAddress serverReliable, serverFast;
	public static boolean isServer;
	public static final long updateRate = 50;

	private static int creationMessagesRecieved = 0;
	private static short p1ID = -2, p2ID = -2;

	public static ClientPlayerInput p1input, p2input;

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

		controller = new JMEGraphicalController();

		// do weird things w/ same computer
		serverReliable = new InetSocketAddress(InetAddress.getLocalHost(), 9001);
		// serverFast = new InetSocketAddress(InetAddress.getLocalHost(), 9002);
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

			private int doneRecieved = 0;

			public void messageReceived(Message message) {
				if (message instanceof NamedChatMessage) {
					if (((NamedChatMessage) message).getText().equals("done")) {
						doneRecieved++;
						if (doneRecieved == 2) {
							System.out.println("Making a battle");

							sbgs = (ServerBattleGameState) (Battle.createNetworkedBattleGameState());

							// I'm not sure this is how you do it
							GameTaskQueueManager.getManager().update(new Callable() {
								public Object call() throws Exception {
									GameStateManager.getInstance().attachChild(sbgs);
									try {
										System.out.println("setactive pre");
										sbgs.setActive(true);
										System.out.println("setactive post");
									} catch (NullPointerException e) {
										e.printStackTrace();
										System.out
												.println("Server doesn't like it when it's invisible and we setActive a gamestate.");
									}
									return null;
								}
							});
						}
					}
					return;
				} else if (message instanceof CharacterAndStageSelectionsMessage) {
					CharacterAndStageSelectionsMessage cassm = (CharacterAndStageSelectionsMessage) message;
					// TODO handle creation properly

					if (creationMessagesRecieved == 0) {
						System.out.println(cassm.getPlayerId());
						p1ID = cassm.getPlayerId();
						System.out.print("1 message recieved! ");
						chars[0] = cassm.getCharacters()[0];
						chars[1] = cassm.getCharacters()[1];
						stage1 = cassm.getStage();
						creationMessagesRecieved++;
						System.out.println("Characters: " + Arrays.toString(cassm.getCharacters())
								+ " Stage: " + stage1);
					} else if (creationMessagesRecieved == 1) {
						p2ID = cassm.getPlayerId();
						System.out.println(cassm.getPlayerId());
						System.out.print("2 message recieved! ");
						chars[2] = cassm.getCharacters()[0];
						chars[3] = cassm.getCharacters()[1];
						stage2 = cassm.getStage();
						System.out.println("Characters: " + Arrays.toString(cassm.getCharacters())
								+ " Stage: " + stage2);
						stageChoice = Math.random() > .5 ? stage1 : stage2;
						System.out.println("Stage choice: " + stageChoice);
						Battle c = Battle.getCurrentBattle();

						c.setPlayers(chars);
						c.setStage(stageChoice);
						c.setPlayerPosition(new Vector3f(0, 0, 0));
						Battle.setCurrentBattle(c);

						// for the lulz, since nothing happens
						server.sendToAll(new CharacterAndStageSelectionsMessage(chars, stageChoice));

						// Creation of objects when the server makes objects
						// will make clients load the right things. it's not the
						// most elegant way, but that's okay.

					}

					// FIXME cause this to send info to clients when all choices
					// have been recieved, and make clients create it, and make
					// sure the server will update the game without displaying
					// it, and fix up all the bs you did to other things
				}
			}

			public void messageSent(Message message) {
			}
		});

		serverSyncManager.addSyncObjectManager(new SyncObjectManager() {
			public boolean remove(SynchronizeRemoveMessage srm, Object obj) {
				return false;
			}

			public Object create(SynchronizeCreateMessage scm) {
				if (scm instanceof SynchronizeCreatePlayerInputMessage) {
					SynchronizeCreatePlayerInputMessage scpim = (SynchronizeCreatePlayerInputMessage) scm;
					System.out.println(scpim.getPlayerId());
					// These shouldn't ever really get used besides for grabbing data from the client
					if (scpim.getPlayerId() == p1ID) {
						p1input = new ClientPlayerInput(sbgs);
						return p1input;
					} else if (scpim.getPlayerId() == p2ID) {
						p2input = new ClientPlayerInput(sbgs);
						return p2input;
					}
				}
				return null;
			}
		});

		JGN.createThread(server, serverSyncManager).start();
	}

	public static void setUpClient(boolean local) throws IOException, InterruptedException {
		isServer = false;

		// Instantiate an instance of a JMEGraphicalController
		controller = new JMEGraphicalController();

		if (local) {
			// server IPs
			serverReliable = new InetSocketAddress(InetAddress.getLocalHost(), 9001);
			serverFast = new InetSocketAddress(InetAddress.getLocalHost(), 9002);
		} else {
			String addr = JOptionPane.showInputDialog("Enter server IPv4 address:");
			serverReliable = new InetSocketAddress(addr, 9001);
			serverFast = new InetSocketAddress(addr, 9002);
		}
		serverFast = null;

		// Start the client
		client = new JGNClient(new InetSocketAddress(InetAddress.getLocalHost(), 9003), null);
		client.addMessageListener(new MessageListener() {
			public void messageCertified(Message message) {
			}

			public void messageFailed(Message message) {
			}

			public void messageReceived(Message message) {
				// System.out.println("I'm in client's listener, and isServer = "
				// + isServer);
				if (message instanceof CharacterAndStageSelectionsMessage) {
					CharacterAndStageSelectionsMessage cassm = (CharacterAndStageSelectionsMessage) message;

					System.out.println("Stage recieved!");

					Battle c = Battle.getCurrentBattle();
					c.setStage(cassm.getStage());
					// so it doesn't load anything, resulting in models just floating around
					c.setPlayers(new String[]{null,null,null,null});
					Battle.setDefaultPlayers(null, null);
					Battle.setCurrentBattle(c);

					GameTaskQueueManager.getManager().update(new Callable() {
						public Object call() throws Exception {
							ClientBattleGameState nbgs = (ClientBattleGameState) Battle
									.createNetworkedBattleGameState();

							GameStateManager.getInstance().attachChild(nbgs);
							nbgs.setActive(true);
							// FIXME: music turned off

							cbgs = nbgs;

							NamedChatMessage qrr = new NamedChatMessage();
							qrr.setText("done");
							client.sendToServer(qrr);

							return null;
						}
					});
				} else if (message instanceof SynchronizeCreateModelMessage) {
					SynchronizeCreateModelMessage scmm = (SynchronizeCreateModelMessage) message;

					System.err.println("Creating " + scmm.getSyncObjectId() + ", "
							+ (scmm.isForPreload() ? "Preloading" : "normal loading"));
					// assuming always for preload because I am a bad person
					LoadingQueue.push(new Model(scmm.getFilename()));
					LoadingQueue.execute(null); // this is rather hackish.
					// consider fixing the
					// preloading system to not be
					// so ugly.
				}
			}

			public void messageSent(Message message) {
			}
		});
		clientSyncManager = new SynchronizationManager(client, controller);
		clientSyncManager.addSyncObjectManager(new SyncObjectManager() {
			// TODO: Consider replacing these Callables with a check in
			// stateUpdate for any new models to preload/load/remove.
			private Queue<Model> forRemove = new LinkedList<Model>();
			private Queue<String> forPreload = new LinkedList<String>(),
					forLoad = new LinkedList<String>();

			public Object create(SynchronizeCreateMessage scm) {
				if (scm instanceof SynchronizeCreateModelMessage) {
					SynchronizeCreateModelMessage scmm = (SynchronizeCreateModelMessage) scm;

					System.err.println("Creating " + scm.getSyncObjectId() + ", "
							+ (scmm.isForPreload() ? "Preloading" : "normal loading"));

					if (!scmm.isForPreload())
						return LoadingQueue.quickLoad(new Model(scmm.getFilename()), cbgs);
				} else if (scm instanceof SynchronizeCreatePlayerInputMessage) {
					return new ClientPlayerInput(cbgs);
				}
				return null;
			}

			public boolean remove(SynchronizeRemoveMessage srm, Object object) {
				if (object instanceof Model) {
					forRemove.add((Model) object);

					GameTaskQueueManager.getManager().update(new Callable() {
						public Object call() throws Exception {

							while (!forRemove.isEmpty()) {
								cbgs.removeModel(forRemove.remove());
							}

							return null;
						}
					});

					return true;
				} else
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

		System.out.println(client.getServerConnection().getReliableClient().getStatus());
		// System.out.println(client.getServerConnection().getFastClient().getStatus());

		// Register our client object with the synchronization manager
		// / clientSyncManager.register( new SynchronizeCreateMessage(), 50);
	}
}