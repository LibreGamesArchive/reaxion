package com.googlecode.reaxion.game.model.prop;

import com.googlecode.reaxion.game.model.Model;
import com.googlecode.reaxion.game.model.Model.Billboard;
import com.googlecode.reaxion.game.state.StageGameState;
import com.jme.math.FastMath;

public class FireTrail extends Model {
	public static final String filename = "fire-trail";
	
	private int lifespan;	
	private int lifeCount = 0;
	
    public FireTrail(int s) {
    	super(filename);
    	lifespan = s;
    	trackable = false;
    	billboarding = Billboard.Free;
    	pitch = FastMath.PI*2*FastMath.nextRandomFloat();
    }
    
    // Change size, and billboard
    @ Override
    public void act(StageGameState b) {
    	super.act(b);
    	
    	model.setLocalScale((float) (lifespan - lifeCount + 1)/(lifespan + 1));
    	
    	if (lifeCount >= lifespan)
    		b.removeModel(this);
    	lifeCount++;
    }
    
}
