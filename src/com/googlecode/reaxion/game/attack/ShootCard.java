package com.googlecode.reaxion.game.attack;

import com.googlecode.reaxion.game.model.Model;
import com.googlecode.reaxion.game.model.attackobject.StraightCard;
import com.googlecode.reaxion.game.model.attackobject.ThrowCard;
import com.googlecode.reaxion.game.state.StageGameState;
import com.googlecode.reaxion.game.util.LoadingQueue;
import com.jme.math.FastMath;
import com.jme.math.Vector3f;

public class ShootCard extends Attack {
	
	private static final String n = "Straight";
	private static final int gc = 4;
	private static final float speed = 3;
	private StraightCard card;
	
	public ShootCard() {
		name = n;
		description="Throws a card towards the target.";
		gaugeCost = gc;
	}
	
	public ShootCard(AttackData ad) {
		super(ad, gc);
		name = n;
	}
	
	public static void load() {
		LoadingQueue.push(new Model(ThrowCard.filename));
	}
	
	@Override
	public void firstFrame(StageGameState b) {
		character.jumpLock = true;
		character.animationLock = true;
		character.tagLock = true;
		character.play("shootUp", b.tpf);
	}
	
	@Override
	public void nextFrame(StageGameState b) {
		if (phase == 0 && character.play("shootUp", b.tpf)) {
			
			// calculate transformations
			Vector3f rotation = character.rotationVector;
			float angle = FastMath.atan2(rotation.x, rotation.z);
			Vector3f translation = new Vector3f(-1*FastMath.sin(angle), 3.7f, -1*FastMath.cos(angle));
			
			card = (StraightCard)LoadingQueue.quickLoad(new StraightCard(getUsers()), b);
			card.yaw = FastMath.PI/2;
			
			card.rotate(rotation);
			card.setVelocity(rotation.mult(speed));
			card.model.setLocalTranslation(character.model.getWorldTranslation().add(translation));
			
			b.getRootNode().updateRenderState();
			
			character.play("shootDown", b.tpf);
			phase++;
		} else if (phase == 1 && character.play("shootDown", b.tpf)) {
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
