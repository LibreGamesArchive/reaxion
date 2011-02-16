package com.googlecode.reaxion.game.attack;

import com.googlecode.reaxion.game.model.Model;
import com.googlecode.reaxion.game.model.attackobject.AquaCircle;
import com.googlecode.reaxion.game.state.StageGameState;
import com.googlecode.reaxion.game.util.LoadingQueue;
import com.jme.math.Vector3f;

public class AquaRing extends Attack {
	
	private static final String n = "Aqua Ring";
	private static final int gc = 20;
	
	public AquaRing() {
		name = n;
		gaugeCost = gc;
		description = "Creates a ring of healing water beneath the user that replenishes HP and Gauge.";
	}
	
	public AquaRing(AttackData ad) {
		super(ad, gc);
		name = n;
		validateGround();
	}
	
	public static void load() {
		LoadingQueue.push(new Model(AquaCircle.filename));
	}
	
	@Override
	public void firstFrame(StageGameState b) {
		// make sure there isn't already an aqua ring
		boolean flag = false;
		for (int i=0; i< b.getModels().size(); i++)
			if (b.getModels().get(i) instanceof AquaCircle && b.getModels().get(i).users.contains(getUsers()[getUsers().length-1])) {
				flag = true;
				break;
			}
		if (flag) {
			character.gauge += gaugeCost;
			finish();
		} else {
			character.moveLock = true;
			character.jumpLock = true;
			character.tagLock = true;
			character.animationLock = true;
			character.play("prayerUp", b.tpf);
		}
	}
	
	@Override
	public void nextFrame(StageGameState b) {
		if (phase == 0 && character.play("prayerUp", b.tpf)) {
			character.play("prayer", b.tpf);
			
			AquaCircle ac = (AquaCircle)LoadingQueue.quickLoad(new AquaCircle(getUsers()), b);
			ac.model.setLocalTranslation(character.model.getLocalTranslation().mult(new Vector3f(1, 0, 1)));
			
			b.getRootNode().updateRenderState();		
			phase++;
			
		} else if (phase == 1 && frameCount >= 18) {
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