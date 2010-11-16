package com.googlecode.reaxion.game.model.prop;

import com.googlecode.reaxion.game.model.Model;
import com.googlecode.reaxion.game.state.StageGameState;
import com.jme.math.FastMath;

public class Dust extends Model {
	private static final String filename = "twilight-dust";
	
	private int duration = 60;
	private float maxSize;
	private boolean growing = true;
	
	private final float sizeRate = .008f;
	private float size = sizeRate;
		
    public Dust() {
    	// Load model
    	super(filename);
    	trackable = false;
    	init();
    }
    
    @Override
    protected void init() {
    	super.init();
    	maxSize = FastMath.nextRandomFloat()*.3f + .2f;
    }
    
    @ Override
    public void act(StageGameState b) {
    	super.act(b);
    	if (size <= 0 || model.getLocalTranslation().y <= -.25f) {
    		b.removeModel(this);
    	} else {
    		if (growing) {
    			size += sizeRate;
    			if (size >= maxSize)
    				growing = false;
    		} else if (duration > 0)
    			duration--;
    		else
    			size -= sizeRate;
    		model.setLocalScale(size);
    	}
    	
    	billboard(b.getCamera(), true);
    	
		model.setLocalTranslation(model.getLocalTranslation().addLocal(velocity).addLocal(b.getPlayer().getVelocity()));
    }
    
}
