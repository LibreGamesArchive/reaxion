package com.googlecode.reaxion.game.attack;

import com.googlecode.reaxion.game.model.Model;
import com.googlecode.reaxion.game.model.attackobject.Bullet;
import com.googlecode.reaxion.game.model.attackobject.Starlight;
import com.googlecode.reaxion.game.model.prop.LightFade;
import com.googlecode.reaxion.game.state.StageGameState;
import com.googlecode.reaxion.game.util.LoadingQueue;
import com.jme.math.FastMath;
import com.jme.math.Vector3f;

public class Teleport extends Attack {
	
	private static final String n = "Teleport";
	private static final int gc = 6;
	
	private final float radius = 7;
	
	public Teleport() {
		name = n;
		gaugeCost = gc;
		description = "The user dissappears and reappears nearby a target.";
	}
	
	public Teleport(AttackData ad) {
		super(ad, gc);
		name = n;
	}
	
	public static void load() {
		LoadingQueue.push(new Model(LightFade.filename));
	}
	
	@Override
	public void firstFrame(StageGameState b) {
		// teleport
		createLight(b.getPlayer().model.getLocalTranslation(), b);
		
		float angle = FastMath.nextRandomFloat()*FastMath.PI*2;
		Vector3f pos = b.getTarget().model.getLocalTranslation();
		b.getPlayer().model.setLocalTranslation(pos.x+radius*FastMath.sin(angle),
				b.getPlayer().model.getLocalTranslation().y,
				pos.z+radius*FastMath.cos(angle));
		b.getStage().contain(b.getPlayer());
		
		createLight(b.getPlayer().model.getLocalTranslation(), b);
		
		b.getRootNode().updateRenderState();
		
		finish();
	}
	
	private void createLight(Vector3f v, StageGameState b) {
		LightFade l = (LightFade)LoadingQueue.quickLoad(new LightFade(), b);
		l.model.setLocalTranslation(v.add(new Vector3f(0, l.charHeight, 0)));
	}
	
	@Override
	public void finish() {
		super.finish();
	}
	
}
