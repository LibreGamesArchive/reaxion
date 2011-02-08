package com.googlecode.reaxion.game.attack;

import com.googlecode.reaxion.game.model.Model;
import com.googlecode.reaxion.game.model.character.Character;
import com.googlecode.reaxion.game.model.attackobject.AttackObject;
import com.googlecode.reaxion.game.model.attackobject.DarkAura;
import com.googlecode.reaxion.game.model.attackobject.RushSpiral;
import com.googlecode.reaxion.game.state.StageGameState;
import com.googlecode.reaxion.game.util.LoadingQueue;
import com.radakan.jme.mxml.anim.MeshAnimationController;

public class ShadowAura extends Attack {
	private static final String n = "Shadow Aura";
	private static final int gc = 18;
	
	private DarkAura aura;
	
	public ShadowAura(AttackData ad) {
		super(ad, gc);
		name = n;
	}
	
	public ShadowAura() {
		name = n;
		gaugeCost = gc;
		description = "Creates a strange aura of darkness around the user...";
	}
	
	public static void load() {
		LoadingQueue.push(new Model(DarkAura.filename));
		LoadingQueue.push(new Model(RushSpiral.filename));
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
			// calculate transformations
			aura = (DarkAura)LoadingQueue.quickLoad(new DarkAura(getUsers()), b);
			b.getRootNode().updateRenderState();
			phase++;
			
		} else if (phase == 1 && character.play("cast", b.tpf)) {
			// restore control
			character.moveLock = false;
			character.jumpLock = false;
			character.tagLock = false;
			character.animationLock = false;	
			phase++;
			
		} else if (phase == 2 && frameCount >= 240) {
			finish();
		}
	}
	
	@Override
	public void interrupt(StageGameState b, Model other) {
		// negate flinch, this attack cannot be interrupted
        character.hp -= other.getDamage()/3;
        
        // reciprocate the hit
		if (other instanceof AttackObject)
			((AttackObject)other).hit(b, character);
	}
	
	
	@Override
	public void switchOut(StageGameState b) {
		finish();
		if (aura != null)
			aura.cancel();
		
		// make the partner shadow rush
		b.getPartner().currentAttack = new ShadowRush(new AttackData(b.getPartner(), new Character[] {b.getPlayer()}, b.getTarget()));
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
