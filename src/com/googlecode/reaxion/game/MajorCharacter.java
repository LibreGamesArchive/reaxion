package com.googlecode.reaxion.game;

public class MajorCharacter extends Character {
	
	/**
	 * Current combo count of character
	 */
	public int combo = 0;
	
	/**
	 * Maximum combo count of character
	 */
	public int maxCombo = 0;
	
	/**
	 * Time before character's combo counter resets
	 */
	public static int comboTime = 60;
	
    public MajorCharacter(String filename) {
    	// Load model
    	super(filename);
    	
    	type();
    	
    	trackable = true;
    	
    	renew();
    }
    
    public MajorCharacter(String filename, Boolean _trackable) {
    	// Load model
    	super(filename);
    	
    	type();
    	
    	trackable = _trackable;
    	
    	renew();
    }
    
    /**
	 * Set traits for all characters of this type, override this to set custom stats
	 */
    public void type() {
    	maxHp = 100;
    	maxAp = 100;
    	speed = .5f;
    }
    
    /**
	 * Replenish depletable variables to new state
	 */
    public void renew() {
    	hp = maxHp;
    	ap = maxAp;
    	combo = 0;
    }
    
    // Standard animated states, only override if specific states differ
    @ Override
    public void act() {
    	super.act();
    	
    	animate("stand", "run");
    }
    
}
