package com.googlecode.reaxion.game.model.enemies;

import com.googlecode.reaxion.game.model.Model;
import com.googlecode.reaxion.game.model.attackobject.AttackObject;
import com.googlecode.reaxion.game.model.attackobject.CrystalSpike;
import com.googlecode.reaxion.game.model.attackobject.DrillBit;
import com.googlecode.reaxion.game.model.attackobject.JumpDrill;
import com.googlecode.reaxion.game.state.StageGameState;
import com.googlecode.reaxion.game.util.LoadingQueue;
import com.jme.math.FastMath;
import com.jme.math.Vector3f;

/**
 * Doriruzu boss. Charges around the field, spawns drills,
 * and leaves a trail of crystals in its wake. Backside is
 * vulnerable to attack.
 * 
 * @author Khoa
 */
public class Doriruzu extends Enemy {

	private final int dropTime = 20;
	private final int numDrills = 8;
	private final int numRiseDrills = 4;
	private final float minDist =  24;
	private final float closeRadius =  280;
	
	private final float chargeSpeed = 3;
	private final float dTheta = FastMath.PI/120;
	
	private int dropCount = 0;
	private boolean charging = false;
	private Vector3f prevPos = new Vector3f();
	
	private DoriruzuDrill drill;
	private DoriruzuLight light;
	
	public Doriruzu() {
		super("enemies/doriruzu", true);
		setInfo("Doriruzu", 600, 1);
		init();
	}

	private void load(StageGameState b) {
		drill = (DoriruzuDrill) LoadingQueue.quickLoad(new DoriruzuDrill(this), b);
		drill.model.setLocalTranslation(model.getWorldTranslation().add(0, 5, 0));
		light = (DoriruzuLight) LoadingQueue.quickLoad(new DoriruzuLight(this), b);
		light.model.setLocalTranslation(model.getWorldTranslation());
		b.getRootNode().updateRenderState();
	}
		
	@Override
	protected void init() {
		super.init();
		mass = 2;
		trackOffset = new Vector3f(0, 8, 0);
		boundRadius = 15f;
		boundHeight = 20f;
	}

	@Override
	protected void step(StageGameState b) {
		// load bounding model
		if (drill == null)
			load(b);
		
		// check death flag
		if (hp <= 0) {
			velocity = new Vector3f();
			return;
		}
		
		// make parts follow
		drill.model.setLocalTranslation(model.getLocalTranslation().add(0, 5, 0));
		light.model.setLocalTranslation(model.getLocalTranslation());
		
		Vector3f dist = b.getPlayer().model.getWorldTranslation().subtract(model.getWorldTranslation());
		float ang = FastMath.atan2(dist.x, dist.z);
		
		if (!charging) {
			// turn to face the player
			facePlayer(dist);
			
			// try to charge
			if (dist.length() >= minDist && Math.abs(((roll + 2*FastMath.PI) % (2*FastMath.PI)) - ((ang + 2*FastMath.PI) % (2*FastMath.PI))) <= dTheta) {
				roll = ang;
				charging = true;
				velocity = dist.mult(new Vector3f(1, 0, 1)).normalize().mult(chargeSpeed);
			}
			
		} else {
			
			// check if path was deflected
			if (Math.abs(model.getLocalTranslation().distance(prevPos) - chargeSpeed) > .001) {
				// stop and turn
				charging = false;
				velocity = new Vector3f();

				// check if was moving
				if (Math.abs(model.getLocalTranslation().distance(prevPos)) < .001)
					// attack!
					if (dist.length() > closeRadius)
						riseDrill(b, ang);
					else
						circleDrill(b, ang);
				
			} else if (hp <= maxHp/2 && dropCount == 0) {
				// drop a crystal
				dropSpike(b);
			}
		}
		
		prevPos = model.getLocalTranslation().clone();
		dropCount = (dropCount + 1) % dropTime;
	}
	
	/**
	 * Attack: Spawn drills at the base.
	 */
	private void circleDrill(StageGameState b, float a) {
		for (int i=0; i<numDrills; i++) {
			float angle = a + 2*FastMath.PI/(float)numDrills * i;
			Vector3f translation = new Vector3f(FastMath.sin(angle), .12f, FastMath.cos(angle)).mult(10);			
			DrillBit d = (DrillBit)LoadingQueue.quickLoad(new DrillBit(this), b);
			d.setVelocity(new Vector3f(FastMath.sin(angle), 0, FastMath.cos(angle)).mult(3));
			d.rotate(d.getVelocity());
			d.model.setLocalTranslation(model.getWorldTranslation().add(translation));
		}

		b.getRootNode().updateRenderState();
	}
	
	/**
	 * Attack: Spawn rising drills.
	 */
	private void riseDrill(StageGameState b, float a) {
		for (int i=0; i<numRiseDrills; i++) {
			float angle = a + 2*FastMath.PI/(float)numRiseDrills * (i - numRiseDrills/2);
			Vector3f translation = new Vector3f(FastMath.sin(angle), 0, FastMath.cos(angle)).mult(32);			
			JumpDrill d = (JumpDrill)LoadingQueue.quickLoad(new JumpDrill(this, b.getPlayer()), b);
			d.model.setLocalTranslation(model.getWorldTranslation().add(translation));
		}
		
		b.getRootNode().updateRenderState();
	}
	
	/**
	 * Attack: Leave a crystal in wake.
	 */
	private void dropSpike(StageGameState b) {	
		CrystalSpike d = (CrystalSpike)LoadingQueue.quickLoad(new CrystalSpike(this), b);
		d.model.setLocalTranslation(model.getWorldTranslation().add(0, -13, 0));
		
		b.getRootNode().updateRenderState();
	}
	
	/**
	 * Rotate by {@code dTheta} in the player's direction.
	 * @param dist Vector pointing to the player
	 */
	private void facePlayer(Vector3f dist) {
		float a = FastMath.atan2(dist.x, dist.z);
		if (roll % (2*FastMath.PI) - a < FastMath.PI && roll % (2*FastMath.PI) - a > 0)
			roll = (float) Math.max((roll - dTheta) % (2*FastMath.PI), a);
		else
			roll = (float) Math.min((roll + dTheta) % (2*FastMath.PI), a + 2*FastMath.PI);
		rotate();
		drill.roll = roll;
		drill.rotate();
		light.roll = roll;
		light.rotate();
	}
	
	@Override
	public boolean reactHit(StageGameState b, Model other) {
		// reciprocate the hit
		if (other instanceof AttackObject)
			((AttackObject)other).hit(b, this);

		// check light
		boolean flag = false;
		for (Model c : light.collisions)
			flag = flag || light.checkHit(b, c);
		if (!flag)
			hp -= other.getDamage()/3;
		
		return true;
	}
	
	public void hitLight(StageGameState b, Model other) {
		hp -= other.getDamage()*2;
	}

}
