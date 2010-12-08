package com.googlecode.reaxion.game.attack;

import com.googlecode.reaxion.game.model.Model;
import com.googlecode.reaxion.game.model.attackobject.WheelLance;
import com.googlecode.reaxion.game.state.StageGameState;
import com.googlecode.reaxion.game.util.LoadingQueue;
import com.jme.math.FastMath;
import com.jme.math.Vector3f;
import com.radakan.jme.mxml.anim.MeshAnimationController;

/**
 * Launches two rolling lances directly forward.
 */
public class LanceWheel extends Attack {
	
	private float angle;
	private WheelLance lanceLft;
	private WheelLance lanceRgt;
	
	public LanceWheel() {
		name = "Lance Wheel";
		gaugeCost = 9;
		description = "Launches two rolling lances directly forward.";
	}
	
	public LanceWheel(AttackData ad) {
		super(ad, 9);
		name = "Lance Wheel";

	}
	
	public static void load() {
		LoadingQueue.push(new Model(WheelLance.filename));
	}
	
	@Override
	public void firstFrame(StageGameState b) {
		character.moveLock = true;
		character.jumpLock = true;
		character.animationLock = true;
		character.play("cast", b.tpf);
		
		// calculate transformations
		Vector3f rotation = character.rotationVector;
		angle = FastMath.atan2(rotation.x, rotation.z);
		
		Vector3f translation = new Vector3f(4*FastMath.sin(angle-FastMath.PI/2), 3.2f, 4*FastMath.cos(angle-FastMath.PI/2));
		lanceLft = (WheelLance)LoadingQueue.quickLoad(new WheelLance(getUsers()), b);
		lanceLft.rotate(rotation);
		lanceLft.model.setLocalTranslation(character.model.getWorldTranslation().add(translation));
		
		translation = new Vector3f(4*FastMath.sin(angle+FastMath.PI/2), 3.2f, 4*FastMath.cos(angle+FastMath.PI/2));
		lanceRgt = (WheelLance)LoadingQueue.quickLoad(new WheelLance(getUsers()), b);
		lanceRgt.rotate(rotation);
		lanceRgt.model.setLocalTranslation(character.model.getWorldTranslation().add(translation));
		
		b.getRootNode().updateRenderState();
	}
	
	@Override
	public void nextFrame(StageGameState b) {
		// if animation is halfway through
		MeshAnimationController animControl = (MeshAnimationController) character.model.getController(0);
		if (phase == 0 && animControl.getCurTime() + b.tpf >= animControl.getAnimationLength("cast")/2) {
			
			lanceLft.setVelocity(new Vector3f(3*FastMath.sin(angle), 0, 3*FastMath.cos(angle)));
			lanceRgt.setVelocity(new Vector3f(3*FastMath.sin(angle), 0, 3*FastMath.cos(angle)));
			
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
