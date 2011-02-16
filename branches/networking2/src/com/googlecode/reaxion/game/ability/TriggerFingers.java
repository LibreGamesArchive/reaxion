package com.googlecode.reaxion.game.ability;

import com.googlecode.reaxion.game.model.attackobject.Bullet;
import com.googlecode.reaxion.game.model.character.Character;
import com.googlecode.reaxion.game.state.StageGameState;
import com.googlecode.reaxion.game.util.LoadingQueue;
import com.jme.math.FastMath;
import com.jme.math.Vector3f;

public class TriggerFingers extends Ability {
	
	private static final float bulletSpeed = 4;
	private static final float chance = .004f;
	
	public TriggerFingers() {
		super("Trigger Fingers");
		description = "Occasionally will fire bullets automatically.";
	}
	
	@Override
	public boolean act(Character c, StageGameState b) {
		if (FastMath.nextRandomFloat() <= chance) {
			System.out.println(c.model+" shot without warning!");
			
			// fire a bullet
			Vector3f rotation = c.rotationVector;
			float angle = FastMath.atan2(rotation.x, rotation.z);
			Vector3f translation = new Vector3f(FastMath.sin(angle), 3.7f, FastMath.cos(angle));
			
			Bullet bullet = (Bullet)LoadingQueue.quickLoad(new Bullet(c), b);
			
			bullet.rotate(rotation);
			bullet.setVelocity(rotation.mult(bulletSpeed));
			bullet.model.setLocalTranslation(c.model.getWorldTranslation().add(translation));
			
			b.getRootNode().updateRenderState();
			
			activate(c, b);
		}
		
		return false;
	}
	
}