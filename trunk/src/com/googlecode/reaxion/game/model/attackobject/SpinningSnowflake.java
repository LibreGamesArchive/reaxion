package com.googlecode.reaxion.game.model.attackobject;

import com.googlecode.reaxion.game.model.Model;
import com.googlecode.reaxion.game.model.character.Character;
import com.googlecode.reaxion.game.state.StageGameState;
import com.googlecode.reaxion.game.util.LoadingQueue;
import com.jme.math.FastMath;
import com.jme.math.Vector3f;

public class SpinningSnowflake extends AttackObject {
	
	public static final String filename = "snowflake";
	protected static final int span = 210;
	protected static final float speed = .2f;
	protected static final float dpf = 0;
	private static final float angleInc = .0044f; // approx. pi/720
	
	public Model target;
	
	private float angle = 0;
	
	public SpinningSnowflake(Model m) {
    	super(filename, dpf, m);
    	flinch = true;
    	lifespan = span;
    }
	
	public SpinningSnowflake(Model[] m) {
    	super(filename, dpf, m);
    	flinch = true;
    	lifespan = span;
    }
	
	@Override
	public void hit(StageGameState b, Character other) {
		finish(b);
		
		// create ice pillar
		IcePillar ice = (IcePillar)LoadingQueue.quickLoad(new IcePillar(users.toArray(new Character[0])), b);		
		ice.model.setLocalTranslation(model.getWorldTranslation().mult(new Vector3f(1, 0, 1)).add(0, -8, 0));
		b.getRootNode().updateRenderState();
		
    }
	
	@ Override
    public void act(StageGameState b) {
        // rotate proportional to life left
    	angle += (float)Math.min(120, lifespan - lifeCount) * angleInc;
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
