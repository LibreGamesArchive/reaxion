package com.googlecode.reaxion.game.attack;

import java.util.ArrayList;

import com.googlecode.reaxion.game.model.Model;
import com.googlecode.reaxion.game.model.attackobject.AttackObject;
import com.googlecode.reaxion.game.model.attackobject.Reflector;
import com.googlecode.reaxion.game.state.StageGameState;
import com.googlecode.reaxion.game.util.LoadingQueue;
import com.jme.math.FastMath;
import com.jme.math.Vector3f;

/**
 * Raises a barrier that reflects projectiles
 */
public class ShieldReflega extends Attack {
	
	private static final int duration = 100;
	private Reflector reflector;
	
	private Vector3f initPos;
	
	public ShieldReflega() {
		name = "Reflega";
		gaugeCost = 12;
	}
	
	public ShieldReflega(AttackData ad) {
		super(ad, 12);
		name = "Reflega";
		validateGround();
	}
	
	public static void load() {
		LoadingQueue.push(new Model(Reflector.filename));
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
		
		reflector = (Reflector)LoadingQueue.quickLoad(new Reflector(getUsers()), b);
		
		reflector.rotate(rotation);
		reflector.model.setLocalTranslation(character.model.getWorldTranslation().add(translation));
		
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
        	if (c == reflector) {
        		// cancel the damage and flinch and reflect the attack
        		other.setVelocity(other.getVelocity().negate());
        		if (other instanceof AttackObject) {
        			other.users = new ArrayList<Model>();
        			other.users.add(character);
        		}
            	return;
        	}
        }
	}
	
	@Override
	public void finish() {
		super.finish();
		if (reflector != null)
			reflector.cancel();
		character.moveLock = false;
		character.jumpLock = false;
		character.animationLock = false;
	}
	
}
