package com.googlecode.reaxion.game.attack;

import com.googlecode.reaxion.game.model.Model;
import com.googlecode.reaxion.game.model.attackobject.Comet;
import com.googlecode.reaxion.game.state.StageGameState;
import com.googlecode.reaxion.game.util.LoadingQueue;
import com.jme.math.FastMath;
import com.jme.math.Vector3f;
import com.radakan.jme.mxml.anim.MeshAnimationController;

public class CometPunch extends Attack {
	
	private static final String n = "Comet Punch";
	private static final int gc = 7;
	private static final float speed = .5f;
	
	public CometPunch() {
		name = n;
		description="Hits opponents with an energetic punch.";
		gaugeCost = gc;
	}
	
	public CometPunch(AttackData ad) {
		super(ad, gc);
		name = n;
	}
	
	public static void load() {
		LoadingQueue.push(new Model(Comet.filename));
	}
	
	@Override
	public void firstFrame(StageGameState b) {
		character.animationLock = true;
		character.tagLock = true;
		character.play("punch", b.tpf);
	}
	
	@Override
	public void nextFrame(StageGameState b) {
		// if animation is halfway through
		MeshAnimationController animControl = (MeshAnimationController) character.model.getController(0);
		if (phase == 0 && animControl.getCurTime() + b.tpf >= animControl.getAnimationLength("punch")/2) {
			
			// calculate transformations
			Vector3f rotation = character.rotationVector;
			float angle = FastMath.atan2(rotation.x, rotation.z);
			Vector3f translation = new Vector3f(FastMath.sin(angle), 3.7f, FastMath.cos(angle));
			
			Comet comet = (Comet)LoadingQueue.quickLoad(new Comet(getUsers()), b);
			
			comet.rotate(rotation);
			comet.setVelocity(rotation.mult(speed));
			comet.model.setLocalTranslation(character.model.getWorldTranslation().add(translation));
			
			b.getRootNode().updateRenderState();
			
			phase++;
		} else if (phase == 1 && character.play("punch", b.tpf)) {
			finish();
		}
	}
	
	@Override
	public void finish() {
		super.finish();
		character.animationLock = false;
		character.tagLock = false;
	}
	
}
