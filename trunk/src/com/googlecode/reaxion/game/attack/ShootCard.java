package com.googlecode.reaxion.game.attack;

import com.googlecode.reaxion.game.model.Model;
import com.googlecode.reaxion.game.model.attackobject.Bullet;
import com.googlecode.reaxion.game.model.attackobject.ThrowCard;
import com.googlecode.reaxion.game.state.StageGameState;
import com.googlecode.reaxion.game.util.LoadingQueue;
import com.jme.math.FastMath;
import com.jme.math.Vector3f;

/**
 * Fires an energy bullet towards the target
 */
public class ShootCard extends Attack {
	
	private static final String n = "Elec Cloud";
	private static final int gc = 10;
	private static final float bulletSpeed = 5;
	private ThrowCard card;
	
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
		character.play("shootUp", b.tpf);
	}
	
	@Override
	public void nextFrame(StageGameState b) {
		if (phase == 0 && character.play("shootUp", b.tpf)) {
			
			// calculate transformations
			Vector3f rotation = character.rotationVector;
			float angle = FastMath.atan2(rotation.x, rotation.z);
			Vector3f translation = new Vector3f(-1*FastMath.sin(angle), 3.7f, -1*FastMath.cos(angle));
			
			card = (ThrowCard)LoadingQueue.quickLoad(new ThrowCard(getUsers()), b);
			card.yaw = FastMath.PI/2;
			
			card.rotate(rotation);
			card.setVelocity(rotation.mult(bulletSpeed));
			card.model.setLocalTranslation(character.model.getWorldTranslation().add(translation));
			
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
		character.animationLock = false;
	}
	
}
