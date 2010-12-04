package com.googlecode.reaxion.game.model.enemies;

import java.util.ArrayList;
import java.util.Arrays;

import com.googlecode.reaxion.game.model.Model;
import com.googlecode.reaxion.game.model.attackobject.AttackObject;
import com.googlecode.reaxion.game.model.attackobject.CircleCard;
import com.googlecode.reaxion.game.model.character.Character;
import com.googlecode.reaxion.game.state.StageGameState;
import com.jme.math.Vector3f;

/**
 * Physical attack prop for {@code Bastion}.
 */
public class BastionShield extends Character {

	public int lifespan = 180;
	protected int lifeCount = 0;
	
	private final int riseTime = 12;
	
	private final int stallTime = 24;
	private final float speed = .1f;
	private final float knockback = 1;
	private final float dist = 6;
	
	private Vector3f point;
	private int stallCount = 0;
	
	public BastionShield(Model[] m, Vector3f pnt) {
    	// Load model
    	super("bastion");
    	users = new ArrayList<Model>();
    	users.addAll(Arrays.asList(m));
    	point = pnt.clone();
    	trackable = false;
    	mass = 2;
		boundRadius = 3f;
		boundHeight = 7f;
    }

	@ Override
	public void act(StageGameState b) {
		// set initials
		if (lifeCount == 0) {
	    	velocity = point.subtract(model.getLocalTranslation()).mult(new Vector3f(1, 0, 1)).normalize().mult(speed);
	    	rotate(velocity);
		}
        
        // check if a hit by another attack with linear approximation
    	Model[] collisions = getModelCollisions(b);
        for (Model c : collisions) {
        	if (c instanceof AttackObject) {
        		boolean flag = false;
        		// check if users include the other attack's users
        		for (Model u : users) {
        			boolean flag2 = false;
        			for (Model o : c.users)
        				if (u == o) {
        					flag2 = true;
        					break;
        				}
        			if (!flag2) {
        				flag = true;
        				break;
        			}
        		}
        		// stall if hit
        		if (flag) {
        			((AttackObject)c).hit(b, (Character)users.get(users.size()-1));
        			stallCount = stallTime;
        			model.setLocalTranslation(model.getLocalTranslation().add(velocity.normalize().negate().mult(knockback)));
        			velocity = new Vector3f();
        		}
        	}
        }
        
        // check distance
        if (point.distance(model.getLocalTranslation()) < dist)
        	velocity = new Vector3f();
        
        // rise or sink
        if (lifeCount < riseTime)
        	velocity.y = 7f/(float)riseTime;
        else if (lifespan - lifeCount < riseTime)
        	velocity.y = -7f/(float)riseTime;
        else
        	velocity.y = 0;
        
		// actually move
		Vector3f loc = model.getLocalTranslation();
		loc.addLocal(velocity);
		model.setLocalTranslation(loc);
		
		// change stall count
		if (stallCount > 0) {
			stallCount--;
			if (stallCount == 0)
				velocity = point.subtract(model.getLocalTranslation()).mult(new Vector3f(1, 0, 1)).normalize().mult(speed);
		}
		
		//check lifespan
        if (lifeCount == lifespan)
        	b.removeModel(this);
        lifeCount++;
	}

}
