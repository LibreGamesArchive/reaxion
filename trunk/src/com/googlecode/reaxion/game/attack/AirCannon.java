package com.googlecode.reaxion.game.attack;

import com.googlecode.reaxion.game.model.Model;
import com.googlecode.reaxion.game.model.attackobject.AirBurst;
import com.googlecode.reaxion.game.model.attackobject.Bubble;
import com.googlecode.reaxion.game.state.StageGameState;
import com.googlecode.reaxion.game.util.LoadingQueue;
import com.jme.math.FastMath;
import com.jme.math.Vector3f;

public class AirCannon extends Attack {
	
	private static final String n = "Air Cannon";
	private static final int gc = 8;
	
	private static final float speed = 1.5f;
	
	public AirCannon() {
		name = n;
		gaugeCost = gc;
		description = "Blows forth a sphere of pressurized air.";
	}
	
	public AirCannon(AttackData ad) {
		super(ad, gc);
		name = n;
		validateGround();
	}
	
	public static void load() {
		LoadingQueue.push(new Model(AirBurst.filename));
	}
	
	@Override
	public void firstFrame(StageGameState b) {
		character.moveLock = true;
		character.jumpLock = true;
		character.animationLock = true;
		character.play("blowDown", b.tpf);
	}
	
	@Override
	public void nextFrame(StageGameState b) {
		if (phase == 0 && character.play("blowDown", b.tpf)) {
			// calculate transformations
			Vector3f rotation = character.rotationVector;
			float angle = FastMath.atan2(rotation.x, rotation.z);
			
			Vector3f translation = new Vector3f(3f*FastMath.sin(angle), 3.7f, 3f*FastMath.cos(angle));
			
			AirBurst a = (AirBurst)LoadingQueue.quickLoad(new AirBurst(getUsers()), b);
			a.setVelocity( new Vector3f(speed*FastMath.sin(angle), 0, speed*FastMath.cos(angle)));
			a.rotate(rotation);
			
			a.model.setLocalTranslation(character.model.getWorldTranslation().add(translation));
			
			b.getRootNode().updateRenderState();
			
			character.play("blowUp", b.tpf);
			phase++;
			
		} else if (phase == 1 && character.play("blowUp", b.tpf)) {
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