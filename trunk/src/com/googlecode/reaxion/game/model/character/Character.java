package com.googlecode.reaxion.game.model.character;

import com.googlecode.reaxion.game.ability.Ability;
import com.googlecode.reaxion.game.attack.Attack;
import com.googlecode.reaxion.game.input.AIInput;
import com.googlecode.reaxion.game.model.Model;
import com.googlecode.reaxion.game.model.attackobject.AttackObject;
import com.googlecode.reaxion.game.state.StageGameState;
import com.jme.math.FastMath;
import com.jme.math.Vector3f;
import com.radakan.jme.mxml.anim.MeshAnimationController;

/**
 * Builds upon {@code Model} by adding characters traits, such as HP and HP.
 * This class such be used for all characters, player or AI-controlled, as input
 * is abstracted from the actual class.
 * @author Khoa
 */
public class Character extends Model {

	/**
	 * AI-input for this character
	 */
	private AIInput ai;

	/**
	 * Current hit points of character
	 */
	public double hp = 0;
	/**
	 * Maximum hit points of character
	 */
	public int maxHp = 0;

	/**
	 * Current gauge points of character
	 */
	public double gauge = 0;

	/**
	 * Rate as which gauge increases
	 */
	public double gaugeRate = 0;

	/**
	 * Lower gauge limit of character
	 */
	public int minGauge = 0;

	/**
	 * Upper gauge limit of character
	 */
	public int maxGauge = 0;

	/**
	 * Abilities for this character
	 */
	public Ability[] abilities;

	/**
	 * Whether movement is being locked
	 */
	public boolean moveLock = false;

	/**
	 * Whether jumping is being locked
	 */
	public boolean jumpLock = false;

	/**
	 * Whether flinching or not
	 */
	public boolean flinching;

	/**
	 * Current attack of character
	 */
	public Attack currentAttack;

	/**
	 * Allowed scalar speed
	 */
	public float speed = 0;

	/**
	 * Initial jump velocity
	 */
	public float jump = 1;
	
	/**
	 * Value used to determine what pushes what
	 */
	public int mass = 1;

	protected boolean frozen = false;

	/**
	 * Bounding capsule (cylinder with spherical ends),
	 * used to facilitate movement detection
	 */
	public float boundRadius = 1;
	public float boundHeight = 4.5f;

	public Character() {
		super();
		init();
	}

	public Character(String filename) {
		super(filename);
		init();
	}

	public void assignAI(AIInput a) {
		ai = a;
	}

	@Override
	protected void init() {
		super.init();
		gravitate = true;
		gravity = -.06f;
	}

	@ Override
	public void act(StageGameState b) {

		// check if alive
		if (hp > 0) {

			// call AI
			if (ai != null)
				ai.makeCommands(b);
			
			super.act(b);

			// call abilities
			if (abilities != null) {
				boolean flag = false;
				for (int i=0; i<abilities.length; i++)
					flag = flag || abilities[i].act(this, b);
				if (flag)
					return;
			}

			// call partner's abilities
			if (this == b.getPlayer()) {
				Character partner =  b.getPartner();
				if (partner.hp > 0 && partner.abilities != null) {
					boolean flag = false;
					for (int i=0; i<partner.abilities.length; i++)
						flag = flag || partner.abilities[i].act(this, b);
					if (flag)
						return;
				}
			}

			// execute attack
			if (currentAttack != null)
				currentAttack.enterFrame(b);

			// rotate to match direction
			if (velocity.x != 0 || velocity.z != 0 /*|| vector.y != 0*/)
				rotate(velocity);

			// apply gravity
			if (gravitate) {
				velocity.y += gravVel;
				if (model.getLocalTranslation().y > 0)
					gravVel += gravity;
			}

			// check the ground
			contactGround();
			// remain inside the stage
			b.getStage().contain(this);
			// push characters around
			moveCollide(b);
			// remain inside the stage again
			b.getStage().contain(this);
			// check the ground once more
			contactGround();
			
			Vector3f loc = model.getLocalTranslation();
			loc.addLocal(velocity);
			model.setLocalTranslation(loc);

			// increase gauge
			gauge = Math.min(gauge + gaugeRate*maxGauge, maxGauge);

			/*
	        if (b.getPlayer() == this)
	        	System.out.println(gauge +" + "+gaugeRate +" : ("+ minGauge +", "+ maxGauge +")");
			*/
			/*
	        if (getCollisions(b).length > 0)
	        	System.out.println("collision with: "+Arrays.toString(getCollisions(b)));
			 */
		}
	}

	/**
	 * Checks for other characters with bounding capsules and adjusts movement vector
	 * based on the first perceived collision, if one exists.
	 */
	private void moveCollide(StageGameState b) {
		for (Model m:b.getModels()) {
			// check if the other Model is a Character, has a bounding capsule, and is more massive
			if (m instanceof Character && m != this && ((Character)m).mass >= mass) {
				Character c = (Character)m;
				if (c.boundRadius != 0 && c.boundHeight != 0) {
					// since both capsules are topologically circles, this suffices as an intersection check
					// current velocity is factored in to predict where the Character will be
					float x = c.model.getWorldTranslation().x + c.velocity.x - model.getWorldTranslation().x - velocity.x;
					float y = c.model.getWorldTranslation().y + c.velocity.y - model.getWorldTranslation().y - velocity.y;
					float z = c.model.getWorldTranslation().z + c.velocity.z - model.getWorldTranslation().z - velocity.z;
					float h = boundRadius + c.boundRadius - FastMath.sqrt(FastMath.pow(x, 2) + FastMath.pow(z, 2));
					// if they overlap, investigate within the plane of intersection along Y
					if (h > 0) {
						System.out.println(model+" collided with "+c.model);
						resolveBounds(c, x, y, z);
						System.out.println(model.getWorldTranslation()+" -> "+model.getWorldTranslation().add(velocity));
						break;
					}
				}
			}
		}
	}

	/**
	 * Resolves collision between two bounding capsules by altering the velocity vector
	 */
	private void resolveBounds(Character c, float x, float y, float z) {
		// angle of displacement in XZ plane
		float angleXZ = FastMath.atan2(z, x);
		// distance between radii along normal axis
		float dd = FastMath.sqrt(FastMath.pow(x, 2) + FastMath.pow(z, 2));
		// if collision within the other Character's cylinder, only displace in the XZ plane
		if ((y + c.boundRadius >= boundRadius && y <= boundHeight - boundRadius) || /* bottom of other cylinder is inside this one */
				(boundRadius - y >= c.boundRadius && boundRadius - y <= c.boundHeight - c.boundRadius)) { /* bottom of this cylinder is inside other */
			//magnitude of displacement in the XZ plane
			float shift = boundRadius + c.boundRadius - dd;
			// fix the velocity vector so that the collision doesn't occur
			velocity.x -= shift*FastMath.cos(angleXZ);
			velocity.z -= shift*FastMath.sin(angleXZ);

			// if collision is between bounding caps, displace in the plane of collision
		} else {
			// distance between radii on Y axis
			float dy = 0;

			// if collision between this bottom cap and other Character's top cap
			if (y > -c.boundHeight && y < c.boundRadius - c.boundHeight) {
				// distance between radii on Y axis
				dy = c.boundHeight - c.boundRadius + (y - boundRadius); 

				// if collision between this top cap and other Character's bottom cap
			} else if (y > boundHeight - boundRadius && y < boundHeight) {
				// distance between radii on Y axis
				dy = c.boundRadius + y - (boundHeight - boundRadius);
			}
			if (dy != 0) {
				// find magnitude of displacement, sum of radii minus actual distance
				float d = boundRadius + c.boundRadius - FastMath.sqrt(FastMath.pow(dd, 2) + FastMath.pow(dy, 2));
				// angle of displacement in plane of collision
				float angleDY = FastMath.atan2(dy, dd);
				// magnitude of shift along line of collision
				float shiftD = d * FastMath.cos(angleDY);
				// shift along Y
				float shiftY = d * FastMath.sin(angleDY);
				// vector pointing from contact point to innermost overlap point
				Vector3f impact = new Vector3f(shiftD * FastMath.cos(angleXZ), shiftY, shiftD * FastMath.sin(angleXZ));
				// fix the velocity vector so that the collision doesn't occur
				velocity = velocity.subtract(impact);
			}
		}
	}

	/**
	 * Sets the abilities for this Character and invokes {@code set()} for each
	 * of these abilities.
	 */
	public void setAbilities (Ability[] a) {
		abilities = a;
		for (Ability t : abilities)
			t.set(this);
	}

	/**
	 * Recovers HP by {@code d} amount.
	 * @param b - current {@code BattleGameState}.
	 * @param d - Amount of HP to recover.
	 * @return Whether healing was executed normally or not.
	 */
	public boolean heal(StageGameState b, double d) {
		// call abilities
		if (abilities != null) {
			boolean flag = false;
			for (int i=0; i<abilities.length; i++)
				flag = flag || abilities[i].heal(this, b, d);
			if (flag)
				return false;
		}
		// call partner's abilities
		if (this == b.getPlayer()) {
			Character partner =  b.getPartner();
			if (partner.hp > 0 && partner.abilities != null) {
				boolean flag = false;
				for (int i=0; i<partner.abilities.length; i++)
					flag = flag || partner.abilities[i].heal(this, b, d);
				if (flag)
					return false;
			}
		}
		hp = Math.min(hp+d, maxHp);
		return true;
	}

	/**
	 * Called by {@code AttackObjects} to register a hit and respond accordingly
	 * 
	 * @param b - current BattleGameState
	 * @param other - Other object involved
	 * @return Whether attack reaction was handled directly or whether the action
	 * was intercepted
	 */
	public boolean hit(StageGameState b, Model other) {
		// call abilities
		if (abilities != null) {
			boolean flag = false;
			for (int i=0; i<abilities.length; i++)
				flag = flag || abilities[i].hit(this, b, other);
			if (flag)
				return false;
		}
		// call partner's abilities
		if (this == b.getPlayer()) {
			Character partner =  b.getPartner();
			if (partner.hp > 0 && partner.abilities != null) {
				boolean flag = false;
				for (int i=0; i<partner.abilities.length; i++)
					flag = flag || partner.abilities[i].hit(this, b, other);
				if (flag)
					return false;
			}
		}
		if (currentAttack != null) {
			currentAttack.interrupt(b, other);
			return false;
		} else {
			return reactHit(b, other);
		}
	}

	/**
	 * After hit is confirmed, react by taking damage and flinching accordingly
	 * 
	 * @param b - current BattleGameState
	 * @param other - Other object involved
	 * @return Whether damage was taken or not
	 */   
	public boolean reactHit(StageGameState b, Model other) {
		// reciprocate the hit
		if (other instanceof AttackObject)
			((AttackObject)other).hit(b, this);

		// call abilities
		if (abilities != null) {
			boolean flag = false;
			for (int i=0; i<abilities.length; i++)
				flag = flag || abilities[i].reactHit(this, b, other);
			if (flag)
				return false;
		}
		// call partner's abilities
		if (this == b.getPlayer()) {
			Character partner =  b.getPartner();
			if (partner.hp > 0 && partner.abilities != null) {
				boolean flag = false;
				for (int i=0; i<partner.abilities.length; i++)
					flag = flag || partner.abilities[i].reactHit(this, b, other);
				if (flag)
					return false;
			}
		}

		if (other.flinch)
			toggleFlinch(true);
		hp -= other.getDamage();

		System.out.println(model+" hit by "+other+": "+(hp+other.getDamage())+" -> "+hp);
		return true;
	}

	/**
	 * Actions to take when entering/exiting flinch
	 */   
	public void toggleFlinch(boolean value) {
		// if flinching, stop and cancel any attacks
		if (value) {
			velocity = new Vector3f();
			if (currentAttack != null)
				currentAttack.finish();
		}
		flinching = value;
		jumpLock = value;
		moveLock = value;
		animationLock = value;
	}

	/**
	 * Handles the checking of animations, provided the names of the animations in
	 * the parameters. Must be called manually, usually by overriding the act method.
	 */
	public void animate(float tpf, String stand, String run, String jump, String cast, String raiseUp,
			String raiseDown, String shootUp, String shootDown, String guard, String flinch, String dying, String dead) {
		if (hp <= 0) {
			// interrupt attacks
			if (currentAttack != null)
				currentAttack.finish();
			
			// Play dying animation
			if (!frozen) {
				if (play(dying, tpf)) {
					play(dead);
					frozen = true;
				}
			}
		} else {
			if (flinching) {
				if (play(flinch, tpf))
					toggleFlinch(false);
			} else if (!animationLock) {
				// Switch animations when moving
				MeshAnimationController animControl = (MeshAnimationController) model.getController(0);
				/*System.out.println("{"+animControl.getActiveAnimation()+": "+
    			animControl.getCurTime()+"/"+animControl.getAnimationLength(animControl.getActiveAnimation())+"}");*/
				if (velocity.y != 0) {
					if (!animControl.getActiveAnimation().equals(jump))
						play(jump);
				} else if (velocity.x != 0 || velocity.z != 0) {
					if (!animControl.getActiveAnimation().equals(run))
						play(run);
				} else {
					if (!animControl.getActiveAnimation().equals(stand))
						play(stand);
				}
			}
		}
	}

}
