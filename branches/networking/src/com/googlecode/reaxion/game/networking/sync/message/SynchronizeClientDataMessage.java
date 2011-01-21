package com.googlecode.reaxion.game.networking.sync.message;

import com.captiveimagination.jgn.message.type.PlayerMessage;
import com.captiveimagination.jgn.synchronization.message.SynchronizeMessage;
import com.googlecode.reaxion.game.attack.Attack;
import com.googlecode.reaxion.game.networking.HudInfoContainer;

public class SynchronizeClientDataMessage extends SynchronizeMessage implements PlayerMessage {

	private Boolean forthOn, leftOn, jumpOn, attackHold, attack1, attack2, attack3, tagOut;
	private float facingX, facingZ;
	private HudInfoContainer target, player, partner;
	private double minGauge, gauge, maxGauge;
	private Class[] playerAttacks;
	private Attack currentAttack;
	
	public SynchronizeClientDataMessage() {
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

	/**
	 * @return the tagOut
	 */
	public Boolean getTagOut() {
		return tagOut;
	}

	/**
	 * @param tagOut the tagOut to set
	 */
	public void setTagOut(Boolean tagOut) {
		this.tagOut = tagOut;
	}

	public HudInfoContainer getTarget() {
		return target;
	}

	public HudInfoContainer getPlayer() {
		return player;
	}

	public HudInfoContainer getPartner() {
		return partner;
	}

	public void setTarget(HudInfoContainer target) {
		this.target = target;
	}

	public void setPlayer(HudInfoContainer player) {
		this.player = player;
	}

	public void setPartner(HudInfoContainer partner) {
		this.partner = partner;
	}

	public double getGauge() {
		return gauge;
	}

	public double getGaugecap() {
		return maxGauge;
	}

	public Class[] getPlayerAttacks() {
		return playerAttacks;
	}

	public void setGauge(double gauge) {
		this.gauge = gauge;
	}

	public void setGaugecap(double gaugecap) {
		this.maxGauge = gaugecap;
	}

	public void setPlayerAttacks(Class[] playerAttacks) {
		this.playerAttacks = playerAttacks;
	}

	public Attack getCurrentAttack() {
		return currentAttack;
	}

	public void setCurrentAttack(Attack currentAttack) {
		this.currentAttack = currentAttack;
	}

	public double getMinGauge() {
		return minGauge;
	}

	public void setMinGauge(double minGauge) {
		this.minGauge = minGauge;
	}
}
