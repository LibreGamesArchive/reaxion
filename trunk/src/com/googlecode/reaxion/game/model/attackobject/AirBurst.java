package com.googlecode.reaxion.game.model.attackobject;

import com.googlecode.reaxion.game.model.Model;
import com.googlecode.reaxion.game.model.character.Character;
import com.googlecode.reaxion.game.state.StageGameState;
import com.jme.math.FastMath;
import com.jme.math.Vector3f;

public class AirBurst extends AttackObject {
	
	public static final String filename = "airburst";
	protected static final int span = 180;
	protected static final float dpf = 18;
	
	private final int stopTime = 160;
	private final float maxSize = 6;
	protected final float angleInc = .1745f; // approx. pi/18;
	
	public AirBurst(Model m) {
    	super(filename, dpf, m);
    	flinch = true;
    	lifespan = span;
    }
	
	public AirBurst(Model[] m) {
    	super(filename, dpf, m);
    	flinch = true;
    	lifespan = span;
    }
	
	@Override
	public void hit(StageGameState b, Character other) {
		finish(b);
    }
	
	@ Override
    public void act(StageGameState b) {
		float peakPoint = (lifespan - stopTime)/2;
		
		// change size
		if (lifeCount >= stopTime) {
			velocity = new Vector3f();
			model.setLocalScale(maxSize*(1 - Math.abs((float)(lifeCount-(stopTime+peakPoint))/peakPoint)) + 1);
		}
		
		// rotate
    	yaw -= angleInc;
    	rotate(rotationVector);
        
    	super.act(b);
    }
	
}
