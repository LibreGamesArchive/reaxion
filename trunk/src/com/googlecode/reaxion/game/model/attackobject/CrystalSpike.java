package com.googlecode.reaxion.game.model.attackobject;

import com.googlecode.reaxion.game.model.Model;
import com.googlecode.reaxion.game.model.character.Character;
import com.googlecode.reaxion.game.state.StageGameState;
import com.jme.math.Vector3f;

public class CrystalSpike extends AttackObject {
	
	public static final String filename = "crystal-spike";
	protected static final int span = 300;
	protected static final float dpf = 13;
	
	private final int upTime = 8;
	private final int downTime = 284;
	
	public CrystalSpike(Model m) {
    	super(filename, dpf, m);
    	flinch = true;
    	lifespan = span;
    }
	
	public CrystalSpike(Model[] m) {
    	super(filename, dpf, m);
    	flinch = true;
    	lifespan = span;
    }
	
	@Override
	public void hit(StageGameState b, Character other) {
		b.removeModel(this);
    }
	
	@ Override
    public void act(StageGameState b) {
		if (lifeCount < upTime)
			velocity = new Vector3f(0, 13f/(float)upTime, 0);
		else if (lifeCount >= downTime)
			velocity = new Vector3f(0, -13f/(float)(span - downTime), 0);
		else
			velocity = new Vector3f();
		
    	super.act(b);
    }
	
}
