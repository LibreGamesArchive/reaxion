package com.googlecode.reaxion.game.burstgrid.info;

import java.util.ArrayList;
import java.util.HashMap;

import com.googlecode.reaxion.game.ability.Ability;
import com.googlecode.reaxion.game.attack.Attack;
import com.googlecode.reaxion.game.audio.SoundEffectType;
import com.googlecode.reaxion.game.burstgrid.BurstGrid;
import com.googlecode.reaxion.game.burstgrid.node.AbilityNode;
import com.googlecode.reaxion.game.burstgrid.node.AttackNode;
import com.googlecode.reaxion.game.burstgrid.node.BurstNode;
import com.googlecode.reaxion.game.burstgrid.node.HPNode;
import com.googlecode.reaxion.game.burstgrid.node.MaxGaugeNode;
import com.googlecode.reaxion.game.burstgrid.node.MinGaugeNode;
import com.googlecode.reaxion.game.burstgrid.node.RateNode;
import com.googlecode.reaxion.game.burstgrid.node.StrengthNode;

public abstract class PlayerInfo{
	
	// default values
	protected int baseHP = 60;
	protected int baseStrength = 0;
	protected int baseMinGauge = 8;
	protected int baseMaxGauge = 16;
	protected int baseRate = 0;
	protected int baseExp = 0;
	
	//current values
	protected int maxHP = 60;
	protected int strength = 0;
	protected int minGauge = 20;
	protected int maxGauge = 30;
	protected int rate = 1;
	protected int exp = 0;
	
	protected BurstGrid grid;
	protected String[] abilities = new String[2];
	protected String[] attacks = new String[6];
	protected ArrayList<Ability> abilityPool = new ArrayList<Ability>();
	protected ArrayList<Attack> attackPool = new ArrayList<Attack>();
	
	protected HashMap<SoundEffectType, String> usableSfx = new HashMap<SoundEffectType, String>();
	
	/**
	 * To be called at the program launch to create all player info.
	 */
	public void init() {
		readStatsFromGrid();
	}
	
	protected void setStats(int hp, int str, int minG, int maxG, int r){
		baseHP = hp;
		baseStrength = str;
		baseMinGauge = minG;
		baseMaxGauge = maxG;
		baseRate = r;
		baseExp = 0;
	}

	public int getBaseHP() {
		return baseHP;
	}

	public int getBaseStrength() {
		return baseStrength;
	}

	public int getBaseMinGauge() {
		return baseMinGauge;
	}

	public int getBaseMaxGauge() {
		return baseMaxGauge;
	}

	public int getBaseRate() {
		return baseRate;
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
		grid = new BurstGrid(location);
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
	
	/**
	 * Reads the character's statistics from his/her burstgrid.
	 */
	public void readStatsFromGrid(){
		ArrayList<BurstNode> bg = grid.getBurstGrid();
		for(BurstNode b:bg){
			if(b.activated){
				if(b instanceof HPNode){
					maxHP+=((HPNode)b).hpPlus;
				}
				else if(b instanceof StrengthNode){
					strength+=((StrengthNode)b).strengthPlus;
				}
				if(b instanceof MinGaugeNode){
					minGauge+=((MinGaugeNode)b).minGPlus;
				}
				if(b instanceof MaxGaugeNode){
					maxGauge+=((MaxGaugeNode)b).maxGPlus;
				}
				if(b instanceof RateNode){
					rate+=((RateNode)b).rate;
				}
				if(b instanceof AbilityNode){
					abilityPool.add(((AbilityNode)b).ab);
				}
				if(b instanceof AttackNode){
					attackPool.add(((AttackNode)b).at);
				}
			}
		}
	}
	
}