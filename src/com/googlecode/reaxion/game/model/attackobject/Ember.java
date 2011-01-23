package com.googlecode.reaxion.game.model.attackobject;

import com.googlecode.reaxion.game.model.Model;
import com.googlecode.reaxion.game.model.Model.Billboard;
import com.googlecode.reaxion.game.model.character.Character;
import com.googlecode.reaxion.game.state.StageGameState;
import com.googlecode.reaxion.game.util.LoadingQueue;
import com.jme.math.FastMath;
import com.jme.math.Vector3f;

public class Ember extends AttackObject {
	
	public static final String filename = "bound-sphere";
	protected static final int span = 240;
	protected static final float dpf = 4;
	
	private final Vector3f offset = new Vector3f(0, 3, 0);
	
	private final int riseTime = 20;
	private final int peakTime = 50;
	
	private float riseSpeed = .2f;
	private float speed = 1f;
	
	public Model target;
	
	private Model glow;
	
	public Ember(Model m) {
    	super(filename, dpf, m);
    	flinch = true;
    	lifespan = span;
    	billboarding = Billboard.Free;
    }
	
	public Ember(Model[] m) {
    	super(filename, dpf, m);
    	flinch = true;
    	lifespan = span;
    	billboarding = Billboard.Free;
    }
	
	public void setUpGlow(StageGameState b) {
		glow = LoadingQueue.quickLoad(new Model("ember"), null);
		model.attachChild(glow.model);
		b.getRootNode().updateRenderState();
	}
	
	@Override
	public void hit(StageGameState b, Character other) {
		finish(b);
    }
	
	@ Override
    public void act(StageGameState b) {
		// set up glow if not already done
		if (glow == null)
			setUpGlow(b);
		
		if (lifeCount < riseTime) {
			// rise
			velocity = new Vector3f(0, riseSpeed, 0);
		} else if (lifeCount == riseTime) {
			// fly away
			Vector3f pos = users.get(users.size()-1).model.getLocalTranslation();
			velocity = new Vector3f(model.getLocalTranslation().subtract(pos).normalize().mult(speed));
		} else {
			// seek the opponent, with decaying accuracy
			Vector3f vel = target.model.getLocalTranslation().add(offset).subtract(model.getLocalTranslation()).normalize().mult(speed);
			float f = 1 - Math.abs((float) (lifeCount - peakTime)/(lifespan - peakTime));
			velocity = velocity.add(vel.subtract(velocity).mult(f)).normalize().mult(speed);
		}
		
    	super.act(b);
    }
	
}
