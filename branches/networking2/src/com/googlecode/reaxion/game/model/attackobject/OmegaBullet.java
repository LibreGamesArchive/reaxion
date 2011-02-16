package com.googlecode.reaxion.game.model.attackobject;

import com.googlecode.reaxion.game.model.Model;
import com.googlecode.reaxion.game.model.character.Character;
import com.googlecode.reaxion.game.state.StageGameState;
import com.jme.math.FastMath;

public class OmegaBullet extends AttackObject {
	
	public static final String filename = "bullet";
	protected static final int span = 80;
	protected static final float dpf = 20;
	
	protected final float maxSize = 7f;
	protected final float angleInc = FastMath.PI/8;
	private final float speed = 4f;
	
	public OmegaBullet(Model m) {
    	super(filename, dpf, m);
    	flinch = true;
    	lifespan = span;
    }
	
	public OmegaBullet(Model[] m) {
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
		float lifeFrac = 1 - FastMath.sqrt((float) lifeCount / lifespan);
		velocity = velocity.normalize().mult(speed*lifeFrac);
		model.setLocalScale(maxSize * (1 - lifeFrac));
        
    	super.act(b);
    }
	
}
