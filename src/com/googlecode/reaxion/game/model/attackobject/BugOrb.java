package com.googlecode.reaxion.game.model.attackobject;

import com.googlecode.reaxion.game.model.Model;
import com.googlecode.reaxion.game.model.character.Character;
import com.googlecode.reaxion.game.state.StageGameState;
import com.jme.math.FastMath;
import com.jme.math.Vector3f;

public class BugOrb extends AttackObject {
	
	public static final String filename = "bug-orb";
	protected static final int span = 360;
	
	protected static final float dpf = 32;
	
	public float speed = 1;
	private boolean changed = false;
	private Vector3f backPoint;
	
	public BugOrb(Model m, Vector3f bP) {
    	super(filename, dpf, m);
    	set();
    	backPoint = bP.clone();
    }
	
	public BugOrb(Model[] m, Vector3f bP) {
    	super(filename, dpf, m);
    	set();
    	backPoint = bP.clone();
    }
	
	private void set() {
		name = "Bug Orb";
    	trackable = true;
    	flinch = true;
    	lifespan = span;
    	gravitate = true;
    	gravity = -.06f;
	}
	
	@Override
	public void hit(StageGameState b, Character other) {
		b.removeModel(this);
    }
	
	@ Override
    public void act(StageGameState b) {
		
		// check if a hit by another attack with linear approximation
    	Model[] collisions = getLinearModelCollisions(b, velocity, .5f);
        for (Model c : collisions) {
        	if (c instanceof AttackObject) {
        		boolean flag = false;
        		// can't touch other circle cards
        		if (c instanceof CircleCard) {
        			flag = true;
        		} else {
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
        		}
        		// turn around
        		if (flag && !changed) {
        			((AttackObject)c).hit(b, (Character)users.get(users.size()-1));
        			users = c.users;
        			changed = true;
        			velocity = backPoint.subtract(model.getLocalTranslation()).normalize().mult(speed);
        			gravitate = false;
        		}
        		
        	// check for character hits
        	} else if (c instanceof Character && !users.contains(c))
        		((Character)c).hit(b, this);
        }
		
		// apply gravity
        if (gravitate) {
        	velocity.y += gravVel;
        	if (model.getLocalTranslation().y > 0)
        		gravVel += gravity;
        	contactGround();
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
	
}
