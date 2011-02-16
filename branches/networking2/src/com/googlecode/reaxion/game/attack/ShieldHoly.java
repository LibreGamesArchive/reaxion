package com.googlecode.reaxion.game.attack;

import java.util.ArrayList;

import com.googlecode.reaxion.game.model.Model;
import com.googlecode.reaxion.game.model.attackobject.AttackObject;
import com.googlecode.reaxion.game.model.attackobject.Holy;
import com.googlecode.reaxion.game.state.StageGameState;
import com.googlecode.reaxion.game.util.LoadingQueue;
import com.jme.math.FastMath;
import com.jme.math.Vector3f;

/**
 * Fires an energy bullet towards the target
 */
public class ShieldHoly extends Attack {
	
	private static final String n = "Holy";
	private static final int gc = 30;
	
	private static final int duration = 240;
	private Holy holy;
	
	public ShieldHoly() {
		name = n;
		gaugeCost = gc;
		description = "Fires an energy bullet towards the target";
	}
	
	public ShieldHoly(AttackData ad) {
		super(ad, gc);
		name = n;
		validateGround();
	}
	
	public static void load() {
		LoadingQueue.push(new Model(Holy.filename));
	}
	
	@Override
	public void firstFrame(StageGameState b) {
		character.moveLock = true;
		character.jumpLock = true;
		character.tagLock = true;
		character.animationLock = true;
		
		character.play("prayerUp");
	}
	
	@Override
	public void nextFrame(StageGameState b) {
		if (phase == 0 && character.play("prayerUp", b.tpf)) {
			//change animation
			character.play("prayer", b.tpf);
			phase++;
			
			Vector3f translation = new Vector3f(0, 3.7f, 0);
			
			holy = (Holy)LoadingQueue.quickLoad(new Holy(getUsers()), b);
			holy.model.setLocalTranslation(character.model.getWorldTranslation().add(translation));
			
			b.getRootNode().updateRenderState();
			
		} else if (frameCount == 140) {
			// unlock when holy starts to shrink
			character.moveLock = false;
			character.jumpLock = false;
			character.tagLock = false;
			character.animationLock = false;
			
		} else if (frameCount == duration) {
			// revive partner if dead
			if (b.getPartner().hp <= 0) {
				b.getPartner().hp = Math.min(10, b.getPartner().maxHp);
				b.getPartner().clearDeathFlag();
			}
			finish();
		}
	}
	
	@Override
	public void interrupt(StageGameState b, Model other) {
		// render immune to damage
		// reciprocate the hit
		if (other instanceof AttackObject)
			((AttackObject)other).hit(b, b.getPlayer());
	}
	
	@Override
	public void finish() {
		super.finish();
		if (holy != null)
			holy.cancel();
		character.moveLock = false;
		character.jumpLock = false;
		character.tagLock = false;
		character.animationLock = false;
	}
	
}
