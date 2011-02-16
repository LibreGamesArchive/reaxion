package com.googlecode.reaxion.game.model.character;

public class Andrew extends MajorCharacter {
	
	private static final String filename = "i_andrew3";
	
    public Andrew() {
    	// Load model
    	super(filename);
    }
    
    public Andrew(Boolean _trackable) {
    	// Load model
    	super(filename, _trackable);
    }
    
    @ Override
    public void type() {
    	super.type();
    	name = "Andrew";
    }
    
}
