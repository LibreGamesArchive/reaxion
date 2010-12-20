package com.googlecode.reaxion.game.model.attackobject;

import com.googlecode.reaxion.game.model.Model;
import com.googlecode.reaxion.game.model.character.Character;
import com.googlecode.reaxion.game.state.StageGameState;

public class StraightCard extends AttackObject {
	
	public static final String filename = "card";
	protected static final int span = 120;
	protected static final float dpf = 5;
	
	public StraightCard(Model m) {
    	super(filename, dpf, m);
    	flinch = true;
    	lifespan = span;
    }
	
	public StraightCard(Model[] m) {
    	super(filename, dpf, m);
    	flinch = true;
    	lifespan = span;
    }
	
	@Override
	public void hit(StageGameState b, Character other) {
		//System.out.println("bullet hit "+other.model);
		b.removeModel(this);
    }
	
}
