package com.googlecode.reaxion.game.attack;

import com.googlecode.reaxion.game.model.Model;
import com.googlecode.reaxion.game.model.attackobject.Tornado;
import com.googlecode.reaxion.game.state.StageGameState;
import com.googlecode.reaxion.game.util.LoadingQueue;
import com.radakan.jme.mxml.anim.MeshAnimationController;

public class Whirlwind extends Attack {
	
	private static final String n = "Whirlwind";
	private static final int gc = 13;
	
	private Model target;
	private Tornado tornado;
	
	public Whirlwind() {
		name = n;
		gaugeCost = gc;
		description = "Creates a gust of wind that seeks the opponent.";
	}
	
	public Whirlwind(AttackData ad) {
		super(ad, gc);
		name = n;
		target = ad.target;
	}
	
	public static void load() {
		LoadingQueue.push(new Model(Tornado.filename));
	}
	
	@Override
	public void firstFrame(StageGameState b) {
		// make sure there isn't already a tornado
		boolean flag = false;
		for (int i=0; i< b.getModels().size(); i++)
			if (b.getModels().get(i) instanceof Tornado && b.getModels().get(i).users.contains(getUsers()[getUsers().length-1])) {
				flag = true;
				break;
			}
		if (flag) {
			character.gauge += gaugeCost;
			finish();
		} else {
			character.moveLock = true;
			character.jumpLock = true;
			character.tagLock = true;
			character.animationLock = true;
			character.play("cast", b.tpf);
		}
	}
	
	@Override
	public void nextFrame(StageGameState b) {
		// if animation is halfway through
		MeshAnimationController animControl = (MeshAnimationController) character.model.getController(0);
		if (phase == 0 && animControl.getCurTime() + b.tpf >= animControl.getAnimationLength("cast")/2) {
			
			tornado = (Tornado)LoadingQueue.quickLoad(new Tornado(getUsers()), b);
			tornado.target = target;
			
			tornado.model.setLocalTranslation(character.model.getWorldTranslation());
			
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
		character.tagLock = false;
		character.animationLock = false;
	}
	
}
