package com.googlecode.reaxion.game.model.attackobject;

import java.util.ArrayList;

import com.googlecode.reaxion.game.model.Model;
import com.googlecode.reaxion.game.model.character.Character;
import com.googlecode.reaxion.game.state.StageGameState;
import com.googlecode.reaxion.game.util.ListFilter;
import com.jme.math.FastMath;
import com.jme.math.Vector3f;

public class Chain extends AttackObject {
	
	public static final String filename = "chain";
	protected static final int span = 320;
	protected static final float dpf = 0;
	
	public final int offset = 32;
	private float speed = 3f;
	private float angle;
	
	private ArrayList<Character> captured = new ArrayList<Character>();
	private ArrayList<Integer> masses = new ArrayList<Integer>();
	
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
    	Model[] collisions = getLinearModelCollisions(b, velocity, .5f, ListFilter.Filter.Character, users);
        for (Model c : collisions) {
        	if (c instanceof Character && !users.contains(c) && !captured.contains(c)) {
        		if (((Character)c).hit(b, this)) {
        			captured.add((Character)c);
        			masses.add(new Integer(((Character)c).mass));
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
        	captured.get(i).mass = 3;
//        	if (lifeCount < offset/speed)
//        		captured.get(i).model.setLocalTranslation(captured.get(i).model.getLocalTranslation().add(
//        				new Vector3f(speed*FastMath.sin(angle), 0, speed*FastMath.cos(angle))));
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
	
	protected void finish(StageGameState b) {
		for (int i=0; i<captured.size(); i++) {
        	// free characters
        	captured.get(i).moveLock = false;
        	captured.get(i).jumpLock = false;
        	captured.get(i).flinching = false;
        	captured.get(i).tagLock = false;
        	captured.get(i).mass = masses.get(i);
        }
		
		super.finish(b);
	}
	
}
