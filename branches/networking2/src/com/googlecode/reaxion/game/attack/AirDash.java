package com.googlecode.reaxion.game.attack;

import com.googlecode.reaxion.game.state.StageGameState;
import com.jme.math.FastMath;

public class AirDash extends Attack {

	private static final String n = "Air Dash";
	private static final int gc = 0;
	
	protected static final int duration = 20;
	protected static final int dashSpeed = 4;
	protected static final int lag = 4;
	
	public AirDash() {
		name = n;
		gaugeCost = gc;
		description = "Dash forward.";
	}
	
	public AirDash(AttackData ad) {
		super(ad, gc);
		name = n;
	}
	
	@Override
	public void firstFrame(StageGameState b) {
		character.moveLock = true;
		character.animationLock = true;
		character.tagLock = true;
		character.jumpLock = true;
		
		character.gravitate = false;
		character.gravVel = 0;
		character.play("dash", b.tpf);
	}
	
	public void nextFrame(StageGameState b) {
//		super.nextFrame(b);
		
		if (frameCount < duration + lag) {
			// create sparkle
//			if (s == null)
//				s = (Sparkle)LoadingQueue.quickLoad(new Sparkle(30, 12), b);
//
//			Vector3f rotation = character.rotationVector;
//			float angle = FastMath.atan2(rotation.x, rotation.z);
//			Vector3f translation = new Vector3f(2.5f*FastMath.sin(angle), 4.4f, 2.5f*FastMath.cos(angle));
//			
//			s.model.setLocalTranslation(character.model.getWorldTranslation().add(translation));
			
			character.setVelocity(character.rotationVector.normalize().mult(dashSpeed*FastMath.pow((float)Math.max(0, duration - frameCount)/duration, 2)));
			if (duration - frameCount <= 8)
				character.gravitate = true;
			
//			b.getRootNode().updateRenderState(); 
		} else {
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
		
		character.gravitate = true;
	}
}
