package com.googlecode.reaxion.game.attack;

import com.googlecode.reaxion.game.model.Model;
import com.googlecode.reaxion.game.model.attackobject.AngelSword;
import com.googlecode.reaxion.game.model.attackobject.AttackObject;
import com.googlecode.reaxion.game.model.attackobject.Starlight;
import com.googlecode.reaxion.game.state.StageGameState;
import com.googlecode.reaxion.game.util.LoadingQueue;
import com.jme.math.FastMath;
import com.jme.math.Vector3f;

public class Oblivion extends Attack {
	
	private static final String n = "Oblivion";
	private static final int gc = 42;
	
	private static final int launchSpeed = 5;	
	private static final int height = 80;
	private static final int riseSpeed = 1;
	
	private Starlight s;
	private boolean oldGravity;
	
	public Oblivion() {
		name = n;
		gaugeCost = gc;
		description = "The ultimate Light attack. The user creates a small star and hurls it towards Earth.";
	}
	
	public Oblivion(AttackData ad) {
		super(ad, gc);
		name = n;
	}
	
	public static void load() {
		LoadingQueue.push(new Model(Starlight.filename));
	}
	
	@Override
	public void firstFrame(StageGameState b) {
		character.moveLock = true;
		character.jumpLock = true;
		character.animationLock = true;
		character.tagLock = true;
		
		oldGravity = character.gravitate;
		character.gravitate = false;
		character.setVelocity(new Vector3f(0, 0, 0));
		character.play("raiseUp", b.tpf);
	}
	
	@Override
	public void nextFrame(StageGameState b) {
		// lock gauge during attack
		character.gauge = 0;
		
		// if risen to the right height and animation is through
		if (phase == 0 && character.play("raiseUp", b.tpf)) {
			character.play("jump", b.tpf);
			phase++;
			
		} else if (phase == 1) {
			if (character.model.getWorldTranslation().y >= height && character.play("jump", b.tpf)) {
				character.setVelocity(new Vector3f(0, 0, 0));
				character.play("heaveUp", b.tpf);
				phase++;
			} else {
				character.setVelocity(new Vector3f(0, riseSpeed, 0));
			}
		} else if (phase == 2 && character.play("heaveUp", b.tpf)) {	
			s = (Starlight)LoadingQueue.quickLoad(new Starlight(getUsers()), b);
			s.model.setLocalTranslation(character.model.getWorldTranslation().add(new Vector3f(0, 36, 0)));
			b.getRootNode().updateRenderState();
			
			character.play("heave", b.tpf);
			phase++;
			
		} else if (phase == 3 && (s.model.getLocalScale().x == s.maxRadius || s.model.getParent() == null)) {
			// launch star
			s.setVelocity(b.getTarget().model.getWorldTranslation().subtract(character.model.getWorldTranslation()).normalize().mult(launchSpeed));
			phase++;
			
		} else if (phase == 4 && s.model.getParent() == null) {
			character.play("jump", b.tpf);
			character.setVelocity(new Vector3f(0, -riseSpeed, 0));
			if (character.model.getWorldTranslation().y <= 0) {
				character.gravitate = oldGravity;
				finish();
			}
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
		character.jumpLock = false;
		character.animationLock = false;
		character.tagLock = false;
	}
	
}
