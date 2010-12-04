package com.googlecode.reaxion.game.attack;

import com.googlecode.reaxion.game.model.Model;
import com.googlecode.reaxion.game.model.attackobject.AttackObject;
import com.googlecode.reaxion.game.model.attackobject.Bubble;
import com.googlecode.reaxion.game.model.attackobject.Chain;
import com.googlecode.reaxion.game.model.attackobject.Starlight;
import com.googlecode.reaxion.game.state.StageGameState;
import com.googlecode.reaxion.game.util.LoadingQueue;
import com.jme.math.FastMath;
import com.jme.math.Vector3f;
import com.radakan.jme.mxml.anim.MeshAnimationController;

public class Stopga extends Attack {
	
	private static final String n = "Stopga";
	private static final int gc = 14;
	
	private Chain[] chain = new Chain[2];
	private Model target;
	
	public Stopga() {
		name = n;
		gaugeCost = gc;
		description = "Summons chains that paralyze the target.";
	}
	
	public Stopga(AttackData ad) {
		super(ad, gc);
		name = n;
		target = ad.target;
	}
	
	public static void load() {
		LoadingQueue.push(new Model(Chain.filename));
	}
	
	@Override
	public void firstFrame(StageGameState b) {
		character.jumpLock = true;
		character.animationLock = true;
		character.tagLock = true;
		
		character.play("cast", b.tpf);
	}
	
	@Override
	public void nextFrame(StageGameState b) {
		// if animation is halfway through
		MeshAnimationController animControl = (MeshAnimationController) character.model.getController(0);
		if (phase == 0 && animControl.getCurTime() + b.tpf >= animControl.getAnimationLength("cast")/2) {
			
			phase++;
			
			Vector3f rotation = target.getVelocity();
			
			// make the chains appear
			for (int i=0; i<chain.length; i++) {
				float angle = FastMath.atan2(rotation.x, rotation.z) + FastMath.PI/chain.length*(i-.5f);
				chain[i] = (Chain)LoadingQueue.quickLoad(new Chain(getUsers(), angle + FastMath.PI), b);
				chain[i].model.setLocalTranslation(target.model.getWorldTranslation().add(
						new Vector3f(chain[i].offset*FastMath.sin(angle), 3.7f, chain[i].offset*FastMath.cos(angle))));
				chain[i].rotate(new Vector3f(-FastMath.sin(angle), 0, -FastMath.cos(angle)));
			}
			
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
		character.tagLock = false;
	}
	
}
