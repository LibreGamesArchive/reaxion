package com.googlecode.reaxion.game.attack;

import com.googlecode.reaxion.game.model.Model;
import com.googlecode.reaxion.game.model.attackobject.AttackObject;
import com.googlecode.reaxion.game.model.attackobject.SeekingLance;
import com.googlecode.reaxion.game.state.StageGameState;
import com.googlecode.reaxion.game.util.LoadingQueue;
import com.jme.math.FastMath;
import com.jme.math.Vector3f;
import com.radakan.jme.mxml.anim.MeshAnimationController;

/**
 * Surrounds the user with protective lances.
 */
public class LanceGuard extends Attack {
	
	private static final String n = "Lance Guard";
	private static final int gc = 22;
	
	private SeekingLance[] lance = new SeekingLance[4];
	
	private float radius = 4;
	private float curAngle = 0;
	private float angleInc = FastMath.PI/16;
	
	public LanceGuard() {
		info.name = n;
		info.gaugeCost = gc;
	}
	
	public LanceGuard(AttackData ad) {
		super(ad, gc);
		info.name = n;
	}
	
	public static void load() {
		LoadingQueue.push(new Model(SeekingLance.filename));
	}
	
	@Override
	public void interrupt(StageGameState b, Model other) {
		// negate flinch, this attack cannot be interrupted
        character.hp -= other.getDamage();
        
        // reciprocate the hit
		if (other instanceof AttackObject)
			((AttackObject)other).hit(b, character);
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
			float angle = FastMath.PI*2/lance.length;
			for (int i=0; i<lance.length; i++) {
				Vector3f translation = new Vector3f(radius*FastMath.sin(i*angle), 2.5f, radius*FastMath.cos(i*angle));
				lance[i] = (SeekingLance)LoadingQueue.quickLoad(new SeekingLance(getUsers()), b);
				lance[i].rotate(new Vector3f(0, 1, 0));
				lance[i].model.setLocalTranslation(character.model.getLocalTranslation().add(translation));
			}
			
			b.getRootNode().updateRenderState();
			
			phase++;
			
		} else if (phase == 1) {
			// restore control
			if (animControl.getCurTime() + b.tpf >= animControl.getAnimationLength("cast")) {
				character.moveLock = false;
				character.jumpLock = false;
				character.animationLock = false;
			}
			
			// circle lances
			float angle = FastMath.PI*2/lance.length;
			for (int i=0; i<lance.length; i++) {
				Vector3f translation = new Vector3f(radius*FastMath.sin(i*angle+curAngle), 2.5f, radius*FastMath.cos(i*angle+curAngle));
				lance[i].model.setLocalTranslation(character.model.getLocalTranslation().add(translation));
			}
			
			curAngle += angleInc;
			
			// check end attack
			if (frameCount >= 320)
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
