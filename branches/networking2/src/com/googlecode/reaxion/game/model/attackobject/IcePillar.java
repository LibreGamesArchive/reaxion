package com.googlecode.reaxion.game.model.attackobject;

import java.util.ArrayList;

import com.googlecode.reaxion.game.model.Model;
import com.googlecode.reaxion.game.model.character.Character;
import com.googlecode.reaxion.game.state.StageGameState;
import com.googlecode.reaxion.game.util.ListFilter;
import com.jme.math.FastMath;
import com.jme.math.Vector3f;

public class IcePillar extends AttackObject {
	
	public static final String filename = "ice-pillar";
	protected static final int span = 280;
	protected static final int riseTime = 12;
	protected static final float dpf = .06f;
	
	private ArrayList<Character> captured = new ArrayList<Character>();
	private ArrayList<Integer> masses = new ArrayList<Integer>();
	
	public IcePillar(Model m) {
    	super(filename, dpf, m);
    	flinch = true;
    	lifespan = (int)(FastMath.nextRandomFloat()*span);
    	checkTriangles = false;
    }
	
	public IcePillar(Model[] m) {
    	super(filename, dpf, m);
    	flinch = true;
    	lifespan = (int)(FastMath.nextRandomFloat()*span);
    	checkTriangles = false;
    }
	
	@ Override
    public void act(StageGameState b) {
		// check if a Character is hit with linear approximation
    	Model[] collisions = getLinearModelCollisions(b, velocity, .5f, ListFilter.Filter.Character, users);
        for (Model c : collisions) {
        	// can't touch other ice pillars
            if (c instanceof IcePillar && ((IcePillar) c).lifeCount > this.lifeCount)
            	finish(b);
            else if (c instanceof Character && !users.contains(c) && !captured.contains(c)) {
        		if (((Character)c).hit(b, this)) {
        			captured.add((Character)c);
        			masses.add(new Integer(((Character)c).mass));
        		}
        	}
        }
        
        // paralyze character
        for (int i=0; i<captured.size(); i++) {	
        	// hit the character
        	captured.get(i).hit(b, this);
        	
        	// move to center
        	captured.get(i).model.getLocalTranslation().x = model.getWorldTranslation().x;
        	captured.get(i).model.getLocalTranslation().z = model.getWorldTranslation().z;
        	
        	captured.get(i).moveLock = true;
        	captured.get(i).jumpLock = true;
        	captured.get(i).flinching = true;
        	captured.get(i).tagLock = true;
        	captured.get(i).mass = 3;
        }
        
        // rise
        if (lifeCount < riseTime)
        	velocity.y = (float) 8/riseTime;
        else if (lifeCount == riseTime)
        	velocity.y = 0;
        
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
