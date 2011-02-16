package com.googlecode.reaxion.game.model.prop;

import com.googlecode.reaxion.game.model.Model;
import com.googlecode.reaxion.game.model.Model.Billboard;
import com.googlecode.reaxion.game.state.StageGameState;
import com.jme.math.Vector3f;

public class Sparkle extends Model {
	public static final String filename = "sparkle";
	
	private int lifespan;
	private int shrinkTime;
	
	private int lifeCount = 0;
	
    public Sparkle(int l, int s) {
    	// Load model
    	super(filename);
    	trackable = false;
    	billboarding = Billboard.Free;
    	lifespan = l;
    	shrinkTime = s;
    }
    
    // Change size, and billboard
    @ Override
    public void act(StageGameState b) {
    	super.act(b);
    	
    	if (lifeCount > lifespan - shrinkTime)
    	model.setLocalScale((float) (lifespan - lifeCount + 1)/(lifespan - shrinkTime + 1));
    	
    	if (lifeCount >= lifespan)
    		b.removeModel(this);
    	lifeCount++;
    }
    
}
