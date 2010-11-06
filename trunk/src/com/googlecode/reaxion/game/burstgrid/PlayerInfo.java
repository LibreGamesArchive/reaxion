package com.googlecode.reaxion.game.burstgrid;

public class PlayerInfo{
	
	protected int maxHP = 0;
	protected int strength = 0;
	protected int minGauge = 0;
	protected int maxGauge = 0;
	protected int rate = 0;
	protected int exp = 0;
	protected BurstGrid bg;
	
	public PlayerInfo(){
		maxHP = 0;
		strength = 0;
		minGauge = 0;
		maxGauge = 0;
		rate = 0;
		exp = 0;
	}
	
	public PlayerInfo(int hp, int str, int minG, int maxG, int r){
		maxHP = hp;
		strength = str;
		minGauge = minG;
		maxGauge = maxG;
		rate = r;
		exp = 0;
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
	
	protected void createBurstGrid(String location){
		bg = new BurstGrid(location);
	}
}