package com.googlecode.reaxion.game.attack;

import com.googlecode.reaxion.game.model.Model;
import com.googlecode.reaxion.game.model.attackobject.Bubble;
import com.googlecode.reaxion.game.state.StageGameState;
import com.googlecode.reaxion.game.util.LoadingQueue;
import com.jme.math.FastMath;
import com.jme.math.Vector3f;
import com.radakan.jme.mxml.anim.MeshAnimationController;

/**
 * Splashes up several bubbles.
 */
public class BubbleBath extends Attack {
	
	private static final String n = "Bubble Bath";
	private static final int gc = 14;
	
	private static final float speed = .75f;
	private Bubble[] bubble = new Bubble[16];
	
	public BubbleBath() {
		name = n;
		gaugeCost = gc;
	}
	
	public BubbleBath(AttackData ad) {
		super(ad, gc);
		name = n;
		validateGround();
	}
	
	public static void load() {
		LoadingQueue.push(new Model(Bubble.filename));
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
			
			Vector3f rotation = character.rotationVector;
			
			// make the bubbles fly
			for (int i=0; i<bubble.length; i++) {
				float angle = FastMath.atan2(rotation.x, rotation.z) + 2*FastMath.PI/bubble.length*i;
				Vector3f translation = new Vector3f(3f*FastMath.sin(angle), 3.7f, 3f*FastMath.cos(angle));			
				bubble[i] = (Bubble)LoadingQueue.quickLoad(new Bubble(getUsers()), b);
				bubble[i].amplitude = 8;
				bubble[i].setVelocity(new Vector3f(FastMath.sin(angle), 0, FastMath.cos(angle)).mult(speed));
				bubble[i].model.setLocalTranslation(character.model.getWorldTranslation().add(translation));
			}
			
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