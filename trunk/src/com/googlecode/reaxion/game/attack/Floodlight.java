package com.googlecode.reaxion.game.attack;

import com.googlecode.reaxion.game.model.Model;
import com.googlecode.reaxion.game.model.attackobject.LightPillar;
import com.googlecode.reaxion.game.state.StageGameState;
import com.googlecode.reaxion.game.util.LoadingQueue;
import com.jme.math.FastMath;

public class Floodlight extends Attack {
	
	private static final String n = "Floodlight";
	private static final int gc = 18;
	
	private final int numLights = 6;
	
	public Floodlight() {
		name = n;
		gaugeCost = gc;
		description = "Pillars of light appear and dance around the user.";
	}
	
	public Floodlight(AttackData ad) {
		super(ad, gc);
		name = n;
		validateGround();
	}
	
	public static void load() {
		LoadingQueue.push(new Model(LightPillar.filename));
	}
	
	@Override
	public void firstFrame(StageGameState b) {
		character.moveLock = true;
		character.jumpLock = true;
		character.tagLock = true;
		character.animationLock = true;
		character.play("heaveUp", b.tpf);
	}
	
	@Override
	public void nextFrame(StageGameState b) {
		if (phase == 0 && character.play("heaveUp", b.tpf)) {
			
			for (int i=0; i<numLights; i++) {
				float angle = FastMath.PI*2/numLights*i;
				LightPillar l = (LightPillar)LoadingQueue.quickLoad(new LightPillar(getUsers()), b);
				l.theta = angle;
			}
			
			b.getRootNode().updateRenderState();
			
			finish();
		}
	}
	
	@Override
	public void finish() {
		super.finish();
		character.moveLock = false;
		character.jumpLock = false;
		character.tagLock = false;
		character.animationLock = false;
	}
	
}
