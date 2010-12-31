package com.googlecode.reaxion.game.model.attackobject;

import com.googlecode.reaxion.game.model.Model;
import com.googlecode.reaxion.game.model.character.Character;
import com.googlecode.reaxion.game.state.StageGameState;
import com.jme.math.FastMath;
import com.jme.math.Vector3f;

public class IceCube extends AttackObject {
	
	public static final String filename = "ice-cube";
	protected static final int span = 90;
	
	private static final float maxDpf = 10;
	protected static final float dpf = maxDpf;
	
	private static final float endSize = .25f;
	
	public IceCube(Model m) {
    	super(filename, dpf, m);
    	flinch = true;
    	lifespan = span;
    	gravitate = true;
    	gravity = -.06f;
    }
	
	public IceCube(Model[] m) {
    	super(filename, dpf, m);
    	flinch = true;
    	lifespan = span;
    	gravitate = true;
    	gravity = -.06f;
    }
	
	@Override
	public void hit(StageGameState b, Character other) {
		finish(b);
    }
	
	@ Override
    public void act(StageGameState b) {
		// change size
		model.setLocalScale((1 - ((float)lifeCount/lifespan)) * (1 - endSize) + endSize);
		changeDamage();
		
		// apply gravity
		velocity.y += gravVel;
		if (model.getLocalTranslation().y > 0)
			gravVel += gravity;
		contactGround();
		
    	super.act(b);
    }
	
	/**
	 * Change {@code damagePerFrame} based on size.
	 */
	private void changeDamage() {
		damagePerFrame = maxDpf * (model.getLocalScale().x);
	}
	
}
