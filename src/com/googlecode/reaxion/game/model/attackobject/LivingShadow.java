package com.googlecode.reaxion.game.model.attackobject;

import com.googlecode.reaxion.game.model.Model;
import com.googlecode.reaxion.game.model.character.Character;
import com.googlecode.reaxion.game.state.StageGameState;
import com.jme.math.Vector3f;

public class LivingShadow extends AttackObject {
	
	public static final String filename = "i_shadow";
	protected static final int span = 190;
	protected static final float dpf = 10;
	
	private final int startY = -5;
	private final int riseTime = 30;
	
	private final float speed = .7f;
	
	private final float maxBonus = 10;
	
	private Character user;
	private double initHp;
	
	public LivingShadow(Model m) {
    	super(filename, dpf, m);
    	flinch = true;
    	lifespan = span;
    }
	
	public LivingShadow(Model[] m) {
    	super(filename, dpf, m);
    	flinch = true;
    	lifespan = span;
    }
	
	@Override
	public void hit(StageGameState b, Character other) {
		b.removeModel(this);
    }
	
	@ Override
    public void act(StageGameState b) {
		// rise or run
		if (lifeCount < riseTime) {
			if (lifeCount == 0) {
				// get initial HP
		    	user = b.getPlayer();
		    	initHp = user.hp;
		    	
				model.setLocalTranslation(model.getLocalTranslation().add(new Vector3f(0, startY, 0)));
	    		play("stand");
			}
			model.setLocalTranslation(model.getLocalTranslation().add(new Vector3f(0, (float) -startY/riseTime, 0)));
			
		} else if (lifeCount >= lifespan - riseTime) {
			changeDamage();
			velocity = new Vector3f();
			play("stand");
			model.setLocalTranslation(model.getLocalTranslation().add(new Vector3f(0, (float) startY/riseTime, 0)));
		} else {
			
			changeDamage();
			play("run");
			// chase target
			velocity = b.getCurrentTarget().model.getWorldTranslation().subtract(model.getWorldTranslation()).normalize().mult(speed);
			velocity.y = 0;
			rotate(velocity);
		}
        
    	super.act(b);
    }
	
	/**
	 * Change {@code damagePerFrame} based on damage done to user since casting.
	 */
	private void changeDamage() {
		damagePerFrame = dpf + (float)Math.min(Math.max(initHp - user.hp, 0), maxBonus);
	}
	
}
