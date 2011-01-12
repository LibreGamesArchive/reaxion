package com.googlecode.reaxion.game.model.attackobject;

import com.googlecode.reaxion.game.model.Model;
import com.googlecode.reaxion.game.state.StageGameState;
import com.googlecode.reaxion.game.util.LoadingQueue;
import com.jme.math.FastMath;

public class SoundBomb extends AttackObject {
	
	public static final String filename = "bound-sphere";
	protected static final int span = 24;
	protected static final float dpf = 8f;
	
	private float angleInc = FastMath.PI/15;
	
	private Model[] sound = new Model[3];
	
	public SoundBomb(Model m) {
    	super(filename, dpf, m);
    	flinch = true;
    	lifespan = span;
    }
	
	public SoundBomb(Model[] m) {
    	super(filename, dpf, m);
    	flinch = true;
    	lifespan = span;
    }
	
	public void setUp(StageGameState b) {
		for (int i=0; i<sound.length; i++) {
			sound[i] = LoadingQueue.quickLoad(new Model("sound-pulse"), null);
			sound[i].model.setLocalScale(FastMath.pow(.8f, i));
		}
		sound[2].yaw = FastMath.PI/2;
		sound[1].roll = FastMath.PI/2;
		sound[1].model.attachChild(sound[2].model);
		sound[0].model.attachChild(sound[1].model);
		model.attachChild(sound[0].model);
		b.getRootNode().updateRenderState();
	}
	
	@ Override
    public void act(StageGameState b) {
		if (sound[0] == null)
			setUp(b);
		
		model.setLocalScale(2.5f * FastMath.abs(FastMath.sin((float)lifeCount*20/FastMath.PI/(lifespan/2) )));
		
		sound[0].roll += angleInc;
		sound[0].rotate();
		sound[1].pitch += angleInc;
		sound[1].rotate();
		sound[2].yaw += angleInc;
		sound[2].rotate();
		
    	super.act(b);
    }
	
}
