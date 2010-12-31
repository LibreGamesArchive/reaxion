package com.googlecode.reaxion.game.model.attackobject;

import com.googlecode.reaxion.game.model.Model;
import com.googlecode.reaxion.game.model.character.Character;
import com.googlecode.reaxion.game.state.StageGameState;
import com.jme.math.Vector3f;

public class LaserSpike extends AttackObject {
	
	public static final String filename = "red-spike";
	protected static final int span = 1500;
	protected static final float dpf = 14;
	
	private final int upTime = 4;
	
	public LaserSpike(Model m) {
    	super(filename, dpf, m);
    	flinch = true;
    	lifespan = span;
    }
	
	public LaserSpike(Model[] m) {
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
		if (lifeCount < upTime)
			velocity.y = (float)2/upTime;
		else if (lifespan - lifeCount < upTime)
			velocity.y = (float)-2/upTime;
		else
			velocity.y = 0;
		
    	super.act(b);
    }
	
}
