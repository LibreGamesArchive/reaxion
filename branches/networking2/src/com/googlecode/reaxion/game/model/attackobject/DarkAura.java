package com.googlecode.reaxion.game.model.attackobject;

import com.googlecode.reaxion.game.model.Model;
import com.googlecode.reaxion.game.model.character.Character;
import com.googlecode.reaxion.game.state.StageGameState;
import com.jme.math.FastMath;
import com.jme.math.Vector3f;

public class DarkAura extends AttackObject {
	
	public static final String filename = "dark-aura";
	protected static final int span = 240;
	protected static final float dpf = 0;
	
	private final float riseTime = 6;
	private final float rollInc = FastMath.PI/18;
	
	private Character user;
	
	public DarkAura(Model m) {
    	super(filename, dpf, m);
    	flinch = false;
    	lifespan = span;
    	user = (Character)m;
    }
	
	public DarkAura(Model[] m) {
    	super(filename, dpf, m);
    	flinch = false;
    	lifespan = span;
    	user = (Character)m[m.length-1];
    }
	
	@ Override
    public void act(StageGameState b) {
		
		// stay with character
		model.setLocalTranslation(user.model.getWorldTranslation());
        
		// scale
		float s = 1;
		if (lifeCount <= riseTime)
			s = (float) (lifeCount+1)/(riseTime+1);
		else if (lifespan - lifeCount <= riseTime)
			s = (float) (lifespan-lifeCount+1)/(riseTime+1);
		model.setLocalScale(new Vector3f(1, s, 1));
        
		// rotate
        roll += rollInc;
        rotate();
        
        super.act(b);
        
    }
	
	// ends attack
	public void cancel() {
		lifeCount = lifespan;
	}
	
}
