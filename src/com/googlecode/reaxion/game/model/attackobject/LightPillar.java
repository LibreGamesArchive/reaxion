package com.googlecode.reaxion.game.model.attackobject;

import com.googlecode.reaxion.game.model.Model;
import com.googlecode.reaxion.game.model.character.Character;
import com.googlecode.reaxion.game.state.StageGameState;
import com.googlecode.reaxion.game.util.LoadingQueue;
import com.jme.math.FastMath;
import com.jme.math.Vector3f;

public class LightPillar extends AttackObject {
	
	public static final String filename = "light-pillar";
	protected static final int span = 180;
	protected static final int growTime = 10;
	protected static final float dpf = 12;
	
	private float radius = 3f;
	private float dr = .24f;
	
	public float theta = 0;
	private float dw = FastMath.PI/60;
	
	private Vector3f center;
	
	public LightPillar(Model m) {
    	super(filename, dpf, m);
    	flinch = true;
    	lifespan = span;
    	center = m.model.getWorldTranslation().clone();
    }
	
	public LightPillar(Model[] m) {
    	super(filename, dpf, m);
    	flinch = true;
    	lifespan = span;
    	center = m[m.length - 1].model.getWorldTranslation().clone();
    }
	
	@Override
	public void hit(StageGameState b, Character other) {
		b.removeModel(this);
    }
	
	@ Override
    public void act(StageGameState b) {
		
		// revolve outwards
		radius += dr;
		model.setLocalTranslation(new Vector3f(center.x + radius*FastMath.cos(theta), model.getLocalTranslation().y, center.z + radius*FastMath.sin(theta)));
		theta = (theta + dw)%(FastMath.PI*2);
		
    	super.act(b);
    }
	
}
