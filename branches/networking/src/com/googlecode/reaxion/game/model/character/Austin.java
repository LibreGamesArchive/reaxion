package com.googlecode.reaxion.game.model.character;

public class Austin extends MajorCharacter {
	
	private static final String filename = "i_austin2";
	
    public Austin() {
    	// Load model
    	super(filename);
    }
    
    public Austin(Boolean _trackable) {
    	// Load model
    	super(filename, _trackable);
    }
    
    @ Override
    public void type() {
    	super.type();
    	name = "Austin";
    }
    
}
