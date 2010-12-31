package com.googlecode.reaxion.game.model.attackobject;

import com.googlecode.reaxion.game.model.Model;
import com.googlecode.reaxion.game.model.character.Character;
import com.googlecode.reaxion.game.state.StageGameState;
import com.jme.math.FastMath;
import com.jme.math.Vector3f;

public class ThrowCard extends AttackObject {
	
	public static final String filename = "card";
	protected static final int span = 240;
	protected static final float dpf = 8;
	final float angleinc = FastMath.PI/90;
	final float spininc = FastMath.PI/16;
	final float speed = 1;
	
	public float angle = 0;
	public int dir;
	
	public ThrowCard(Model m) {
    	super(filename, dpf, m);
    	flinch = true;
    	lifespan = span;
    }
	
	public ThrowCard(Model[] m) {
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
		
		angle = (angle+angleinc) % (FastMath.PI*2);
		velocity = new Vector3f(FastMath.sin(angle*dir)*speed, 0, FastMath.cos(angle*dir)*speed);		
		
		roll += spininc;
		yaw = FastMath.PI/2;
		
		rotate();
		super.act(b);
	}
}
