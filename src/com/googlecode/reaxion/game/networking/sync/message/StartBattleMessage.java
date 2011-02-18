package com.googlecode.reaxion.game.networking.sync.message;

import com.captiveimagination.jgn.message.Message;

public class StartBattleMessage extends Message {

	private String stage;
	private String[] characters;

	public StartBattleMessage() {

	}

	public StartBattleMessage(String[] characters, String stage) {
		this.characters = new String[] { characters[0], characters[1],
				characters[2], characters[3] };
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
		this.characters = new String[] { characters[0], characters[1],
				characters[2], characters[3] };
	}
}