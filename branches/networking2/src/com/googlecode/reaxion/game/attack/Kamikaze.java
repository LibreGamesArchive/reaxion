package com.googlecode.reaxion.game.attack;

import com.googlecode.reaxion.game.model.Model;
import com.googlecode.reaxion.game.model.attackobject.FlyingBird;
import com.googlecode.reaxion.game.state.StageGameState;
import com.googlecode.reaxion.game.util.LoadingQueue;
import com.jme.math.FastMath;
import com.jme.math.Vector3f;

public class Kamikaze extends Attack {
	private static final String n = "Kamikaze";
	private static final int gc = 22;
	
	private static final int delay = 12;
	private static final int numBirds = 6;
	
	private Model target;
	
	public Kamikaze() {
		name = n;
		gaugeCost = gc;
		description = "Summons a flock of birds to dive bomb the opponent.";
	}
	
	public Kamikaze(AttackData ad) {
		super(ad, gc);
		name = n;
		target = ad.target;
	}
	
	public static void load() {
		LoadingQueue.push(new Model(FlyingBird.filename));
	}
	
	@Override
	public void firstFrame(StageGameState b) {
		character.moveLock = true;
		character.tagLock = true;
		character.animationLock = true;
		character.play("heaveUp", b.tpf);
	}
	
	@Override
	public void nextFrame(StageGameState b) {
		if (phase == 0 && character.play("heaveUp", b.tpf)) {
			character.play("heave", b.tpf);
			
			phase++;
			
		} else if (phase >= 1  && phase < numBirds+2) {
			if (frameCount % delay == 0) {
				Vector3f rotation = character.rotationVector;
				float angle = FastMath.atan2(rotation.x, rotation.z);
				Vector3f pivot = new Vector3f(5*FastMath.sin(angle + (phase%2)*FastMath.PI/2), 0, 5*FastMath.cos(angle + (phase%2)*FastMath.PI/2));
				
				FlyingBird bird = (FlyingBird)LoadingQueue.quickLoad(new FlyingBird(getUsers()), b);
				bird.pivot = character.model.getWorldTranslation().add(pivot);
				bird.angle = angle - (phase%2)*FastMath.PI/2;
				bird.dTheta *= (phase % 2)*2 - 1;
				bird.target = target;

				bird.model.setLocalTranslation(character.model.getWorldTranslation().add(0, 7, 0));

				b.getRootNode().updateRenderState();
				phase++;
			}
		} else if (phase >= numBirds+1) {
				finish();
		}
	}
	
	@Override
	public void finish() {
		super.finish();		
		character.moveLock = false;
		character.tagLock = false;
		character.animationLock = false;
	}

}
