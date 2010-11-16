package com.googlecode.reaxion.game.model.enemies;

import com.googlecode.reaxion.game.audio.AudioPlayer;
import com.googlecode.reaxion.game.model.Model;
import com.googlecode.reaxion.game.model.attackobject.AttackObject;
import com.googlecode.reaxion.game.model.attackobject.DarkOrb;
import com.googlecode.reaxion.game.model.attackobject.Shockwave;
import com.googlecode.reaxion.game.state.StageGameState;
import com.googlecode.reaxion.game.util.LoadingQueue;
import com.jme.math.FastMath;
import com.jme.math.Vector3f;

/**
 * Toybox boss. Jumps around the field, shoots dark orbs, and tries to
 * smash the opponent. Can be toppled by attacking its head.
 * 
 * @author Khoa
 */
public class Toybox extends Enemy {

	private final float jumpError = 4f;
	private final int restTime = 40;
	private final int thrashWait = 140;
	private final int popWait = 80;
	private final float jumpSpeed = 1f;
	private final float dTheta = FastMath.PI/60;
	private final int fallTime = 520;
	
	private final float orbSpeed = 2f;
	
	private int diePhase = 0;
	
	private ToyboxHead head;
	private boolean prone = false;
	private int fallPhase = 0;
	
	private int fallCount = 0;
	
	private int jumpCount = 1;
	private int thrashCount = 0;
	private int popCount = 0;
	private int thrashPhase = 0;
	private int popPhase = 0;
	private int thrashNumber = 0;
	
	public Toybox() {
		super("enemies/toybox", true);
		setInfo("Toybox", 1400, 1);
		init();
	}

	private void load(StageGameState b) {
		head = (ToyboxHead) LoadingQueue.quickLoad(new ToyboxHead(this), b);
		head.model.setLocalTranslation(model.getWorldTranslation());
		b.getRootNode().updateRenderState();
	}
		
	@Override
	protected void init() {
		super.init();
		mass = 2;
		gravitate = true;
		gravity = -.06f;
		trackOffset = new Vector3f(0, 6, 0);
		boundRadius = 4f;
		boundHeight = 20f;
	}

	@Override
	protected void step(StageGameState b) {
		// load bounding model
		if (head == null)
			load(b);
		
		// check death flag
		if (hp <= 0) {
			velocity = new Vector3f();
			
			// lie down and die
			if (diePhase == 1 && play("fall", b.tpf)) {
				play("stun", b.tpf);
				diePhase = 2;
			}
			return;
		}
		// make head follow
		head.model.setLocalTranslation(model.getLocalTranslation());
		
		Vector3f dist = b.getPlayer().model.getWorldTranslation().subtract(model.getWorldTranslation());
		
		// check if fallen
		if (fallPhase > 0) {
			velocity = new Vector3f();
			
			if (fallPhase == 1 && play("fall", b.tpf)) {
				fallPhase = 2;
				play("stun", b.tpf);
			} else if (fallPhase == 2) {
				if (fallCount >= fallTime) {
					fallPhase = 3;
					fallCount = 0;
					play("getUp", b.tpf);
				} else
					fallCount++;
			}  else if (fallPhase == 3 && play("getUp", b.tpf)) {
				fallPhase = 4;
				play("popIn", b.tpf);
			} else if (fallPhase == 4 && play("popIn", b.tpf)) {
				// pop back in and end the attack
				prone = false;
				fallPhase = 0;
			}
			
		} else
		// check if thrashing
		if (thrashPhase > 0) {
			velocity = new Vector3f();
			
			// turn to aim
			if (thrashPhase == 4)
				facePlayer(dist);
			
			if (thrashPhase == 1 && play("popUp", b.tpf)) {
				thrashPhase = 2;
				play("bendDown", b.tpf);
			} else if (thrashPhase == 2 && play("bendDown", b.tpf)) {
				thrashPhase = 3;
				play("slamDown", b.tpf);
			} else if (thrashPhase == 3 && play("slamDown", b.tpf)) {
				thrashPhase = 4;
				play("slamUp", b.tpf);
				thrashNumber--;
			} else if (thrashPhase == 4 && play("slamUp", b.tpf)) {
				// thrash more if able to
				if (thrashNumber > 0)
					thrashPhase = 3;
				else {
					thrashPhase = 5;
					play("bendUp", b.tpf);
				}
			} else if (thrashPhase == 5 && play("bendUp", b.tpf)) {
				thrashPhase = 6;
				play("popIn", b.tpf);
			} else if (thrashPhase == 6 && play("popIn", b.tpf)) {
				// pop back in and end the attack
				prone = false;
				thrashPhase = 0;
				head.setDamage(false);
			}
		} else
		// check if popping
		if (popPhase > 0) {
			velocity = new Vector3f();
			if (popPhase == 1 && play("popUp", b.tpf)) {
				// create orbs
				for (int i=0; i<24; i++) {
					DarkOrb d = (DarkOrb)LoadingQueue.quickLoad(new DarkOrb(this), b);
					d.theta = i*FastMath.PI/6 + FastMath.nextRandomFloat()*FastMath.PI/6;
					d.speed = FastMath.nextRandomFloat()*orbSpeed;
					d.model.setLocalTranslation(model.getWorldTranslation().add(new Vector3f(0, 10, 0)));
					b.getRootNode().updateRenderState();
				}
				popPhase = 2;
				play("bounce", b.tpf);
			} else if (popPhase == 2 && play("bounce", b.tpf)) {
				popPhase = 3;
				play("popIn", b.tpf);
			} else if (popPhase == 3 && play("popIn", b.tpf)) {
				// pop back in and end the attack
				prone = false;
				popPhase = 0;
			}
				
		} else if (model.getLocalTranslation().y == 0) {
			
			// try to thrash
			if (thrashCount >= thrashWait && FastMath.nextRandomFloat() < .5f) {	
				play("popUp", b.tpf);
				thrashPhase = 1;
				thrashNumber = FastMath.nextRandomInt(6, 10);
				prone = true;
				head.setDamage(true);
				
			} else
			// try to pop
			if (popCount >= popWait && FastMath.nextRandomFloat() < .5f) {
				play("popUp", b.tpf);
				popPhase = 1;
				prone = true;
				
			} else {
				
				// check if just landed
				if (jumpCount == 0) {
					velocity = new Vector3f();
					// create a shockwave
					Shockwave s = (Shockwave)LoadingQueue.quickLoad(new Shockwave(this, 32, 36, 10), b);
					s.model.setLocalTranslation(model.getWorldTranslation());
					b.getRootNode().updateRenderState();
					
				} else if (jumpCount >= restTime) {
					// jump close to player
					float r = FastMath.nextRandomFloat()*jumpError;
					float t = FastMath.nextRandomFloat()*FastMath.PI*2;
					Vector3f s = new Vector3f(dist.x+r*FastMath.cos(t), 0, dist.z+r*FastMath.sin(t));
					float vy = -s.length()*gravity/(2*jumpSpeed);
					velocity = s.normalize().mult(jumpSpeed).add(new Vector3f(0, vy, 0));
					
					//System.out.println("jump: "+dist+" : "+velocity);
					
					// reset count
					jumpCount = -1;
				}
				
				facePlayer(dist);
				
				play("idle", b.tpf);
			
				jumpCount++;
			}
			
			popCount = (popCount+1)%(popWait+1);
			thrashCount = (thrashCount+1)%(thrashWait+1);
		}
	}
	
	/**
	 * Override to also animate head.
	 */
	@Override
	public boolean play(String state, float tpf) {
		head.play(state, tpf);
		return super.play(state, tpf);
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
		head.roll = roll;
		head.rotate();
	}
	
	@Override
	public boolean reactHit(StageGameState b, Model other) {
		// reciprocate the hit
		if (other instanceof AttackObject)
			((AttackObject)other).hit(b, this);

		// only take 1/10 damage if not prone
		if (!prone)
			hp -= other.getDamage()/10;
		else {
			if (fallPhase == 0) {
				// check head
				boolean flag = false;
				for (Model c : head.collisions)
					flag = flag || head.checkHit(b, c);
				if (!flag)
					hp -= other.getDamage()/10;
			} else
				hp -= other.getDamage();
		}
		if (hp <= 0) {
			diePhase = 1;
			play("fall", b.tpf);
		}
		
		return true;
	}
	
	public void hitHead(StageGameState b, Model other) {
		if (prone) {
			// fall down
			if (fallPhase == 0) {
				popPhase = 0;
				thrashPhase = 0;
				popCount = 0;
				thrashCount = 0;
				play("fall", b.tpf);
				fallPhase = 1;
				head.setDamage(false);
			}
			hp -= other.getDamage();
			
			if (hp <= 0) {
				diePhase = 1;
				play("fall", b.tpf);
			}
		}
	}

}
