package com.googlecode.reaxion.game.model.attackobject;

import com.googlecode.reaxion.game.model.Model;
import com.googlecode.reaxion.game.state.StageGameState;
import com.jme.math.Vector3f;

public class Reflector extends AttackObject {
	
	public static final String filename = "reflega";
	protected static final int span = 100;
	protected static final int fadePoint = 88;
	protected static final float dpf = 0;
	
	public Reflector(Model m) {
    	super(filename, dpf, m);
    	lifespan = span;
    }
	
	public Reflector(Model[] m) {
    	super(filename, dpf, m);
    	lifespan = span;
    }
	
	@Override
	public void act(StageGameState b) {
		
		if (lifeCount >= fadePoint) {
			float factor = ((float)lifespan - lifeCount)/(lifespan - fadePoint);
			model.setLocalScale(new Vector3f(factor, factor, factor));
			model.setLocalTranslation(model.getLocalTranslation().add(new Vector3f(0, 2.75f/(lifespan - fadePoint), 0)));
		}
        
        //check lifespan
        if (lifeCount == lifespan)
        	b.removeModel(this);
        lifeCount++;
    }
	
	// ends attack
	public void cancel() {
		lifeCount = lifespan;
	}
	
}
