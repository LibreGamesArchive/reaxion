package com.googlecode.reaxion.game;

import java.util.ArrayList;
import java.util.Arrays;

import com.jme.math.Vector3f;
import com.jme.renderer.Camera;

/**
 * Any object involved as part of an attack should extend this class
 * @author Khoa Ha
 *
 */
public class AttackObject extends Model {
	
	/**
	 * Number of frames this object is supposed to exist for, by default it
	 * exists forever
	 */
	public int lifespan = -1;
	protected int lifeCount = 0;
	
	/**
	 * Create an attack object with filename fn, damage-per-frame dpf, and user
	 * set to a Model, m.
	 * 
	 * @param fn
	 * @param dpf
	 * @param m
	 */
	public AttackObject(String fn, float dpf, Model m) {
		super(fn);
		init();
		damagePerFrame = dpf;
    	users.add(m);
    }
	
	/**
	 * Create an attack object with filename fn, damage-per-frame dpf, and users
	 * set to an Array of Models, m.
	 * 
	 * @param fn
	 * @param dpf
	 * @param m
	 */
	public AttackObject(String fn, float dpf, Model[] m) {
		super(fn);
		init();
		damagePerFrame = dpf;
    	users.addAll(Arrays.asList(m));
    }
	
	@Override
    protected void init() {
		super.init();
		users = new ArrayList<Model>();
	}
	
	@ Override
    public void act(BattleGameState b) {
    	super.act(b);
        
        // check if a Character is hit with linear approximation
    	Model[] collisions = getLinearModelCollisions(b, velocity, .5f);
        for (Model c : collisions) {
        	if (c instanceof Character && !users.contains(c)) {
        		((Character)c).hit(b, this);
        		hit(b, ((Character)c));
        	}
        }
        
        // actually move
        Vector3f loc = model.getLocalTranslation();
        loc.addLocal(velocity);
        model.setLocalTranslation(loc);
        
        //check lifespan
        if (lifeCount == lifespan)
        	b.removeModel(this);
        lifeCount++;
        
    }
	
	/**
	 * Called when Character is hit, override to add functionality
	 */
    public void hit(BattleGameState b, Character other) {
    }
	
}
