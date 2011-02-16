package com.googlecode.reaxion.game.model.attackobject;

import com.googlecode.reaxion.game.model.Model;
import com.googlecode.reaxion.game.model.Model.Billboard;
import com.googlecode.reaxion.game.model.character.Character;
import com.googlecode.reaxion.game.state.StageGameState;
import com.googlecode.reaxion.game.util.LoadingQueue;
import com.jme.math.FastMath;
import com.jme.math.Vector3f;

public class BeaconOrb extends AttackObject {
	
	public static final String filename = "bound-sphere";
	protected static final int span = 140;
	protected static final float dpf = 3;
	
	private float radius = 5;
	private final int strikeTime = 112;
	
	public float theta = 0;
	private float dw = FastMath.PI/16;
	
	public Model target;
	private Vector3f targetPos;
	
	private Model glow;
	
	public BeaconOrb(Model m) {
    	super(filename, dpf, m);
    	flinch = true;
    	lifespan = span;
    	billboarding = Billboard.Free;
    }
	
	public BeaconOrb(Model[] m) {
    	super(filename, dpf, m);
    	flinch = true;
    	lifespan = span;
    	billboarding = Billboard.Free;
    }
	
	public void setUpGlow(StageGameState b) {
		glow = LoadingQueue.quickLoad(new Model("beacon"), null);
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
		
		// revolve around target
		if (lifeCount > strikeTime)
			radius = (float)(span - lifeCount)/(span - strikeTime) * 2;
		
		// check if target is lost
		if (target != null) {
			targetPos = target.model.getLocalTranslation();
			Vector3f pos = model.getLocalTranslation();
			Vector3f center = new Vector3f(pos.x - radius*FastMath.cos(theta), pos.y, pos.z - radius*FastMath.sin(theta));
			if (center.distance(targetPos) > radius)
				target = null;
		}
		model.setLocalTranslation(new Vector3f(targetPos.x + radius*FastMath.cos(theta), model.getLocalTranslation().y, targetPos.z + radius*FastMath.sin(theta)));
		theta = (theta + dw)%(FastMath.PI*2);
		
    	super.act(b);
    }
	
}
