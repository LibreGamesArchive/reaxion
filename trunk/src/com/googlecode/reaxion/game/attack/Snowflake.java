package com.googlecode.reaxion.game.attack;

import com.googlecode.reaxion.game.model.Model;
import com.googlecode.reaxion.game.model.attackobject.IcePillar;
import com.googlecode.reaxion.game.model.attackobject.SpinningSnowflake;
import com.googlecode.reaxion.game.state.StageGameState;
import com.googlecode.reaxion.game.util.LoadingQueue;
import com.jme.math.FastMath;
import com.jme.math.Vector3f;
import com.radakan.jme.mxml.anim.MeshAnimationController;

public class Snowflake extends Attack {
	private static final String n = "Snowflake";
	private static final int gc = 13;
	
	private Model target;
	
	public Snowflake() {
		name = n;
		gaugeCost = gc;
		description = "Creates a snowflake that freeze the opponent on contact.";
	}
	
	public Snowflake(AttackData ad) {
		super(ad, gc);
		name = n;
		target = ad.target;
	}
	
	public static void load() {
		LoadingQueue.push(new Model(SpinningSnowflake.filename));
		LoadingQueue.push(new Model(IcePillar.filename));
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
			
			// calculate transformations
			Vector3f rotation = character.rotationVector;
			float angle = FastMath.atan2(rotation.x, rotation.z);
			Vector3f translation = new Vector3f(6*FastMath.sin(angle), 3.2f, 6*FastMath.cos(angle));
			
			SpinningSnowflake flake = (SpinningSnowflake)LoadingQueue.quickLoad(new SpinningSnowflake(getUsers()), b);
			flake.target = target;
			
			flake.rotate(rotation);
			flake.model.setLocalTranslation(character.model.getWorldTranslation().add(translation));
			
			b.getRootNode().updateRenderState();
			phase++;
			
		} else if (character.play("cast", b.tpf)) {
			finish();
		}
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
