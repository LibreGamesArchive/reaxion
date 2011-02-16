package com.googlecode.reaxion.game.attack;

import com.googlecode.reaxion.game.model.Model;
import com.googlecode.reaxion.game.model.attackobject.Fireball;
import com.googlecode.reaxion.game.model.attackobject.LivingShadow;
import com.googlecode.reaxion.game.state.StageGameState;
import com.googlecode.reaxion.game.util.LoadingQueue;
import com.jme.math.FastMath;
import com.jme.math.Vector3f;

/**
 * Summon a shadow clone to pursue the target.
 * Damage rises with that the player receives after creating the shadow.
 */
public class ShadowTag extends Attack {
	
	private static final String n = "Shadow Tag";
	private static final int gc = 17;
	
	private LivingShadow shadow;
	
	public ShadowTag() {
		name = n;
		gaugeCost = gc;
		description = "Summon a shadow clone to pursue the target.";
	}
	
	public ShadowTag(AttackData ad) {
		super(ad, gc);
		name = n;
		validateGround();
	}
	
	public static void load() {
		LoadingQueue.push(new Model(LivingShadow.filename));
	}
	
	@Override
	public void firstFrame(StageGameState b) {
		character.moveLock = true;
		character.jumpLock = true;
		character.tagLock = true;
		character.animationLock = true;
		character.play("command", b.tpf);
	}
	
	@Override
	public void nextFrame(StageGameState b) {
		if (phase == 0 && character.play("command", b.tpf)) {
			//change animation
			character.play("smite", b.tpf);
			phase++;
		} else if (phase == 1 && character.play("smite", b.tpf)) {
			// calculate transformations
			Vector3f rotation = character.rotationVector;
			float angle = FastMath.atan2(rotation.x, rotation.z);
			
			Vector3f translation = new Vector3f(3*FastMath.sin(angle), 0, 3*FastMath.cos(angle));
			
			shadow = (LivingShadow)LoadingQueue.quickLoad(new LivingShadow(getUsers()), b);
			shadow.model.setLocalTranslation(character.model.getWorldTranslation().add(translation));
			
			b.getRootNode().updateRenderState();
			
			finish();
		}
	}
	
	@Override
	public void finish() {
		super.finish();
		character.moveLock = false;
		character.jumpLock = false;
		character.tagLock = false;
		character.animationLock = false;
	}
	
}