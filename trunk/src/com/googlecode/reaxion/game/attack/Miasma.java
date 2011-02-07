package com.googlecode.reaxion.game.attack;

import com.googlecode.reaxion.game.model.Model;
import com.googlecode.reaxion.game.model.attackobject.Fire;
import com.googlecode.reaxion.game.state.StageGameState;
import com.googlecode.reaxion.game.util.LoadingQueue;
import com.jme.math.FastMath;
import com.jme.math.Vector3f;

public class Miasma extends Attack {
	private static final String n = "Miasma";
	private static final int gc = 21;
	
	private static final int radius = 3;
	private static final int delay = 2;
	private static final int numFires = 80;
	
	public Miasma() {
		name = n;
		gaugeCost = gc;
		description = "Creates a fog of fire around the user.";		
	}
	
	public Miasma(AttackData ad) {
		super(ad, gc);
		name = n;
	}
	
	public static void load() {
		LoadingQueue.push(new Model(Fire.filename));
	}
	
	@Override
	public void firstFrame(StageGameState b) {
		character.animationLock = true;
		character.tagLock = true;
		
		character.play("raiseUp", b.tpf);
	}
	
	@Override
	public void nextFrame(StageGameState b) {
		if (phase == 0 && character.play("raiseUp", b.tpf)) {
			// allow to animate
			character.animationLock = false;
			phase++;
			
		} else if (phase >= 1 && phase < numFires+1 && frameCount % delay == 0) {
			
			// determine spawn point
			float a = FastMath.nextRandomFloat()*FastMath.PI*2;
			float p = FastMath.nextRandomFloat()*FastMath.PI*2;
			Vector3f translation = new Vector3f(radius*FastMath.sin(p)*FastMath.sin(a), radius*FastMath.cos(p), radius*FastMath.sin(p)*FastMath.cos(a));
			
			Fire fire = (Fire)LoadingQueue.quickLoad(new Fire(getUsers()), b);
			fire.setVelocity(translation.mult(.2f/(float)radius));
			fire.model.setLocalTranslation(character.model.getLocalTranslation().add(translation));
			
			b.getRootNode().updateRenderState();
			phase++;
			
		} else if (phase >= numFires+1) {
			finish();	
		}
	}
	
	@Override
	public void finish() {
		super.finish();
		character.animationLock = false;
		character.tagLock = false;
	}
	
}
