package com.googlecode.reaxion.game.model.attackobject;

import com.googlecode.reaxion.game.model.Model;
import com.googlecode.reaxion.game.state.StageGameState;
import com.googlecode.reaxion.game.util.LoadingQueue;
import com.jme.math.Vector3f;

public class CyberBeam extends AttackObject {
	
	public static final String filename = "cyber-beam";
	protected static final float dpf = .3f;
	
	private static final Vector3f offset = new Vector3f(0, 3, 0); // offset beam so it doesn't sit between the legs
	
	private Model from;
	private Vector3f hit;
	
	public CyberBeam(Model m, Model f, Vector3f h, int span) {
    	super(filename, dpf, m);
    	lifespan = span;
    	from = f;
    	hit = h.add(offset);
    }
	
	public CyberBeam(Model[] m, Model f, Vector3f h, int span) {
    	super(filename, dpf, m);
    	lifespan = span;
    	from = f;
    	hit = h.add(offset);
    }
	
	@ Override
    public void act(StageGameState b) {
		Vector3f fr = from.model.getWorldTranslation();
		model.setLocalScale(new Vector3f(1, 1, fr.distance(hit)));
		model.setLocalTranslation(fr.add(hit).divide(2));
		rotate(hit.subtract(fr));
		
    	super.act(b);
    }
	
}
