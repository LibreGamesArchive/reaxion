package com.googlecode.reaxion.game.networking.sync.message;

import com.captiveimagination.jgn.message.type.PlayerMessage;
import com.captiveimagination.jgn.synchronization.message.SynchronizeMessage;

public class SynchronizePlayerInputMessage extends SynchronizeMessage implements PlayerMessage {

	private Boolean forthOn, leftOn, jumpOn, attackHold, attack1, attack2, attack3;
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

	public Boolean getAttackHold() {
		return attackHold;
	}

	public void setAttackHold(Boolean attackHold) {
		this.attackHold = attackHold;
	}

	public Boolean getAttack1() {
		return attack1;
	}

	public void setAttack1(Boolean attack1) {
		this.attack1 = attack1;
	}

	public Boolean getAttack2() {
		return attack2;
	}

	public void setAttack2(Boolean attack2) {
		this.attack2 = attack2;
	}

	public Boolean getAttack3() {
		return attack3;
	}

	public void setAttack3(Boolean attack3) {
		this.attack3 = attack3;
	}

}
