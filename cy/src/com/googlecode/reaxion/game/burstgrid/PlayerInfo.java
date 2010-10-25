package com.googlecode.reaxion.game.burstgrid;

public class PlayerInfo{
	
	protected int maxHP = 0;
	protected int strength = 0;
	protected int minGauge = 0;
	protected int maxGauge = 0;
	
	public PlayerInfo(){
		maxHP = 0;
		strength = 0;
		minGauge = 0;
		maxGauge = 0;
	}

	public int getMaxHP() {
		return maxHP;
	}

	public void setMaxHP(int maxHP) {
		this.maxHP = maxHP;
	}

	public int getStrength() {
		return strength;
	}

	public void setStrength(int strength) {
		this.strength = strength;
	}

	public int getMinGauge() {
		return minGauge;
	}

	public void setMinGauge(int minGauge) {
		this.minGauge = minGauge;
	}

	public int getMaxGauge() {
		return maxGauge;
	}

	public void setMaxGauge(int maxGauge) {
		this.maxGauge = maxGauge;
	}
}