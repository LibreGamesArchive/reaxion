package com.googlecode.reaxion.game.model.attackobject;

import com.googlecode.reaxion.game.model.Model;
import com.googlecode.reaxion.game.model.character.Character;
import com.googlecode.reaxion.game.state.StageGameState;
import com.jme.math.FastMath;
import com.jme.math.Vector3f;

public class CircleCard extends AttackObject {
	
	public static final String filename = "card";
	protected static final float dpf = 0;
	
	private final float radius = 3.5f;
	private final float dtheta = FastMath.PI/18;
	
	private Model target;
	
	public float theta;
	
	public CircleCard(Model m, Model t) {
    	super(filename, dpf, m);
    	target = t;
    	flinch = false;
    }
	
	public CircleCard(Model[] m, Model t) {
    	super(filename, dpf, m);
    	target = t;
    	flinch = false;
    }
	
	@Override
	public void hit(StageGameState b, Character other) {
		
    }
	
	@ Override
    public void act(StageGameState b) {
        
        // check if a hit by another attack with linear approximation
    	Model[] collisions = getModelCollisions(b);
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
        		// kill this card
        		if (flag) {
        			((AttackObject)c).hit(b, (Character)users.get(users.size()-1));
        			finish(b);
        		}
        	}
        }
        
        // rotate
        rotate(new Vector3f(FastMath.cos(theta), 0, FastMath.sin(theta)));
        
        // actually move
        model.setLocalTranslation(target.model.getLocalTranslation().add(new Vector3f(radius*FastMath.cos(theta), 0, radius*FastMath.sin(theta))));
        theta = (theta + dtheta) % (FastMath.PI*2);
        
    }
	
}
