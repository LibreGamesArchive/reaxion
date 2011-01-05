package com.googlecode.reaxion.game.attack;

import com.googlecode.reaxion.game.model.Model;
import com.googlecode.reaxion.game.model.attackobject.Angel;
import com.googlecode.reaxion.game.model.attackobject.CyberBeam;
import com.googlecode.reaxion.game.model.enemies.BastionShield;
import com.googlecode.reaxion.game.model.enemies.SatelliteFlower;
import com.googlecode.reaxion.game.state.StageGameState;
import com.googlecode.reaxion.game.util.LoadingQueue;
import com.jme.math.FastMath;
import com.jme.math.Vector3f;
import com.radakan.jme.mxml.anim.MeshAnimationController;

public class FloralFallal extends Attack {
	private static final String n = "Floral Fallal";
	private static final int gc = 34;
	
	public FloralFallal() {
		name = n;
		gaugeCost = gc;
		description = "Summons a ring of satellites that orbit the field and attack enemies.";
	}
	
	public FloralFallal(AttackData ad) {
		super(ad, gc);
		name = n;
	}
	
	public static void load() {
		LoadingQueue.push(new Model("satellite-flower"));
		LoadingQueue.push(new Model(CyberBeam.filename));
	}
	
	@Override
	public void firstFrame(StageGameState b) {
		// make sure there isn't already a Floral Fallal
		boolean flag = false;
		for (int i=0; i< b.getModels().size(); i++)
			if (b.getModels().get(i) instanceof SatelliteFlower && b.getModels().get(i).users.contains(getUsers()[getUsers().length-1])) {
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
			
			// create satellites
			for (int i=0; i<4; i++) {
				float angle = FastMath.PI*2/4*i;
				Vector3f translation = new Vector3f(1000*FastMath.sin(angle), -4f, 1000*FastMath.cos(angle));
				SatelliteFlower s = (SatelliteFlower)LoadingQueue.quickLoad(new SatelliteFlower(getUsers()), b);
				s.model.setLocalTranslation(translation);
				s.angle = angle;
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
		character.tagLock = false;
		character.animationLock = false;
	}
	
}
