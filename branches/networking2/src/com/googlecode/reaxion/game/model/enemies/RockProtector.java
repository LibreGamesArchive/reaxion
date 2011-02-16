package com.googlecode.reaxion.game.model.enemies;

import java.util.ArrayList;
import com.googlecode.reaxion.game.model.Model;
import com.googlecode.reaxion.game.model.character.Character;
import com.googlecode.reaxion.game.state.StageGameState;
import com.jme.math.FastMath;
import com.jme.math.Vector3f;

/**
 * Orbits Pyroclast and absorbs hits.
 */
public class RockProtector extends Character {
	
	private final float radius = 24;
	private final float angleInc = FastMath.PI/90;
	private final float pitchInc = FastMath.PI/25;
	
	private Pyroclast user;
	private Vector3f center;
	private float angle;
	
	public RockProtector(ArrayList<Model> m, Pyroclast u, float a) {
    	// Load model
    	super("meteor");
    	hp = 30;
    	user = u;
    	users = m;
    	trackable = false;
    	mass = 4;
    	
    	center = user.model.getWorldTranslation().mult(new Vector3f(1, 0, 1));
    	angle = a;
    }
	
	@ Override
    public void act(StageGameState b) {		
		// orbit
		model.setLocalTranslation(radius*FastMath.sin(angle), 4, radius*FastMath.cos(angle));
		angle = (angle + angleInc) % (2*FastMath.PI);
		
		// rotate
		pitch = (pitch + pitchInc) % (2*FastMath.PI);
		rotate(center.subtract(model.getWorldTranslation().mult(new Vector3f(1, 0, 1))));
		
		// check life
		if (hp <= 0) {
			b.removeModel(this);
			user.numRocks--;
		}
    	
    }
	
}
