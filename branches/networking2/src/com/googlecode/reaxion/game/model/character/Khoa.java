package com.googlecode.reaxion.game.model.character;

public class Khoa extends MajorCharacter {
	
	private static final String filename = "i_khoa6";
	
    public Khoa() {
    	// Load model
    	super(filename);
    }
    
    public Khoa(Boolean _trackable) {
    	// Load model
    	super(filename, _trackable);
    }
    
    @ Override
    public void type() {
    	super.type();
    	name = "Khoa";
    }
    
}
