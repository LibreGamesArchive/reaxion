package com.googlecode.reaxion.game.attack;

import com.googlecode.reaxion.game.model.Model;
import com.googlecode.reaxion.game.model.attackobject.AttackObject;
import com.googlecode.reaxion.game.model.attackobject.Bubble;
import com.googlecode.reaxion.game.model.attackobject.SnowVortex;
import com.googlecode.reaxion.game.state.StageGameState;
import com.googlecode.reaxion.game.util.LoadingQueue;
import com.jme.math.FastMath;
import com.jme.math.Vector3f;

public class IcyWind extends Attack {
	
	private static final String n = "Icy Wind";
	private static final int gc = 5;
	
	private SnowVortex snow;
	
	public IcyWind() {
		name = n;
		gaugeCost = gc;
		description = "The user looses a stream of snow from their mouth.";
	}
	
	public IcyWind(AttackData ad) {
		super(ad, gc);
		name = n;
		validateGround();
	}
	
	public static void load() {
		LoadingQueue.push(new Model(Bubble.filename));
	}
	
	@Override
	public void firstFrame(StageGameState b) {
		character.moveLock = true;
		character.jumpLock = true;
		character.animationLock = true;
		character.play("blowDown", b.tpf);
	}
	
	@Override
	public void nextFrame(StageGameState b) {
		if (phase == 0 && character.play("blowDown", b.tpf)) {
			//change animation
			character.play("blow", b.tpf);
			phase++;
			
			// calculate transformations
			Vector3f rotation = character.rotationVector;
			float angle = FastMath.atan2(rotation.x, rotation.z);
			
			Vector3f translation = new Vector3f(2.5f*FastMath.sin(angle), 3.7f, 2.5f*FastMath.cos(angle));
			
			snow = (SnowVortex)LoadingQueue.quickLoad(new SnowVortex(getUsers()), b);
			snow.rotationVector = character.rotationVector;
			snow.model.setLocalTranslation(character.model.getWorldTranslation().add(translation));
			
			b.getRootNode().updateRenderState();
			
		} else if (phase == 1 && snow.model.getParent() == null) {		
			character.play("blowUp", b.tpf);
			phase++;
			
		} else if (phase == 2 && character.play("blowUp", b.tpf)) {
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
		if (snow != null)
			snow.cancel();
		character.moveLock = false;
		character.jumpLock = false;
		character.animationLock = false;
	}
	
}