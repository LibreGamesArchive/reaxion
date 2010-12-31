package com.googlecode.reaxion.game.model.attackobject;

import java.util.ArrayList;
import java.util.Arrays;

import com.googlecode.reaxion.game.model.Model;
import com.googlecode.reaxion.game.model.character.Character;
import com.googlecode.reaxion.game.state.StageGameState;
import com.jme.math.Vector3f;

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
		if (m instanceof Character)
			damageMult = ((Character)m).strengthMult;
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
		if (m[m.length-1] instanceof Character)
			damageMult = ((Character)m[m.length-1]).strengthMult;
    	users.addAll(Arrays.asList(m));
    }
	
	@Override
    protected void init() {
		super.init();
		users = new ArrayList<Model>();
	}
	
	@ Override
    public void act(StageGameState b) {
    	super.act(b);
        
        // check if a Character is hit with linear approximation
    	Model[] collisions = getLinearModelCollisions(b, velocity, .5f);
        for (Model c : collisions) {
        	if (c instanceof Character && !users.contains(c))
        		((Character)c).hit(b, this);
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
		b.removeModel(this);
	}
	
	/**
	 * Called when Character is hit, override to add functionality
	 */
    public void hit(StageGameState b, Character other) {
    }
	
}
