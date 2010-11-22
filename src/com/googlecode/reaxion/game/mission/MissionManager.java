package com.googlecode.reaxion.game.mission;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import com.googlecode.reaxion.game.Reaxion;
import com.googlecode.reaxion.game.mission.missions.Mission00;
import com.googlecode.reaxion.game.mission.missions.MissionHGS;
import com.googlecode.reaxion.game.mission.missions.VsToybox;
import com.jmex.game.state.GameStateManager;

/**
 * Allows iteration through the {@code GameState} objects in {@code Mission} objects. 
 * Used for conduction missions in story mode.
 * 
 * @author Brian Clanton
 *
 */

public class MissionManager {
	
	private static HashMap<MissionID, Mission> missions = new HashMap<MissionID, Mission>();
	private static Mission currentMission = null;
	private static int currentIndex;
	
	/**
	 * Fills the missions list. Should be called at startup.
	 */
	public static void createMissions() {
		missions.put(MissionID.DEFEAT_LIGHT_USER, new Mission00());
		missions.put(MissionID.OPEN_HUBGAMESTATE, new MissionHGS());
		missions.put(MissionID.VS_TOYBOX, new VsToybox());
	}
	
	/**
	 * Starts a mission by ID number.
	 * 
	 * @param missionID
	 */
	public static void startMission(MissionID missionID) {
		currentMission = missions.get(missionID);
		currentIndex = 0;
		
		currentMission.init();
		
		GameStateManager.getInstance().attachChild(currentMission.getStateAt(currentIndex));
		currentMission.activateStateAt(currentIndex);
	}
	
	/**
	 * Progresses to next {@code GameState} in {@code currentMission}.
	 */
	public static void startNext() {
		if(currentIndex + 1 == currentMission.getStateCount()) {
			endMission();
			Reaxion.terminate();
//			GameStateManager.getInstance().getChild(HubGameState.NAME).setActive(true);
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
	public static void endMission() {
		for(int i = 0; i <= currentIndex; i++)
			GameStateManager.getInstance().detachChild(currentMission.getStateAt(i));
		
		currentMission = null;
		currentIndex = 0;
	}
	
	/**
	 * Checks to see if a mission is currently in progress.
	 * 
	 * @return {@code true} if mission progress, {@code false} if no mission in progress
	 */
	public static boolean hasCurrentMission() {
		return currentMission != null;
	}

	/**
	 * Returns an {@code ArrayList} of type {@code Mission} containing all of the non-test missions in 
	 * {@code MissionManager}.
	 * 
	 * @return missions
	 */
	public static ArrayList<Mission> getMissions() {
		ArrayList<Mission> temp = new ArrayList<Mission>();
		temp.addAll(missions.values());
		
		ArrayList<Mission> testMissions = new ArrayList<Mission>();
		
		for (int i = 0; i < temp.size(); i++)
			if (temp.get(i).getMissionIDNum() <= 0)
				testMissions.add(temp.get(i));
		
		for (Mission m : testMissions)
			temp.remove(m);
		
		Collections.sort(temp);
		
		return temp;
	}
	
}
