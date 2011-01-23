package com.googlecode.reaxion.game.model.attackobject;

import com.googlecode.reaxion.game.model.Model;
import com.googlecode.reaxion.game.model.Model.Billboard;
import com.googlecode.reaxion.game.model.character.Character;
import com.googlecode.reaxion.game.state.StageGameState;
import com.googlecode.reaxion.game.util.LoadingQueue;
import com.jme.math.FastMath;
import com.jme.math.Vector3f;

public class PelletPulled extends AttackObject {
	
	public static final String filename = "bound-sphere";
	protected static final float dpf = 7;
	
	private final int growTime = 4;
	private final float speed = 3;
	
	private Vector3f dir;
	
	private Model bullet;
	
	public PelletPulled(Model m, Vector3f pos, Vector3f endPoint) {
    	super(filename, dpf, m);
    	flinch = true;
    	billboarding = Billboard.Free;
    	lifespan = (int)Math.floor(pos.distance(endPoint)/speed);
    	dir = endPoint.subtract(pos).normalize();
    }
	
	public PelletPulled(Model[] m, Vector3f pos, Vector3f endPoint) {
    	super(filename, dpf, m);
    	flinch = true;
    	billboarding = Billboard.Free;
    	lifespan = (int)Math.floor(pos.distance(endPoint)/speed);
    	dir = endPoint.subtract(pos).normalize();
    }
	
	public void setUpBullet(StageGameState b) {
		bullet = LoadingQueue.quickLoad(new Model("pellet"), null);
		model.attachChild(bullet.model);
		b.getRootNode().updateRenderState();
	}
	
	@Override
	public void hit(StageGameState b, Character other) {
		finish(b);
    }
	
	@ Override
    public void act(StageGameState b) {
		// set up glow if not already done
		if (bullet == null)
			setUpBullet(b);
		
		velocity = dir.mult(speed);
		
		// resize
		if (lifeCount <= growTime)
			model.setLocalScale((float) (lifeCount+1)/(growTime+1));
		else if (lifespan - lifeCount < growTime)
			model.setLocalScale((float) (lifespan-lifeCount+1)/(growTime+1));
		
    	super.act(b);
    }
	
}
