package com.googlecode.reaxion.game.model.attackobject;

import java.util.ArrayList;

import com.googlecode.reaxion.game.model.Model;
import com.googlecode.reaxion.game.model.character.Character;
import com.googlecode.reaxion.game.state.StageGameState;
import com.googlecode.reaxion.game.util.ListFilter;
import com.googlecode.reaxion.game.util.LoadingQueue;
import com.jme.math.FastMath;
import com.jme.math.Vector3f;

public class RushSpiral extends AttackObject {
	
	public static final String filename = "dark-rush";
	protected static final float dpf = 20;
	
	private final float pitchInc = FastMath.PI/18;
	
	private Model user;
	
	private ArrayList<Model> hitList = new ArrayList<Model>();
	
	private boolean done = false;
	
	public RushSpiral(Model m) {
    	super(filename, dpf, m);
    	flinch = true;
    	user = m;
    }
	
	public RushSpiral(Model[] m) {
    	super(filename, dpf, m);
    	flinch = true;
    	user = m[m.length-1];
    }
	
	@Override
	public void act(StageGameState b) {	
        
		// stay with character
		Vector3f rotation = user.rotationVector;
		pitch += pitchInc;
		rotate(rotation);
		model.setLocalTranslation(user.model.getWorldTranslation().add(0, 3, 0));
		
		// check if a Character is hit with linear approximation
    	Model[] collisions = getLinearModelCollisions(b, velocity, .5f, ListFilter.Filter.Character, users);
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
        		// move character
        		c.model.setLocalTranslation(c.model.getLocalTranslation().add(user.getVelocity()));
        	}
        }
        
        // check for removal
        if (done)
        	b.removeModel(this);
    }
	
	// ends attack
	public void cancel() {
		done = true;
	}
	
}
