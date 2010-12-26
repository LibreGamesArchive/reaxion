package com.googlecode.reaxion.game.model.enemies;

import com.googlecode.reaxion.game.model.Model;
import com.googlecode.reaxion.game.model.attackobject.AttackObject;
import com.googlecode.reaxion.game.model.attackobject.BugOrb;
import com.googlecode.reaxion.game.model.attackobject.DataBeam;
import com.googlecode.reaxion.game.model.attackobject.Laser;
import com.googlecode.reaxion.game.model.attackobject.Pellet;
import com.googlecode.reaxion.game.state.StageGameState;
import com.googlecode.reaxion.game.util.LoadingQueue;
import com.jme.math.FastMath;
import com.jme.math.Vector3f;

/**
 * Remnant boss. Attacks from out of bounds with lasers,
 * pillars of data, bug orbs, and bullets. Bug orbs can
 * be reflected for damage.
 * 
 * @author Khoa
 */
public class Remnant extends Enemy {

	private String mode = "idle"; // idle, ball, laser, array, circle, flinch

	private final int changeTime = 180;
	private final int numBeams = 20;
	private final int beamDelay = 30;
	private final int numBeamRows = 12;
	private final int numBullets = 24;
	private final int bulletDelay = 30;
	private final int numBulletRounds = 18;
	
	private int timer = 0;
	private int step = 0;
	private int beamCount = 0;
	private int bulletCount = 0;
	
	private Laser laser;
	
	public Remnant() {
		super("enemies/fragment", true);
		setInfo("Remnant", 512, 1);
		init();
	}
		
	@Override
	protected void init() {
		super.init();
		mass = 10;
		gravitate = true;
		gravity = -.06f;
		trackOffset = new Vector3f(0, 12*4, 0);
		boundRadius = 12f*8;
		boundHeight = 18f*8;
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

			// check the ground
			contactGround();
			
			// push characters around
			moveCollide(b);
			
			// check the ground once more
			contactGround();
			
			Vector3f loc = model.getLocalTranslation();
			loc.addLocal(velocity);
			model.setLocalTranslation(loc);
			
		}
	}
	
	@Override
	protected void step(StageGameState b) {
		
		// check death flag
		if (hp <= 0) {
			play("idle", b.tpf);
			return;
		}
		
		// stop moving
		velocity = new Vector3f();
		
		// change mode at random
		if (mode == "idle") {
			play("idle", b.tpf);

			if (timer >= changeTime)  {
				float r = FastMath.nextRandomFloat();
				if (r < hp/maxHp*(.4f - .1f) + .1f)
					mode = "ball";
				else if (r > 1 - (hp/maxHp*(.5f - .05f) + .05f))
					mode = "laser";
				else {
					r = FastMath.nextRandomFloat();
					if (r < .6f) {
						mode = "array";
						beamCount = 0;
					} else {
						mode = "circle";
						bulletCount = 0;
					}
				}
				timer = 0;
				step = 0;
			}
		
		// flinch
		} else if (mode == "flinch" && play("flinch", b.tpf)) {
			mode = "idle";
		
		// array
		} else if (mode == "array") {
			if (step == 0 && play("pushForth", b.tpf)) {
				play("pushHold", b.tpf);
				step++;
				
			} else if (step == 1) {
				int i = (int)Math.floor(Math.random()*numBeams);				
				if (beamCount / beamDelay <= numBeamRows - 1) {
					if (beamCount % beamDelay == 0) {
						i = (i + (int)Math.floor(Math.random()*5) -2) % numBeams;
						float offset = FastMath.nextRandomFloat()*4 - 2;
						for (int j= 0; j<numBeams; j++) {
							if (j != i) {
								DataBeam d = (DataBeam)LoadingQueue.quickLoad(new DataBeam(this), b);
								d.model.setLocalTranslation(model.getWorldTranslation().add((180*2/numBeams)*(j-numBeams/2) + offset, -24, 14*8));
								d.setVelocity(new Vector3f(0, 0, 2));
							}
						}
						b.getRootNode().updateRenderState();
					}
					beamCount++;
				} else {
					play("pushBack", b.tpf);
					step++;
				}
				
			} else if (step == 2 && play("pushBack", b.tpf)) {
				mode = "idle";
			}
			
		// laser
		} else if (mode == "laser") {
			if (step == 0 && play("laserForth", b.tpf)) {
				play("laserHold", b.tpf);
				Vector3f t = model.getLocalTranslation();
				Vector3f p = b.getPlayer().model.getLocalTranslation().mult(new Vector3f(1, 0, 1));
				float a = FastMath.nextRandomFloat()*FastMath.PI*2;
				laser = (Laser)LoadingQueue.quickLoad(new Laser(this, t.add(0, 11f*8, 9.5f*8),
						p.add(FastMath.sin(a)*48, 0, FastMath.cos(a)*48), b.getPlayer()), b);
				b.getRootNode().updateRenderState();
				step++;
				
			} else if (step == 1) {			
				if (laser.model.getParent() == null) {
					play("laserBack", b.tpf);
					step++;
				}
				
			} else if (step == 2 && play("laserBack", b.tpf)) {
				mode = "idle";
			}
		
		// circle
		} else if (mode == "circle") {
			if (step == 0 && play("pushForth", b.tpf)) {
				play("pushHold", b.tpf);
				step++;

			} else if (step == 1) {
				if (bulletCount / bulletDelay <= numBulletRounds - 1) {
					if (bulletCount % bulletDelay == 0) {
						for (int j= 0; j<numBullets; j++)  {
							float a = FastMath.nextRandomFloat()*FastMath.PI*2;
							Vector3f pt = new Vector3f(FastMath.sin(a)*180, 1, FastMath.cos(a)*180);
							Pellet p = (Pellet)LoadingQueue.quickLoad(new Pellet(this, pt, new Vector3f(0, 1, 0)), b);
							p.model.setLocalTranslation(pt);
						}
						b.getRootNode().updateRenderState();
					}
					bulletCount++;
				} else {
					play("pushBack", b.tpf);
					step++;
				}
			} else if (step == 2 && play("pushBack", b.tpf)) {
				mode = "idle";
			}
		
		// ball
		} else if (mode == "ball") {
			if (step == 0 && play("swipeForth", b.tpf)) {
				//for (int j= -1; j<=1; j++)  {
					BugOrb d = (BugOrb)LoadingQueue.quickLoad(new BugOrb(this, model.getLocalTranslation().add(0, 12*8, 6*8)), b);
					d.setVelocity(new Vector3f(0, 0, 1).mult(d.speed));
					d.rotate(d.getVelocity());
					d.model.setLocalTranslation(model.getWorldTranslation().add(/*8*9*j*/0, 10*8, 14*8));
				//}
				b.getRootNode().updateRenderState();
				play("swipeBack", b.tpf);
				step++;
			} else if (step == 1 && play("swipeBack", b.tpf)) {
				mode = "idle";
			}
		}
		
		// increase timer
		timer++;
		
	}
	
	@Override
	public boolean reactHit(StageGameState b, Model other) {
		// reciprocate the hit
		if (other instanceof AttackObject) {
			((AttackObject)other).hit(b, this);
			
			if (other instanceof BugOrb) {
				hp -= other.getDamage();
				
				// interrupt current action
				if (mode == "laser" && laser.model.getParent() != null)
					b.removeModel(laser);
				mode = "flinch";
				play("flinch", b.tpf);
			} else
				hp -= other.getDamage()/16;
		}
		
		return true;
	}

}
