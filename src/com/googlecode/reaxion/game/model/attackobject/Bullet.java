package com.googlecode.reaxion.game.model.attackobject;

import com.googlecode.reaxion.game.model.Model;
import com.googlecode.reaxion.game.model.character.Character;
import com.googlecode.reaxion.game.state.StageGameState;

public class Bullet extends AttackObject {
	
	public static final String filename = "bullet";
	protected static final int span = 120;
	protected static final float dpf = 3;
	
	public Bullet(Model m) {
    	super(filename, dpf, m);
    	flinch = true;
    	lifespan = span;
    }
	
	public Bullet(Model[] m) {
    	super(filename, dpf, m);
    	flinch = true;
    	lifespan = span;
    }
	
	@Override
	public void hit(StageGameState b, Character other) {
		//System.out.println("bullet hit "+other.model);
		finish(b);
    }
	
	/*
	@Override
	public void initialize() {
		super.initialize();

		model.setCullHint(CullHint.Never);
		model.setLightCombineMode(LightCombineMode.Off);
		model.setTextureCombineMode(TextureCombineMode.Replace);
		model.updateRenderState();
	}
	*/
	
}
