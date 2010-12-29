package com.googlecode.reaxion.game.mission;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import com.googlecode.reaxion.game.mission.missions.*;
import com.googlecode.reaxion.game.state.HubGameState;
import com.googlecode.reaxion.game.util.Battle;
import com.jme.math.Vector3f;
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
	private static HubGameState currentHGS;
	
	/**
	 * Fills the missions list. Should be called at startup.
	 */
	public static void createMissions() {
		missions.put(MissionID.DEFEAT_LIGHT_USER, new Mission00());
		missions.put(MissionID.VS_TOYBOX, new VsToybox());
		missions.put(MissionID.VS_DORIRUZU, new VsDoriruzu());
		missions.put(MissionID.VS_MONICA_1, new VsMonica1());
		missions.put(MissionID.VS_REMNANT, new VsRemnant());
		missions.put(MissionID.VS_SKYTANK, new VsSkytank());
	}
	
	/**
	 * Starts a mission by ID number.
	 * 
	 * @param missionID {@code MissionID} object that denotes which mission to initiate
	 */
	public static void startMission(MissionID missionID) {
		endHubGameState();
		
		currentMission = missions.get(missionID).clone();
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
		} else {
			currentMission.deactivateStateAt(currentIndex);
			GameStateManager.getInstance().detachChild(currentMission.getStateAt(currentIndex));
			currentIndex++;
			GameStateManager.getInstance().attachChild(currentMission.getStateAt(currentIndex));
			currentMission.activateStateAt(currentIndex);
		}
	}
	
	/**
	 * Ends mission in progress and returns control to {@code HubGameState}.
	 */
	public static void endMission() {		
		// Ensures that all states added by the current mission are cleared from the GameStateManager
		for (int i = 0; i < currentMission.getStateCount(); i++)
			if (GameStateManager.getInstance().hasChild(currentMission.getStateAt(i))) {
				currentMission.deactivateStateAt(i);
				GameStateManager.getInstance().detachChild(currentMission.getStateAt(i));
			}
		
		startHubGameState();
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
	 * @return An {@code ArrayList} of all non-test missions
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
	
	public static String getCurrentMissionTitle() {
		String title = null;
		
		if(hasCurrentMission())
			title = currentMission.getTitle();
	
		return title;
	}
	
	/**
	 * Initializes and activates the {@code HubGameState} object of {@code MissionManager}.
	 */
	public static void startHubGameState() {
		Battle temp = Battle.getCurrentBattle();
		temp.setPlayerPosition(new Vector3f(0, 0, 10));
		Battle.setCurrentBattle(temp);
		
		currentHGS = Battle.createHubGameState();
		GameStateManager.getInstance().attachChild(currentHGS);
		currentHGS.setActive(true);
	}
	
	/**
	 * Ends and resets the {@code HubGameState} object of {@code MissionManager}.
	 */
	public static void endHubGameState() {
		currentHGS.setActive(false);
		GameStateManager.getInstance().detachChild(currentHGS);
		currentHGS = null;
	}
	
}
