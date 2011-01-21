package com.googlecode.reaxion.game.model.enemies;

import java.util.ArrayList;

import com.googlecode.reaxion.game.model.Model;
import com.googlecode.reaxion.game.model.attackobject.AttackObject;
import com.googlecode.reaxion.game.model.attackobject.Meteor;
import com.googlecode.reaxion.game.state.StageGameState;
import com.googlecode.reaxion.game.util.LoadingQueue;
import com.jme.math.FastMath;
import com.jme.math.Vector3f;

/**
 * Pyroclast boss. Divided into five segments that
 * must be defeated one at a time.
 * 
 * @author Khoa
 */
public class Pyroclast extends Enemy {
	
	private final int numSegments = 5;
	private final int orbDist = 32;
	private final float floatHeight = 2;
	private final int meteorsPerLayer = 10;
	private final int meteorLayers = 3;
	private final int meteorLayerHeight = 48;
	private final int meteorLayerJitter = 18;
	private final int meteorLayerTime = 80;
	private ArrayList<PyroclastRock> sections = new ArrayList<PyroclastRock>();
	private PyroclastGeode[] orbs = new PyroclastGeode[2];
	public int[] deadOrb = new int[2];
	private Model core;
	
	private int rockTimer = 0;
	private int meteorTimer = 0;
	private int sweepTimer = 0;
	private int floatTimer = 0;
	
	private boolean falling = false;
	private boolean meteor = false;
	public int numRocks = 0;
	
	private final float dTheta = FastMath.PI/20;
	
	public Pyroclast() {
		super("enemies/rock-demon_head", true);
		setInfo("Pyroclast", 300, 1);
		init();
	}
		
	@Override
	protected void init() {
		super.init();
		mass = 10;
		gravity = -.06f;
		trackOffset = new Vector3f(0, 12, 0);
		boundRadius = 9f;
		boundHeight = 27f;
	}
	
	private void loadParts(StageGameState b) {
		// create segments
		for (int i=0; i<numSegments; i++) {
			sections.add((PyroclastRock) LoadingQueue.quickLoad(new PyroclastRock(this), b));
			sections.get(i).model.setLocalTranslation(model.getWorldTranslation().add(0, i*7.25f, 0));
			sections.get(i).roll = (i % 2 + i*2/numSegments) * FastMath.PI;
		}
		
		// create orb
		for (int i=0; i<orbs.length; i++) {
			orbs[i] = (PyroclastGeode) LoadingQueue.quickLoad(new PyroclastGeode(this, (i*2 - 1)*orbDist), b);
			orbs[i].model.setLocalTranslation((1 - i*2)*orbDist, -4, 0);
		}
		
		// create core
		core = LoadingQueue.quickLoad(new Model("enemies/rock-demon_core"), b);
		
		// reposition self
		model.setLocalTranslation(model.getLocalTranslation().add(0, 7.25f*numSegments, 0));
		
		// set tracking
		trackable = false;
		sections.get(0).trackable = true;
		sections.get(0).vulnerable = true;
		b.nextTarget(0);
		
		b.getRootNode().updateRenderState();
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
		// check for loading
		if (core == null)
			loadParts(b);
		
		// billboard core
		core.model.setLocalTranslation(model.getLocalTranslation().add(0, 11.25f, 0));
		core.billboard(b.getCamera(), true);
		
		// check death flag
		if (hp <= 0) {
			b.removeModel(core);
			velocity = new Vector3f();
			return;
			
		} else {
			
			// watch for falling
			if (falling)
				checkFall();
			
			// float if no boulders left
			if (sections.size() == 0) {
				Vector3f pos = model.getLocalTranslation();
				model.setLocalTranslation(pos.x, floatHeight - FastMath.sin(floatTimer*2*FastMath.PI/90), pos.z);
				floatTimer = (floatTimer + 1) % 90;
			}
			
			// try to revive dead orbs
			for (int i=0; i<deadOrb.length; i++) {
				if (deadOrb[i] >= 0) {
					if (deadOrb[i] == 0 && orbs[i].hp <= 0) {
						orbs[i] = (PyroclastGeode) LoadingQueue.quickLoad(new PyroclastGeode(this, (i*2 - 1)*orbDist), b);
						orbs[i].model.setLocalTranslation((1 - i*2)*orbDist, -4, 0);
					}
					deadOrb[i]--;
				}
			}
			
			if (meteor) {
				// rain meteors
				if (meteorTimer % meteorLayerTime == 0) {
					for (int i=0; i<meteorsPerLayer; i++) {
						Meteor m = (Meteor)LoadingQueue.quickLoad(new Meteor(getUsers().toArray(new Model[0])), b);
						float theta = 2*FastMath.PI*FastMath.nextRandomFloat();
						m.model.setLocalTranslation(138*FastMath.sin(theta),
								meteorLayerHeight + FastMath.nextRandomFloat()*meteorLayerJitter*2 - meteorLayerJitter,
								138*FastMath.cos(theta));
						
					}
					b.getRootNode().updateRenderState();
					
					if (meteorTimer/meteorLayerTime >= meteorLayers) {
						meteor = false;
						rockTimer = -1;
						meteorTimer = -1;
					}
				}
				
			} else {
				
				// try to create defenses
				if (rockTimer == 180) {
					if (numRocks == 0 && FastMath.nextRandomFloat() < .3f) {
						numRocks = 5;
						for (int i=0; i<5; i++) {
							RockProtector r = (RockProtector)LoadingQueue.quickLoad(new RockProtector(getUsers(), this, 2*FastMath.PI/5*i), b);
						}
						b.getRootNode().updateRenderState();
					}
					rockTimer = -1;
				}
				
				// try to rain meteors
				if (meteorTimer == 270) {
					if (FastMath.nextRandomFloat() < .7f) {
						meteor = true;
					}
					meteorTimer = -1;
				}
				
				// try to sweep fire
				if (sweepTimer == 270) {
					int dir = FastMath.nextRandomInt(0, 1)*2 - 1;
					for (int i=0; i<orbs.length; i++) {
						if (orbs[i].hp > 0 && orbs[i].mode == "idle") {
							orbs[i].mode = "sweep";
							orbs[i].dir = dir;
						}
					}
				}
			}
			
			rockTimer++;
			meteorTimer++;
			sweepTimer = (sweepTimer + 1) % 271;
			face(b.getPlayer().model.getWorldTranslation());
		}
	}
	
	/**
	 * Stops falling when bottom piece is in contact.
	 */
	private void checkFall() {
		Model low;		
		if (sections.size() > 0)
			low = sections.get(0);
		else
			low = this;
		float dist = low.model.getWorldTranslation().y;
		if (sections.size() == 0)
			dist -= floatHeight;
		if (dist - low.getVelocity().y + low.gravity <= 0) {
			// hit ground!
			falling = false;
			for (int i=0; i<sections.size(); i++) {
				sections.get(i).gravitate = false;
				sections.get(i).setVelocity(new Vector3f());
				sections.get(i).model.setLocalTranslation(sections.get(i).model.getLocalTranslation().subtract(0, dist, 0));
			}
			gravitate = false;
			velocity = new Vector3f();
			model.setLocalTranslation(model.getLocalTranslation().subtract(0, dist, 0));
		}
	}
	
	/**
	 * Destroy the lowest rock segment.
	 */
	public void reduce(StageGameState b) {
		b.removeModel(sections.remove(0));
		falling = true;
		gravitate = true;
		
		if (sections.size() > 0) {
			sections.get(0).trackable = true;
			sections.get(0).vulnerable = true;
			for (int i=0; i<sections.size(); i++)
				sections.get(i).gravitate = true;
		} else
			trackable = true;
	}
	
	public ArrayList<Model> getUsers() {
		ArrayList<Model> m = new ArrayList<Model>();
		m.addAll(sections);
		m.add(orbs[0]);
		m.add(orbs[1]);
		m.add(this);
		return m;
	}
	
	/**
	 * Rotate by {@code dTheta} in vector direction.
	 */
	private void face(Vector3f vec) {
		Vector3f to = vec.subtract(model.getWorldTranslation());
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
	
	@Override
	public boolean reactHit(StageGameState b, Model other) {
		// only take damage if no segments are left
		if (sections.size() == 0) {
			// reciprocate the hit
			if (other instanceof AttackObject) {
				((AttackObject)other).hit(b, this);
				hp -= other.getDamage()*1/5;
			}
			
			return true;
		}
		
		return false;
	}

}
