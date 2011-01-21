package com.googlecode.reaxion.game.attack;

import com.googlecode.reaxion.game.model.Model;
import com.googlecode.reaxion.game.model.attackobject.Bullet;
import com.googlecode.reaxion.game.state.StageGameState;
import com.googlecode.reaxion.game.util.LoadingQueue;
import com.jme.math.FastMath;
import com.jme.math.Vector3f;

public class BulletStorm extends Attack {

	private float bulletSpeed = ShootBullet.getBulletSpeed() * 2;
	private int maxBullets = 100;
	private int height = 40;
	private int riseSpeed = 1;
	private int delay = 1;
	
	private boolean oldGravity;
	
	public BulletStorm() {
		info.name = "Bullet Storm";
		info.gaugeCost = 18;	
	}
	
	public BulletStorm(AttackData ad) {
		super(ad, 2);
		info.name = "Bullet Storm";
	}

	public static void load() {
		LoadingQueue.push(new Model(Bullet.filename));
	}
	
	@Override
	public void firstFrame(StageGameState b) {
		character.moveLock = true;
		character.jumpLock = true;
		character.animationLock = true;
		
		oldGravity = character.gravitate;
		character.gravitate = false;
		character.setVelocity(new Vector3f(0, 0, 0));
		character.play("shootUp", b.tpf);
	}

	@Override
	public void nextFrame(StageGameState b) {
		if (character.play("shootUp", b.tpf) && phase < maxBullets + 2 && frameCount % delay == 0) {
			Vector3f rotation = character.rotationVector;
			float angle = FastMath.atan2(rotation.x, rotation.z);
			Vector3f translation = new Vector3f(-1*FastMath.sin(angle), 3.7f, -1*FastMath.cos(angle));
			
			Bullet bullet = (Bullet) LoadingQueue.quickLoad(new Bullet(getUsers()), b);
			
			bullet.rotate();
			bullet.setVelocity(rotation.mult(bulletSpeed));
			bullet.model.setLocalTranslation(character.model.getWorldTranslation().add(translation));
			
			b.getRootNode().updateRenderState();
			
			character.play("shootDown", b.tpf);
			phase++;
		} else if (phase >= maxBullets + 1) {
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
