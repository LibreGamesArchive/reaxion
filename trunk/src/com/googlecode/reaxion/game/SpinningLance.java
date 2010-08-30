package com.googlecode.reaxion.game;

import com.jme.math.FastMath;
import com.jme.math.Vector3f;

public class SpinningLance extends AttackObject {
	
	protected static final String filename = "lance";
	protected static final int span = 380;
	protected static final float speed = .3f;
	protected static final float dpf = 8;
	private static final float angleInc = .0044f; // approx. pi/720
	
	public Model target;
	
	private float angle = 0;
	
	public SpinningLance(Model m) {
    	super(filename, dpf, m);
    	flinch = true;
    	lifespan = span;
    	allowYaw = false;
    	allowPitch = false;
    }
	
	public SpinningLance(Model[] m) {
    	super(filename, dpf, m);
    	flinch = true;
    	lifespan = span;
    	allowYaw = false;
    	allowPitch = false;
    }
	
	@Override
	public void hit(BattleGameState b, Character other) {
		b.removeModel(this);
    }
	
	@ Override
    public void act(BattleGameState b) {
        // rotate proportional to life left
    	angle += (float)(lifespan - lifeCount) * angleInc;
    	rotate(new Vector3f(FastMath.sin(angle), 0, FastMath.cos(angle)));
    	
    	// get position
    	Vector3f p = model.getWorldTranslation();
    	// get target position
    	Vector3f t;
    	if (target != null)
    		t = target.model.getWorldTranslation();
    	else
    		t = p;
    	// find direction of velocity
    	velocity = t.subtract(p).normalize().mult(speed);
    	velocity.y = 0;
    	
    	//check for collisions with characters
    	boolean flag = false;
    	Model[] collisions = getModelCollisions(b);
    	for (Model m : collisions) {
    		if (m instanceof Character) {
    			flag = true;
    			break;
    		}
    	}
    	if (flag)
    		velocity = new Vector3f();
        
    	super.act(b);
    }
	
}
