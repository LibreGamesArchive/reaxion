package com.googlecode.reaxion.game.model.character;

import com.googlecode.reaxion.game.burstgrid.info.PlayerInfo;
import com.googlecode.reaxion.game.state.StageGameState;
import com.googlecode.reaxion.game.util.PlayerInfoManager;
import com.jme.math.Vector3f;

public class MajorCharacter extends Character {
	
	public PlayerInfo info;
	
    public MajorCharacter(String filename) {
    	// Load model
    	super(filename);
    	trackable = true;
    	init();
    }
    
    public MajorCharacter(String filename, Boolean _trackable) {
    	// Load model
    	super(filename);
    	trackable = _trackable;
    	init();
    }
    
    @Override
    protected void init() {
    	super.init();
    	type();
    	updateStats();
    	trackOffset = new Vector3f(0, 3, 0);
    	renew();
    }
    
    /**
	 * Override this to set additional character-specific data, such as name.
	 */
    public void type() {
    	/*
    	maxHp = 100;
    	gaugeRate = .0015;
    	minGauge = 18;
    	maxGauge = 30;
    	*/
    	speed = .5f;
    }
    
    /**
	 * Set traits for all characters of this type based on {@code PlayerInfo}.
	 */
    public void updateStats() {
    	// create stats based on PlayerInfo
    	if (info == null)
    		info = PlayerInfoManager.get(name);
    	
    	maxHp = info.getMaxHp();
    	strengthMult = info.getAttackMultiplier();
    	minGauge = info.getMinGauge();
    	maxGauge = info.getMaxGauge();
    	gaugeRate = info.getScaledGauge();
    }
    
    /**
	 * Replenish depletable variables to new state
	 */
    public void renew() {
    	hp = maxHp;
    	gauge = minGauge;
    }
    
    /**
     * Removes death animation lock
     */
    public void clearDeathFlag() {
    	frozen = false;
    }
    
    // Standard animated states, only override if specific states differ
    @ Override
    public void act(StageGameState b) {
    	super.act(b);
    	
    	// switch if dead
    	if (b.getPlayer() == this && hp <= 0)
    		b.tagSwitch();
    	
    	animate(b.tpf, "stand", "run", "jump", "cast", "raiseUp", "raiseDown", "shootUp", "shootDown", "guard", "flinch", "dying", "dead");
    }
    
}
