package com.googlecode.reaxion.game.networking.sync.message;

import com.captiveimagination.jgn.synchronization.message.Synchronize3DMessage;

public class SynchronizeModelMessage extends Synchronize3DMessage {
	private float scaleX, scaleY, scaleZ;
	
	private String animation;

	public float getScaleX() {
		return scaleX;
	}

	public void setScaleX(float scaleX) {
		this.scaleX = scaleX;
	}

	public float getScaleY() {
		return scaleY;
	}

	public void setScaleY(float scaleY) {
		this.scaleY = scaleY;
	}

	public float getScaleZ() {
		return scaleZ;
	}

	public void setScaleZ(float scaleZ) {
		this.scaleZ = scaleZ;
	}

	public String getAnimation() {
		return animation;
	}

	public void setAnimation(String animation) {
		this.animation = animation;
	}
}
