package com.googlecode.reaxion.game.model.attackobject;

import com.googlecode.reaxion.game.model.Model;
import com.googlecode.reaxion.game.model.character.Character;
import com.googlecode.reaxion.game.state.StageGameState;
import com.googlecode.reaxion.game.util.LoadingQueue;
import com.jme.math.FastMath;
import com.jme.math.Vector3f;

public class DarkPit extends AttackObject {
	
	public static final String filename = "dark-pit";
	protected static final int span = 500;
	protected static final float dpf = 0;
	protected static final float maxdpf = .25f;
	
	private final int upTime = 80;
	private final int downTime = 440;
	
	private Model user;
	
	public DarkPit(Model m) {
    	super(filename, dpf, m);
    	lifespan = span;
    	user = m;
    }
	
	public DarkPit(Model[] m) {
    	super(filename, dpf, m);
    	lifespan = span;
    	user = m[m.length-1];
    }
	
	@Override
	public void hit(StageGameState b, Character other) {
		// give hp to user
		((Character)user).heal(b, damagePerFrame);
    }
	
	@ Override
    public void act(StageGameState b) {
		if (lifeCount < upTime)
			model.setLocalScale((float)(lifeCount+1)/upTime);
		else if (lifeCount > downTime) {
			model.setLocalScale((float)(lifespan - lifeCount + 1)/(lifespan - downTime));
			damagePerFrame = 0;
		} else
			damagePerFrame = maxdpf;
		
    	super.act(b);
    }
	
}
