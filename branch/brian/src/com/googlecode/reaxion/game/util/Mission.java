package com.googlecode.reaxion.game.util;

import java.util.ArrayList;

import com.jmex.game.state.GameState;

public class Mission {
	
	private String title;
	private int missionID;
	private int difficultyRating;
	private boolean required;
	private String imageURL;
	
	private ArrayList<GameState> states;
	
	public Mission() {
		title = "???";
		missionID = 0;
		difficultyRating = 0;
		required = false;
		states = new ArrayList<GameState> ();
	}
	
	public Mission(String title, int missionID, int difficultyRating, boolean required, String imageURL, ArrayList<GameState> states) {
		this.title = title;
		this.missionID = missionID;
		this.difficultyRating = difficultyRating;
		this.required = required;
		this.imageURL = imageURL;
		this.states = states;
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

	public ArrayList<GameState> getStates() {
		return states;
	}

	public void setStates(ArrayList<GameState> states) {
		this.states = states;
	}
}
