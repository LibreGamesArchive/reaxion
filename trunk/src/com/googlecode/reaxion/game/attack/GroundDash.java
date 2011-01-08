package com.googlecode.reaxion.game.attack;

import com.googlecode.reaxion.game.state.StageGameState;
import com.jme.math.FastMath;

public class GroundDash extends Attack {
	
	private static final String n = "Ground Dash";
	private static final int gc = 0;
	
	protected static final int duration = 8;
	protected static final int dashSpeed = 6;
	protected static final int lag = 12;
	
//	protected Sparkle s;
	
	public GroundDash() {
		name = n;
		gaugeCost = gc;
		description = "Dash forward.";
	}
	
	public GroundDash(AttackData ad) {
		super(ad, gc);
		name = n;
	}
	
	public static void load() {
	//	LoadingQueue.push(new Model(DashForce.filename));
	}
	
	@Override
	public void firstFrame(StageGameState b) {
		character.moveLock = true;
		character.animationLock = true;
		character.tagLock = true;
		character.jumpLock = true;
		
		character.play("dash", b.tpf);
	}
	
	@Override
	public void nextFrame(StageGameState b) {
//			if (frameCount < duration) {
//				// create sparkle
//				if (s == null)
//					s = (Sparkle)LoadingQueue.quickLoad(new Sparkle(30, 12), b);
//
//				Vector3f rotation = character.rotationVector;
//				float angle = FastMath.atan2(rotation.x, rotation.z);
//				Vector3f translation = new Vector3f(2.5f*FastMath.sin(angle), 4.4f, 2.5f*FastMath.cos(angle));
//				
//				s.model.setLocalTranslation(character.model.getWorldTranslation().add(translation));
//				b.getRootNode().updateRenderState();
		
		if (frameCount < duration)
			character.setVelocity(character.rotationVector.mult(dashSpeed*FastMath.pow((float)(duration - frameCount)/duration, 1.5f)));
				
		if (phase == 0) {
			if (frameCount >= duration) {
				character.play("dashUp", b.tpf);
				phase++;
			}
		} else if (phase == 1 && character.play("dashUp", b.tpf)) {
			finish();
		}
		
	}
	
	@Override
	public void finish() {
		super.finish();
		character.moveLock = false;
		character.animationLock = false;
		character.tagLock = false;
		character.jumpLock = false;
	}
	
}
