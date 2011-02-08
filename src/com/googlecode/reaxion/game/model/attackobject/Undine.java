package com.googlecode.reaxion.game.model.attackobject;

import com.googlecode.reaxion.game.model.Model;
import com.googlecode.reaxion.game.model.character.Character;
import com.googlecode.reaxion.game.state.StageGameState;
import com.jme.math.FastMath;
import com.jme.math.Vector3f;

public class Undine extends AttackObject {
	
	public static final String filename = "i_undine";
	protected static final float speed = .32f;
	protected static final float dpf = 16;
	private static final float angleInc = FastMath.PI/40;
	
	private final float fall = -.06f;
	
	public Model target;
	
	private float angle = 0;
	
	public Undine(Model m) {
    	super(filename, dpf, m);
    	flinch = true;
    }
	
	public Undine(Model[] m) {
    	super(filename, dpf, m);
    	flinch = true;
    }
	
	@Override
	public void hit(StageGameState b, Character other) {
		finish(b);
    }
	
	@ Override
    public void act(StageGameState b) {
		// animate
		play("jump");
		
        // rotate proportional to y velocity
    	angle -= FastMath.abs(velocity.y / fall) * angleInc;
    	rotate(new Vector3f(FastMath.sin(angle), 0, FastMath.cos(angle)));
    	
    	// seek if falling
    	if (velocity.y < 0) {
    		// get position
    		Vector3f p = model.getWorldTranslation();
    		// get target position
    		Vector3f t;
    		if (target != null)
    			t = target.model.getWorldTranslation();
    		else
    			t = p;
    		// find direction of velocity
    		velocity = t.subtract(p).normalize().mult(new Vector3f(speed, 0, speed)).add(0, velocity.y, 0);
    	}
    	
    	
    	// apply gravity
    	if (velocity.y > 0)
    		velocity.y += fall;
    	else if (velocity.y <= 0)
    		velocity.y = fall;
    	
    	// check for ground
    	if (velocity.y < 0 && model.getWorldTranslation().y <= -5)
			finish(b);
        
    	super.act(b);
    }
	
}
