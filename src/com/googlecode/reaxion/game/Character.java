package com.googlecode.reaxion.game;

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
	 * Current hit points of character
	 */
	public int hp = 0;
	/**
	 * Maximum hit points of character
	 */
	public int maxHp = 0;
	
	/**
	 * Current ability points of character
	 */
	public int ap = 0;
	
	/**
	 * Maximum ability points of character
	 */
	public int maxAp = 0;
	
	/**
	 * Allowed scalar speed
	 */
	public float speed = 0;
	
    public Character() {
    	super();
    	restrict();
    }
    
    public Character(String filename) {
    	super(filename);
    	restrict();
    }
    
    private void restrict() {
    	// Limit rotation to XZ plane
    	allowYaw = false;
    	allowPitch = false;
    }
    
    @ Override
    public void act() {
    	super.act();
    	
    	// move
    	vector = vector.mult(speed);
        Vector3f loc = model.getLocalTranslation();
        loc.addLocal(vector);
        model.setLocalTranslation(loc);
        
        // rotate to match direction
        if (vector.x != 0 || vector.z != 0 || vector.y != 0)
        	rotate(vector);
    }
    
    /**
	 * Handles the checking of animations, provided the names of the animations in
	 * the parameters. Must be called manually, usually by overriding the act method.
	 */
    public void animate(String stand, String run) {
    	// Switch animations when moving
    	MeshAnimationController animControl = (MeshAnimationController) model.getController(0);
    	//System.out.println("{"+animControl.getActiveAnimation()+"}");
    	if (vector.x != 0 || vector.z != 0 || vector.y != 0) {
    		if (!animControl.getActiveAnimation().equals(run))
    			play(run);
    	} else {
    		if (!animControl.getActiveAnimation().equals(stand))
    			play(stand);
    	}
    }
    
}
