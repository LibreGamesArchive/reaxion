package com.googlecode.reaxion.game;

import com.captiveimagination.jgn.synchronization.message.SynchronizeCreateMessage;
import com.googlecode.reaxion.game.model.Model;


public class SynchronizeCreateModelMessage extends SynchronizeCreateMessage {

	Model model;
	
	public SynchronizeCreateModelMessage(Model model) {
		super();
		
		this.model = model;
	}
	
}
