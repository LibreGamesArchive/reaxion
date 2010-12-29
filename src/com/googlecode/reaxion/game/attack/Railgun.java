package com.googlecode.reaxion.game.attack;

import com.googlecode.reaxion.game.model.Model;
import com.googlecode.reaxion.game.model.attackobject.ElectroBeam;
import com.googlecode.reaxion.game.model.prop.Sparkle;
import com.googlecode.reaxion.game.state.StageGameState;
import com.googlecode.reaxion.game.util.LoadingQueue;
import com.jme.math.FastMath;
import com.jme.math.Vector3f;

public class Railgun extends Attack {
	
	private static final String n = "Railgun";
	private static final int gc = 34;
	private static final float speed = 15;
	
	public Railgun() {
		name = n;
		description = "Manipulates local EM fields to propel a projectile at high speeds.";
		gaugeCost = gc;
	}
	
	public Railgun(AttackData ad) {
		super(ad, gc);
		name = n;
		validateGround();
	}
	
	public static void load() {
		LoadingQueue.push(new Model(Sparkle.filename));
		LoadingQueue.push(new Model(ElectroBeam.filename));
	}
	
	@Override
	public void firstFrame(StageGameState b) {
		character.jumpLock = true;
		character.animationLock = true;
		character.tagLock = true;
		character.play("halt", b.tpf);
		
		// create sparkle
		Vector3f rotation = character.rotationVector;
		float angle = FastMath.atan2(rotation.x, rotation.z);
		Vector3f translation = new Vector3f(2.5f*FastMath.sin(angle), 4.4f, 2.5f*FastMath.cos(angle));
		
		Sparkle s = (Sparkle)LoadingQueue.quickLoad(new Sparkle(30, 12), b);
		s.model.setLocalTranslation(character.model.getWorldTranslation().add(translation));
		
		b.getRootNode().updateRenderState();
	}
	
	@Override
	public void nextFrame(StageGameState b) {
		if (frameCount >= 30) {
			
			// calculate transformations
			Vector3f rotation = character.rotationVector;
			float angle = FastMath.atan2(rotation.x, rotation.z);
			Vector3f translation = new Vector3f(2.5f*FastMath.sin(angle), 4.4f, 2.5f*FastMath.cos(angle));
			
			ElectroBeam beam = (ElectroBeam)LoadingQueue.quickLoad(new ElectroBeam(getUsers()), b);
			
			beam.rotate(rotation);
			beam.setVelocity(rotation.mult(speed));
			beam.model.setLocalTranslation(character.model.getWorldTranslation().add(translation));
			
			b.getRootNode().updateRenderState();
			
			finish();
		}
	}
	
	@Override
	public void finish() {
		super.finish();
		character.jumpLock = false;
		character.animationLock = false;
		character.tagLock = false;
	}
	
}
