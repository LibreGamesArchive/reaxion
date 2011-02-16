package com.googlecode.reaxion.game.model.prop;

import com.googlecode.reaxion.game.model.character.Character;
import com.googlecode.reaxion.game.model.Model;
import com.googlecode.reaxion.game.state.StageGameState;
import com.jme.math.FastMath;
import com.jme.math.Vector3f;

public class DeathWarp extends Model {
	private static final String filename = "death-warp";
	
	private final float lifespan = 160;
	private final float charHeight = 4.7f;
	private final float growTime = 16;
	
	private final float[] angleInc = {FastMath.PI/32, FastMath.PI/8};
	
	private Model character;
	
	private int lifeCount = 0;
	
    public DeathWarp(Model c) {
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
    	
    	if (lifeCount <= growTime)
    		model.setLocalScale((FastMath.PI*lifeCount+1)/(growTime+1));
    	model.setLocalTranslation(new Vector3f(charPos.x, charPos.y + charHeight/2f, charPos.z));
    	
    	float angle = (angleInc[1] - angleInc[0]) * lifeCount + angleInc[0];
    	Vector3f rot = new Vector3f(FastMath.cos(angle), 0, FastMath.sin(angle));
    	rotate(rot);
    	
    	if (lifeCount >= lifespan)
    		b.removeModel(this);
    	lifeCount++;
    }
    
}
