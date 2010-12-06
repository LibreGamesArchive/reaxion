package com.googlecode.reaxion.game.networking.sync.message;

import com.captiveimagination.jgn.message.Message;
import com.captiveimagination.jgn.message.type.PlayerMessage;

public class CharacterAndStageSelectionsMessage extends Message implements PlayerMessage {

	private String stage;
	private String[] characters;

	public CharacterAndStageSelectionsMessage() {
	}
	
	/**
	 * 
	 * @param characters 
	 * @param stage
	 */
	public CharacterAndStageSelectionsMessage(String[] characters, String stage) {
		this.characters = new String[characters.length];
		for(int i = 0; i < characters.length; i ++)
			this.characters[i] = characters[i];
		
		this.stage = stage;
	}
	
	public String getStage() {
		return stage;
	}

	public void setStage(String stage) {
		this.stage = stage;
	}

	public String[] getCharacters() {
		return characters;
	}

	public void setCharacters(String[] characters) {
		this.characters = new String[characters.length];
		for(int i = 0; i < characters.length; i ++)
			this.characters[i] = characters[i];
	}
}
