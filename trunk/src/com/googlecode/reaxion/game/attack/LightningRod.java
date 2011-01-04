package com.googlecode.reaxion.game.attack;

import com.googlecode.reaxion.game.model.Model;
import com.googlecode.reaxion.game.model.attackobject.ElecStream;
import com.googlecode.reaxion.game.state.StageGameState;
import com.googlecode.reaxion.game.util.LoadingQueue;
import com.jme.math.FastMath;
import com.jme.math.Vector3f;

public class LightningRod extends Attack {
	
	private static final String n = "Lightning Rod";
	private static final int gc = 5;
	
	private static final float speed = 4;
	
	public LightningRod() {
		name = n;
		gaugeCost = gc;
		description="Channels distant electricity towards the user.";
	}
	
	public LightningRod(AttackData ad) {
		super(ad, gc);
		name = n;
	}
	
	public static void load() {
		LoadingQueue.push(new Model(ElecStream.filename));
	}
	
	@Override
	public void firstFrame(StageGameState b) {
		character.jumpLock = true;
		character.tagLock = true;
		character.animationLock = true;
		character.play("shootUp", b.tpf);
	}
	
	@Override
	public void nextFrame(StageGameState b) {
		if (phase == 0 && character.play("shootUp", b.tpf)) {
			
			// calculate transformations
			Vector3f rotation = character.rotationVector;
			float angle = FastMath.atan2(rotation.x, rotation.z);
			Vector3f translation = new Vector3f(400*FastMath.sin(angle), 3.7f, 400*FastMath.cos(angle));
			
			ElecStream e = (ElecStream)LoadingQueue.quickLoad(new ElecStream(getUsers()), b);
			
			e.model.setLocalScale(new Vector3f(2, 2, 4));
			e.rotate(rotation);
			e.setVelocity(rotation.mult(-speed));
			e.model.setLocalTranslation(character.model.getWorldTranslation().add(translation));
			
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
		character.tagLock = false;
		character.animationLock = false;
	}
	
}
