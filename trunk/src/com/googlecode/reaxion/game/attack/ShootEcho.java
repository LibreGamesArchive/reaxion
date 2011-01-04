package com.googlecode.reaxion.game.attack;

import com.googlecode.reaxion.game.model.Model;
import com.googlecode.reaxion.game.model.attackobject.Echo;
import com.googlecode.reaxion.game.state.StageGameState;
import com.googlecode.reaxion.game.util.LoadingQueue;
import com.jme.math.FastMath;
import com.jme.math.Vector3f;
import com.radakan.jme.mxml.anim.MeshAnimationController;

public class ShootEcho extends Attack {
	
	private static final String n = "Echo";
	private static final int gc = 8;
	
	private Model target;
	
	public ShootEcho() {
		name = n;
		gaugeCost = gc;
		description = "User emits an ultrasonic pulse that can bounce off opponents and return to the sender.";
	}
	
	public ShootEcho(AttackData ad) {
		super(ad, gc);
		name = n;
		target = ad.target;
	}
	
	public static void load() {
		LoadingQueue.push(new Model(Echo.filename));
	}
	
	@Override
	public void firstFrame(StageGameState b) {
		character.moveLock = true;
		character.jumpLock = true;
		character.tagLock = true;
		character.animationLock = true;
		character.play("cast", b.tpf);
	}
	
	@Override
	public void nextFrame(StageGameState b) {
		// if animation is halfway through
		MeshAnimationController animControl = (MeshAnimationController) character.model.getController(0);
		if (phase == 0 && animControl.getCurTime() + b.tpf >= animControl.getAnimationLength("cast")/2) {
			
			phase++;
			
			// calculate transformations
			Vector3f rotation = character.rotationVector;
			float angle = FastMath.atan2(rotation.x, rotation.z);
			Vector3f translation = new Vector3f(2*FastMath.sin(angle), 3.7f, 2*FastMath.cos(angle));
			
			Echo e = (Echo)LoadingQueue.quickLoad(new Echo(getUsers(), target), b);
			
			e.rotate(rotation);
			e.setVelocity(rotation.mult(e.speed));
			e.model.setLocalTranslation(character.model.getWorldTranslation().add(translation));
			
			b.getRootNode().updateRenderState();
			
		} else if (character.play("cast", b.tpf)) {
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