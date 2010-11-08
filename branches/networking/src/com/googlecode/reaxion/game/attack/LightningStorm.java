package com.googlecode.reaxion.game.attack;

import com.googlecode.reaxion.game.model.Model;
import com.googlecode.reaxion.game.model.attackobject.RoamingCloud;
import com.googlecode.reaxion.game.model.attackobject.Lightning;
import com.googlecode.reaxion.game.state.StageGameState;
import com.googlecode.reaxion.game.util.LoadingQueue;
import com.jme.math.FastMath;
import com.jme.math.Vector3f;
import com.radakan.jme.mxml.anim.MeshAnimationController;

/**
 * Summons multiple storm clouds that roam the area.
 */
public class LightningStorm extends Attack {
	private static final String n = "Elec Storm";
	private static final int gc = 22;
	
	private RoamingCloud[] cloud = new RoamingCloud[10];
	
	public LightningStorm() {
		name = n;
		gaugeCost = gc;
	}
	
	public LightningStorm(AttackData ad) {
		super(ad, gc);
		name = n;
	}
	
	public static void load() {
		LoadingQueue.push(new Model(RoamingCloud.filename));
		LoadingQueue.push(new Model(Lightning.filename));
	}
	
	@Override
	public void firstFrame(StageGameState b) {
		character.moveLock = true;
		character.jumpLock = true;
		character.animationLock = true;
		character.play("heaveUp", b.tpf);
	}
	
	@Override
	public void nextFrame(StageGameState b) {
		if (phase == 0 && character.play("heaveUp", b.tpf)) {
			
			for (int i=0; i<cloud.length; i++) {
				float angle = FastMath.PI*2/cloud.length*i;
				cloud[i] = (RoamingCloud)LoadingQueue.quickLoad(new RoamingCloud(getUsers()), b);	
				Vector3f t = character.model.getWorldTranslation().add(new Vector3f(10*FastMath.cos(angle), 0, 10*FastMath.sin(angle)));
				t.y = 7;
				cloud[i].model.setLocalTranslation(t);
			}
			
			b.getRootNode().updateRenderState();
			character.play("heave", b.tpf);
			phase++;
			
		} else if (phase == 1 && frameCount >= 60) {
			finish();
		}
	}
	
	@Override
	public void finish() {
		super.finish();
		character.moveLock = false;
		character.jumpLock = false;
		character.animationLock = false;
	}
	
}
