package com.googlecode.reaxion.game.attack;

import com.googlecode.reaxion.game.audio.SoundEffectType;
import com.googlecode.reaxion.game.model.Model;
import com.googlecode.reaxion.game.model.attackobject.ScreenCard;
import com.googlecode.reaxion.game.model.attackobject.ThrowCard;
import com.googlecode.reaxion.game.state.StageGameState;
import com.googlecode.reaxion.game.util.LoadingQueue;
import com.jme.math.FastMath;
import com.jme.math.Vector3f;

public class CardThrow extends Attack {	
	private static final String n = "Card Throw";
	private static final int gc = 4;
	
	private static final SoundEffectType[] sfxTypes = {SoundEffectType.ATTACK_CARD_THROW};
	
	private ThrowCard card;
	
	public CardThrow() {
		name = n;
		gaugeCost = gc;
		description = "Launches a spinning, curving card at high speed.";
	}
	
	public CardThrow(AttackData ad) {
		super(ad, gc);
		name = n;
	}
	
	public static void load() {
		LoadingQueue.push(new Model(ScreenCard.filename));
	}
	
	@Override
	public void firstFrame(StageGameState b) {
		character.jumpLock = true;
		character.tagLock = true;
		character.animationLock = true;
		character.play("command", b.tpf);
	}
	
	@Override
	public void nextFrame(StageGameState b) {
		if (phase == 0 && character.play("command", b.tpf)) {
			
			triggerSoundEffect(sfxTypes, false);
			
			// calculate transformations
			Vector3f rotation = character.rotationVector;
			float angle = FastMath.atan2(rotation.x, rotation.z);
			Vector3f translation = new Vector3f(8*FastMath.sin(angle), 3.7f, 8*FastMath.cos(angle));
			
			card = (ThrowCard)LoadingQueue.quickLoad(new ThrowCard(getUsers()), b);
			card.model.setLocalTranslation(character.model.getWorldTranslation().add(translation));
			card.dir = Math.round(FastMath.nextRandomFloat())*2 - 1;
			card.angle = FastMath.PI/4 * card.dir + FastMath.PI/4;
			
			b.getRootNode().updateRenderState();
			
			finish();
		}
	}
	
	@Override
	public void finish() {
		super.finish();
		character.jumpLock = false;
		character.animationLock = false;
		character.tagLock = false;
	}
	
}
