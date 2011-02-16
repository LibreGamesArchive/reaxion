package com.googlecode.reaxion.game.attack;

import com.googlecode.reaxion.game.model.Model;
import com.googlecode.reaxion.game.model.attackobject.AttackObject;
import com.googlecode.reaxion.game.model.attackobject.SoundBomb;
import com.googlecode.reaxion.game.state.StageGameState;
import com.googlecode.reaxion.game.util.LoadingQueue;
import com.jme.math.FastMath;
import com.jme.math.Vector3f;
import com.radakan.jme.mxml.anim.MeshAnimationController;

public class Cacophony extends Attack {
	
	private static final String n = "Cacophony";
	private static final int gc = 24;
	
	private float[] radius = {3.5f, 9};
	private int numStart = 3;
	private int rate = 15;
	
	public Cacophony() {
		name = n;
		gaugeCost = gc;
		description = "Creates a harsh dissonance of sound around the user.";
	}
	
	public Cacophony(AttackData ad) {
		super(ad, gc);
		name = n;
	}
	
	public static void load() {
		LoadingQueue.push(new Model("sound-pulse"));
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
		character.tagLock = true;
		character.animationLock = true;
		character.play("cast", b.tpf);
	}
	
	@Override
	public void nextFrame(StageGameState b) {
		// if animation is halfway through
		MeshAnimationController animControl = (MeshAnimationController) character.model.getController(0);
		if (phase == 0 && animControl.getCurTime() + b.tpf >= animControl.getAnimationLength("cast")/2) {
			
			// create initial wave
			float angle = FastMath.PI*2/numStart;
			for (int i=0; i<numStart; i++) {
				Vector3f translation = new Vector3f(radius[0]*FastMath.sin(i*angle), 2.5f, radius[0]*FastMath.cos(i*angle));
				SoundBomb s = (SoundBomb)LoadingQueue.quickLoad(new SoundBomb(getUsers()), b);
				s.rotate(translation);
				s.model.setLocalTranslation(character.model.getLocalTranslation().add(translation));
			}
			
			b.getRootNode().updateRenderState();
			
			phase++;
			
		} else if (phase == 1) {
			// restore control
			if (animControl.getCurTime() + b.tpf >= animControl.getAnimationLength("cast")) {
				character.moveLock = false;
				character.jumpLock = false;
				character.tagLock = false;
				character.animationLock = false;
			}
			
			// spawn new bombs
			if (frameCount % rate == 0) {
				float r = FastMath.nextRandomFloat()*(radius[1] - radius[0]) + radius[0];
				float angle = FastMath.PI*2*FastMath.nextRandomFloat();
				Vector3f translation = new Vector3f(r*FastMath.sin(angle), 2.5f, r*FastMath.cos(angle));
				SoundBomb s = (SoundBomb)LoadingQueue.quickLoad(new SoundBomb(getUsers()), b);
				s.rotate(translation);
				s.model.setLocalTranslation(character.model.getLocalTranslation().add(translation));
			}
			
			// check end attack
			if (frameCount >= 360)
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
