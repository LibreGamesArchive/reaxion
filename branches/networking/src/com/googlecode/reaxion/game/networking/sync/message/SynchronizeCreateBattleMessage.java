package com.googlecode.reaxion.game.networking.sync.message;

import com.captiveimagination.jgn.synchronization.message.SynchronizeCreateMessage;

public class SynchronizeCreateBattleMessage extends SynchronizeCreateMessage {

	private String stage;
	private String[] characters;

	public SynchronizeCreateBattleMessage() {
		
	}
	
	public SynchronizeCreateBattleMessage(String[] characters, String stage) {
		this.characters = new String[]{characters[0],characters[1]};
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
		this.characters = new String[]{characters[0],characters[1]};
	}
}
