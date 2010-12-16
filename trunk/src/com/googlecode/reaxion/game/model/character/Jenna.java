package com.googlecode.reaxion.game.model.character;

public class Jenna extends MajorCharacter {
	
	private static final String filename = "i_jenna3";
	
    public Jenna() {
    	// Load model
    	super(filename);
    }
    
    public Jenna(Boolean _trackable) {
    	// Load model
    	super(filename, _trackable);
    }
    
    @ Override
    public void type() {
    	super.type();
    	name = "Jenna";
    }
    
}
