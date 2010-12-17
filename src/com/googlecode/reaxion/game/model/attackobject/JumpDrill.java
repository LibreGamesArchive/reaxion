package com.googlecode.reaxion.game.model.attackobject;

import com.googlecode.reaxion.game.model.Model;
import com.googlecode.reaxion.game.model.character.Character;
import com.googlecode.reaxion.game.state.StageGameState;
import com.jme.math.Vector3f;

public class JumpDrill extends AttackObject {
	
	public static final String filename = "drill";
	protected static final int span = 200;
	protected static final int flyTime = 40;
	protected static final float dpf = 6;
	
	protected static final float riseSpeed = 1.5f;
	protected static final float flySpeed = 8;
	
	private Model target;
	
	public JumpDrill(Model m, Model t) {
    	super(filename, dpf, m);
    	flinch = true;
    	lifespan = span;
    	target = t;
    }
	
	public JumpDrill(Model[] m, Model t) {
    	super(filename, dpf, m);
    	flinch = true;
    	lifespan = span;
    	target = t;
    }
	
	@Override
	public void hit(StageGameState b, Character other) {
		b.removeModel(this);
    }
	
	@ Override
    public void act(StageGameState b) {
		if (lifeCount < flyTime)
			velocity = new Vector3f(0, riseSpeed, 0);
		else if (lifeCount == flyTime)
			velocity = target.model.getLocalTranslation().subtract(model.getLocalTranslation()).normalize().mult(flySpeed);
		else if (model.getLocalTranslation().y < -2)
			b.removeModel(this);
		
		rotate(velocity);
		
		super.act(b);
	}
	
}
