package com.googlecode.reaxion.game.attack;

import org.eclipse.swt.internal.win32.ACTCTX;

import com.googlecode.reaxion.game.model.Model;
import com.googlecode.reaxion.game.model.attackobject.Bullet;
import com.googlecode.reaxion.game.model.attackobject.HomingBullet;
import com.googlecode.reaxion.game.state.StageGameState;
import com.googlecode.reaxion.game.util.LoadingQueue;
import com.jme.math.FastMath;
import com.jme.math.Vector3f;

/**
 * Fires an energy bullet towards the target
 */
public class ShootBullet extends Attack {
	
	private HomingBullet bullet;
	private Model target;
	
	public ShootBullet() {
		name = "Shoot";
		description="Fires an energy bullet towards the target.";
		gaugeCost = 3;
	}
	
	public ShootBullet(AttackData ad) {
		super(ad, 2);
		name = "Shoot";
		target = ad.target;
	}
	
	public static void load() {
		LoadingQueue.push(new Model(Bullet.filename));
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
			Vector3f translation = new Vector3f(-1*FastMath.sin(angle), 3.7f, -1*FastMath.cos(angle));
			
			bullet = (HomingBullet)LoadingQueue.quickLoad(new HomingBullet(getUsers(), rotation), b);
			
			bullet.setVelocity(rotation.mult(4f));
			bullet.rotate(rotation);
			bullet.target = target;
			bullet.model.setLocalTranslation(character.model.getWorldTranslation().add(translation));
			
			b.getRootNode().updateRenderState();
			
			/*			
			// check the point blank case, between the character and the bullet's actual creation point
	        Model[] collisions = bullet.getLinearModelCollisions(b, new Vector3f(-translation.x, 0, -translation.z), 1);
	        for (Model c : collisions) {
	        	if (c instanceof Character && !bullet.users.contains(c)) {
	        		((Character)c).hit(b, bullet);
	        		bullet.hit(b, ((Character)c));
	        	}
	        }
	        */
			
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
