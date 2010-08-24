package com.googlecode.reaxion.game;

import java.util.ArrayList;

public class LoadingQueue {

	private static ArrayList<Model> queue = new ArrayList<Model>();
	private static BattleGameState state;
	
	/**
	 *  Clears the queue
	 */
	public static void resetQueue() {
		queue = new ArrayList<Model>();
	}
	
	/**
	 *  Add a new model to the queue
	 * @param m Model
	 * @return
	 */
	public static Model push(Model m) {
		queue.add(m);
		return (m);
	}
	
	/**
	 *  Loads everything in the queue into current {@code BattleGameState}
	 * @param b BattleGameState
	 */
	public static void execute(BattleGameState b) {
		state = b;
		System.out.println("Loading queue executed.");
		while (!queue.isEmpty()) {
			Model m = queue.get(0);
			ModelLoader.load(m, m.filename);
		}
	}
	
	/**
	 *  Removes loaded model from queue and adds it to {@code state},
	 *  only to be called by ModelLoader
	 * @param m Model
	 */
	public static void pop(Model m) {
		int i = queue.indexOf(m);
		if (i != -1) {
			queue.remove(i);
			state.addModel(m);
			System.out.println("Loaded: "+m);
		}
	}
	
}
