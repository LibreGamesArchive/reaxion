package com.googlecode.reaxion.game.attack;

import com.googlecode.reaxion.game.model.Model;
import com.googlecode.reaxion.game.model.attackobject.ArcingLance;
import com.googlecode.reaxion.game.state.StageGameState;
import com.googlecode.reaxion.game.util.LoadingQueue;
import com.jme.math.FastMath;
import com.jme.math.Vector3f;
import com.radakan.jme.mxml.anim.MeshAnimationController;

/**
 * Fires a vertical barrage of lances - upwards if on ground,
 * downward if in the air.
 */
public class LanceArc extends Attack {
	
	private final int numLances = 4;
	private final float maxAngle = FastMath.PI/5;
	private ArcingLance[] lance = new ArcingLance[numLances];
	
	public LanceArc() {
		info.name = "Lance Arc";
		info.gaugeCost = 13;
	}
	
	public LanceArc(AttackData ad) {
		super(ad, 13);
		info.name = "Lance Arc";
	}
	
	public static void load() {
		LoadingQueue.push(new Model(ArcingLance.filename));
	}
	
	@Override
	public void firstFrame(StageGameState b) {
		character.moveLock = true;
		character.jumpLock = true;
		character.animationLock = true;
		character.play("cast", b.tpf);
	}
	
	@Override
	public void nextFrame(StageGameState b) {
		// if animation is halfway through
		MeshAnimationController animControl = (MeshAnimationController) character.model.getController(0);
		if (phase == 0 && animControl.getCurTime() + b.tpf >= animControl.getAnimationLength("cast")/2) {
			
			// create lances
			Vector3f rotation = character.rotationVector;
			float angle = FastMath.atan2(rotation.x, rotation.z);
			int dir = (character.model.getWorldTranslation().y > 0)? -1 : 1;
			
			for (int i=0; i<numLances; i++) {
				float yAngle = dir*maxAngle/numLances*i;
				float minor = 4*FastMath.cos(yAngle);
				Vector3f translation = new Vector3f(minor*FastMath.sin(angle), 3.2f+4*FastMath.sin(yAngle), minor*FastMath.cos(angle));
				lance[i] = (ArcingLance)LoadingQueue.quickLoad(new ArcingLance(getUsers()), b);
				lance[i].setVelocity(new Vector3f(minor*FastMath.sin(angle), 4*FastMath.sin(yAngle), minor*FastMath.cos(angle)).normalize().mult(3));
				lance[i].yaw = FastMath.PI - 2*yAngle;
				lance[i].rotate(lance[i].getVelocity());
				lance[i].model.setLocalTranslation(character.model.getWorldTranslation().add(translation));
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
		character.animationLock = false;
	}
	
}
