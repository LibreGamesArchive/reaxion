package com.googlecode.reaxion.game.model.attackobject;

import java.util.ArrayList;

import com.googlecode.reaxion.game.model.Model;
import com.googlecode.reaxion.game.model.character.Character;
import com.googlecode.reaxion.game.state.StageGameState;
import com.googlecode.reaxion.game.util.ListFilter;
import com.jme.math.FastMath;
import com.jme.math.Vector3f;

public class Echo extends AttackObject {
	
	public static final String filename = "echo";
	protected static final int span = 480;
	protected static final float dpf = 8;
	
	public final float speed = 3;
	
	private final double wait = .05;
	private int combo = 5;
	
	private ArrayList<Model> hitList = new ArrayList<Model>();
	private ArrayList<Double> hitTime = new ArrayList<Double>();
	
	private Character user;
	private Model target;
	
	public Echo(Model m, Model t) {
    	super(filename, dpf, m);
    	flinch = true;
    	lifespan = span;
    	user = (Character)m;
    	target = t;
    }
	
	public Echo(Model[] m, Model t) {
    	super(filename, dpf, m);
    	flinch = true;
    	lifespan = span;
    	user = (Character)m[m.length-1];
    	target = t;
    }
	
	@Override
	public void hit(StageGameState b, Character other) {
		
    }
	
	@ Override
    public void act(StageGameState b) {
        
		// check if a Character is hit with linear approximation
    	Model[] collisions = getLinearModelCollisions(b, velocity, .5f, ListFilter.Filter.Character, users);
        for (Model c : collisions) {
        	if (c == user) {
        		if (!hitList.contains(user)) {
        			// restore gauge
        			user.gauge = Math.min(user.maxGauge, user.gauge + 8);
        			hitList.add(user);
        			hitTime.add(b.getTotalTime());

        			// bounce
        			Vector3f to = target.model.getLocalTranslation().subtract(user.model.getLocalTranslation()).mult(new Vector3f(1, 0, 1)).normalize();
        			if (to.angleBetween(velocity.mult(new Vector3f(-1, 0, -1)).normalize()) <= FastMath.PI/4)
        				velocity = to.mult(speed);
        			else
        				velocity = velocity.mult(-1);
        			rotate(velocity);
        			
        			// lower combo
        			combo--;
        			if (combo <= 0)
        				finish(b);
        		}
        	} else if (!users.contains(c)) {
        		// only deal damage once, if not hit before
        		if (!hitList.contains(c)) {
	        		hitList.add(c);
	        		hitTime.add(b.getTotalTime());
	        		damagePerFrame = dpf;
	        		
	        		if (c instanceof Character)
	        			((Character)c).hit(b, this);
	        		
	        		// bounce
	        		Vector3f to = user.model.getLocalTranslation().subtract(c.model.getLocalTranslation()).mult(new Vector3f(1, 0, 1)).normalize();
	        		if (to.angleBetween(velocity.mult(new Vector3f(-1, 0, -1)).normalize()) <= FastMath.PI/4)
	        			velocity = to.mult(speed);
	        		else
	        			velocity = velocity.mult(-1);
	        		rotate(velocity);
	        		
	        		// lower combo
	    			combo--;
	    			if (combo <= 0)
	    				finish(b);
	    			
        		} else {
        			damagePerFrame = 0;
        		}
        	}
        	
        	// clear list if stale entries
        	for (int i=0; i<hitList.size(); i++) {
        		if (b.getTotalTime() >= hitTime.get(i)+wait) {
        			hitList.remove(i);
        			hitTime.remove(i);
        			i--;
        		} else {
        			break;
        		}
        	}
        }
        
        // actually move
        Vector3f loc = model.getLocalTranslation();
        loc.addLocal(velocity);
        model.setLocalTranslation(loc);
        
        //check lifespan
        if (lifeCount == lifespan)
        	finish(b);
        lifeCount++;
    }
	
}
