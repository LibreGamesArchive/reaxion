package com.googlecode.reaxion.game.model.attackobject;

import com.googlecode.reaxion.game.model.Model;
import com.googlecode.reaxion.game.model.character.Character;
import com.googlecode.reaxion.game.state.StageGameState;
import com.jme.math.FastMath;
import com.jme.math.Vector3f;

public class Tornado extends AttackObject {
	
	public static final String filename = "whirlwind";
	protected static final int span = 380;
	protected static final float speed = .25f;
	protected static final float dpf = .18f;
	private static final float angleInc = FastMath.PI/60;
	private final int sizeTime = 12;
	
	public Model target;
	
	public Tornado(Model m) {
    	super(filename, dpf, m);
    	flinch = true;
    	lifespan = span;
    }
	
	public Tornado(Model[] m) {
    	super(filename, dpf, m);
    	flinch = true;
    	lifespan = span;
    }
	
	@ Override
    public void act(StageGameState b) {
        // rotate
    	roll = (roll + angleInc) % (FastMath.PI*2);
    	rotate();
    	
    	// grow/shrink
    	float s = 1;
		if (lifeCount <= sizeTime)
			s = (float)(lifeCount+1)/(sizeTime+1);
		else if (lifeCount >= lifespan - sizeTime)
			s = (float)(lifespan-lifeCount+1)/(sizeTime+1);
		model.setLocalScale(new Vector3f(s, 1, s));
    	
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
    		if (m instanceof Character && !users.contains(m)) {
    			flag = true;
    			break;
    		}
    	}
    	if (flag)
    		velocity = new Vector3f();
        
    	super.act(b);
    }
	
}
