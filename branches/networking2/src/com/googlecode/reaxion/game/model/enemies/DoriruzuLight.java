package com.googlecode.reaxion.game.model.enemies;

import com.googlecode.reaxion.game.model.Model;
import com.googlecode.reaxion.game.model.attackobject.AttackObject;
import com.googlecode.reaxion.game.model.character.Character;
import com.googlecode.reaxion.game.state.StageGameState;

/**
 * Doriruzu's rear light component.
 */
public class DoriruzuLight extends Enemy {

	private final float dpf = 1;
	private Doriruzu master;
	
	public Model[] collisions = new Model[0];
	
	public DoriruzuLight(Doriruzu t) {
		super("enemies/doriruzu-light", false);
		master = t;
		setInfo("", 0, 1);
		init();
	}
    
    @Override
    protected void init() {
    	super.init();
    }
    
	@ Override
	public void act(StageGameState b) {
		collisions = getModelCollisions(b);
		
		// check if a Character is hit
		if (damagePerFrame > 0) {
			for (Model c : collisions) {
				if (c instanceof Character && c != master)
					((Character)c).hit(b, this);
			}
		}
	}
	@Override
	public boolean hit(StageGameState b, Model other) {
		return false;
	}

	public boolean checkHit(StageGameState b, Model other) {
		// reciprocate the hit
		if (other instanceof AttackObject && ((AttackObject)other).users.get(0) != master) {
			((AttackObject)other).hit(b, this);
			
			master.hitLight(b, other);
			return true;

		}
		
		return false;
	}

}
