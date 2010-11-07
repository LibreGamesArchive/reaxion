package com.googlecode.reaxion.game.burstgrid.info;

import com.googlecode.reaxion.game.burstgrid.BurstGrid;

public abstract class PlayerInfo{
	
	// default values
	protected int maxHP = 100;
	protected int strength = 0;
	protected int minGauge = 18;
	protected int maxGauge = 30;
	protected int rate = 0;
	protected int exp = 0;
	protected BurstGrid bg;
	
	/**
	 * To be called at the program launch to create all player info.
	 */
	public void init() {
		
	}
	
	protected void setStats(int hp, int str, int minG, int maxG, int r){
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

	public void setMaxHP(int val) {
		maxHP = val;
	}

	public int getStrength() {
		return strength;
	}
	
	/**
	 * Returns the damage multiplier for this character's attacks as a function of {@code strength}.
	 */
	public double getAtkMultiplier() {
		return 1.5/12*strength + 1;
	}

	public void setStrength(int val) {
		strength = val;
	}

	public int getMinGauge() {
		return minGauge;
	}

	public void setMinGauge(int val) {
		minGauge = val;
	}

	public int getMaxGauge() {
		return maxGauge;
	}

	public void setMaxGauge(int val) {
		maxGauge = val;
	}
	
	/**
	 * Returns the gauge rate scaled by a factor of 10.
	 */
	public double getScaledGauge() {
		return rate/10+.05;
	}
	
	protected void createBurstGrid(String location){
		bg = new BurstGrid(location);
	}
}