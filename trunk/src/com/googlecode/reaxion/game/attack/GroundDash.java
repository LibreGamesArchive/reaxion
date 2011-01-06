package com.googlecode.reaxion.game.attack;

import com.googlecode.reaxion.game.model.Model;
import com.googlecode.reaxion.game.model.attackobject.AttackObject;
import com.googlecode.reaxion.game.model.attackobject.Barrier;
import com.googlecode.reaxion.game.model.attackobject.DashForce;
import com.googlecode.reaxion.game.model.attackobject.Starlight;
import com.googlecode.reaxion.game.model.prop.Sparkle;
import com.googlecode.reaxion.game.state.StageGameState;
import com.googlecode.reaxion.game.util.LoadingQueue;
import com.jme.math.FastMath;
import com.jme.math.Vector3f;

public class GroundDash extends Attack {
	
	private static final String n = "Ground Dash";
	private static final int gc = 0;
	
	protected static final int duration = 10;
	protected static final int dashSpeed = 5;
	
	protected Sparkle s;
	
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
	//	character.tagLock = true;
		
	//	character.play("command", b.tpf);
	}
	
	@Override
	public void nextFrame(StageGameState b) {
			character.play("guard", b.tpf);

			if (frameCount < duration) {
				// create sparkle
				if (s == null)
					s = (Sparkle)LoadingQueue.quickLoad(new Sparkle(30, 12), b);

				Vector3f rotation = character.rotationVector;
				float angle = FastMath.atan2(rotation.x, rotation.z);
				Vector3f translation = new Vector3f(2.5f*FastMath.sin(angle), 4.4f, 2.5f*FastMath.cos(angle));
				
				s.model.setLocalTranslation(character.model.getWorldTranslation().add(translation));
				character.setVelocity(character.rotationVector.mult(dashSpeed));
				
				b.getRootNode().updateRenderState();
			} else {
				finish();
			}
		
	}
	
	@Override
	public void finish() {
		super.finish();
		character.moveLock = false;
		character.animationLock = false;
	}
	
}
