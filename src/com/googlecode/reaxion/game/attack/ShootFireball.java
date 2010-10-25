package com.googlecode.reaxion.game.attack;

import com.googlecode.reaxion.game.model.Model;
import com.googlecode.reaxion.game.model.attackobject.Fireball;
import com.googlecode.reaxion.game.state.StageGameState;
import com.googlecode.reaxion.game.util.LoadingQueue;
import com.jme.math.FastMath;
import com.jme.math.Vector3f;

/**
 * Spits out a fireball that flies toward the target
 */
public class ShootFireball extends Attack {
	
	private static final String n = "Fireball";
	private static final int gc = 6;
	
	private static final float speed = 3;
	private Fireball fireball;
	
	public ShootFireball() {
		name = n;
		gaugeCost = gc;
	}
	
	public ShootFireball(AttackData ad) {
		super(ad, gc);
		name = n;
		validateGround();
	}
	
	public static void load() {
		LoadingQueue.push(new Model(Fireball.filename));
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
			//change animation
			character.play("blow", b.tpf);
			phase++;
			
			// calculate transformations
			Vector3f rotation = character.rotationVector;
			float angle = FastMath.atan2(rotation.x, rotation.z);
			
			Vector3f translation = new Vector3f(3f*FastMath.sin(angle), 3.7f, 3f*FastMath.cos(angle));
			
			fireball = (Fireball)LoadingQueue.quickLoad(new Fireball(getUsers()), b);
			
			fireball.rotate(rotation);
			fireball.model.setLocalTranslation(character.model.getWorldTranslation().add(translation));
			
			b.getRootNode().updateRenderState();
			
		} else if (phase == 1 && (fireball.model.getLocalScale().x == 1 || fireball.model.getParent() == null)) {
			// set the fireball's velocity
			Vector3f rotation = character.rotationVector;
			Vector3f targetLine = b.getTarget().model.getWorldTranslation().subtract(b.getPlayer().model.getWorldTranslation()).normalize();
			
			float angle = FastMath.atan2(rotation.x, rotation.z);
			
			// make homing if within line of sight
			float targetAngle = FastMath.atan2(targetLine.x, targetLine.z);
			if (FastMath.abs(targetAngle - angle) < FastMath.PI/2) {
				rotation = targetLine;
				angle = FastMath.atan2(rotation.x, rotation.z);
			}
			
			fireball.rotate(rotation);
			fireball.setVelocity(rotation.mult(speed));
			
			character.play("blowUp", b.tpf);
			phase++;
			
		} else if (phase == 2 && character.play("blowUp", b.tpf)) {
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