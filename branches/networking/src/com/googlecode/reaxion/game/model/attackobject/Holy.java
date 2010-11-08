package com.googlecode.reaxion.game.model.attackobject;

import com.googlecode.reaxion.game.model.Model;
import com.googlecode.reaxion.game.state.StageGameState;

public class Holy extends AttackObject {
	
	public static final String filename = "holy";
	protected static final int span = 240;
	protected static final float dpf = .1f;
	
	public int peakTime = 160;
	
	private final float angleInc = .1745f; // approx. pi/18;
	
	public Holy(Model m) {
    	super(filename, dpf, m);
    	lifespan = span;
    }
	
	public Holy(Model[] m) {
    	super(filename, dpf, m);
    	lifespan = span;
    }
	
	@Override
	public void act(StageGameState b) {
		
		if (lifeCount < peakTime)
			model.setLocalScale((float) (lifeCount+1)/(peakTime+1));
		else
			model.setLocalScale((float) (lifespan-lifeCount)/(lifespan-peakTime));
        
		/*
		roll += angleInc;
		rotate();
		*/
		
       super.act(b);
    }
	
	// ends attack
	public void cancel() {
		lifeCount = lifespan;
	}
	
}
