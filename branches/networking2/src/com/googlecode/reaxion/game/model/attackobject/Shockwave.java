package com.googlecode.reaxion.game.model.attackobject;

import com.googlecode.reaxion.game.model.Model;
import com.googlecode.reaxion.game.model.character.Character;
import com.googlecode.reaxion.game.state.StageGameState;
import com.googlecode.reaxion.game.util.LoadingQueue;
import com.jme.math.FastMath;
import com.jme.math.Vector3f;

public class Shockwave extends AttackObject {
	
	public static final String filename = "shockwave";
	protected static final int fallTime = 15;
	
	private int maxRadius;
	
	public Shockwave(Model m) {
    	this(m, 10, 100, 10);
    }
	
	public Shockwave(Model m, int radius, int time, float dpf) {
    	super(filename, dpf, m);
    	maxRadius = radius;
    	lifespan = time + fallTime;
    }
	
	public Shockwave(Model[] m) {
		this(m, 10, 100, 10);
    }
	
	public Shockwave(Model[] m, int radius, int time, float dpf) {
    	super(filename, dpf, m);
    	maxRadius = radius;
    	lifespan = time + fallTime;
    }
	
	@Override
	public void hit(StageGameState b, Character other) {
		finish(b);
    }
	
	@ Override
    public void act(StageGameState b) {
		if (lifeCount < lifespan - fallTime) {
			float s = (float) maxRadius * (lifeCount + 1)/(lifespan - fallTime + 1);
			model.setLocalScale(new Vector3f(s, 1, s));
		} else
			model.setLocalTranslation(model.getLocalTranslation().add(new Vector3f(0,(float) -1.5f / fallTime, 0)));
		
    	super.act(b);
    }
	
}
