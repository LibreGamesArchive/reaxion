package com.googlecode.reaxion.game.model.enemies;

import com.googlecode.reaxion.game.model.Model;
import com.googlecode.reaxion.game.model.character.Character;
import com.googlecode.reaxion.game.state.StageGameState;
import com.jme.math.FastMath;

/**
 * Toybox's head component.
 */
public class DoriruzuDrill extends Enemy {

	private final float angleInc = FastMath.PI/10;
	
	private final float dpf = 1;
	private Doriruzu master;
	
	public Model[] collisions = new Model[0];
	
	public DoriruzuDrill(Doriruzu t) {
		super("enemies/doriruzu-drill", false);
		master = t;
		setInfo("", 0, 1);
		init();
	}
	
	@Override
	protected void init() {
		super.init();
    	flinch = false;
    	damagePerFrame = dpf;
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
		
		// rotate
		pitch = (pitch + angleInc) % (2*FastMath.PI);
		rotate();
	}
	@Override
	public boolean hit(StageGameState b, Model other) {
		return false;
	}

}
