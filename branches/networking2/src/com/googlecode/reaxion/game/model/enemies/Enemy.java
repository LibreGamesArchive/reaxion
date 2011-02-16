package com.googlecode.reaxion.game.model.enemies;

import com.googlecode.reaxion.game.model.Model;
import com.googlecode.reaxion.game.model.attackobject.AttackObject;
import com.googlecode.reaxion.game.model.character.Character;
import com.googlecode.reaxion.game.state.StageGameState;
import com.jme.math.Vector3f;

/**
 * Character that represents a non-playable enemy unit. To be extended
 * by all in-game enemies.
 * 
 * @author Khoa
 */
public class Enemy extends Character {

	public Enemy(String filename) {
    	// Load model
    	super(filename);
    	trackable = true;
    	init();
    }
    
    public Enemy(String filename, Boolean _trackable) {
    	// Load model
    	super(filename);
    	trackable = _trackable;
    	init();
    }
    
    @Override
    protected void init() {
    	hp = maxHp;
    }
	
    /**
     * Specify this enemy's name, HP, and attack multiplier.
     */
	protected void setInfo(String n, int hp, double str) {
		name = n;
		maxHp = hp;
    	strengthMult = str;
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

			// check the ground
			contactGround();
			// remain inside the stage
			b.getStage().contain(this);
			// push characters around
			moveCollide(b);
			// remain inside the stage again
			b.getStage().contain(this);
			// check the ground once more
			contactGround();
			
			Vector3f loc = model.getLocalTranslation();
			loc.addLocal(velocity);
			model.setLocalTranslation(loc);
			
		}
	}
	
	/**
	 * Called every frame by {@code act()}. Override to add functionality.
	 */
	protected void step(StageGameState b) {
		
	}

	@Override
	public boolean hit(StageGameState b, Model other) {
		return reactHit(b, other);
	}

	@Override
	public boolean reactHit(StageGameState b, Model other) {
		// reciprocate the hit
		if (other instanceof AttackObject)
			((AttackObject)other).hit(b, this);

		/*
		if (other.flinch)
			toggleFlinch(true);
		*/
		hp -= other.getDamage();

		System.out.println(model+" hit by "+other+": "+(hp+other.getDamage())+" -> "+hp);
		return true;
	}

}
