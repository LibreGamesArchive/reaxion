package com.googlecode.reaxion.game.model.enemies;

import com.googlecode.reaxion.game.model.Model;
import com.googlecode.reaxion.game.model.attackobject.AttackObject;
import com.googlecode.reaxion.game.model.attackobject.Pellet;
import com.googlecode.reaxion.game.state.StageGameState;
import com.googlecode.reaxion.game.util.LoadingQueue;
import com.jme.math.FastMath;
import com.jme.math.Vector3f;

/**
 * Havoc Skytank boss. Flies around the sky shooting.
 * Sometimes descends to the ground and shoots.
 * 
 * @author Khoa
 */
public class HavocSkytank extends Enemy {

	private final int bulletsPerSpin = 3;
	private final int numSpins = 4;
	private final float spinTime =  30;
	private final float chargeError =  FastMath.PI/2;
	
	private final float dTheta = FastMath.PI/20;
	
	private final float actDelay = 300;
	
	private final float chargeSpeed = 1;
	private final float flySpeed = 2;
	
	private final int shootTime = 50;
	private final int chargeShootTime = 30;
	
	private final float flyHeight = 48;
	private final float flyRadius = 70;
	private final float ySpeed = 2;
	
	private float destAngle;
	
	private int timer = 0;
	
	private String mode = "flying";
	
	public HavocSkytank() {
		super("enemies/skytank", true);
		setInfo("Havoc Skytank", 140, 1);
		init();
	}
		
	@Override
	protected void init() {
		super.init();
		mass = 10;
		trackOffset = new Vector3f(0, 6, 0);
		boundRadius = 28f;
		boundHeight = 15f;
	}	

	@Override
	protected void step(StageGameState b) {
		
		// check death flag
		if (hp <= 0) {
			velocity = new Vector3f();
			return;
		}
		
		Vector3f pos = model.getWorldTranslation();
		Vector3f charPos = b.getPlayer().model.getWorldTranslation();
		float omega = flySpeed/flyRadius;
		
		if (mode == "flying") {
			// rise if not in air
			if (pos.y < flyHeight) {
				velocity = new Vector3f(0, ySpeed, 0);
			
			// fly to radius if not there
			} else if (pos.mult(new Vector3f(1, 0, 1)).length() <= flyRadius - flySpeed) {
				destAngle = FastMath.atan2(pos.x, pos.z);
				if (destAngle == 0)
					destAngle = FastMath.nextRandomFloat()*FastMath.PI*2;
				velocity = new Vector3f(FastMath.sin(destAngle), 0, FastMath.cos(destAngle)).mult(flySpeed);
				
			// fly around
			} else {
				float angle = FastMath.atan2(pos.x, pos.z) + omega;
				Vector3f nextPos = new Vector3f(FastMath.sin(angle), 0, FastMath.cos(angle)).mult(flyRadius);
				velocity = nextPos.subtract(new Vector3f(pos.x, 0, pos.z));
				
				// shoot
				if (timer % shootTime == 0) {
					Vector3f shootPoint = pos.add(6*FastMath.sin(roll), -7, 6*FastMath.cos(roll));
					Pellet d = (Pellet)LoadingQueue.quickLoad(new Pellet(this), b);
					d.setVelocity(charPos.add(0, 3, 0).subtract(shootPoint).normalize().mult(3));
					d.model.setLocalTranslation(shootPoint);
					b.getRootNode().updateRenderState();
				}
			}
			
			// change modes
			if (timer >= actDelay) {
				timer = 0;
				float rand = FastMath.nextRandomFloat();
				if (rand < .5)
					mode = "charge";
				else if (rand < .7)
					mode = "center";
			} else
				timer++;
			
			face();
			
		} else if (mode == "charge") {
			// fall if in air
			if (pos.y > 0) {
				velocity= new Vector3f(0, -ySpeed, 0);
				destAngle = FastMath.PI + FastMath.atan2(pos.x, pos.z);
				
			// charge through center
			} else {
				Vector3f mvmt = new Vector3f(FastMath.sin(destAngle), 0, FastMath.cos(destAngle)).mult(chargeSpeed);
				
				// check if going too far
				if (pos.y == 0 && pos.add(mvmt).length() >= flyRadius) {
					// return to the sky
					mode = "flying";
					timer = 0;
				
				// check if angle of separation is too large
				} else if (FastMath.abs((destAngle+FastMath.PI*4)%(FastMath.PI*2)
						- (FastMath.atan2(charPos.x, charPos.z)+FastMath.PI*4)%(FastMath.PI*2)) >= chargeError) {
					mode = "spin";
					timer = 0;
					
				} else {
					velocity = mvmt;
					
					// shoot
					if (timer % chargeShootTime == 0) {
						int i = FastMath.nextRandomInt(-1, 2);
						Vector3f shootPoint = pos.add(27*FastMath.sin(roll)+3.25f*FastMath.cos(roll)*i, 4, 27*FastMath.cos(roll)+3.25f*FastMath.cos(roll)*i);
						Pellet d = (Pellet)LoadingQueue.quickLoad(new Pellet(this), b);
						d.setVelocity(new Vector3f(FastMath.sin(roll), 0, FastMath.cos(roll)).mult(3));
						d.model.setLocalTranslation(shootPoint);
						b.getRootNode().updateRenderState();
					}
					
					timer++;
				}
			}
			
			face();
			
		} else if (mode == "center") {
			// move towards the center
			if (pos.mult(new Vector3f(1, 0, 1)).length() >= flySpeed)
				velocity = new Vector3f(pos.x, 0, pos.z).normalize().mult(-flySpeed);
			else
				mode = "spin";
			
			face();
			
		} else if (mode == "spin") {
			// fall if in air
			if (pos.y > 0) {
				velocity= new Vector3f(0, -ySpeed, 0);
				
			} else {
				velocity = new Vector3f();
				if (roll / (2*FastMath.PI) >= numSpins) {
					mode = "flying";
					timer = 0;
					
				} else {
					roll += 2*FastMath.PI/spinTime;
					rotate();
					
					// shoot
					if (roll % 2*FastMath.PI < 4*FastMath.PI/spinTime) {
						float a = FastMath.PI*2*FastMath.nextRandomFloat();
						Pellet d = (Pellet)LoadingQueue.quickLoad(new Pellet(this), b);
						d.setVelocity(new Vector3f(FastMath.sin(a), 0, FastMath.cos(a)).mult(3));
						d.model.setLocalTranslation(10*FastMath.sin(a), 5, 10*FastMath.cos(a));
						b.getRootNode().updateRenderState();
					}
					
					timer++;
				}
			}
		}
	}
	
	/**
	 * Rotate by {@code dTheta} in velocity direction.
	 */
	private void face() {
		float a = (FastMath.atan2(velocity.x, velocity.z)+FastMath.PI*4) % (2*FastMath.PI);
		float r = (roll+FastMath.PI*4) % (2*FastMath.PI);
		if (a != r) {
			float dif = r - a;
			if (dif < 0)
				dif += FastMath.PI*2;
			if (dif < FastMath.PI)
				roll = (float) Math.max(((r - dTheta) + FastMath.PI*2) % (FastMath.PI*2), a);
			else
				roll = (float) Math.min((r + dTheta), a);
			rotate();
		}
	}
	
	@Override
	public boolean reactHit(StageGameState b, Model other) {
		// reciprocate the hit
		if (other instanceof AttackObject) {
			((AttackObject)other).hit(b, this);
			hp -= other.getDamage()*2/3;
		}
		
		return true;
	}

}
