package com.googlecode.reaxion.game.model.prop;

import com.googlecode.reaxion.game.model.Model;
import com.googlecode.reaxion.game.state.StageGameState;

public class WaterDrop extends Model {
	private static final String filename = "droplet";
	
	private final int lifespan = 60;	
	private int lifeCount = 0;
	private float gravity = -.05f;
		
    public WaterDrop() {
    	// Load model
    	super(filename);
    	trackable = false;
    	billboarding = Billboard.Free;
    }
    
    @ Override
    public void act(StageGameState b) {
    	super.act(b);
    	
    	model.setLocalScale((float) (lifespan - lifeCount + 1)/(lifespan + 1));
    	
		model.setLocalTranslation(model.getLocalTranslation().addLocal(velocity));
		velocity.y += gravity;
		
		if (lifeCount >= lifespan || model.getLocalTranslation().y <= -.5f)
    		b.removeModel(this);
    	lifeCount++;
    }
    
}
