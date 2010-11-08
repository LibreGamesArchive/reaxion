package com.googlecode.reaxion.game.attack;

import com.googlecode.reaxion.game.audio.AudioPlayer;
import com.googlecode.reaxion.game.audio.SoundEffectType;
import com.googlecode.reaxion.game.model.Model;
import com.googlecode.reaxion.game.model.attackobject.Lightning;
import com.googlecode.reaxion.game.model.attackobject.SeekingCloud;
import com.googlecode.reaxion.game.model.character.MajorCharacter;
import com.googlecode.reaxion.game.state.StageGameState;
import com.googlecode.reaxion.game.util.LoadingQueue;
import com.jme.math.Vector3f;

/**
 * Spawns a cloud of lightning that seeks the opponent.
 */
public class LightningCloud extends Attack {
	private static final String n = "Elec Cloud";
	private static final int gc = 10;
	private static final SoundEffectType[] sfxTypes = {SoundEffectType.ATTACK_LIGHTNING_CLOUD};
	
	private Model target;
	private SeekingCloud cloud;
	
	public LightningCloud() {
		name = n;
		gaugeCost = gc;
	}
	
	public LightningCloud(AttackData ad) {
		super(ad, gc);
		name = n;
		target = ad.target;
	}
	
	public static void load() {
		LoadingQueue.push(new Model(SeekingCloud.filename));
		LoadingQueue.push(new Model(Lightning.filename));
	}
	
	@Override
	public void firstFrame(StageGameState b) {
		character.moveLock = true;
		character.jumpLock = true;
		character.animationLock = true;
		character.play("heaveUp", b.tpf);
	}
	
	@Override
	public void nextFrame(StageGameState b) {
		if (phase == 0 && character.play("heaveUp", b.tpf)) {
			
			cloud = (SeekingCloud)LoadingQueue.quickLoad(new SeekingCloud(getUsers()), b);
			cloud.target = target;
			
			cloud.model.setLocalTranslation(character.model.getWorldTranslation().add(new Vector3f(0, 7, 0)));
			
			b.getRootNode().updateRenderState();
			character.play("heave", b.tpf);
			
			triggerSoundEffect(sfxTypes, false);
			
			phase++;
			
		} else if (phase == 1 && frameCount >= 60) {
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
