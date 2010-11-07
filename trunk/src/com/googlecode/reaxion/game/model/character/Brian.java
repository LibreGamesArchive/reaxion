package com.googlecode.reaxion.game.model.character;

public class Brian extends MajorCharacter {
	
	private static final String filename = "i_brian2";
	
    public Brian() {
    	// Load model
    	super(filename);
    }
    
    public Brian(Boolean _trackable) {
    	// Load model
    	super(filename, _trackable);
    }
    
    @ Override
    public void type() {
    	super.type();
    	name = "Brian";
    	maxHp = 100;
    	speed = .5f;
    }
    
}
