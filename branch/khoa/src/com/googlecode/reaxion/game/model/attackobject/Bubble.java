package com.googlecode.reaxion.game.model.attackobject;

import com.googlecode.reaxion.game.model.Model;
import com.googlecode.reaxion.game.model.character.Character;
import com.googlecode.reaxion.game.state.StageGameState;
import com.jme.math.FastMath;
import com.jme.math.Vector3f;

public class Bubble extends AttackObject {
	
	public static final String filename = "bubble";
	protected static final int span = 520;
	protected static final float dpf = 4;
	
	private final float maxDpf = 14;
	
	private int maxSize = 4;
	public int peakTime = 20;
	
	public float amplitude = .1f;
	
	private float baseY;
	
	public Bubble(Model m) {
    	super(filename, dpf, m);
    	flinch = true;
    	lifespan = span;
    }
	
	public Bubble(Model[] m) {
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
		// change size
		if (lifeCount < peakTime) {
			if (lifeCount == 0)
				baseY = model.getLocalTranslation().y;
			model.setLocalScale((float) (lifeCount+1)/peakTime);
		} else if (lifeCount < lifespan - peakTime) {
			model.setLocalScale(1 + (maxSize-1)*(float)(lifeCount - peakTime)/(lifespan - peakTime));
			changeDamage();
		} else {
			model.setLocalScale((float) maxSize*(lifespan - lifeCount)/peakTime);
			changeDamage();
		}
		
		// reset initial amplitude
		if ((float) lifeCount/10 >= FastMath.PI)
			amplitude = .1f;
        
		// bob
		Vector3f pos = model.getLocalTranslation();
		pos.y = baseY;
		model.setLocalTranslation(pos.add(new Vector3f(0, amplitude*FastMath.sin((float) lifeCount/10), 0)));
		
    	super.act(b);
    }
	
	
	/**
	 * Change {@code damagePerFrame} based on bubble's size.
	 */
	private void changeDamage() {
		damagePerFrame = dpf + maxDpf * (model.getLocalScale().x - 1) / (maxSize - 1);
	}
}
