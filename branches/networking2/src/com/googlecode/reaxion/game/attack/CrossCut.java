package com.googlecode.reaxion.game.attack;

import com.googlecode.reaxion.game.model.Model;
import com.googlecode.reaxion.game.model.attackobject.Chain;
import com.googlecode.reaxion.game.model.attackobject.DashForce;
import com.googlecode.reaxion.game.model.attackobject.Slash;
import com.googlecode.reaxion.game.state.StageGameState;
import com.googlecode.reaxion.game.util.LoadingQueue;
import com.jme.math.FastMath;
import com.jme.math.Vector3f;

public class CrossCut extends Attack {
	
	private static final String n = "Cross Cut";
	private static final int gc = 5;
	
	private final float slashSpeed = .15f;
	
	private Slash[] slash = new Slash[2];
	
	public CrossCut() {
		name = n;
		gaugeCost = gc;
		description = "Slashes at the opponent at point blank range.";
	}
	
	public CrossCut(AttackData ad) {
		super(ad, gc);
		name = n;
	}
	
	public static void load() {
		LoadingQueue.push(new Model(Slash.filename));
	}
	
	@Override
	public void firstFrame(StageGameState b) {
		character.animationLock = true;
		character.tagLock = true;
		
		character.play("command", b.tpf);
	}
	
	@Override
	public void nextFrame(StageGameState b) {
		if (phase == 0 && character.play("command", b.tpf)) {
			
			Vector3f rotation = character.rotationVector;
			float rotAngle = FastMath.atan2(rotation.x, rotation.z);
			
			// make the cuts appear
			for (int i=0; i<slash.length; i++) {
				float angle = FastMath.PI/2 * (1 + (i-.5f));
				slash[i] = (Slash)LoadingQueue.quickLoad(new Slash(getUsers()), b);
				Vector3f displacement =
					new Vector3f(3*FastMath.cos(angle)*FastMath.sin(rotAngle-FastMath.PI/2), 3*FastMath.sin(angle), 3*FastMath.cos(angle)*FastMath.cos(rotAngle-FastMath.PI/2));
				slash[i].model.setLocalTranslation(character.model.getWorldTranslation().add(
						new Vector3f(FastMath.sin(rotAngle), 0, FastMath.cos(rotAngle)).mult(3.5f)).add(
						new Vector3f(0, 3, 0)).add(
						displacement ));
				slash[i].rotate(displacement.negate());
				slash[i].setVelocity(displacement.mult(-slashSpeed));
			}
			
			b.getRootNode().updateRenderState();
			
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
