package com.googlecode.reaxion.game.model.enemies;

import com.googlecode.reaxion.game.model.Model;
import com.googlecode.reaxion.game.model.attackobject.Fire;
import com.googlecode.reaxion.game.model.attackobject.RockBall;
import com.googlecode.reaxion.game.state.StageGameState;
import com.googlecode.reaxion.game.util.LoadingQueue;
import com.jme.math.FastMath;
import com.jme.math.Vector3f;

/**
 * Prop for Pyroclast. Shoots fire and boulders.
 */
public class PyroclastGeode extends Enemy {
	
	private final int reviveTime = 1080;
	private final float floatHeight = 12;
	// limit shooting range to pi/4 on the opposite side
	private final float maxDist = new Vector3f(-32, 0, 0).distance(new Vector3f(138*FastMath.sin(FastMath.PI/4), 0, 138*FastMath.sin(FastMath.PI/4)));
	private final float speed = 3;
	private final float sweepRadius = 112;
	private final float sweepHeight = 4;
	private final float sweepAngle = FastMath.PI*2/3;
	private final float sweepSpeed = FastMath.PI/300;
	private final int numRocks = 3;
	private final int rockDelay = 24;
	
	private Model target;
	private Pyroclast user;
	private float offset;
	
	public String mode = "idle";
	
	private int shootTimer = 0;
	
	private int sweepPhase = 0;
	private int sweepCount = 0;
	private float angleLeft = 0;
	public int dir;
	private float startAngle;

	private final float dTheta = FastMath.PI/80;
	
	public PyroclastGeode(Pyroclast u, float a) {
		super("enemies/rock-demon_shooter", true);
		setInfo("Geode", 30, 1);
		init();
		
		user = u;
		offset = a;
	}
		
	@Override
	protected void init() {
		super.init();
		mass = 10;
		trackOffset = new Vector3f(0, 0, 0);
		boundRadius = 4f;
		boundHeight = 8f;
    }
	
	@ Override
	public void act(StageGameState b) {

		// receive actions
		step(b);
		
		// check if alive
		if (hp > 0) {
			
			// apply gravity
			if (gravitate && model.getLocalTranslation().y > 0)
				velocity.y += gravity;

			// push characters around
//			moveCollide(b);
			
			Vector3f loc = model.getLocalTranslation();
			loc.addLocal(velocity);
			model.setLocalTranslation(loc);
			
		}
	}
	
	@Override
	protected void step(StageGameState b) {
		// check life
		if (hp <= 0) {
			// flag for revival
			user.deadOrb[(offset > 0)? 1 : 0] = reviveTime;
			b.removeModel(this);
			return;
		}
		
		target = b.getPlayer();
		Vector3f pos = model.getLocalTranslation();
		
		if (mode == "idle") {
			face(target.model.getLocalTranslation().subtract(pos));
			follow();
			
			if (shootTimer == 90 && pos.subtract(target.model.getLocalTranslation()).mult(new Vector3f(1, 0, 1)).length() <= maxDist &&
					FastMath.nextRandomFloat() < .4f) {
				mode = "shoot";
			}
			
			shootTimer = (shootTimer + 1) % 91;
			
		} else if (mode == "shoot") {
			// check distance
			Vector3f to = target.model.getLocalTranslation().subtract(pos);
			if (shootTimer/rockDelay >= numRocks || to.mult(new Vector3f(1, 0, 1)).length() > maxDist) {
				shootTimer = 0;
				mode = "idle";
			} else {
				// fire boulders
				if (shootTimer % rockDelay == 0) {
					RockBall m = (RockBall)LoadingQueue.quickLoad(new RockBall(user.getUsers().toArray(new Model[0])), b);
					m.model.setLocalTranslation(pos);
					m.setVelocity(to.normalize().mult(3));
					b.getRootNode().updateRenderState();
				}
			}
			shootTimer++;
			
		} else if (mode == "sweep") {
    		face(pos.mult(new Vector3f(1, 0, 1)));
    		switch (sweepPhase) {
    		case 0:
    			// pick a starting position
    			startAngle = FastMath.atan2(pos.x, pos.z) + FastMath.PI*FastMath.nextRandomFloat() - FastMath.PI/2;
    			angleLeft = sweepAngle;
    			sweepPhase++;
    			break;
    		case 1:
    			// fly outward
    			if (pos.mult(new Vector3f(1, 0, 1)).length() < sweepRadius)
    				velocity = new Vector3f(sweepRadius*FastMath.sin(startAngle), sweepHeight, sweepRadius*FastMath.cos(startAngle)).subtract(
    						pos).normalize().mult(speed);
    			else
    				sweepPhase++;
    			break;
    		case 2:
    			// circle around
    			float angle = FastMath.atan2(pos.x, pos.z) + (float)dir*sweepSpeed;
    			velocity = new Vector3f();
    			angleLeft -= sweepSpeed;
    			model.setLocalTranslation(sweepRadius*FastMath.sin(angle), sweepHeight, sweepRadius*FastMath.cos(angle));
    			
    			// shoot flames
    			if (sweepCount % 3 == 0) {
    				Fire f = (Fire)LoadingQueue.quickLoad(new Fire(user.getUsers().toArray(new Model[0])), b);
    				f.model.setLocalTranslation(pos);
    				f.setVelocity(pos.mult(new Vector3f(1, 0, 1)).normalize());
    				b.getRootNode().updateRenderState();
    			}
    			sweepCount++;
    			
    			// check angle
    			if (angleLeft <= 0)
    				sweepPhase++;
    			break;
    		case 3:
    			// retreat
    			if (follow()) {
    				sweepCount = 0;
    				sweepPhase = 0;
    				mode = "idle";
    			}
    		}
    	}
    }
	
	private boolean follow() {
		// move into line
		Vector3f pos = model.getLocalTranslation();
		Vector3f to = target.model.getWorldTranslation().subtract(user.model.getWorldTranslation()).mult(new Vector3f(1, 0, 1));
		float a = FastMath.atan2(to.x, to.z) - FastMath.PI/2;
		to = new Vector3f(offset*FastMath.sin(a), floatHeight, offset*FastMath.cos(a));
		if (pos.distance(to) > speed) {
			velocity = to.subtract(pos).normalize().mult(speed);
			return false;
		} else {
			velocity = to.subtract(pos);
			return true;
		}
	}
	
	/**
	 * Face indicated vector.
	 */
	private void face(Vector3f to) {
		float a = (FastMath.atan2(to.x, to.z)+FastMath.PI*4) % (2*FastMath.PI);
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
	
}
