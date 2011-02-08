package com.googlecode.reaxion.game.attack;

import com.googlecode.reaxion.game.model.Model;
import com.googlecode.reaxion.game.model.attackobject.Fire;
import com.googlecode.reaxion.game.model.attackobject.Undine;
import com.googlecode.reaxion.game.model.prop.WaterDrop;
import com.googlecode.reaxion.game.state.StageGameState;
import com.googlecode.reaxion.game.util.LoadingQueue;
import com.jme.math.FastMath;
import com.jme.math.Vector3f;

public class NymphSong extends Attack {
	
	private static final String n = "Nymph Song";
	private static final int gc = 19;
	
	private static final float speed = 1.6f;
	
	private Model target;
	private Vector3f t;
	
	public NymphSong() {
		name = n;
		gaugeCost = gc;
		description="Calls forth an undine to attack the opponent from underneath.";
	}
	
	public NymphSong(AttackData ad) {
		super(ad, gc);
		name = n;
		validateGround();
		target = ad.target;
	}
	
	public static void load() {
		LoadingQueue.push(new Model(Undine.filename));
	}
	
	@Override
	public void firstFrame(StageGameState b) {
		character.jumpLock = true;
		character.tagLock = true;
		character.animationLock = true;
		t = target.model.getWorldTranslation().mult(new Vector3f(1, 0, 1));
		character.play("cast", b.tpf);
	}
	
	@Override
	public void nextFrame(StageGameState b) {
		if (phase == 0) {
			// splash a bit
			splash(b, 1);
			
			if (character.play("cast", b.tpf))
				phase++;
			
		} else if (phase == 1) {
			// splash a lot
			splash(b, 14);
			
			// calculate transformations
			Vector3f c = character.model.getWorldTranslation();
			
			float angle = FastMath.atan2(t.x - c.x, t.z - c.z);
			Vector3f translation = new Vector3f(1*FastMath.sin(angle), -5, 1*FastMath.cos(angle));
			
			Undine e = (Undine)LoadingQueue.quickLoad(new Undine(getUsers()), b);
			e.target = target;
			e.setVelocity(new Vector3f(0, speed, 0));
			e.model.setLocalTranslation(t.add(translation));
			
			b.getRootNode().updateRenderState();
			
			finish();
		}
	}
	
	private void splash(StageGameState b, int numDrops) {
		for (int i=0; i<numDrops; i++) {
			// determine spawn point
			float a = FastMath.nextRandomFloat()*FastMath.PI/2;
			float p = FastMath.nextRandomFloat()*FastMath.PI*2;
			Vector3f vel = new Vector3f(FastMath.sin(p)*FastMath.sin(a), FastMath.cos(p), FastMath.sin(p)*FastMath.cos(a)).mult(2/3f);

			WaterDrop drop = (WaterDrop)LoadingQueue.quickLoad(new WaterDrop(), b);
			drop.setVelocity(vel);
			drop.model.setLocalTranslation(t.clone());
		}

		b.getRootNode().updateRenderState();
	}
	
	@Override
	public void finish() {
		super.finish();
		character.jumpLock = false;
		character.tagLock = false;
		character.animationLock = false;
	}
	
}
