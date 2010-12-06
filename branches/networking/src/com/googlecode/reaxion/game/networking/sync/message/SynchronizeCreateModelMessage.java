package com.googlecode.reaxion.game.networking.sync.message;

import com.captiveimagination.jgn.synchronization.message.SynchronizeCreateMessage;
import com.googlecode.reaxion.game.model.Model;

public class SynchronizeCreateModelMessage extends SynchronizeCreateMessage {

	String filename;
	boolean forPreload;

	public SynchronizeCreateModelMessage() {
		super();
	}

	public SynchronizeCreateModelMessage(Model model, boolean forPreload) {
		super();
		this.filename = model.filename;
		this.forPreload = forPreload;
	}

	public boolean isForPreload() {
		return forPreload;
	}

	public void setForPreload(boolean forPreload) {
		this.forPreload = forPreload;
	}

	public String getFilename() {
		return filename;
	}

	public void setFilename(String model) {
		this.filename = model;
	}
}
