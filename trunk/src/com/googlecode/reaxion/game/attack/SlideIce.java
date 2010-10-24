package com.googlecode.reaxion.game.attack;

import com.googlecode.reaxion.game.model.Model;
import com.googlecode.reaxion.game.model.attackobject.Bubble;
import com.googlecode.reaxion.game.model.attackobject.Bullet;
import com.googlecode.reaxion.game.model.attackobject.IceCube;
import com.googlecode.reaxion.game.state.StageGameState;
import com.googlecode.reaxion.game.util.LoadingQueue;
import com.jme.math.FastMath;
import com.jme.math.Vector3f;
import com.radakan.jme.mxml.anim.MeshAnimationController;

/**
 * Slides an ice cube forward.
 */
public class SlideIce extends Attack {
	
	private static final String n = "Ice Cube";
	private static final int gc = 6;
	
	private static final float speed = 3;
	private IceCube ice;
	
	public SlideIce() {
		name = n;
		gaugeCost = gc;
	}
	
	public SlideIce(AttackData ad) {
		super(ad, gc);
		name = n;
	}
	
	public static void load() {
		LoadingQueue.push(new Model(IceCube.filename));
	}
	
	@Override
	public void firstFrame(StageGameState b) {
		character.moveLock = true;
		character.jumpLock = true;
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
			Vector3f translation = new Vector3f(2*FastMath.sin(angle), 0f, 2*FastMath.cos(angle));
			
			ice = (IceCube)LoadingQueue.quickLoad(new IceCube(getUsers()), b);
			
			ice.rotate(rotation);
			ice.setVelocity(rotation.mult(speed));
			ice.model.setLocalTranslation(character.model.getWorldTranslation().add(translation));
			
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
		character.animationLock = false;
	}
	
}