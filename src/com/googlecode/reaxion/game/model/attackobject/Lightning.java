package com.googlecode.reaxion.game.model.attackobject;

import java.util.ArrayList;

import com.googlecode.reaxion.game.model.Model;
import com.googlecode.reaxion.game.model.Model.Billboard;
import com.googlecode.reaxion.game.model.character.Character;
import com.googlecode.reaxion.game.state.StageGameState;
import com.jme.math.Vector3f;

public class Lightning extends AttackObject {
	
	public static final String filename = "lightning";
	protected static final int span = 2;
	protected static final float dpf = 12;
	
	private ArrayList<Model> hitList = new ArrayList<Model>();
	
	public Lightning(Model m) {
    	super(filename, dpf, m);
    	flinch = true;
    	lifespan = span;
    	billboarding = Billboard.YLocked;
    }
	
	public Lightning(Model[] m) {
    	super(filename, dpf, m);
    	flinch = true;
    	lifespan = span;
    	billboarding = Billboard.YLocked;
    }
	
	@Override
	public void hit(StageGameState b, Character other) {
		finish(b);
    }
	
	@ Override
    public void act(StageGameState b) {
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
	
}
