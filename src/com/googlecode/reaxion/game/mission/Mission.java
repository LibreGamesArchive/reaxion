package com.googlecode.reaxion.game.mission;

import java.util.ArrayList;

import com.jmex.game.state.GameState;

public class Mission {
	
	private String title;
	private int missionID;
	private int difficultyRating;
	private boolean required;
	private String imageURL;
	
	private ArrayList<GameState> states;
	
	private int stateCount;
	
	public Mission() {
		title = "???";
		missionID = 0;
		difficultyRating = 0;
		required = false;
		states = new ArrayList<GameState> ();
		stateCount = 0;
	}
	
	public Mission(String title, int missionID, int difficultyRating, boolean required, String imageURL, ArrayList<GameState> states) {
		this.title = title;
		this.missionID = missionID;
		this.difficultyRating = difficultyRating;
		this.required = required;
		this.imageURL = imageURL;
		this.states = states;
		stateCount = states.size();
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public int getMissionID() {
		return missionID;
	}

	public void setMissionID(int missionID) {
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
	
	public void addState(GameState g) {
		states.add(g);
		stateCount++;
	}
	
	public GameState getStateAt(int index) {
		return states.get(index);
	}
	
	public void activateStateAt(int index) {
		states.get(index).setActive(true);
	}

	public int getStateCount() {
		return stateCount;
	}
}
