package com.googlecode.reaxion.game.model.attackobject;

import java.util.ArrayList;

import com.googlecode.reaxion.game.model.Model;
import com.googlecode.reaxion.game.model.character.Character;
import com.googlecode.reaxion.game.state.StageGameState;
import com.googlecode.reaxion.game.util.LoadingQueue;
import com.jme.math.FastMath;
import com.jme.math.Vector3f;

public class DashForce extends AttackObject {
	
	public static final String filename = "slide-field";
	protected static final int span = 32;
	protected static final int fadePoint = 28;
	protected static final float dpf = 10;
	
	private Model user;
	
	private ArrayList<Model> hitList = new ArrayList<Model>();
	
	public DashForce(Model m) {
    	super(filename, dpf, m);
    	flinch = true;
    	lifespan = span;
    	user = m;
    }
	
	public DashForce(Model[] m) {
    	super(filename, dpf, m);
    	flinch = true;
    	lifespan = span;
    	user = m[m.length-1];
    }
	
	@Override
	public void act(StageGameState b) {	
		if (lifeCount >= fadePoint)
			model.setLocalScale(((float)lifespan - lifeCount)/(float)(lifespan - fadePoint));
        
		// stay with character
		Vector3f rotation = user.rotationVector;
		float angle = FastMath.atan2(rotation.x, rotation.z);
		Vector3f translation = new Vector3f(1f*FastMath.sin(angle), 0, 1f*FastMath.cos(angle));		
		rotate(rotation);
		model.setLocalTranslation(user.model.getWorldTranslation().add(translation));
		
		// check if a Character is hit with linear approximation
    	Model[] collisions = getLinearModelCollisions(b, velocity, .5f);
        for (Model c : collisions) {
        	if (c instanceof Character && !users.contains(c)) {
        		// only deal damage once, if not hit before
        		if (!hitList.contains(c)) {
	        		hitList.add(c);
	        		damagePerFrame = dpf;
        		} else {
        			damagePerFrame = 0;
        		}
        		((Character)c).hit(b, this);
        	}
        }
        
        //check lifespan
        if (lifeCount == lifespan)
        	finish(b);
        lifeCount++;
    }
	
	// ends attack
	public void cancel() {
		lifeCount = lifespan;
	}
	
}
