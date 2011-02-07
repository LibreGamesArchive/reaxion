package com.googlecode.reaxion.game.attack;

import com.googlecode.reaxion.game.model.Model;
import com.googlecode.reaxion.game.model.attackobject.AttackObject;
import com.googlecode.reaxion.game.model.attackobject.ShadowArm;
import com.googlecode.reaxion.game.model.character.Character;
import com.googlecode.reaxion.game.model.prop.DeathBurst;
import com.googlecode.reaxion.game.model.prop.DeathWarp;
import com.googlecode.reaxion.game.state.StageGameState;
import com.googlecode.reaxion.game.util.LoadingQueue;
import com.jme.math.FastMath;
import com.jme.math.Vector3f;
import com.radakan.jme.mxml.anim.MeshAnimationController;

public class DarkHand extends Attack {
	
	private static final String n = "Dark Hand";
	private static final int gc = 15;
	
	public DarkHand() {
		name = n;
		gaugeCost = gc;
		description = "Launches forth a hand made of shadows that pushes away foes.";
	}
	
	public DarkHand(AttackData ad) {
		super(ad, gc);
		name = n;
		validateGround();
	}
	
	public static void load() {
		LoadingQueue.push(new Model(ShadowArm.filename));
	}
	
	@Override
	public void firstFrame(StageGameState b) {
		character.moveLock = true;
		character.animationLock = true;
		character.jumpLock = true;
		character.tagLock = true;
		
		character.play("halt", b.tpf);
	}
	
	@Override
	public void nextFrame(StageGameState b) {
		if (phase == 0) {
			// calculate transformations
			Vector3f rotation = character.rotationVector;
			float angle = FastMath.atan2(rotation.x, rotation.z);
			Vector3f translation = new Vector3f(2.5f*FastMath.sin(angle), 4.4f, 2.5f*FastMath.cos(angle));
			
			ShadowArm s = (ShadowArm)LoadingQueue.quickLoad(new ShadowArm(getUsers()), b);
			s.rotate(rotation);
			s.model.setLocalTranslation(character.model.getWorldTranslation().add(translation));
			
			b.getRootNode().updateRenderState();
			
			phase++;
		} else if (phase == 1 && frameCount >= 40) {
			finish();
		}
	}
	
	@Override
	public void interrupt(StageGameState b, Model other) {
		// negate flinch, this attack cannot be interrupted
        character.hp -= other.getDamage()/2;
        
        // reciprocate the hit
		if (other instanceof AttackObject)
			((AttackObject)other).hit(b, character);
	}
	
	@Override
	public void finish() {
		super.finish();
		character.moveLock = false;
		character.animationLock = false;
		character.jumpLock = false;
		character.tagLock = false;
	}
	
}