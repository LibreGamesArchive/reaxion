package com.googlecode.reaxion.game.model.attackobject;

import java.util.ArrayList;

import com.googlecode.reaxion.game.model.Model;
import com.googlecode.reaxion.game.model.character.Character;
import com.googlecode.reaxion.game.state.StageGameState;
import com.jme.math.FastMath;
import com.jme.math.Vector3f;

public class Chain extends AttackObject {
	
	public static final String filename = "chain";
	protected static final int span = 320;
	protected static final float dpf = 0;
	
	public final int offset = 80;
	private float speed = 5f/3f;
	private float angle;
	
	private ArrayList<Character> captured = new ArrayList<Character>();
	
	public Chain(Model m, float a) {
    	super(filename, dpf, m);
    	flinch = true;
    	lifespan = span;
    	angle = a;
    }
	
	public Chain(Model[] m, float a) {
    	super(filename, dpf, m);
    	flinch = true;
    	lifespan = span;
    	angle = a;
    }
	
	@ Override
    public void act(StageGameState b) {
        
		// check if a Character is hit with linear approximation
    	Model[] collisions = getLinearModelCollisions(b, velocity, .5f);
        for (Model c : collisions) {
        	if (c instanceof Character && !users.contains(c) && !captured.contains(c)) {
        		if (((Character)c).hit(b, this)) {
        			captured.add((Character)c);
        		}
        	}
        }
        
        // move captive characters towards cross point
        for (int i=0; i<captured.size(); i++) {
        	// paralyze character
        	captured.get(i).moveLock = true;
        	captured.get(i).jumpLock = true;
        	captured.get(i).flinching = true;
        	captured.get(i).tagLock = true;
        	if (lifeCount < offset/speed)
        		captured.get(i).model.setLocalTranslation(captured.get(i).model.getLocalTranslation().add(
        				new Vector3f(speed*FastMath.sin(angle), 0, speed*FastMath.cos(angle))));
        }
        
        // advance
        if (lifeCount < offset/speed) {
        	velocity = new Vector3f(speed*FastMath.sin(angle), 0, speed*FastMath.cos(angle));
        } else {
        	if (captured.size() == 0)
        		finish(b);
        	else
        		velocity = new Vector3f();
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
	
	private void finish(StageGameState b) {
		for (int i=0; i<captured.size(); i++) {
        	// free characters
        	captured.get(i).moveLock = false;
        	captured.get(i).jumpLock = false;
        	captured.get(i).flinching = false;
        	captured.get(i).tagLock = false;
        }
		b.removeModel(this);
	}
	
}
