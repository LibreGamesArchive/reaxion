package com.googlecode.reaxion.game.model.attackobject;

import com.googlecode.reaxion.game.model.Model;
import com.googlecode.reaxion.game.model.character.Character;
import com.googlecode.reaxion.game.state.StageGameState;
import com.googlecode.reaxion.game.util.LoadingQueue;

public class Pellet extends AttackObject {
	
	public static final String filename = "bound-sphere";
	protected static final int span = 180;
	protected static final float dpf = 3;
	
	private Model bullet;
	
	public Pellet(Model m) {
    	super(filename, dpf, m);
    	flinch = true;
    	lifespan = span;
    }
	
	public Pellet(Model[] m) {
    	super(filename, dpf, m);
    	flinch = true;
    	lifespan = span;
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
		
		// billboard
		bullet.billboard(b.getCamera(), true);
		
    	super.act(b);
    }
	
}
