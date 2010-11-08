package com.googlecode.reaxion.game.attack;

import com.googlecode.reaxion.game.audio.AudioPlayer;
import com.googlecode.reaxion.game.audio.SoundEffectType;
import com.googlecode.reaxion.game.model.Model;
import com.googlecode.reaxion.game.model.attackobject.DarkPit;
import com.googlecode.reaxion.game.model.attackobject.DarkSpike;
import com.googlecode.reaxion.game.model.attackobject.Fireball;
import com.googlecode.reaxion.game.model.attackobject.LivingShadow;
import com.googlecode.reaxion.game.state.StageGameState;
import com.googlecode.reaxion.game.util.LoadingQueue;
import com.jme.math.FastMath;
import com.jme.math.Vector3f;

/**
 * Creates a black hole under the target's feet that robs HP.
 */
public class BlackHole extends Attack {
	
	private static final String n = "Black Hole";
	private static final int gc = 20;
	private static final SoundEffectType[] sfxTypes = {SoundEffectType.ATTACK_BLACK_HOLE};
	
	private Model target;
	
	public BlackHole() {
		name = n;
		gaugeCost = gc;
	}
	
	public BlackHole(AttackData ad) {
		super(ad, gc);
		name = n;
		validateGround();
		target = ad.target;
	}
	
	public static void load() {
		LoadingQueue.push(new Model(DarkPit.filename));
	}
	
	@Override
	public void firstFrame(StageGameState b) {
		character.moveLock = true;
		character.jumpLock = true;
		character.animationLock = true;
		character.play("smite", b.tpf);
		triggerSoundEffect(sfxTypes, true);
	}
	
	@Override
	public void nextFrame(StageGameState b) {
		if (character.play("smite", b.tpf)) {
			
			DarkPit dp = (DarkPit)LoadingQueue.quickLoad(new DarkPit(getUsers()), b);
			dp.model.setLocalTranslation(target.model.getLocalTranslation().mult(new Vector3f(1, 0, 1)));
			
			b.getRootNode().updateRenderState();
			
			finish();
		}
	}
	
	@Override
	public void finish() {
		super.finish();
		stopSoundEffect(sfxTypes);
		character.moveLock = false;
		character.jumpLock = false;
		character.animationLock = false;
	}
	
}