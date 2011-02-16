package com.googlecode.reaxion.game.model.attackobject;

import com.googlecode.reaxion.game.model.Model;
import com.googlecode.reaxion.game.model.Model.Billboard;
import com.googlecode.reaxion.game.model.character.Character;
import com.googlecode.reaxion.game.state.StageGameState;
import com.googlecode.reaxion.game.util.LoadingQueue;
import com.jme.math.FastMath;

public class Fire extends AttackObject {
	
	public static final String filename = "bound-sphere";
	protected static final int span = 30;
	protected static final float dpf = 4;
	
	private Model fire;
	
	public Fire(Model m) {
    	super(filename, dpf, m);
    	flinch = true;
    	lifespan = span;
    	billboarding = Billboard.Free;
    }
	
	public Fire(Model[] m) {
    	super(filename, dpf, m);
    	flinch = true;
    	lifespan = span;
    	billboarding = Billboard.Free;
    }
	
	private void setUpFire(StageGameState b) {
		fire = LoadingQueue.quickLoad(new Model("fire-trail"), null);
		fire.model.setLocalScale(.5f);
		model.attachChild(fire.model);
		model.setLocalScale(2);
		fire.pitch = FastMath.PI*2*FastMath.nextRandomFloat();
		b.getRootNode().updateRenderState();
	}
	
	@Override
	public void hit(StageGameState b, Character other) {
		finish(b);
    }
	
	@ Override
    public void act(StageGameState b) {
		// set up glow if not already done
		if (fire == null)
			setUpFire(b);
		
    	super.act(b);
    }
	
}
