package com.googlecode.reaxion.game.attack;

import java.util.ArrayList;

import com.googlecode.reaxion.game.model.Model;
import com.googlecode.reaxion.game.model.attackobject.AttackObject;
import com.googlecode.reaxion.game.model.attackobject.MagneticField;
import com.googlecode.reaxion.game.model.character.Character;
import com.googlecode.reaxion.game.state.StageGameState;
import com.googlecode.reaxion.game.util.LoadingQueue;
import com.jme.math.Vector3f;

public class MagneticWorld extends Attack {
	private static final String n = "Mgntc World";
	private static final int gc = 30;
	
	private float maxForce = 2f;
	private float minForce = .3f;
	private float minDistance = 2f;
	
	private MagneticField m;
	
	private boolean massChanged = false;
	
	public MagneticWorld() {
		name = n;
		gaugeCost = gc;
		description = "Creates a unique magnetic field that draws in others.";
	}
	
	public MagneticWorld(AttackData ad) {
		super(ad, gc);
		name = n;
		validateGround();
	}
	
	public static void load() {
		LoadingQueue.push(new Model(MagneticField.filename));
	}
	
	@Override
	public void firstFrame(StageGameState b) {
		character.moveLock = true;
		character.jumpLock = true;
		character.tagLock = true;
		character.animationLock = true;
		character.mass *= 2;
		massChanged = true;
		character.play("heaveUp", b.tpf);
	}
	
	@Override
	public void nextFrame(StageGameState b) {
		if (phase == 0 && character.play("heaveUp", b.tpf)) {
			
			m = (MagneticField)LoadingQueue.quickLoad(new MagneticField(getUsers()), b);
			
			m.model.setLocalTranslation(character.model.getWorldTranslation());
			
			b.getRootNode().updateRenderState();
			character.play("heave", b.tpf);
			
			phase++;
			
		} else if (phase == 1) {
			
			// draw other characters inward
			ArrayList<Model> l = b.getModels();
			for (int i=0; i<l.size(); i++) {
				if (l.get(i) != character && l.get(i) instanceof Character && ((Character)l.get(i)).hp > 0 && ((Character)l.get(i)).mass < character.mass) {
					Vector3f vec = character.model.getWorldTranslation().subtract(l.get(i).model.getWorldTranslation());
					float force = (vec.length() > minDistance) ? (maxForce*minDistance/vec.length() + minForce) : 0;
					l.get(i).model.setLocalTranslation(l.get(i).model.getLocalTranslation().add(vec.normalize().mult(force)));
				}
			}
			
			if (frameCount >= 300)
				finish();
		}
	}
	
	@Override
	public void interrupt(StageGameState b, Model other) {
		// negate flinch, this attack cannot be interrupted except from within
		if (other.model.getWorldTranslation().distance(character.model.getWorldTranslation()) > 6) {
			character.hp -= other.getDamage()/2;
	        
	        // reciprocate the hit
			if (other instanceof AttackObject)
				((AttackObject)other).hit(b, character);
		} else
			character.reactHit(b, other);
	}
	
	@Override
	public void finish() {
		super.finish();
		if (m != null)
			m.cancel();
		character.moveLock = false;
		character.jumpLock = false;
		character.tagLock = false;
		character.animationLock = false;
		if (massChanged)
			character.mass /= 2;
	}

}
