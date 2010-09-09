package com.googlecode.reaxion.game.model.prop;

import com.googlecode.reaxion.game.model.character.Character;
import com.googlecode.reaxion.game.model.Model;
import com.googlecode.reaxion.game.state.BattleGameState;
import com.jme.math.FastMath;
import com.jme.math.Vector3f;

public class GlowRing extends Model {
	private static final String filename = "glow-ring";
	
	private final float lifespan = 16;
	private final float charHeight = 4.7f;
	
	private Character character;
	
	private int lifeCount = 0;
	
    public GlowRing(Character c) {
    	// Load model
    	super(filename);
    	trackable = false;
    	
    	character = c;
    }
    
    // Follow character, change size, and rise
    @ Override
    public void act(BattleGameState b) {
    	super.act(b);
    	
    	Vector3f charPos = character.model.getWorldTranslation();
    	
    	model.setLocalScale(new Vector3f(1, FastMath.sin((FastMath.PI*lifeCount+1)/(lifespan+1)), 1));
    	model.setLocalTranslation(new Vector3f(charPos.x, charPos.y + charHeight*lifeCount/lifespan, charPos.z));
    	
    	if (lifeCount >= lifespan)
    		b.removeModel(this);
    	lifeCount++;
    }
    
}
