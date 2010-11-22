package com.googlecode.reaxion.game.mission;

import java.util.ArrayList;

import com.googlecode.reaxion.game.state.StageGameState;
import com.jmex.game.state.GameState;

/**
 * Represents an in-game mission. All specific missions should extend this class.
 *
 * @author Brian Clanton
 */

public abstract class Mission implements Comparable<Mission> {
	
	private String title;
	private MissionID missionID;
	private int difficultyRating;
	private boolean required;
	private String description;
	private String imageURL;
	
	private ArrayList<GameState> states;
	
	private int stateCount;
	
	public Mission() {
		title = "???";
		difficultyRating = 0;
		required = false;
		description = "";
		states = new ArrayList<GameState> ();
		stateCount = 0;
	}
	
	public Mission(String title, MissionID missionID, int difficultyRating, boolean required, String description, String imageURL) {
		this.title = title;
		this.missionID = missionID;
		this.difficultyRating = difficultyRating;
		this.required = required;
		this.description = description;
		this.imageURL = imageURL;
		states = new ArrayList<GameState> ();
		stateCount = states.size();
	}
	
	public int compareTo(Mission m) {
		return getMissionIDNum() - m.getMissionIDNum();
	}

	/**
	 * Set up all states for this mission. Override to add content.
	 */
	public void init() {
		
	}
	
	public String toString() {
		String s = "Mission No. " + missionID + " : " + title + "\n";
		s += "\t" + description + "\n";
		s += "\tDifficulty: " + difficultyRating + "\n";
		s += "\t" + (required ? "Required" : "Not Required") + "\n";
		s += "\tImage URL: " + imageURL;
		return s;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public int getMissionIDNum() {
		return missionID.id;
	}
	
	public MissionID getMissionID() {
		return missionID;
	}

	public void setMissionID(MissionID missionID) {
		this.missionID = missionID;
	}

	public int getDifficultyRating() {
		return difficultyRating;
	}

	public void setDifficultyRating(int difficultyRating) {
		this.difficultyRating = difficultyRating;
	}

	public boolean isRequired() {
		return required;
	}

	public void setRequired(boolean required) {
		this.required = required;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getImageURL() {
		return imageURL;
	}

	public void setImageURL(String imageURL) {
		this.imageURL = imageURL;
	}

	public void setStates(ArrayList<GameState> states) {
		this.states = states;
		stateCount = states.size();
	}
	
	/**
	 * Adds a state to the mission scenario.
	 * 
	 * @param g
	 */
	public void addState(GameState g) {
		states.add(g);
		stateCount++;
	}
	
	/**
	 * Returns a state at a given index.
	 * 
	 * @param index
	 * @return state at index
	 */
	public GameState getStateAt(int index) {
		return states.get(index);
	}
	
	/**
	 * Sets active a state at a given index.
	 * 
	 * @param index
	 */
	public void activateStateAt(int index) {
		states.get(index).setActive(true);
		if(states.get(index) instanceof StageGameState)
			((StageGameState) states.get(index)).startBGM();
	}
	
	/**
	 * Sets inactive a state at a given index.
	 * 
	 * @param index
	 */
	public void deactivateStateAt(int index) {
		states.get(index).setActive(false);
	}

	public int getStateCount() {
		return stateCount;
	}
}
