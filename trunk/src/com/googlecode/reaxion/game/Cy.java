package com.googlecode.reaxion.game;

public class Cy extends MajorCharacter {
	
	private static final String filename = "i_cy2";
	
    public Cy() {
    	// Load model
    	super(filename);
    }
    
    public Cy(Boolean _trackable) {
    	// Load model
    	super(filename, _trackable);
    }
    
    @ Override
    public void type() {
    	maxHp = 100;
    	maxGauge = 100;
    	speed = .5f;
    }
    
}
