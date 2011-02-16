package com.googlecode.reaxion.game.model.attackobject;

import com.googlecode.reaxion.game.model.Model;
import com.googlecode.reaxion.game.model.character.Character;
import com.googlecode.reaxion.game.state.StageGameState;
import com.jme.math.FastMath;
import com.jme.math.Vector3f;

public class FlyingBird extends AttackObject {
	
	public static final String filename = "bird";
	protected static final int span = 310;
	protected static final float dpf = 12;
	
	private final float flyHeight = 4;
	private final float riseSpeed = .3f;
	
	public float dTheta = FastMath.PI/30;
	private final float radius = 6;
	
	public float angle;
	public Vector3f pivot;
	
	public Model target;
	
	private final int delay = 12;
	private int delayCount = 0;
	
	public FlyingBird(Model m) {
    	super(filename, dpf, m);
    	flinch = true;
    	lifespan = span;
    }
	
	public FlyingBird(Model[] m) {
    	super(filename, dpf, m);
    	flinch = true;
    	lifespan = span;
    }
	
	@Override
	public void hit(StageGameState b, Character other) {
		finish(b);
    }
	
	@ Override
    public void act(StageGameState b) {
		// animate
		play("fly");
		
		// chase target
		float sign = (dTheta > 0) ? 1 : -1;
		Vector3f dir = new Vector3f(FastMath.sin(angle + sign*FastMath.PI/2), 0 , FastMath.cos(angle + sign*FastMath.PI/2));
		if (delayCount == 0 &&
				Math.abs(dir.angleBetween((target.model.getWorldTranslation().subtract(model.getWorldTranslation()).mult(new Vector3f(1,0,1)).normalize()))) <= 2*Math.abs(dTheta)) {
			// flip pivot point
			pivot = pivot.add(new Vector3f(FastMath.sin(angle), 0 , FastMath.cos(angle)).mult(radius*2));
			dTheta *= -1;
			angle = (angle + FastMath.PI) % (2*FastMath.PI);
			
			delayCount = delay;
		}
		
		// count down delay
		if (delayCount > 0) {
			// fly tangent
			model.setLocalTranslation(model.getLocalTranslation().add(dir.mult(radius*Math.abs(dTheta))));
			
			delayCount--;
			
		} else {
			// set position
			model.setLocalTranslation(pivot.add(new Vector3f(FastMath.sin(angle), 0, FastMath.cos(angle)).mult(radius)));

			// increment angle
			angle = (angle + dTheta + 2*FastMath.PI) % (2*FastMath.PI);
		}
		
		// follow vertically
		float dif = target.model.getWorldTranslation().y + flyHeight - model.getWorldTranslation().y;
		if (Math.abs(dif) < riseSpeed)
			model.getLocalTranslation().y = target.model.getLocalTranslation().y + flyHeight;
		else
			model.getLocalTranslation().y += ((dif > 0)? 1 : -1)*riseSpeed;
        
    	super.act(b); 
    	
		// face direction
		rotate(dir);		
    }
	
}
