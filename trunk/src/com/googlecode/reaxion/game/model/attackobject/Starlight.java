package com.googlecode.reaxion.game.model.attackobject;

import java.util.ArrayList;

import com.googlecode.reaxion.game.model.Model;
import com.googlecode.reaxion.game.model.character.Character;
import com.googlecode.reaxion.game.state.StageGameState;
import com.googlecode.reaxion.game.util.LoadingQueue;
import com.jme.math.FastMath;
import com.jme.math.Vector3f;

public class Starlight extends AttackObject {
	
	public static final String filename = "bound-sphere";
	private final int chargeTime = 260;	
	public final float maxRadius = 32;
	
	private ArrayList<Model> hitList = new ArrayList<Model>();
	
	private Character user;
	private double initHp;
	
	private Model glow;
	
	public Starlight(Model m) {
    	super(filename, 0, m);
    	flinch = true;
    }
	
	public Starlight(Model[] m) {
    	super(filename, 0, m);
    	flinch = true;
    }
	
	public void setUpGlow(StageGameState b) {
		glow = LoadingQueue.quickLoad(new Model("oblivion"), b);
		b.removeModel(glow);
		model.attachChild(glow.model);
		b.getRootNode().updateRenderState();
	}
	
	@Override
	public void hit(StageGameState b, Character other) {
		b.removeModel(this);
    }
	
	@ Override
    public void act(StageGameState b) {
		// check if underground
		if (model.getLocalTranslation().y <= -maxRadius)
			b.removeModel(this);
		
		// count user's hp
		if (lifeCount == 0) {
			user = b.getPlayer();
			initHp = user.hp;
		}
		
		// set up glow if not already done
		if (glow == null)
			setUpGlow(b);
		
		// billboard glow
		glow.billboard(b.getCamera(), true);
		
		// grow with quadratic rate
		if (lifeCount <= chargeTime)
			model.setLocalScale(maxRadius * FastMath.sqrt((float) (lifeCount+1)/(chargeTime+1)));
		
		// check if a Character is hit with linear approximation
    	Model[] collisions = getLinearModelCollisions(b, velocity, .5f);
        for (Model c : collisions) {
        	if (c instanceof Character && !users.contains(c)) {
        		// only deal damage once, if not hit before
        		if (!hitList.contains(c)) {
	        		hitList.add(c);
	        		damagePerFrame = (float) ((Character)c).hp * getDamageMultiplier(b, ((Character)c));
        		} else {
        			damagePerFrame = 0;
        		}
        		((Character)c).hit(b, this);
        	}
        }
        
        // actually move
        Vector3f loc = model.getLocalTranslation();
        loc.addLocal(velocity);
        model.setLocalTranslation(loc);
        
        //increment lifeCount
        lifeCount++;
    }
	
	/**
	 * Determines the damage to inflict upon a Character c.
	 */
	private float getDamageMultiplier(StageGameState b, Character c) {
		// time elapsed accounts for up to 25%, peaking at 8 minutes
		// percent of Hp lost by the user is added to the proportion
		// difference between user's and target's Hp accounts for up to 25%
		// minimum multiplier is 25%
		return (float) (.25*Math.min(1, b.getTotalTime()/(8*60)) +
				(initHp - user.hp)/user.maxHp + 
				.25*Math.max(0, c.hp/c.maxHp - user.hp/user.maxHp) +
				.25);
	}
	
}
