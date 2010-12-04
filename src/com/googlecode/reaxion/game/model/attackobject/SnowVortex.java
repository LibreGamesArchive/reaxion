package com.googlecode.reaxion.game.model.attackobject;

import com.googlecode.reaxion.game.model.Model;
import com.googlecode.reaxion.game.state.StageGameState;
import com.googlecode.reaxion.game.util.LoadingQueue;
import com.jme.math.FastMath;
import com.jme.math.Vector3f;

public class SnowVortex extends AttackObject {
	
	public static final String filename = "snow-vortex";
	protected static final int span = 32;
	protected static final int fadeTime = 6;
	protected static final float dpf = .6f;
	
	private final float scaleFactor = 3f;
	
	private final float angleInc = FastMath.PI/16;
	
	private Model vortex;
	
	public SnowVortex(Model m) {
    	super(filename, dpf, m);
    	flinch = true;
    	lifespan = span;
    }
	
	public SnowVortex(Model[] m) {
    	super(filename, dpf, m);
    	flinch = true;
    	lifespan = span;
    }
	
	public void setUp(StageGameState b) {
		vortex = LoadingQueue.quickLoad(new Model("snow-vortex"), null);
		vortex.model.setLocalScale(new Vector3f(.5f, .5f, .67f));
		model.attachChild(vortex.model);
		b.getRootNode().updateRenderState();
	}
	
	@Override
	public void act(StageGameState b) {
		if (vortex == null)
			setUp(b);
		
		if (lifeCount < fadeTime)
			model.setLocalScale(scaleFactor*(float)(lifeCount)/(float)(fadeTime));
		else if (lifespan - lifeCount < fadeTime)
			model.setLocalScale(scaleFactor*(float)(lifespan - lifeCount)/(float)(fadeTime));
        
		vortex.pitch = (vortex.pitch + FastMath.PI*2 - 2*angleInc) % (FastMath.PI*2);
		vortex.rotate();
		pitch = (pitch + angleInc) % (FastMath.PI*2);
		rotate(rotationVector);
		
		super.act(b);
    }
	
	// ends attack
	public void cancel() {
		lifeCount = lifespan;
	}
	
}
