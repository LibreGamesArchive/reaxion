package com.googlecode.reaxion.game.attack;

import com.googlecode.reaxion.game.model.Model;
import com.googlecode.reaxion.game.model.attackobject.AttackObject;
import com.googlecode.reaxion.game.model.attackobject.DashForce;
import com.googlecode.reaxion.game.model.attackobject.RushSpiral;
import com.googlecode.reaxion.game.state.StageGameState;
import com.googlecode.reaxion.game.util.LoadingQueue;
import com.jme.math.Vector3f;

public class ShadowRush extends Attack {
	
	private static final String n = "";
	private static final int gc = 0;
	
	private static final int dashSpeed = 8;
	
	private Vector3f prevPos = new Vector3f();
	private boolean massChanged = false;
	
	private RushSpiral force;
	
	public ShadowRush() {
		name = n;
		gaugeCost = gc;
		description = "";
	}
	
	public ShadowRush(AttackData ad) {
		super(ad, gc);
		name = n;
	}
	
	public static void load() {
		
	}
	
	@Override
	public void firstFrame(StageGameState b) {
		character.moveLock = true;
		character.animationLock = true;
		character.tagLock = true;
		character.mass *= 2;
		massChanged = true;
		character.play("gaurd", b.tpf);
	}
	
	@Override
	public void nextFrame(StageGameState b) {
		if (phase == 0) {
			// add the dash force
			force = (RushSpiral)LoadingQueue.quickLoad(new RushSpiral(getUsers()), b);
			force.rotationVector = character.rotationVector.mult(new Vector3f(1, 0, 1));
			b.getRootNode().updateRenderState();
			character.setVelocity(character.rotationVector.mult(dashSpeed));
			prevPos = character.model.getLocalTranslation().clone();
			phase++;
			
		} else if (phase == 1) {
			// check for boundary
			if (Math.abs(character.model.getLocalTranslation().distance(prevPos) - dashSpeed) > .001) {
				phase++;
			} else {
				character.setVelocity(character.rotationVector.mult(dashSpeed));
				prevPos = character.model.getLocalTranslation().clone();
			}
			
		} else if (phase == 2) {	
			finish();
		}
	}
	
	@Override
	public void interrupt(StageGameState b, Model other) {
		// negate damage, this attack cannot be interrupted
        
        // reciprocate the hit
		if (other instanceof AttackObject)
			((AttackObject)other).hit(b, character);
	}
	
	@Override
	public void finish() {
		super.finish();
		if (force != null)
			force.cancel();
		character.moveLock = false;
		character.animationLock = false;
		character.tagLock = false;
		if (massChanged)
			character.mass /= 2;
	}
	
}
