package com.googlecode.reaxion.game.model.prop;

import com.googlecode.reaxion.game.model.Model;
import com.googlecode.reaxion.game.state.StageGameState;
import com.jme.math.Vector3f;

public class LightFade extends Model {
	public static final String filename = "light";
	
	private final float lifespan = 12;
	public final float charHeight = 4.7f;
	
	private int lifeCount = 0;
	
    public LightFade() {
    	// Load model
    	super(filename);
    	trackable = false;
    	billboarding = Billboard.Free;
    }
    
    // Change size, and billboard
    @ Override
    public void act(StageGameState b) {
    	super.act(b);
    	
    	model.setLocalScale(new Vector3f(1, (float)charHeight * (lifespan - lifeCount + 1)/(lifespan + 1), 1));
    	
    	if (lifeCount >= lifespan)
    		b.removeModel(this);
    	lifeCount++;
    }
    
}
