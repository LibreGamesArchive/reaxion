package com.googlecode.reaxion.game.model.prop;

import com.googlecode.reaxion.game.model.Model;
import com.googlecode.reaxion.game.state.StageGameState;
import com.jme.math.FastMath;

public class IceTrail extends Model {
	public static final String filename = "ice-trail";
	
	private int lifespan;	
	private int lifeCount = 0;
	
	private float speed;
	
    public IceTrail(int s, float p) {
    	super(filename);
    	lifespan = s;
    	speed = p;
    	trackable = false;
    	billboarding = Billboard.Free;
    	pitch = FastMath.PI*2*FastMath.nextRandomFloat();
    }
    
    // Change size, and billboard
    @ Override
    public void act(StageGameState b) {
    	super.act(b);
    	
    	model.setLocalScale((float) (lifespan - lifeCount + 1)/(lifespan + 1));
    	
    	model.setLocalTranslation(model.getLocalTranslation().add(0, speed, 0));
    	
    	if (lifeCount >= lifespan)
    		b.removeModel(this);
    	lifeCount++;
    }
    
}
