package com.googlecode.reaxion.game.attack;

import com.googlecode.reaxion.game.model.Model;
import com.googlecode.reaxion.game.model.attackobject.Barrier;
import com.googlecode.reaxion.game.state.StageGameState;
import com.googlecode.reaxion.game.util.LoadingQueue;
import com.jme.math.FastMath;
import com.jme.math.Vector3f;

/**
 * Raises a barrier that cuts down damage
 */
public class ShieldBarrier extends Attack {
	
	private static final int duration = 180;
	private Barrier barrier;
	
	private Vector3f initPos;
	
	public ShieldBarrier() {
		name = "Barrier";
		gaugeCost = 2;
		description = "Raises a barrier that cuts down damage";
	}
	
	public ShieldBarrier(AttackData ad) {
		super(ad, 2);
		name = "Barrier";
		validateGround();
	}
	
	public static void load() {
		LoadingQueue.push(new Model(Barrier.filename));
	}
	
	@Override
	public void firstFrame(StageGameState b) {
		character.moveLock = true;
		character.jumpLock = true;
		character.animationLock = true;
		
		character.play("guard");
		character.setVelocity(new Vector3f(0, 0, 0));
		
		// calculate transformations
		Vector3f rotation = character.rotationVector;
		float angle = FastMath.atan2(rotation.x, rotation.z);
		Vector3f translation = new Vector3f(1.5f*FastMath.sin(angle), 0, 1.5f*FastMath.cos(angle));
		
		barrier = (Barrier)LoadingQueue.quickLoad(new Barrier(getUsers()), b);
		
		barrier.rotate(rotation);
		barrier.model.setLocalTranslation(character.model.getWorldTranslation().add(translation));
		
		initPos = character.model.getLocalTranslation().clone();
		
		b.getRootNode().updateRenderState();
	}
	
	@Override
	public void nextFrame(StageGameState b) {
		if (frameCount >= duration || !character.model.getLocalTranslation().equals(initPos)) {
			finish();
		}
	}
	
	@Override
	public void interrupt(StageGameState b, Model other) {
		// check if the interrupting object passed through the barrier
        Model[] collisions = other.getLinearModelCollisions(b, other.getVelocity().normalize().mult(-1.5f), .02f);
        for (Model c : collisions) {
        	if (c == barrier) {
        		// third the damage and no flinch!
            	character.hp -= other.getDamage()/3;
            	//System.out.println(character.model+" hit by "+other+": "+(character.hp+other.damagePerFrame/2)+" -> "+character.hp);
            	return;
        	}
        }
        character.reactHit(b, other);
	}
	
	@Override
	public void finish() {
		super.finish();
		if (barrier != null)
			barrier.cancel();
		character.moveLock = false;
		character.jumpLock = false;
		character.animationLock = false;
	}
	
}
