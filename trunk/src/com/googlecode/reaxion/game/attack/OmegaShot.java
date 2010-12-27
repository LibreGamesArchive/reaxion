package com.googlecode.reaxion.game.attack;

import com.googlecode.reaxion.game.model.Model;
import com.googlecode.reaxion.game.model.attackobject.OmegaBullet;
import com.googlecode.reaxion.game.state.StageGameState;
import com.googlecode.reaxion.game.util.LoadingQueue;
import com.jme.math.FastMath;
import com.jme.math.Vector3f;

public class OmegaShot extends Attack {
	
	private static final String n = "Omega Shot";
	private static final int gc = 12;
	
	public OmegaShot() {
		name = n;
		gaugeCost = gc;
		description = "Fires an enormous mass of energy.";
	}
	
	public OmegaShot(AttackData ad) {
		super(ad, gc);
		name = n;
	}
	
	public static void load() {
		LoadingQueue.push(new Model(OmegaBullet.filename));
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
			Vector3f translation = new Vector3f(FastMath.sin(angle), 3.7f, FastMath.cos(angle));
			
			OmegaBullet bullet = (OmegaBullet)LoadingQueue.quickLoad(new OmegaBullet(getUsers()), b);
			
			bullet.rotate(rotation);
			bullet.setVelocity(rotation);
			bullet.model.setLocalTranslation(character.model.getWorldTranslation().add(translation));
			
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
		character.tagLock = true;
		character.animationLock = false;
	}
	
}
