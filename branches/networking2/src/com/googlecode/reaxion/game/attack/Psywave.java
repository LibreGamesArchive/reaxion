package com.googlecode.reaxion.game.attack;

import com.googlecode.reaxion.game.model.Model;
import com.googlecode.reaxion.game.model.attackobject.AttackObject;
import com.googlecode.reaxion.game.model.attackobject.PinkWave;
import com.googlecode.reaxion.game.state.StageGameState;
import com.googlecode.reaxion.game.util.LoadingQueue;
import com.radakan.jme.mxml.anim.MeshAnimationController;

public class Psywave extends Attack {
	
	private static final String n = "Psywave";
	private static final int gc = 14;
	
	public Psywave() {
		name = n;
		gaugeCost = gc;
		description = "Releases psychic energy in the form of a shockwave.";
	}
	
	public Psywave(AttackData ad) {
		super(ad, gc);
		name = n;
	}
	
	public static void load() {
		LoadingQueue.push(new Model(PinkWave.filename));
	}
	
	@Override
	public void firstFrame(StageGameState b) {
		character.jumpLock = true;
		character.animationLock = true;
		character.moveLock = true;
		character.tagLock = true;
		
		character.play("cast", b.tpf);
	}
	
	@Override
	public void nextFrame(StageGameState b) {
		// if animation is halfway through
		MeshAnimationController animControl = (MeshAnimationController) character.model.getController(0);
		if (phase == 0 && animControl.getCurTime() + b.tpf >= animControl.getAnimationLength("cast")/2) {
			
			phase++;
			
			// make the shockwave appear
			PinkWave p = (PinkWave)LoadingQueue.quickLoad(new PinkWave(getUsers()), b);
			p.model.setLocalTranslation(character.model.getWorldTranslation().add(0, 3.2f, 0));
			
			b.getRootNode().updateRenderState();
			
		} else if (character.play("cast", b.tpf)) {
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
		character.jumpLock = false;
		character.animationLock = false;
		character.moveLock = false;
		character.tagLock = false;
	}
	
}
