package com.googlecode.reaxion.game.networking.sync.message;

import com.captiveimagination.jgn.message.type.PlayerMessage;
import com.captiveimagination.jgn.synchronization.message.SynchronizeMessage;

public class SynchronizePlayerInputMessage extends SynchronizeMessage implements PlayerMessage {

	private Boolean forthOn, leftOn, jumpOn;
	private float facingX, facingZ;
	
	public SynchronizePlayerInputMessage() {
		super();
	}

	public Boolean getForthOn() {
		return forthOn;
	}

	public void setForthOn(Boolean forthOn) {
		this.forthOn = forthOn;
	}

	public Boolean getLeftOn() {
		return leftOn;
	}

	public void setLeftOn(Boolean leftOn) {
		this.leftOn = leftOn;
	}

	public Boolean getJumpOn() {
		return jumpOn;
	}

	public void setJumpOn(Boolean jumpOn) {
		this.jumpOn = jumpOn;
	}

	public float getFacingX() {
		return facingX;
	}

	public void setFacingX(float facingX) {
		this.facingX = facingX;
	}

	public float getFacingZ() {
		return facingZ;
	}

	public void setFacingZ(float facingZ) {
		this.facingZ = facingZ;
	}

}
