package com.googlecode.reaxion.game.model.attackobject;

import java.util.ArrayList;

import com.googlecode.reaxion.game.model.Model;
import com.googlecode.reaxion.game.model.character.Character;
import com.googlecode.reaxion.game.state.StageGameState;
import com.googlecode.reaxion.game.util.LoadingQueue;
import com.jme.math.FastMath;
import com.jme.math.Vector3f;

public class LightDrill extends AttackObject {
	
	public static final String filename = "light-drill";
	protected static final int span = 32;
	protected static final int fadePoint = 28;
	protected static final float dpf = .6f;
	
	private final float angleInc = FastMath.PI/8;
	
	private Model user;
	
	public boolean strike = false;
	
	public LightDrill(Model m) {
    	super(filename, dpf, m);
    	flinch = true;
    	lifespan = span;
    	user = m;
    }
	
	public LightDrill(Model[] m) {
    	super(filename, dpf, m);
    	flinch = true;
    	lifespan = span;
    	user = m[m.length-1];
    }
	
	@Override
	public void act(StageGameState b) {	
		if (lifeCount >= fadePoint)
			model.setLocalScale(((float)lifespan - lifeCount)/(float)(lifespan - fadePoint));
        
		// stay with character
		Vector3f rotation = user.rotationVector;
		Vector3f translation = rotation.normalize().mult(new Vector3f(2.4f, 0, 2.4f)).add(0, 4.2f, 0);
		pitch = (pitch + angleInc) % (FastMath.PI*2);
		rotate(rotation);
		model.setLocalTranslation(user.model.getWorldTranslation().add(translation));
		
		// check if a Character is hit with linear approximation
    	Model[] collisions = getLinearModelCollisions(b, velocity, .5f);
        for (Model c : collisions) {
        	if (c instanceof Character && !users.contains(c)) {
        		if (((Character)c).hit(b, this)) {
        			strike = true;
        			user.setVelocity(new Vector3f());
        			((Character)c).gauge = Math.max(0, ((Character)c).gauge - ((Character)c).gaugeRate);
        		}
        	}
        }
        
        //check lifespan
        if (lifeCount == lifespan)
        	b.removeModel(this);
        lifeCount++;
    }
	
	// ends attack
	public void cancel() {
		lifeCount = lifespan;
	}
	
}
