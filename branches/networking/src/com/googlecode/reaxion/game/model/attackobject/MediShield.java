package com.googlecode.reaxion.game.model.attackobject;

import com.googlecode.reaxion.game.model.Model;
import com.googlecode.reaxion.game.model.character.Character;
import com.googlecode.reaxion.game.state.StageGameState;
import com.jme.math.Vector3f;

public class MediShield extends AttackObject {
	
	public static final String filename = "mediguard";
	protected static final int span = 140;
	protected static final int fadePoint = 128;
	protected static final float dpf = 0;
	
	private final double hpf = .1;
	private Vector3f position;
	private boolean displaced = false;
	
	public MediShield(Model m) {
    	super(filename, dpf, m);
    	lifespan = span;
    	position = users.get(users.size()-1).model.getWorldTranslation().clone();
    }
	
	public MediShield(Model[] m) {
    	super(filename, dpf, m);
    	lifespan = span;
    	position = users.get(users.size()-1).model.getWorldTranslation().clone();
    }
	
	@Override
	public void act(StageGameState b) {
		// check if user moved or switched
		if (!displaced && (b.getPlayer() != users.get(users.size()-1) ||  !users.get(users.size()-1).model.getWorldTranslation().equals(position)))
			displaced = true;
		
		// heal if user hasn't moved
		if (!displaced)
			((Character)users.get(users.size()-1)).heal(b, hpf);
		
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
