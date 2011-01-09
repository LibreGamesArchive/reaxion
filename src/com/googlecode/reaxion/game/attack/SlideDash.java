package com.googlecode.reaxion.game.attack;

import com.googlecode.reaxion.game.model.Model;
import com.googlecode.reaxion.game.model.attackobject.AttackObject;
import com.googlecode.reaxion.game.model.attackobject.Barrier;
import com.googlecode.reaxion.game.model.attackobject.DashForce;
import com.googlecode.reaxion.game.model.attackobject.Starlight;
import com.googlecode.reaxion.game.state.StageGameState;
import com.googlecode.reaxion.game.util.LoadingQueue;
import com.jme.math.FastMath;
import com.jme.math.Vector3f;

public class SlideDash extends Attack {
	
	private static final String n = "Slide Dash";
	private static final int gc = 6;
	
	private static final int duration = 40;
	private static final int dashSpeed = 9;
	
	private DashForce force;
	
	public SlideDash() {
		name = n;
		gaugeCost = gc;
		description = "Charges forward, dealing damage to any opponents in the way.";
	}
	
	public SlideDash(AttackData ad) {
		super(ad, gc);
		name = n;
	}
	
	public static void load() {
		LoadingQueue.push(new Model(DashForce.filename));
	}
	
	@Override
	public void firstFrame(StageGameState b) {
		character.moveLock = true;
		character.animationLock = true;
		character.tagLock = true;
		
		character.play("command", b.tpf);
	}
	
	@Override
	public void nextFrame(StageGameState b) {
		if (phase == 0 && character.play("command", b.tpf)) {
			//change animation
			character.play("guard", b.tpf);
			phase++;
			
		} else if (phase == 1) {
			if (frameCount < duration) {
				// add the dash force
				if (force == null) {
					force = (DashForce)LoadingQueue.quickLoad(new DashForce(getUsers()), b);
					b.getRootNode().updateRenderState();
				}
				character.setVelocity(character.rotationVector.mult(dashSpeed*FastMath.pow((float)(duration - frameCount)/duration, 1.5f)));
			} else {
				phase++;
			}
		} else if (phase == 2) {	
			finish();
		}
	}
	
	@Override
	public void interrupt(StageGameState b, Model other) {
		// negate flinch, this attack cannot be interrupted
        character.hp -= other.getDamage()/2;
        
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
	}
	
}
