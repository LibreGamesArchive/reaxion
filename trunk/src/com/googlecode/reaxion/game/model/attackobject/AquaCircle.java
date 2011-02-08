package com.googlecode.reaxion.game.model.attackobject;

import com.googlecode.reaxion.game.model.Model;
import com.googlecode.reaxion.game.model.character.Character;
import com.googlecode.reaxion.game.state.StageGameState;
import com.jme.math.FastMath;
import com.jme.math.Vector3f;

public class AquaCircle extends AttackObject {
	
	public static final String filename = "aqua-ring";
	protected static final int span = 800;
	protected static final double hpf = .08;
	
	private final int riseTime = 14;
	
	public AquaCircle(Model m) {
    	super(filename, 0, m);
    	lifespan = span;
    }
	
	public AquaCircle(Model[] m) {
    	super(filename, 0, m);
    	lifespan = span;
    }
	
	@ Override
    public void act(StageGameState b) {
		// try to heal the user
		Model[] collisions = getModelCollisions(b);
        for (Model c : collisions)
        	if (c instanceof Character && users.contains(c)) {
        		((Character)c).heal(b, hpf);
        		((Character)c).gauge = Math.min(((Character)c).gauge + ((Character)c).gaugeRate/5, ((Character)c).maxGauge);
        	}
		
        float s = 1;
		if (lifeCount < riseTime)
			s = (float)(lifeCount+1)/riseTime;
		else if (lifeCount > lifespan - riseTime)
			s = (float)(lifespan - lifeCount + 1)/riseTime;
		
		model.setLocalScale(new Vector3f(s, (FastMath.sin((float)lifeCount/10) + 1)/2*s, s));
		model.getLocalTranslation().y = .15f;
		
    	super.act(b);
    }
	
}
