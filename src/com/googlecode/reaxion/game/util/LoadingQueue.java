package com.googlecode.reaxion.game.util;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;

import com.googlecode.reaxion.game.model.Model;
import com.googlecode.reaxion.game.model.stage.Stage;
import com.googlecode.reaxion.game.networking.NetworkingObjects;
import com.googlecode.reaxion.game.networking.sync.message.SynchronizeCreateModelMessage;
import com.googlecode.reaxion.game.state.StageGameInterface;
import com.googlecode.reaxion.game.state.StageGameState;
import com.googlecode.reaxion.game.state.CharacterSelectionState;
import com.googlecode.reaxion.game.state.StageSelectionState;
import com.googlecode.reaxion.test.ModelTest;
import com.jme.util.resource.ResourceLocatorTool;
import com.jme.util.resource.SimpleResourceLocator;

public class LoadingQueue {

	private static ArrayList<Model> queue = new ArrayList<Model>();
	private static StageGameInterface state;
	private static CharacterSelectionState Cstate;
	private static StageSelectionState Sstate;
	private static SimpleResourceLocator locator;

	/**
	 * Clears the queue
	 */
	public static void resetQueue() {
		queue = new ArrayList<Model>();
	}

	/**
	 * Convenience method to quickly push model m and execute the stack, model
	 * should still be loaded before this method is called
	 * 
	 * @param m
	 *            Model
	 * @param b
	 *            BattleGameState
	 */
	public static Model quickLoad(Model m, StageGameState b) {
		push(m);
		execute(b);
		return m;
	}

	/**
	 * Add a new model to the queue
	 * 
	 * @param m
	 *            Model
	 * @return
	 */
	public static Model push(Model m) {
		queue.add(m);
		return (m);
	}

	/**
	 * Loads everything in the queue into current {@code BattleGameState} and
	 * then clears the queue
	 * 
	 * @param b
	 *            BattleGameState
	 */
	public static void execute(StageGameState b) {

		// TODO (nwk) make it send stuff to the server.
		if(b != null) 
			state = b;
		System.out.println("Loading queue executed.");
		if (locator == null)
			locateTextures();
		while (!queue.isEmpty()) {
			Model m = queue.get(0);
			ModelLoader.load(m, m.filename);
			if (NetworkingObjects.isServer) {
				// khoa was biscuiting about efficiency here. w/e.
				try {
					NetworkingObjects.serverSyncManager
							.register(m, new SynchronizeCreateModelMessage(m,
									state == null),
									NetworkingObjects.updateRate);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		resetQueue();
	}

	/**
	 * Removes loaded model from queue and adds it to {@code state}, only to be
	 * called by ModelLoader
	 * 
	 * @param m
	 *            Model
	 */
	public static void pop(Model m) {
		int i = queue.indexOf(m);
		if (i != -1) {
			queue.remove(i);
			if (state != null) {
				if (m instanceof Stage)
					state.setStage((Stage) m);
				else
					state.addModel(m);
			} else
				System.out.println("state is null");
			System.out.println("Processed: " + m);
		}
	}

	/**
	 * Points to the location of texture files to be loaded on the system
	 */
	private static void locateTextures() {
		try {
			locator = new SimpleResourceLocator(ModelTest.class
					.getClassLoader().getResource(
							"com/googlecode/reaxion/resources/"));
			ResourceLocatorTool.addResourceLocator(
					ResourceLocatorTool.TYPE_TEXTURE, locator);
			ResourceLocatorTool.addResourceLocator(
					ResourceLocatorTool.TYPE_MODEL, locator);
		} catch (URISyntaxException e1) {
			System.out.println("Unable to setup texture directory.");
		}
	}

}
