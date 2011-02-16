package com.googlecode.reaxion.game.model.attackobject;

import com.googlecode.reaxion.game.model.Model;
import com.googlecode.reaxion.game.model.character.Character;
import com.googlecode.reaxion.game.model.prop.FireTrail;
import com.googlecode.reaxion.game.state.StageGameState;
import com.googlecode.reaxion.game.util.LoadingQueue;
import com.jme.math.FastMath;

public class Meteor extends AttackObject {
	
	public static final String filename = "meteor";
	protected static final float dpf = 30;
	protected static final float speed = .5f;
	
	private float rollInc, yawInc, pitchInc;
	
	public Meteor(Model m) {
    	super(filename, dpf, m);
    	flinch = true;
    	
    	rollInc = FastMath.nextRandomFloat()*.1f - .05f;
    	yawInc = FastMath.nextRandomFloat()*.1f - .05f;
    	pitchInc = FastMath.nextRandomFloat()*.1f - .05f;
    }
	
	public Meteor(Model[] m) {
    	super(filename, dpf, m);
    	flinch = true;
    	
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
		velocity.y = -speed;
		
		// check for ground
		if (model.getWorldTranslation().y <= -2)
    		finish(b);
		
		// rotate
		roll += rollInc;
		yaw += yawInc;
		pitch += pitchInc;
		rotate();
		
		// create a trail of fire
		if (lifeCount % 5 == 0)
			leaveTrail(b);
			
    	super.act(b);
    }
	
	private void leaveTrail(StageGameState b) {
		FireTrail f = (FireTrail)LoadingQueue.quickLoad(new FireTrail(40), b);
		f.model.setLocalTranslation(model.getWorldTranslation().clone());
		b.getRootNode().updateRenderState();
	}
	
}
