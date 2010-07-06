package com.googlecode.reaxion.game;

public class Khoa extends Character {
	
    public Khoa() {
    	// Load model
    	super("i_khoa4");
    	
    	// Set character constants
    	maxHp = 100;
    	maxAp = 100;
    	speed = .5f;
    	
    	renew();
    }
    
    /**
	 * Replenish depletable variables
	 */
    public void renew() {
    	hp = maxHp;
    	ap = maxAp;
    }
    
    @ Override
    public void act() {
    	super.act();
    	
    	animate("stand", "run");
    }
    
}
