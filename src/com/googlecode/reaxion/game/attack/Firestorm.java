package com.googlecode.reaxion.game.attack;

import com.googlecode.reaxion.game.model.Model;
import com.googlecode.reaxion.game.model.attackobject.Ember;
import com.googlecode.reaxion.game.state.StageGameState;
import com.googlecode.reaxion.game.util.LoadingQueue;
import com.jme.math.FastMath;
import com.jme.math.Vector3f;

public class Firestorm extends Attack {
	
	private static final String n = "Firestorm";
	private static final int gc = 27;
	
	private Ember[] ember = new Ember[5];
	
	private Model target;
	
	public Firestorm() {
		name = n;
		description = "Creates a circle of embers that chase the foe.";
		gaugeCost = gc;
	}
	
	public Firestorm(AttackData ad) {
		super(ad, gc);
		name = n;
		validateGround();
		target = ad.target;
	}
	
	public static void load() {
		LoadingQueue.push(new Model(Ember.filename));
	}
	
	@Override
	public void firstFrame(StageGameState b) {
		character.jumpLock = true;
		character.animationLock = true;
		character.tagLock = true;
		character.play("heaveUp", b.tpf);
	}
	
	@Override
	public void nextFrame(StageGameState b) {
		if (phase == 0 && character.play("heaveUp", b.tpf)) {
			character.play("heave", b.tpf);
			phase++;
		} else if (frameCount >= 20) {
			
			// create embers
			float angle = FastMath.PI*2/ember.length;
			for (int i=0; i<ember.length; i++) {
				Vector3f translation = new Vector3f(4*FastMath.sin(i*angle), -1f, 4*FastMath.cos(i*angle));
				ember[i] = (Ember)LoadingQueue.quickLoad(new Ember(getUsers()), b);
				ember[i].target = target;
				ember[i].model.setLocalTranslation(character.model.getLocalTranslation().add(translation));
			}
			
			b.getRootNode().updateRenderState();
			
			finish();
		}
	}
	
	@Override
	public void finish() {
		super.finish();
		character.jumpLock = false;
		character.animationLock = false;
		character.tagLock = false;
	}
	
}
