package com.googlecode.reaxion.game.attack;

import com.googlecode.reaxion.game.model.Model;
import com.googlecode.reaxion.game.model.attackobject.Bubble;
import com.googlecode.reaxion.game.state.StageGameState;
import com.googlecode.reaxion.game.util.LoadingQueue;
import com.jme.math.FastMath;
import com.jme.math.Vector3f;

/**
 * Blows forth a bubble that swells in size.
 */
public class SpawnBubble extends Attack {
	
	private static final String n = "Bubble";
	private static final int gc = 1;
	
	private static final float speed = .25f;
	private Bubble bubble;
	
	public SpawnBubble() {
		name = n;
		gaugeCost = gc;
		description = "Blows forth a bubble that swells in size.";
	}
	
	public SpawnBubble(AttackData ad) {
		super(ad, gc);
		name = n;
		validateGround();
	}
	
	public static void load() {
		LoadingQueue.push(new Model(Bubble.filename));
	}
	
	@Override
	public void firstFrame(StageGameState b) {
		character.moveLock = true;
		character.jumpLock = true;
		character.animationLock = true;
		character.play("blowDown", b.tpf);
	}
	
	@Override
	public void nextFrame(StageGameState b) {
		if (phase == 0 && character.play("blowDown", b.tpf)) {
			//change animation
			character.play("blow", b.tpf);
			phase++;
			
			// calculate transformations
			Vector3f rotation = character.rotationVector;
			float angle = FastMath.atan2(rotation.x, rotation.z);
			
			Vector3f translation = new Vector3f(3f*FastMath.sin(angle), 3.7f, 3f*FastMath.cos(angle));
			
			bubble = (Bubble)LoadingQueue.quickLoad(new Bubble(getUsers()), b);
			
			bubble.model.setLocalTranslation(character.model.getWorldTranslation().add(translation));
			
			b.getRootNode().updateRenderState();
			
		} else if (phase == 1 && (bubble.model.getLocalScale().x == 1 || bubble.model.getParent() == null)) {
			// set the bubble's velocity
			Vector3f rotation = character.rotationVector;
			
			bubble.rotate(rotation);
			bubble.setVelocity(rotation.mult(speed));
			
			character.play("blowUp", b.tpf);
			phase++;
			
		} else if (phase == 2 && character.play("blowUp", b.tpf)) {
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