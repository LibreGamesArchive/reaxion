package com.googlecode.reaxion.game.attack;

import com.googlecode.reaxion.game.model.Model;
import com.googlecode.reaxion.game.model.attackobject.BeaconOrb;
import com.googlecode.reaxion.game.model.enemies.BastionShield;
import com.googlecode.reaxion.game.state.StageGameState;
import com.googlecode.reaxion.game.util.LoadingQueue;
import com.jme.math.FastMath;
import com.jme.math.Vector3f;

public class Bastion extends Attack {
	private static final String n = "Bastion";
	private static final int gc = 18;
	
	private Model target;
	
	public Bastion() {
		name = n;
		gaugeCost = gc;
		description = "Impregnable walls advance on the opponent and surround them.";
	}
	
	public Bastion(AttackData ad) {
		super(ad, gc);
		name = n;
		target = ad.target;
	}
	
	public static void load() {
		LoadingQueue.push(new Model("bastion"));
	}
	
	@Override
	public void firstFrame(StageGameState b) {
		character.moveLock = true;
		character.jumpLock = true;
		character.tagLock = true;
		character.animationLock = true;
		character.play("raiseUp", b.tpf);
	}
	
	@Override
	public void nextFrame(StageGameState b) {
		if (phase == 0 && character.play("raiseUp", b.tpf)) {
			
			// create shields
			for (int i=0; i<6; i++) {
				float angle = FastMath.PI/3*i;
				Vector3f translation = target.model.getLocalTranslation().mult(new Vector3f(1, 0, 1)).add(12*FastMath.sin(angle), -7f, 12*FastMath.cos(angle));
				BastionShield s = (BastionShield)LoadingQueue.quickLoad(new BastionShield(getUsers(), target.model.getLocalTranslation().mult(new Vector3f(1, 0, 1))), b);
				s.model.setLocalTranslation(translation);
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
		character.tagLock = false;
		character.animationLock = false;
	}
	
}
