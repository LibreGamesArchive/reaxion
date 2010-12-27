package com.googlecode.reaxion.game.attack;

import com.googlecode.reaxion.game.model.Model;
import com.googlecode.reaxion.game.model.attackobject.AttackObject;
import com.googlecode.reaxion.game.model.attackobject.Bubble;
import com.googlecode.reaxion.game.model.attackobject.SnowVortex;
import com.googlecode.reaxion.game.model.character.Character;
import com.googlecode.reaxion.game.model.prop.DeathBurst;
import com.googlecode.reaxion.game.model.prop.DeathWarp;
import com.googlecode.reaxion.game.state.StageGameState;
import com.googlecode.reaxion.game.util.LoadingQueue;
import com.jme.math.FastMath;
import com.jme.math.Vector3f;
import com.radakan.jme.mxml.anim.MeshAnimationController;

public class SheerCold extends Attack {
	
	private static final String n = "Sheer Cold";
	private static final int gc = 38;
	
	private DeathWarp warp;
	private Model target;
	
	public SheerCold(AttackData ad) {
		super(ad, gc);
		name = n;
		target = ad.target;
		validateGround();
	}
	
	public SheerCold() {
		name = n;
		gaugeCost = gc;
		description = "Creates a cold stream around the opponent that can reduce large amounts of HP.";
	}
	
	public static void load() {
		LoadingQueue.push(new Model("death-warp"));
	}
	
	@Override
	public void firstFrame(StageGameState b) {
		character.moveLock = true;
		character.animationLock = true;
		character.jumpLock = true;
		character.tagLock = true;
		
		character.play("cast", b.tpf);
	}
	
	@Override
	public void nextFrame(StageGameState b) {
		// if animation is halfway through
		MeshAnimationController animControl = (MeshAnimationController) character.model.getController(0);
		if (phase == 0 && animControl.getCurTime() + b.tpf >= animControl.getAnimationLength("cast")/2) {
			
			phase++;
			warp = (DeathWarp)LoadingQueue.quickLoad(new DeathWarp(target), b);
			b.getRootNode().updateRenderState();
		
		} else if (phase == 1 && character.play("cast", b.tpf)) {
			character.play("halt", b.tpf);
			phase++;
			
		} else if (phase == 2 && character.play("halt", b.tpf)) {
			character.rotate(target.model.getLocalTranslation().subtract(character.model.getLocalTranslation()).mult(new Vector3f(1, 0, 1)).normalize());
			if (warp.model.getParent() == null) {
				death(target);
				LoadingQueue.quickLoad(new DeathBurst(target), b);
				b.getRootNode().updateRenderState();
				finish();
			}
		}
	}
	
	/**
	 * Try to dispatch the opponent.
	 */
	private void death(Model t) {
		if (t instanceof Character) {
			Character c = (Character)t;
			double chance = .2 * (character.hp - c.hp/2)/character.hp;
			if (Math.random() <= chance)
				// execute death
				c.hp = (c.hp > 1) ? 1 : 0;
			else
				// do 1 uncounted damage
				c.hp -= 1;
			c.toggleFlinch(true);
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
		character.animationLock = false;
		character.jumpLock = false;
		character.tagLock = false;
	}
	
}