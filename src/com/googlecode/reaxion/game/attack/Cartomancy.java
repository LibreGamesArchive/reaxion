package com.googlecode.reaxion.game.attack;

import com.googlecode.reaxion.game.model.Model;
import com.googlecode.reaxion.game.model.attackobject.AngelSword;
import com.googlecode.reaxion.game.model.attackobject.AttackObject;
import com.googlecode.reaxion.game.model.attackobject.FallingCard;
import com.googlecode.reaxion.game.model.attackobject.PoisonCard;
import com.googlecode.reaxion.game.state.StageGameState;
import com.googlecode.reaxion.game.util.LoadingQueue;
import com.jme.math.FastMath;
import com.jme.math.Vector3f;
import com.radakan.jme.mxml.anim.MeshAnimationController;

public class Cartomancy extends Attack {
	private static final String n = "Cartomancy";
	private static final int gc = 32;
	
	private static final int height = 64;
	private static final int heightVar = 48;
	private static final float cardSpeed = .2f;
	private static final int radius = 250;
	private static final int numCards = 104;
	
	public Cartomancy() {
		name = n;
		description="Summons a mix of poisoned cards and regular cards.";
		gaugeCost = gc;
	}
	
	public Cartomancy(AttackData ad) {
		super(ad, gc);
		name = n;
	}
	
	public static void load() {
		LoadingQueue.push(new Model(PoisonCard.filename));
		LoadingQueue.push(new Model(FallingCard.filename));
	}
	
	@Override
	public void firstFrame(StageGameState b) {
		character.moveLock = true;
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
			
			for (int i=0; i<numCards; i++) {
				// determine spawn point
				float r = FastMath.nextRandomFloat()*radius;
				float a = FastMath.nextRandomFloat()*FastMath.PI*2;
				Vector3f translation = new Vector3f(r*FastMath.sin(a), height+FastMath.nextRandomFloat()*(float)heightVar-heightVar/2, r*FastMath.cos(a));

				AttackObject c;
				if (FastMath.nextRandomFloat() > .5)
					c = (PoisonCard)LoadingQueue.quickLoad(new PoisonCard(getUsers()), b);
				else
					c = (FallingCard)LoadingQueue.quickLoad(new FallingCard(getUsers()), b);

				c.model.setLocalTranslation(character.model.getWorldTranslation().add(translation));
				c.setVelocity(new Vector3f(0, -cardSpeed, 0));
			}
			
			b.getRootNode().updateRenderState();
			phase++;
			
		} else if (character.play("cast", b.tpf)) {
			finish();
		}
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
