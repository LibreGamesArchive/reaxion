package com.googlecode.reaxion.game.attack;

import com.googlecode.reaxion.game.model.Model;
import com.googlecode.reaxion.game.model.attackobject.AngelSword;
import com.googlecode.reaxion.game.model.attackobject.AttackObject;
import com.googlecode.reaxion.game.model.attackobject.IceSpike;
import com.googlecode.reaxion.game.state.StageGameState;
import com.googlecode.reaxion.game.util.LoadingQueue;
import com.jme.math.FastMath;
import com.jme.math.Vector3f;

public class FrozenWorld extends Attack {
	private static final String n = "Frozen World";
	private static final int gc = 29;
	
	private static final int height = 10;
	private static final int riseSpeed = 1;
	private static final int minRadius = 4;
	private static final int maxRadius = 40;
	private static final int delay = 4;
	private static final int numSpikes = 60;
	
	private boolean oldGravity;
	
	public FrozenWorld() {
		name = n;
		gaugeCost = gc;
		description = "Summons masts of ice out of the ground around the user that will freeze foes on contact.";		
	}
	
	public FrozenWorld(AttackData ad) {
		super(ad, gc);
		name = n;
	}
	
	public static void load() {
		LoadingQueue.push(new Model(IceSpike.filename));
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
		// if risen to the right height and animation is through
		if (phase == 0 && character.play("raiseUp", b.tpf)) {
			character.play("jump", b.tpf);
			phase++;
		} else if (phase == 1) {
			if (character.model.getWorldTranslation().y >= height && character.play("jump", b.tpf)) {
				character.setVelocity(new Vector3f(0, 0, 0));
				phase++;
			} else {
				character.setVelocity(new Vector3f(0, riseSpeed, 0));
			}
		} else if (phase > 1 && phase < numSpikes+2 && frameCount % delay == 0) {
			
			// determine spawn point
			Vector3f pos = character.model.getWorldTranslation();
			float r = FastMath.nextRandomFloat()*(maxRadius - minRadius) + minRadius;
			float a = FastMath.nextRandomFloat()*FastMath.PI*2;
			Vector3f translation = new Vector3f(pos.x + r*FastMath.sin(a), -8, pos.z + r*FastMath.cos(a));
			
			IceSpike spike = (IceSpike)LoadingQueue.quickLoad(new IceSpike(getUsers()), b);

			spike.model.setLocalTranslation(translation);
			
			b.getRootNode().updateRenderState();
			phase++;
			
		} else if (phase >= numSpikes+1) {
			character.gravitate = oldGravity;
			finish();	
		}
	}
	
	@Override
	public void interrupt(StageGameState b, Model other) {
		// negate flinch, this attack cannot be interrupted
        character.hp -= other.getDamage()/4;
        
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
