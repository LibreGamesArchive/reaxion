package com.googlecode.reaxion.game.attack;

import com.googlecode.reaxion.game.model.Model;
import com.googlecode.reaxion.game.model.attackobject.Mirror;
import com.googlecode.reaxion.game.model.prop.LightFade;
import com.googlecode.reaxion.game.state.StageGameState;
import com.googlecode.reaxion.game.util.LoadingQueue;
import com.jme.math.FastMath;
import com.jme.math.Vector3f;
import com.radakan.jme.mxml.anim.MeshAnimationController;

public class Mirage extends Attack {
	
	private static final String n = "Mirage";
	private static final int gc = 18;
	
	private final int radius = 5;
	
	private Model target;
	
	public Mirage() {
		name = n;
		gaugeCost = gc;
		description = "Fashions a special mirror that will reproduce the user's next attack.";
	}
	
	public Mirage(AttackData ad) {
		super(ad, gc);
		name = n;
		validateGround();
		target = ad.target;
	}
	
	public static void load() {
		LoadingQueue.push(new Model(Mirror.filename));
		LoadingQueue.push(new Model("i_specter"));
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
			
			Mirror mirror = (Mirror)LoadingQueue.quickLoad(new Mirror(getUsers()), b);
			mirror.target = target;
			
			float angle = FastMath.nextRandomFloat()*FastMath.PI*2;
			Vector3f pos = target.model.getLocalTranslation();
			mirror.model.setLocalTranslation(pos.x+radius*FastMath.sin(angle), 0, pos.z+radius*FastMath.cos(angle));
			
			LightFade l = (LightFade)LoadingQueue.quickLoad(new LightFade(), b);
			l.model.setLocalTranslation(mirror.model.getLocalTranslation().add(new Vector3f(0, l.charHeight, 0)));
			
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