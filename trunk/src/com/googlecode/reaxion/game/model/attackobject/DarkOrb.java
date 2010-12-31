package com.googlecode.reaxion.game.model.attackobject;

import com.googlecode.reaxion.game.model.Model;
import com.googlecode.reaxion.game.model.character.Character;
import com.googlecode.reaxion.game.state.StageGameState;
import com.googlecode.reaxion.game.util.LoadingQueue;
import com.jme.math.FastMath;
import com.jme.math.Vector3f;

public class DarkOrb extends AttackObject {
	
	public static final String filename = "bound-sphere";
	protected static final float dpf = 12;
	
	private final float terminalSpeed = -.2f;
	
	public float theta = 0;
	public float speed = .1f;
	public float vSpeed = 2f;
	
	private Model glow;
	
	public DarkOrb(Model m) {
    	super(filename, dpf, m);
    	flinch = true;
    	gravity = -.06f;
    }
	
	public DarkOrb(Model[] m) {
    	super(filename, dpf, m);
    	flinch = true;
    	gravity = -.06f;
    }
	
	public void setUp(StageGameState b) {
		velocity.y = vSpeed;
		velocity.x = speed*FastMath.cos(theta);
		velocity.z = speed*FastMath.sin(theta);
		model.setLocalScale(2);
		glow = LoadingQueue.quickLoad(new Model("dark-orb"), null);
		glow.model.setLocalScale(.5f);
		model.attachChild(glow.model);
		b.getRootNode().updateRenderState();
	}
	
	@Override
	public void hit(StageGameState b, Character other) {
		finish(b);
    }
	
	@ Override
    public void act(StageGameState b) {
		// set up conditions
		if (glow == null)
			setUp(b);
		
		// billboard glow
		glow.billboard(b.getCamera(), true);
		
		if (velocity.y > terminalSpeed)
			velocity.y = (float) Math.max(velocity.y+gravity, terminalSpeed);
		
		// check for removal
		if (model.getWorldTranslation().y <= -1)
			finish(b);
		
    	super.act(b);
    }
	
}
