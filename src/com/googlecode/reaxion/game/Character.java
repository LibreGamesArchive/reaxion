package com.googlecode.reaxion.game;

import java.util.ArrayList;
import java.util.Arrays;

import com.jme.math.FastMath;
import com.jme.math.Vector3f;
import com.jme.scene.Spatial;
import com.jme.scene.shape.Sphere;
import com.radakan.jme.mxml.anim.MeshAnimationController;

/**
 * Builds upon {@code Model} by adding characters traits, such as HP and HP.
 * This class such be used for all characters, player or AI-controlled, as input
 * is abstracted from the actual class.
 * @author Khoa
 */
public class Character extends Model {
	
	/**
	 * Current hit points of character
	 */
	public int hp = 0;
	/**
	 * Maximum hit points of character
	 */
	public int maxHp = 0;
	
	/**
	 * Current gauge points of character
	 */
	public int gauge = 0;
	
	/**
	 * Maximum gauge points of character
	 */
	public int maxGauge = 0;
	
	/**
	 * Allowed scalar speed
	 */
	public float speed = 0;
	
	/**
	 * Initial jump velocity
	 */
	public float jump = 1;
	
	/**
	 * Y-velocity while airborne
	 */
	public float gravVel = 0;
	
	/**
	 * Bounding capsule (cylinder with spherical ends),
	 * used to facilitate movement detection
	 */
	public float boundRadius = 1;
	public float boundHeight = 4.5f;
	
    public Character() {
    	super();
    	gravity = -.06f;
    	restrict();
    }
    
    public Character(String filename) {
    	super(filename);
    	gravity = -.06f;
    	restrict();
    }
    
    private void restrict() {
    	// Limit rotation to XZ plane
    	allowYaw = false;
    	allowPitch = false;
    }
    
    @ Override
    public void act(BattleGameState b) {
    	super.act(b);
    	
        // rotate to match direction
        if (velocity.x != 0 || velocity.z != 0 /*|| vector.y != 0*/)
        	rotate(velocity);
    	
    	// move
        velocity = velocity.mult(speed);
        // apply gravity
        velocity.y += gravVel;
        gravVel += gravity;
        // check the ground
    	contactGround();
    	// let other Characters push player around
    	if (b.getPlayer() == this)
    		moveCollide(b);
    	// check the ground once more
    	contactGround();
        Vector3f loc = model.getLocalTranslation();
        loc.addLocal(velocity);
        model.setLocalTranslation(loc);
        
//        if (getCollisions(b).length > 0) {
//        	System.out.println("collision with: "+Arrays.toString(getCollisions(b)));
//        }
        
    }
    
    /**
     * Make the character jump if grounded (assuming ground is y=0), returns whether on ground
     */
    /*
    public void jump() {
    	if (model.getWorldTranslation().y<=0)
    		gravVel = jump;
    	return 
    }
    */
    
    /**
     * Checks for other characters with bounding capsules and adjusts movement vector
     * based on the first perceived collision, if one exists.
     */
    private void moveCollide(BattleGameState b) {
    	for (Model m:b.getModels()) {
    		// check if the other Model is a Character and has a bounding capsule
    		if (m instanceof Character && m != this) {
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
    						resolveBounds(c, x, y, z);
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
	 * Handles the checking of animations, provided the names of the animations in
	 * the parameters. Must be called manually, usually by overriding the act method.
	 */
    public void animate(String stand, String run, String jump) {
    	// Switch animations when moving
    	MeshAnimationController animControl = (MeshAnimationController) model.getController(0);
    	//System.out.println("{"+animControl.getActiveAnimation()+"}");
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
