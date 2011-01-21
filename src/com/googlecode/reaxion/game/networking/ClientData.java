package com.googlecode.reaxion.game.networking;

import java.io.Serializable;

import com.googlecode.reaxion.game.attack.Attack;
import com.googlecode.reaxion.game.attack.AttackDisplayInfo;

public class ClientData {
	private boolean forthOn = false;
	private boolean leftOn = false;
	private boolean jumpOn = false;
	private boolean jumping = false; // jumping is for server only
	private boolean tagOut = false;
	private boolean attackHold = false;
	private boolean attack1 = false;
	private boolean attack2 = false;
	private boolean attack3 = false;
	private float facingX, facingZ; 
	
	private HudInfoContainer target, player, partner;
	private double minGauge, gauge, maxGauge;
	private AttackDisplayInfo[] playerAttacks;
	private String currentAttackName;
	
	public ClientData() {
		target = new HudInfoContainer();
		player = new HudInfoContainer();
		partner = new HudInfoContainer();
	}
	
	public boolean getForthOn() {
		return forthOn;
	}
	public boolean getLeftOn() {
		return leftOn;
	}
	public boolean getJumpOn() {
		return jumpOn;
	}
	public boolean getJumping() {
		return jumping;
	}
	public boolean getTagOut() {
		return tagOut;
	}
	public boolean getAttackHold() {
		return attackHold;
	}
	public boolean getAttack1() {
		return attack1;
	}
	public boolean getAttack2() {
		return attack2;
	}
	public boolean getAttack3() {
		return attack3;
	}
	public float getFacingX() {
		return facingX;
	}
	public float getFacingZ() {
		return facingZ;
	}
	public void setForthOn(boolean forthOn) {
		this.forthOn = forthOn;
	}
	public void setLeftOn(boolean leftOn) {
		this.leftOn = leftOn;
	}
	public void setJumpOn(boolean jumpOn) {
		this.jumpOn = jumpOn;
	}
	public void setJumping(boolean jumping) {
		this.jumping = jumping;
	}
	public void setTagOut(boolean tagOut) {
		this.tagOut = tagOut;
	}
	public void setAttackHold(boolean attackHold) {
		this.attackHold = attackHold;
	}
	public void setAttack1(boolean attack1) {
		this.attack1 = attack1;
	}
	public void setAttack2(boolean attack2) {
		this.attack2 = attack2;
	}
	public void setAttack3(boolean attack3) {
		this.attack3 = attack3;
	}
	public void setFacingX(float facingX) {
		this.facingX = facingX;
	}
	public void setFacingZ(float facingZ) {
		this.facingZ = facingZ;
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

	public AttackDisplayInfo[] getPlayerAttacks() {
		return playerAttacks;
	}

	public void setGauge(double gauge) {
		this.gauge = gauge;
	}

	public void setGaugecap(double gaugecap) {
		this.maxGauge = gaugecap;
	}

	public void setPlayerAttacks(AttackDisplayInfo[] adi) {
		this.playerAttacks = adi;
	}

	public double getMinGauge() {
		return minGauge;
	}

	public void setMinGauge(double minGauge) {
		this.minGauge = minGauge;
	}

	public String getCurrentAttackName() {
		return currentAttackName;
	}

	public void setCurrentAttackName(String currentAttackName) {
		this.currentAttackName = currentAttackName;
	}
}
