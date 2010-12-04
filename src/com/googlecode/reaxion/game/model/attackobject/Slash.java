package com.googlecode.reaxion.game.model.attackobject;

import java.util.ArrayList;

import com.googlecode.reaxion.game.model.Model;
import com.googlecode.reaxion.game.model.character.Character;
import com.googlecode.reaxion.game.state.StageGameState;
import com.googlecode.reaxion.game.util.LoadingQueue;
import com.jme.math.FastMath;
import com.jme.math.Vector3f;

public class Slash extends AttackObject {
	
	public static final String filename = "slash";
	protected static final int span = 8;
	protected static final int growTime = 2;
	protected static final float dpf = 18;
	
	private ArrayList<Model> hitList = new ArrayList<Model>();
	
	private Vector3f lastCharPos;
	
	public Slash(Model m) {
    	super(filename, dpf, m);
    	flinch = true;
    	lifespan = span;
    	lastCharPos = m.model.getWorldTranslation();
    }
	
	public Slash(Model[] m) {
    	super(filename, dpf, m);
    	flinch = true;
    	lifespan = span;
    	lastCharPos = m[m.length - 1].model.getWorldTranslation();
    }
	
	@Override
	public void act(StageGameState b) {
		if (lifeCount < growTime)
			model.setLocalScale((float)(lifeCount+1)/(growTime+1));
		else if (lifeCount > span - growTime)
			model.setLocalScale((float)(lifespan - lifeCount)/(growTime));
		
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
        
        // remain with Character
        model.setLocalTranslation( model.getLocalTranslation().add(users.get(users.size() - 1).model.getWorldTranslation().subtract(lastCharPos)) );
        lastCharPos = users.get(users.size() - 1).model.getWorldTranslation().clone();
        
        // actually move
        Vector3f loc = model.getLocalTranslation();
        loc.addLocal(velocity);
        model.setLocalTranslation(loc);
        
        //check lifespan
        if (lifeCount == lifespan)
        	b.removeModel(this);
        lifeCount++;
    }
	
	// ends attack
	public void cancel() {
		lifeCount = lifespan;
	}
	
}
