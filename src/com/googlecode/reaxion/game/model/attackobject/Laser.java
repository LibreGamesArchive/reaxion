package com.googlecode.reaxion.game.model.attackobject;

import com.googlecode.reaxion.game.model.Model;
import com.googlecode.reaxion.game.state.StageGameState;
import com.googlecode.reaxion.game.util.LoadingQueue;
import com.jme.math.Vector3f;

public class Laser extends AttackObject {
	
	public static final String filename = "laser";
	protected static final int span = 420;
	protected static final float dpf = .4f;
	
	private static final Vector3f offset = new Vector3f(-.3f, 0, 0); // offset beam so it doesn't sit between the legs
	
	private final int dropDist = 3;
	private final int dropTime = 12;	
	Vector3f lastDrop = new Vector3f();
	
	private Vector3f from;
	public Model target;
	private Vector3f hit;
	
	public Laser(Model m, Vector3f f, Vector3f h, Model t) {
    	super(filename, dpf, m);
    	lifespan = span;
    	from = f;
    	hit = h;
    	target = t;
    }
	
	public Laser(Model[] m, Vector3f f, Vector3f h, Model t) {
    	super(filename, dpf, m);
    	lifespan = span;
    	from = f;
    	hit = h;
    	target = t;
    }
	
	@ Override
    public void act(StageGameState b) {
		hit = hit.add(target.model.getLocalTranslation().add(offset).subtract(hit).mult(new Vector3f(1, 0, 1)).normalize().mult(.45f));
		model.setLocalScale(new Vector3f(1, 1, from.distance(hit)));
		model.setLocalTranslation(from.add(hit).divide(2));
		rotate(hit.subtract(from));
		if (lifeCount % dropTime == 0 && hit.distance(lastDrop) >= dropDist) {
			// drop a spike
			lastDrop = hit.clone();
			LaserSpike s = (LaserSpike)LoadingQueue.quickLoad(new LaserSpike(users.toArray(new Model[] {})), b);
			s.model.setLocalTranslation(hit.add(0, -2, 0));
			b.getRootNode().updateRenderState();
		}
		
    	super.act(b);
    }
	
}
