package com.googlecode.reaxion.game.networking.sync.message;

import com.captiveimagination.jgn.synchronization.message.SynchronizeCreateMessage;
import com.googlecode.reaxion.game.model.Model;

public class SynchronizeCreateModelMessage extends SynchronizeCreateMessage {
	
	// FIXME CANNOT SERIALIZE WTFAIL

	String filename;
	
	public SynchronizeCreateModelMessage() {
		super();
	}

	public SynchronizeCreateModelMessage(Model model) {
		super();
		this.filename = model.filename;
	}

	public String getFilename() {
		return filename;
	}

	public void setFilename(String model) {
		this.filename = model;
	}
}
