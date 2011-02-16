package com.googlecode.reaxion.game.attack;

import com.googlecode.reaxion.game.model.Model;
import com.googlecode.reaxion.game.model.attackobject.Bubble;
import com.googlecode.reaxion.game.model.attackobject.Bullet;
import com.googlecode.reaxion.game.model.attackobject.Chain;
import com.googlecode.reaxion.game.state.StageGameState;
import com.googlecode.reaxion.game.util.LoadingQueue;
import com.jme.math.FastMath;
import com.jme.math.Vector3f;
import com.radakan.jme.mxml.anim.MeshAnimationController;

public class BulletWave extends Attack {
	
	private static final String n = "Bullet Wave";
	private static final int gc = 6;
	private static final float bulletSpeed = 4;
	
	private Bullet[] bullet = new Bullet[5];
	
	public BulletWave() {
		name = n;
		gaugeCost = gc;
		description = "Releases a wave of bullets forward.";
	}
	
	public BulletWave(AttackData ad) {
		super(ad, gc);
		name = n;
	}
	
	public static void load() {
		LoadingQueue.push(new Model(Chain.filename));
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
			
			phase++;
			
			Vector3f rotation = character.rotationVector;
			
			// make the bullets fly
			for (int i=0; i<bullet.length; i++) {
				float angle = FastMath.atan2(rotation.x, rotation.z) + 2f/3f*(float)i*FastMath.PI/(float)bullet.length - FastMath.PI/3f;
				Vector3f translation = new Vector3f(FastMath.sin(angle), 3.7f, FastMath.cos(angle));			
				bullet[i] = (Bullet)LoadingQueue.quickLoad(new Bullet(getUsers()), b);
				bullet[i].setVelocity(new Vector3f(FastMath.sin(angle), 0, FastMath.cos(angle)).mult(bulletSpeed));
				bullet[i].rotate(bullet[i].getVelocity());
				bullet[i].model.setLocalTranslation(character.model.getWorldTranslation().add(translation));
			}
			
			b.getRootNode().updateRenderState();
			
			character.play("shootDown", b.tpf);
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
