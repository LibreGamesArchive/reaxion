package com.googlecode.reaxion.game.burstgrid.info;

import java.util.ArrayList;
import java.util.HashMap;

import com.googlecode.reaxion.game.audio.SoundEffectType;
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
	protected String[] abilities = new String[2];
	protected String[] attacks = new String[6];
	
	protected HashMap<SoundEffectType, String> usableSfx = new HashMap<SoundEffectType, String>();
	
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
	public double getAttackMultiplier() {
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
	
	/**
	 * Sets abilities according to {@code abl}.
	 */
	public void setAbilities(String[] abl) {
		abilities = abl;
	}
	
	/**
	 * Returns an array of abilities, omitting null values.
	 */
	public String[] getAbilities() {
		ArrayList<String> a = new ArrayList<String>();  
		for (int i = 0; i < abilities.length; i++) {  
			if (abilities[i] != null)
				a.add(abilities[i]);   
		}
		return a.toArray(new String[0]);  
	}
	
	/**
	 * Sets attacks according to {@code atk}.
	 */
	public void setAttacks(String[] atk) {
		attacks = atk;
	}
	
	/**
	 * Returns an array of attacks, omitting null values.
	 */
	public String[] getAttacks() {
		ArrayList<String> a = new ArrayList<String>();  
		for (int i = 0; i < attacks.length; i++) {  
			if (attacks[i] != null)
				a.add(attacks[i]);   
		}  
		return a.toArray(new String[0]);  
	}
	
	protected void createBurstGrid(String location){
		bg = new BurstGrid(location);
	}
	
	/**
	 * Sets the {@code HashMap} of usable sound effects. Must be overridden by each subclass of {@code PlayerInfo};
	 */
	protected void setUsableSfx() {
		
	}
	
	/**
	 * Checks if a {@code SoundEffectType} is usable by a character.
	 * 
	 * @param type
	 * @return {@code true} if the character has a defined filename for {@code type}, {@code false} if not.
	 */
	public boolean hasSoundEffectType(SoundEffectType type) {
		return usableSfx.containsKey(type);
	}
	
	/**
	 * Gets a sound effect filename given a sound effect type.
	 * 
	 * @param type
	 * @return Sound effect filename
	 */
	public String getSoundEffect(SoundEffectType type) {
		return usableSfx.get(type);
	}
	
}