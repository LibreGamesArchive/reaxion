package com.googlecode.reaxion.game.model.character;

public class DarkShine extends MajorCharacter {
	
	private static final String filename = "i_shine6-dark";
	
    public DarkShine() {
    	// Load model
    	super(filename);
    }
    
    public DarkShine(Boolean _trackable) {
    	// Load model
    	super(filename, _trackable);
    }
    
    @ Override
    public void type() {
    	super.type();
    	name = "Shine";
    }
    
}
