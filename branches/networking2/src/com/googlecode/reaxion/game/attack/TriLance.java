package com.googlecode.reaxion.game.attack;

import com.googlecode.reaxion.game.model.Model;
import com.googlecode.reaxion.game.model.attackobject.SeekingLance;
import com.googlecode.reaxion.game.state.StageGameState;
import com.googlecode.reaxion.game.util.LoadingQueue;
import com.jme.math.FastMath;
import com.jme.math.Vector3f;
import com.radakan.jme.mxml.anim.MeshAnimationController;

/**
 * Fires three lances that cross at the opponent's location.
 */
public class TriLance extends Attack {
	
	private final float maxAngle = FastMath.PI/4;
	private SeekingLance[] lance = new SeekingLance[3];
	
	private Model target;
	
	public TriLance() {
		name = "Lance Cross";
		gaugeCost = 13;
		description = "Fires three lances that cross at the opponent's location.";
	}
	
	public TriLance(AttackData ad) {
		super(ad, 13);
		name = "Lance Cross";
		target = ad.target;
	}
	
	public static void load() {
		LoadingQueue.push(new Model(SeekingLance.filename));
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
			
			// create lances
			Vector3f tarPos = target.model.getWorldTranslation();
			Vector3f charPos = character.model.getWorldTranslation();
			float angle = FastMath.atan2(charPos.x - tarPos.x, charPos.z - tarPos.z);
			
			float dist = FastMath.sqrt(FastMath.pow(charPos.x - tarPos.x, 2) + FastMath.pow(charPos.z - tarPos.z, 2)) - 4;
			
			for (int i=0; i<lance.length; i++) {
				float lAngle = maxAngle*(i-1);
				Vector3f translation = new Vector3f(dist*FastMath.sin(angle+lAngle), 2f, dist*FastMath.cos(angle+lAngle));
				lance[i] = (SeekingLance)LoadingQueue.quickLoad(new SeekingLance(getUsers()), b);
				lance[i].setVelocity(new Vector3f(FastMath.sin(angle+lAngle), 0, FastMath.cos(angle+lAngle)).negate());
				lance[i].rotate(lance[i].getVelocity());
				lance[i].model.setLocalTranslation(tarPos.add(translation));
			}
			
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
