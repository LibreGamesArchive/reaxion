package com.googlecode.reaxion.game.model.enemies;

import com.googlecode.reaxion.game.model.Model;
import com.googlecode.reaxion.game.model.attackobject.AttackObject;
import com.googlecode.reaxion.game.state.StageGameState;
import com.jme.math.FastMath;
import com.jme.math.Vector3f;

/**
 * Represents one of Pyroclast's segments.
 */
public class PyroclastRock extends Enemy {

	private Pyroclast master;
	
	private final float rollRate = FastMath.PI/30;
	
	public boolean vulnerable = false;
	
	public PyroclastRock(Pyroclast p) {
		super("enemies/rock-demon_rock", false);
		master = p;
		setInfo("Pyroclast", 200, 1);
		init();
	}
    
    @Override
    protected void init() {
    	super.init();
    	mass = 10;
    	gravity = -.06f;
		trackOffset = new Vector3f(0, 3.75f, 0);
		boundRadius = 9f;
		boundHeight = 7.25f;
    }
    
    @ Override
	public void act(StageGameState b) {

		// receive actions
		step(b);
		
		// check if alive
		if (hp > 0) {
			
			// apply gravity
			if (gravitate && model.getLocalTranslation().y > 0)
				velocity.y += gravity;

//			// check the ground
//			contactGround();
//			
//			// push characters around
//			moveCollide(b);
//			
//			// check the ground once more
//			contactGround();
			
			Vector3f loc = model.getLocalTranslation();
			loc.addLocal(velocity);
			model.setLocalTranslation(loc);
			
		}
	}
    
    @Override
	protected void step(StageGameState b) {
    	roll = (roll + rollRate) % (2*FastMath.PI);
    	rotate();
    }
    
	@Override
	public boolean reactHit(StageGameState b, Model other) {
		// only take damage if no segments are left beneath
		if (vulnerable) {
			// reciprocate the hit
			if (other instanceof AttackObject) {
				((AttackObject)other).hit(b, this);
				hp -= other.getDamage()*1/3;
				
				if (hp <= 0)
					master.reduce(b);
			}
			
			return true;
		}
		
		return false;
	}

}
