package com.googlecode.reaxion.game.attack;

import com.googlecode.reaxion.game.model.Model;
import com.googlecode.reaxion.game.model.attackobject.BeaconOrb;
import com.googlecode.reaxion.game.state.StageGameState;
import com.googlecode.reaxion.game.util.LoadingQueue;
import com.jme.math.FastMath;
import com.jme.math.Vector3f;

/**
 * Flags the enemy to be targeted by light orbs.
 */
public class Beacon extends Attack {
	private static final String n = "Beacon";
	private static final int gc = 16;
	
	private Model target;
	private BeaconOrb[] orb = new BeaconOrb[4];
	
	public Beacon() {
		name = n;
		gaugeCost = gc;
	}
	
	public Beacon(AttackData ad) {
		super(ad, gc);
		name = n;
		target = ad.target;
	}
	
	public static void load() {
		LoadingQueue.push(new Model(BeaconOrb.filename));
	}
	
	@Override
	public void firstFrame(StageGameState b) {
		character.moveLock = true;
		character.jumpLock = true;
		character.animationLock = true;
		character.play("raiseUp", b.tpf);
	}
	
	@Override
	public void nextFrame(StageGameState b) {
		if (phase == 0 && character.play("raiseUp", b.tpf)) {
			
			// create orbs
			for (int i=0; i<orb.length; i++) {
				orb[i] = (BeaconOrb)LoadingQueue.quickLoad(new BeaconOrb(getUsers()), b);
				orb[i].model.setLocalTranslation(new Vector3f(0, b.getCurrentTarget().model.getLocalTranslation().y + 3f, 0));
				orb[i].theta = i*FastMath.PI*2/orb.length;
				orb[i].target = b.getCurrentTarget();
			}
			
			b.getRootNode().updateRenderState();
			
			character.play("raiseDown", b.tpf);
			phase++;
		} else if (phase == 1 && character.play("raiseDown", b.tpf)) {
			finish();
		}
	}
	
	@Override
	public void finish() {
		super.finish();
		character.moveLock = false;
		character.jumpLock = false;
		character.animationLock = false;
	}
	
}
