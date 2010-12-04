package com.googlecode.reaxion.game.model.prop;

import com.googlecode.reaxion.game.model.character.Character;
import com.googlecode.reaxion.game.model.Model;
import com.googlecode.reaxion.game.state.StageGameState;
import com.jme.math.FastMath;
import com.jme.math.Vector3f;

public class DeathBurst extends Model {
	private static final String filename = "death-burst";
	
	private final float lifespan = 16;
	private final float maxRadius = 24;
	private final float charHeight = 4.7f;

	private Model character;
	
	private int lifeCount = 0;
	
    public DeathBurst(Model c) {
    	// Load model
    	super(filename);
    	trackable = false;
    	
    	character = c;
    }
    
    // Follow character, change size, and rise
    @ Override
    public void act(StageGameState b) {
    	super.act(b);
    	
    	Vector3f charPos = character.model.getWorldTranslation();
    	model.setLocalTranslation(new Vector3f(charPos.x, charPos.y + charHeight/2f, charPos.z));
    	
    	float s = maxRadius * (lifeCount + 1)/(lifespan + 1);
		model.setLocalScale(new Vector3f(s, 1, s));
    	
    	if (lifeCount >= lifespan)
    		b.removeModel(this);
    	lifeCount++;
    }
    
}
