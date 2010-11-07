package com.googlecode.reaxion.game.model.character;

public class Shine extends MajorCharacter {
	
	private static final String filename = "i_shine6";
	
    public Shine() {
    	// Load model
    	super(filename);
    }
    
    public Shine(Boolean _trackable) {
    	// Load model
    	super(filename, _trackable);
    }
    
    @ Override
    public void type() {
    	super.type();
    	name = "Shine";
    }
    
}
