package com.googlecode.reaxion.game.attack;

import com.googlecode.reaxion.game.model.Model;
import com.googlecode.reaxion.game.model.attackobject.DarkSpike;
import com.googlecode.reaxion.game.model.attackobject.Fireball;
import com.googlecode.reaxion.game.model.attackobject.LivingShadow;
import com.googlecode.reaxion.game.state.StageGameState;
import com.googlecode.reaxion.game.util.LoadingQueue;
import com.jme.math.FastMath;
import com.jme.math.Vector3f;

/**
 * Summon five spikes that line up in front of the user.
 */
public class SpikeLine extends Attack {
	
	private static final String n = "Ruin";
	private static final int gc = 13;
	
	private int spikeCount = 5;
	
	private Vector3f rotation;
	private Vector3f position;
	
	public SpikeLine() {
		name = n;
		gaugeCost = gc;
	}
	
	public SpikeLine(AttackData ad) {
		super(ad, gc);
		name = n;
		validateGround();
	}
	
	public static void load() {
		LoadingQueue.push(new Model(DarkSpike.filename));
	}
	
	@Override
	public void firstFrame(StageGameState b) {
		character.moveLock = true;
		character.jumpLock = true;
		character.animationLock = true;
		character.play("smite", b.tpf);
		
		rotation = character.rotationVector;
		position = character.model.getWorldTranslation().clone();
		position.y = 0;
	}
	
	@Override
	public void nextFrame(StageGameState b) {
		if (phase == 0 && character.play("smite", b.tpf)) {
			//free character
			character.animationLock = false;
			character.moveLock = false;
			character.jumpLock = false;
			phase++;
			
		} else if (phase == 1) {
			if (frameCount % 6 == 0) {
			// calculate transformations
			float angle = FastMath.atan2(rotation.x, rotation.z);
			
			Vector3f translation = new Vector3f((float)8*(6-spikeCount)*FastMath.sin(angle), -4, (float)8*(6-spikeCount)*FastMath.cos(angle));
			
			DarkSpike ds = (DarkSpike)LoadingQueue.quickLoad(new DarkSpike(getUsers()), b);
			ds.model.setLocalTranslation(position.add(translation));
			
			b.getRootNode().updateRenderState();
			
			spikeCount--;
			
			// end attack
			if (spikeCount == 0)
				finish();
			}
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