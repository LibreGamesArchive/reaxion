package com.googlecode.reaxion.game.model.character;

public class Savannah extends MajorCharacter {
	
	private static final String filename = "i_savannah3";
	
    public Savannah() {
    	// Load model
    	super(filename);
    }
    
    public Savannah(Boolean _trackable) {
    	// Load model
    	super(filename, _trackable);
    }
    
    @ Override
    public void type() {
    	super.type();
    	name = "Savannah";
    }
    
}
