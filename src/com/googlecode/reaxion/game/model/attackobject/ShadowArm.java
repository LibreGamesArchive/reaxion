package com.googlecode.reaxion.game.model.attackobject;

import java.util.ArrayList;

import com.googlecode.reaxion.game.model.Model;
import com.googlecode.reaxion.game.model.character.Character;
import com.googlecode.reaxion.game.state.StageGameState;
import com.googlecode.reaxion.game.util.ListFilter;
import com.googlecode.reaxion.game.util.LoadingQueue;
import com.jme.math.FastMath;
import com.jme.math.Vector3f;

public class ShadowArm extends AttackObject {
	
	public static final String filename = "dark-hand";
	protected static final int span = 40;
	protected static final float dpf = 14;
	
	private final int reach = 32;
	
	private Model arm;
	
	private ArrayList<Model> hitList = new ArrayList<Model>();
	
	public ShadowArm(Model m) {
    	super(filename, dpf, m);
    	flinch = true;
    	lifespan = span;
    }
	
	public ShadowArm(Model[] m) {
    	super(filename, dpf, m);
    	flinch = true;
    	lifespan = span;
    }
	
	public void setUpArm(StageGameState b) {
		arm = LoadingQueue.quickLoad(new Model("dark-arm"), null);
		model.attachChild(arm.model);
		b.getRootNode().updateRenderState();
	}
	
	@ Override
    public void act(StageGameState b) {
		// set up arm if not already done
		if (arm == null)
			setUpArm(b);
		
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
        		c.model.setLocalTranslation(c.model.getLocalTranslation().add(rotationVector.normalize().mult(2*(float)reach/lifespan)));
        	}
        }
		
		float s = (float) reach * (1f - Math.abs(2f*(float)(lifeCount + 1)/(lifespan + 1) - 1f));
		arm.model.setLocalScale(new Vector3f(1, 1, s));
		
		velocity = rotationVector.normalize().mult(2f*((lifeCount <= lifespan/2) ? 1 : -1)*(float)reach/lifespan);
		
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
