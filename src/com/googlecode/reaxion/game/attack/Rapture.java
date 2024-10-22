package com.googlecode.reaxion.game.attack;

import com.googlecode.reaxion.game.model.Model;
import com.googlecode.reaxion.game.model.attackobject.AttackObject;
import com.googlecode.reaxion.game.model.attackobject.DashForce;
import com.googlecode.reaxion.game.model.attackobject.LightDrill;
import com.googlecode.reaxion.game.state.StageGameState;
import com.googlecode.reaxion.game.util.LoadingQueue;
import com.jme.math.FastMath;
import com.jme.math.Vector3f;

public class Rapture extends Attack {
	
	private static final String n = "Hand Sonic";
	private static final int gc = 11;
	
	private static final int duration = 34;
	private static final int dashSpeed = 4;
	
	private LightDrill drill;
	
	private boolean stopped = false;
	
	public Rapture() {
		name = n;
		gaugeCost = gc;
		description = "Drills the opponent with light, sapping them of Gauge.";
	}
	
	public Rapture(AttackData ad) {
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
		
		character.play("halt", b.tpf);
		
		// add the drill
		drill = (LightDrill)LoadingQueue.quickLoad(new LightDrill(getUsers()), b);
		b.getRootNode().updateRenderState();
	}
	
	@Override
	public void nextFrame(StageGameState b) {
		if (phase == 0) {
			if (frameCount < duration) {
				if (!drill.strike)
					character.setVelocity(character.rotationVector.mult(dashSpeed*FastMath.pow((float)(duration - frameCount)/duration, 2.5f)));
				else
					if (!stopped) {
						stopped = true;
						character.model.setLocalTranslation(character.model.getLocalTranslation().add(
								character.rotationVector.mult(-dashSpeed*FastMath.pow((float)(duration - (frameCount-1))/duration, 2.5f))));
					} else
						character.setVelocity(new Vector3f());
			} else {
				finish();
			}
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
		if (drill != null)
			drill.cancel();
		character.moveLock = false;
		character.animationLock = false;
		character.tagLock = false;
	}
	
}
