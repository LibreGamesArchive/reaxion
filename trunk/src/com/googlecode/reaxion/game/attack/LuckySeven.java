package com.googlecode.reaxion.game.attack;

import com.googlecode.reaxion.game.model.Model;
import com.googlecode.reaxion.game.model.attackobject.SevenCard;
import com.googlecode.reaxion.game.state.StageGameState;
import com.googlecode.reaxion.game.util.LoadingQueue;
import com.jme.math.Vector3f;
import com.radakan.jme.mxml.anim.MeshAnimationController;

public class LuckySeven extends Attack {	
	private static final String n = "Lucky Seven";
	private static final int gc = 7;
	
	private int duration = 260;
	
	private SevenCard s;
	
	public LuckySeven() {
		name = n;
		gaugeCost = gc;
		description = "Conjures up the seven of diamonds.";
	}
	
	public LuckySeven(AttackData ad) {
		super(ad, gc);
		name = n;
	}
	
	public static void load() {
		LoadingQueue.push(new Model(SevenCard.filename));
	}
	
	@Override
	public void firstFrame(StageGameState b) {
		character.tagLock = true;
		character.animationLock = true;
		character.play("cast", b.tpf);
	}
	
	@Override
	public void nextFrame(StageGameState b) {
		// if animation is halfway through
		MeshAnimationController animControl = (MeshAnimationController) character.model.getController(0);
		if (phase == 0 && animControl.getCurTime() + b.tpf >= animControl.getAnimationLength("cast")/2) {
			
			s = (SevenCard)LoadingQueue.quickLoad(new SevenCard(getUsers()), b);
			
			b.getRootNode().updateRenderState();		
			phase++;
			
		} else if (phase == 1 && character.play("cast", b.tpf)) {
			character.tagLock = false;
			character.animationLock = false;
			phase++;
			
		} else if (phase == 2 && frameCount >= duration) {
			finish();
		}
	}
	
	@Override
	public void finish() {
		super.finish();
		if (s != null) {
			s.follow = false;
			s.setVelocity(s.rotationVector.mult(new Vector3f(1, 0, 1)).normalize().mult(3.5f));
		}
		character.tagLock = false;
		character.animationLock = false;
	}
	
}
