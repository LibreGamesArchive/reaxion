package com.googlecode.reaxion.game.model.character;

public class Raina extends MajorCharacter {
	
	private static final String filename = "i_raina4";
	
    public Raina() {
    	// Load model
    	super(filename);
    }
    
    public Raina(Boolean _trackable) {
    	// Load model
    	super(filename, _trackable);
    }
    
    @ Override
    public void type() {
    	super.type();
    	name = "Raina";
    }
    
}
