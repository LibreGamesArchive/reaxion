package com.googlecode.reaxion.game.model.character;

import com.googlecode.reaxion.game.state.StageGameState;
import com.jme.math.Vector3f;

public class MajorCharacter extends Character {
	
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
    	trackOffset = new Vector3f(0, 3, 0);
    	renew();
    }
    
    /**
	 * Set traits for all characters of this type, override this to set custom stats
	 */
    public void type() {
    	maxHp = 100;
    	gaugeRate = .0015;
    	minGauge = 10;
    	maxGauge = 18;
    	speed = .5f;
    }
    
    /**
	 * Replenish depletable variables to new state
	 */
    public void renew() {
    	hp = maxHp;
    	gauge = minGauge;
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
