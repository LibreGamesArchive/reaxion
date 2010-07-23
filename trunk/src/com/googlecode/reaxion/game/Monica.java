package com.googlecode.reaxion.game;

public class Monica extends MajorCharacter {
	
	private static final String filename = "i_monica6-4";
	
    public Monica() {
    	// Load model
    	super(filename);
    }
    
    public Monica(Boolean _trackable) {
    	// Load model
    	super(filename, _trackable);
    }
    
    @ Override
    public void type() {
    	maxHp = 100;
    	maxAp = 100;
    	speed = .5f;
    }
    
}
