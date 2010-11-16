package com.googlecode.reaxion.game.model.attackobject;

import com.googlecode.reaxion.game.model.Model;
import com.googlecode.reaxion.game.model.character.Character;
import com.googlecode.reaxion.game.state.StageGameState;

public class Fireball extends AttackObject {
	
	public static final String filename = "fireball";
	protected static final int span = 240;
	protected static final float dpf = 13;
	
	public int peakTime = 20;
	protected final float angleInc = .1745f; // approx. pi/18;
	
	public Fireball(Model m) {
    	super(filename, dpf, m);
    	flinch = true;
    	lifespan = span;
    }
	
	public Fireball(Model[] m) {
    	super(filename, dpf, m);
    	flinch = true;
    	lifespan = span;
    }
	
	@Override
	public void hit(StageGameState b, Character other) {
		b.removeModel(this);
    }
	
	@ Override
    public void act(StageGameState b) {
		// change size
		if (lifeCount < peakTime)
			model.setLocalScale((float) (lifeCount+1)/peakTime);
		else
			model.setLocalScale((float) (lifespan - lifeCount)/(lifespan - peakTime));
		
		// rotate
    	yaw -= angleInc;
    	rotate();
        
    	super.act(b);
    }
	
}
