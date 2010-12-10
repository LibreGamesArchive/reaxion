package com.googlecode.reaxion.game.attack;

import com.googlecode.reaxion.game.model.Model;
import com.googlecode.reaxion.game.model.character.Character;
import com.googlecode.reaxion.game.model.attackobject.Bullet;
import com.googlecode.reaxion.game.model.prop.LightFade;
import com.googlecode.reaxion.game.state.StageGameState;
import com.googlecode.reaxion.game.util.LoadingQueue;
import com.jme.math.FastMath;
import com.jme.math.Vector3f;
import com.radakan.jme.mxml.anim.MeshAnimationController;

public class BulletStorm extends Attack {

	private static final String n = "Bullet Storm";
	private static final int gc = 10;
	private static final float bulletSpeed = 4;
	
	private float maxRadius = 80;
	
	private Bullet[] bullet = new Bullet[12];
	
	private Character user;
	private Model target;
	
	public BulletStorm() {
		name = n;
		gaugeCost = gc;
		description = "Summons bullets from all angles to decimate the target.";
	}
	
	public BulletStorm(AttackData ad) {
		super(ad, gc);
		name = n;
		user = ad.character;
		target = ad.target;
	}

	public static void load() {
		LoadingQueue.push(new Model(Bullet.filename));
	}
	
	@Override
	public void firstFrame(StageGameState b) {
		character.moveLock = true;
		character.jumpLock = true;
		character.tagLock = true;
		character.animationLock = true;
		character.play("cast", b.tpf);
	}

	@Override
	public void nextFrame(StageGameState b) {
		// if animation is halfway through
		MeshAnimationController animControl = (MeshAnimationController) character.model.getController(0);
		if (phase == 0 && animControl.getCurTime() + b.tpf >= animControl.getAnimationLength("cast")/2) {
			
			phase++;
			
			// average center of convergence
			Vector3f opPos = target.model.getWorldTranslation();
			Vector3f center = user.model.getWorldTranslation().add(opPos).divide(2);
			center.y = opPos.y;
			
			// make the bullets fly
			for (int i=0; i<bullet.length; i++) {
				float angle = FastMath.nextRandomFloat()*FastMath.PI*2;
				float radius = FastMath.nextRandomFloat()*maxRadius;
				Vector3f translation = center.add(new Vector3f(radius*FastMath.sin(angle), 3.7f , radius*FastMath.cos(angle)));			
				bullet[i] = (Bullet)LoadingQueue.quickLoad(new Bullet(getUsers()), b);
				bullet[i].setVelocity(opPos.subtract(translation).normalize().mult(bulletSpeed));
				bullet[i].rotate(bullet[i].getVelocity());
				bullet[i].model.setLocalTranslation(translation);
				
				createLight(translation, b);
			}
			
			b.getRootNode().updateRenderState();
			
		} else if (character.play("cast", b.tpf)) {
			finish();
		}
	}
	
	private void createLight(Vector3f v, StageGameState b) {
		LightFade l = (LightFade)LoadingQueue.quickLoad(new LightFade(), b);
		l.model.setLocalTranslation(v);
	}

	@Override
	public void finish() {
		super.finish();
		character.moveLock = false;
		character.jumpLock = false;
		character.tagLock = false;
		character.animationLock = false;
	}
	
}
