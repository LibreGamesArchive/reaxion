package com.googlecode.reaxion.game.mission;

import java.awt.Point;
import java.util.ArrayList;

import com.googlecode.reaxion.game.Reaxion;
import com.googlecode.reaxion.game.state.DialogueGameState;
import com.googlecode.reaxion.game.util.Actor;
import com.googlecode.reaxion.game.util.Battle;
import com.jmex.game.state.GameStateManager;

/**
 * Allows iteration through the {@code GameState} objects in {@code Mission} objects. Used for conduction missions
 * in story mode.
 * 
 * @author Brian
 *
 */

public class MissionManager {
	
	private static ArrayList<Mission> missions = new ArrayList<Mission>();
	private static Mission currentMission = null;
	private static int currentIndex;
	
	/**
	 * Starts a mission by ID number.
	 * 
	 * @param missionNumber
	 */
	public static void startMission(int missionNumber) {
		currentMission = missions.get(missionNumber);
		currentIndex = 0;
		
		GameStateManager.getInstance().attachChild(currentMission.getStateAt(currentIndex));
		currentMission.activateStateAt(currentIndex);
	}
	
	/**
	 * Progresses to next {@code GameState} in {@code currentMission}.
	 */
	public static void startNext() {
		if(currentIndex + 1 == currentMission.getStateCount()) {
			endMission();
		} else {
			currentMission.deactivateStateAt(currentIndex);
			currentIndex++;
			GameStateManager.getInstance().attachChild(currentMission.getStateAt(currentIndex));
			currentMission.activateStateAt(currentIndex);
		}
	}
	
	/**
	 * Ends mission in progress and returns control to {@code HubGameState}.
	 */
	private static void endMission() {
		for(int i = 0; i <= currentIndex; i++)
			GameStateManager.getInstance().detachChild(currentMission.getStateAt(i));
		
		currentMission = null;
		currentIndex = 0;
		
		Reaxion.terminate();
//		GameStateManager.getInstance().getChild(HubGameState.NAME).setActive(true);
	}
	
	/**
	 * Triggers the intialization of all misisons and their addition to {@code missions}.
	 */
	public static void initMissions() {
		Mission temp = new Mission();
		
		// dialogue code
		int[] times = new int[24];
		for(int i = 0; i < times.length; i++)
			times[i] = i * 20;
		
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
		temp.addState(dialogueState);
		
		Battle b = new Battle();
		b.setPlayers(new String[] {"Brian", "Cy", "Khoa"});
		b.setStage("TwilightKingdom");
		Battle.setCurrentBattle(b);
		
		temp.addState(Battle.createBattleGameState());
		
		missions.add(temp);
	}
	
	/**
	 * Checks to see if a mission is currently in progress.
	 * 
	 * @return {@code true} if mission progress, {@code false} if no mission in progress
	 */
	public static boolean hasCurrentMission() {
		return currentMission != null;
	}
	
}
