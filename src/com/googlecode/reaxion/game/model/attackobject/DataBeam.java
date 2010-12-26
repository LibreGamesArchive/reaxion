package com.googlecode.reaxion.game.model.attackobject;

import com.googlecode.reaxion.game.model.Model;
import com.googlecode.reaxion.game.model.character.Character;
import com.googlecode.reaxion.game.state.StageGameState;
import com.jme.math.FastMath;
import com.jme.math.Vector3f;

public class DataBeam extends AttackObject {
	
	public static final String filename = "byte_beam";
	protected static final int span = 240;
	protected static final float dpf = 3;
	
	private final int upTime = 12;
	
	public DataBeam(Model m) {
    	super(filename, dpf, m);
    	flinch = true;
    	lifespan = span;
    }
	
	public DataBeam(Model[] m) {
    	super(filename, dpf, m);
    	flinch = true;
    	lifespan = span;
    }
	
	@ Override
    public void act(StageGameState b) {
		if (lifeCount < upTime)
			velocity.y = (float)24/upTime;
		else if (lifespan - lifeCount < upTime)
			velocity.y = (float)-24/upTime;
		else {
			velocity.y = 0;
			velocity.x = .5f*FastMath.sin((float)lifeCount/7);
		}
		
    	super.act(b);
    }
	
}
