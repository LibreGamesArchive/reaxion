package com.googlecode.reaxion.game.model.attackobject;

import com.googlecode.reaxion.game.model.Model;
import com.googlecode.reaxion.game.model.character.Character;
import com.googlecode.reaxion.game.state.StageGameState;
import com.googlecode.reaxion.game.util.LoadingQueue;
import com.jme.math.FastMath;
import com.jme.math.Vector3f;

public class MagneticField extends AttackObject {
	
	public static final String filename = "magnetic-field";
	protected static final int span = 300;
	protected static final float dpf = 1.3f;
	
	private float rollInc = FastMath.PI/40;
	
	public MagneticField(Model m) {
    	super(filename, dpf, m);
    	flinch = true;
    	lifespan = span;
    }
	
	public MagneticField(Model[] m) {
    	super(filename, dpf, m);
    	flinch = true;
    	lifespan = span;
    }
	
	@ Override
    public void act(StageGameState b) {
		float s = (float) 2.5f * (FastMath.sin((float)lifeCount/10)/2 + 1);
		model.setLocalScale(new Vector3f(s, 1, s));
		
		roll = (roll + rollInc) % (2*FastMath.PI);
		rotate();
		
    	super.act(b);
    }
	
}
