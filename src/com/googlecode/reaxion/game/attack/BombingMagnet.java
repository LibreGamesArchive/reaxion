package com.googlecode.reaxion.game.attack;

import com.googlecode.reaxion.game.model.Model;
import com.googlecode.reaxion.game.model.attackobject.Bullet;
import com.googlecode.reaxion.game.model.attackobject.MagnetBomb;
import com.googlecode.reaxion.game.state.StageGameState;
import com.googlecode.reaxion.game.util.LoadingQueue;
import com.jme.math.FastMath;
import com.jme.math.Vector3f;

/**
 * Fires an magnetic particle forward
 */
public class BombingMagnet extends Attack {
	
	private static final String n = "Magnet Bomb";
	private static final int gc = 6;
	
	private static final float bulletSpeed = 2f;
	private MagnetBomb bomb;
	
	public BombingMagnet() {
		name = n;
		gaugeCost = gc;
		description = "Fires an magnetic particle forward";
	}
	
	public BombingMagnet(AttackData ad) {
		super(ad, gc);
		name = n;
	}
	
	public static void load() {
		LoadingQueue.push(new Model(MagnetBomb.filename));
	}
	
	@Override
	public void firstFrame(StageGameState b) {
		character.jumpLock = true;
		character.animationLock = true;
		character.play("shootUp", b.tpf);
	}
	
	@Override
	public void nextFrame(StageGameState b) {
		if (phase == 0 && character.play("shootUp", b.tpf)) {
			
			// calculate transformations
			Vector3f rotation = character.rotationVector;
			float angle = FastMath.atan2(rotation.x, rotation.z);
			Vector3f translation = new Vector3f(2*FastMath.sin(angle), 3.7f, 2*FastMath.cos(angle));
			
			bomb = (MagnetBomb)LoadingQueue.quickLoad(new MagnetBomb(getUsers()), b);
			
			bomb.rotate(rotation);
			bomb.setVelocity(rotation.mult(bulletSpeed));
			bomb.model.setLocalTranslation(character.model.getWorldTranslation().add(translation));
			
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
	}
	
}
