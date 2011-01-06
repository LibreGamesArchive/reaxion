package com.googlecode.reaxion.game.attack;

import com.googlecode.reaxion.game.model.prop.Sparkle;
import com.googlecode.reaxion.game.state.StageGameState;
import com.googlecode.reaxion.game.util.LoadingQueue;
import com.jme.math.FastMath;
import com.jme.math.Vector3f;

public class AirDash extends GroundDash {

	public AirDash() {
		super();
		name = "Air Dash";
	}
	
	public AirDash(AttackData ad) {
		super(ad);
		name = "Air Dash";
	}
	
	@Override
	public void firstFrame(StageGameState b) {
		super.firstFrame(b);
		
		character.gravitate = false;
		character.gravVel = 0;
		character.play("raiseUp", b.tpf);
	}
	
	public void nextFrame(StageGameState b) {
//		super.nextFrame(b);

		if(duration - frameCount <= 4)
			character.gravitate = true;
		
		if (frameCount < duration) {
			// create sparkle
			if (s == null)
				s = (Sparkle)LoadingQueue.quickLoad(new Sparkle(30, 12), b);

			Vector3f rotation = character.rotationVector;
			float angle = FastMath.atan2(rotation.x, rotation.z);
			Vector3f translation = new Vector3f(2.5f*FastMath.sin(angle), 4.4f, 2.5f*FastMath.cos(angle));
			
			s.model.setLocalTranslation(character.model.getWorldTranslation().add(translation));
			character.setVelocity(character.rotationVector.mult(dashSpeed));
			
			b.getRootNode().updateRenderState();
		} else {
			finish();
		}
	
}
	@Override
	public void finish() {
		super.finish();
		character.gravitate = true;
	}
}
