package com.googlecode.reaxion.game.model.character;

public class Polina extends MajorCharacter {
	
	private static final String filename = "i_polina3";
	
    public Polina() {
    	// Load model
    	super(filename);
    }
    
    public Polina(Boolean _trackable) {
    	// Load model
    	super(filename, _trackable);
    }
    
    @ Override
    public void type() {
    	super.type();
    	name = "Polina";
    }
    
}
