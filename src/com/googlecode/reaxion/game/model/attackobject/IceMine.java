package com.googlecode.reaxion.game.model.attackobject;

import com.googlecode.reaxion.game.model.Model;
import com.googlecode.reaxion.game.model.character.Character;
import com.googlecode.reaxion.game.model.prop.IceTrail;
import com.googlecode.reaxion.game.state.StageGameState;
import com.googlecode.reaxion.game.util.LoadingQueue;

public class IceMine extends AttackObject {
	
	public static final String filename = "bound-sphere";
	protected static final int span = 480;
	protected static final float dpf = 22;
	
	public IceMine(Model m) {
    	super(filename, dpf, m);
    	flinch = true;
    	lifespan = span;
    }
	
	public IceMine(Model[] m) {
    	super(filename, dpf, m);
    	flinch = true;
    	lifespan = span;
    }
	
	@Override
	public void hit(StageGameState b, Character other) {
		finish(b);
    }
	
	@ Override
    public void act(StageGameState b) {
		// set scale
		model.setLocalScale(2);
		
		// create ice trail
		if (lifeCount % 12 == 0) {
			IceTrail f = (IceTrail)LoadingQueue.quickLoad(new IceTrail(40, .05f), b);
			f.model.setLocalTranslation(model.getWorldTranslation().clone());
			b.getRootNode().updateRenderState();
		}
		
    	super.act(b);
    }
	
}
