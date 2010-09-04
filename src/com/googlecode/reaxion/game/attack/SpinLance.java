package com.googlecode.reaxion.game.attack;

import com.googlecode.reaxion.game.model.Model;
import com.googlecode.reaxion.game.model.attackobject.SpinningLance;
import com.googlecode.reaxion.game.state.BattleGameState;
import com.googlecode.reaxion.game.util.LoadingQueue;
import com.jme.math.FastMath;
import com.jme.math.Vector3f;
import com.radakan.jme.mxml.anim.MeshAnimationController;

/**
 * Fires an energy bullet towards the target
 */
public class SpinLance extends Attack {
	
	private Model target;
	private SpinningLance lance;
	
	public SpinLance() {
		name = "Lance Blitz";
	}
	
	public SpinLance(AttackData ad) {
		super(ad, 5);
		name = "Lance Blitz";
		target = ad.target;
	}
	
	@Override
	public void load() {
		LoadingQueue.push(new Model(SpinningLance.filename));
	}
	
	@Override
	public void firstFrame(BattleGameState b) {
		character.moveLock = true;
		character.jumpLock = true;
		character.animationLock = true;
		character.play("cast", b.tpf);
	}
	
	@Override
	public void nextFrame(BattleGameState b) {
		// if animation is halfway through
		MeshAnimationController animControl = (MeshAnimationController) character.model.getController(0);
		if (phase == 0 && animControl.getCurTime() + b.tpf >= animControl.getAnimationLength("cast")/2) {
			
			// calculate transformations
			Vector3f rotation = character.rotationVector;
			float angle = FastMath.atan2(rotation.x, rotation.z);
			Vector3f translation = new Vector3f(6*FastMath.sin(angle), 3.2f, 6*FastMath.cos(angle));
			
			lance = (SpinningLance)LoadingQueue.quickLoad(new SpinningLance(character), b);
			lance.target = target;
			
			lance.rotate(rotation);
			lance.model.setLocalTranslation(character.model.getWorldTranslation().add(translation));
			
			b.getRootNode().updateRenderState();
			phase++;
			
		} else if (character.play("cast", b.tpf)) {
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
