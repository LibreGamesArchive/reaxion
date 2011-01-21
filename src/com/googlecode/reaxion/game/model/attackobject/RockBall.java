package com.googlecode.reaxion.game.model.attackobject;

import com.googlecode.reaxion.game.model.Model;
import com.googlecode.reaxion.game.model.character.Character;
import com.googlecode.reaxion.game.state.StageGameState;
import com.jme.math.FastMath;

public class RockBall extends AttackObject {
	
	public static final String filename = "meteor";
	protected static final int span = 60;
	protected static final float dpf = 18;
	
	private float rollInc, yawInc, pitchInc;
	
	public RockBall(Model m) {
    	super(filename, dpf, m);
    	flinch = true;
    	lifespan = span;
    	
    	rollInc = FastMath.nextRandomFloat()*.1f - .05f;
    	yawInc = FastMath.nextRandomFloat()*.1f - .05f;
    	pitchInc = FastMath.nextRandomFloat()*.1f - .05f;
    }
	
	public RockBall(Model[] m) {
    	super(filename, dpf, m);
    	flinch = true;
    	lifespan = span;
    	
    	rollInc = FastMath.nextRandomFloat()*.1f - .05f;
    	yawInc = FastMath.nextRandomFloat()*.1f - .05f;
    	pitchInc = FastMath.nextRandomFloat()*.1f - .05f;
    }
	
	@Override
	public void hit(StageGameState b, Character other) {
		finish(b);
    }
	
	@ Override
    public void act(StageGameState b) {
		// scale
		if (lifeCount == 0)
			model.setLocalScale(.7f);
		
		// check for ground
		if (model.getWorldTranslation().y <= -2)
    		finish(b);
		
		// rotate
		roll += rollInc;
		yaw += yawInc;
		pitch += pitchInc;
		rotate();
			
    	super.act(b);
    }
	
}
