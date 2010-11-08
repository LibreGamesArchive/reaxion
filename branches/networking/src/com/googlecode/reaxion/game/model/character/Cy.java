package com.googlecode.reaxion.game.model.character;

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
    	super.type();
    	name = "Cy";
    }
    
}
