package com.googlecode.reaxion.game.model.attackobject;

import com.googlecode.reaxion.game.model.Model;
import com.googlecode.reaxion.game.model.character.Character;
import com.googlecode.reaxion.game.state.StageGameState;
import com.googlecode.reaxion.game.util.LoadingQueue;
import com.jme.math.FastMath;
import com.jme.math.Vector3f;

public class PinkWave extends AttackObject {
	
	public static final String filename = "psywave";
	protected static final int span = 120;
	protected static final float dpf = 14;
	
	private int maxRadius = 28;
	
	public PinkWave(Model m) {
    	super(filename, dpf, m);
    	flinch = true;
    	lifespan = span;
    }
	
	public PinkWave(Model[] m) {
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
		float s = (float) maxRadius * (lifeCount + 1)/(lifespan + 1);
		model.setLocalScale(new Vector3f(s, 1, s));
		
    	super.act(b);
    }
	
}
