package com.googlecode.reaxion.game;

import com.jme.input.InputHandler;
import com.jme.input.KeyBindingManager;
import com.jme.input.KeyInput;
import com.jme.math.FastMath;
import com.jme.math.Vector3f;
import com.jme.renderer.Camera;

/**
 * Input Handler for the allows player to move it forward, backward, left, and right.
 * Attacks and other inputs pertaining to the player will also go here.
 * @author Khoa
 *
 */
public class PlayerInput extends InputHandler {

	private MajorCharacter player;
	private Camera camera;
	
	private Boolean forthOn = false;
	private Boolean leftOn = false;
	private Boolean upOn = false;
	
    /**
     * Supply the node to control and the api that will handle input creation.
     * @param p the player character
     * @param c the camera for the current state
     */
    public PlayerInput(MajorCharacter p, Camera c) {
    	player = p;
    	camera = c;
        setKeyBindings();
    }

    /**
     * Creates the keyboard object, allowing us to obtain the values of a keyboard as keys are
     * pressed. It then sets the actions to be triggered based on if certain keys are pressed (numpad).
     * @author Khoa
     */
    private void setKeyBindings() {
        KeyBindingManager keyboard = KeyBindingManager.getKeyBindingManager();

        keyboard.set("forth", KeyInput.KEY_NUMPAD8);
        keyboard.set("back", KeyInput.KEY_NUMPAD2);
        keyboard.set("right", KeyInput.KEY_NUMPAD6);
        keyboard.set("left", KeyInput.KEY_NUMPAD4);
        keyboard.set("up", KeyInput.KEY_NUMPAD7);
        keyboard.set("down", KeyInput.KEY_NUMPAD1);
    }
    
    /**
     * Must be called during {@code update()} by the {@code GameState}. Checks the current state of
     * input commands, preserving priority by the order in which they are pressed. Sets player's unit
     * vector accordingly. (Since theta = 0 is along the Z axis,)
     * @author Khoa
     */
    public void checkKeys() {
    	// check priority key order
    	if (KeyBindingManager.getKeyBindingManager().isValidCommand("forth", false))
    		forthOn = true;
    	if (KeyBindingManager.getKeyBindingManager().isValidCommand("back", false))
    		forthOn = false;
    	if (KeyBindingManager.getKeyBindingManager().isValidCommand("right", false))
    		leftOn = false;
    	if (KeyBindingManager.getKeyBindingManager().isValidCommand("left", false))
    		leftOn = true;
    	if (KeyBindingManager.getKeyBindingManager().isValidCommand("up", false))
    		upOn = false;
    	if (KeyBindingManager.getKeyBindingManager().isValidCommand("down", false))
    		upOn = true;
    	
    	// create unit vector and check for priority releases
    	float unitX = 0f;
    	float unitY = 0f;
    	float unitZ = 0f;
    	if (KeyBindingManager.getKeyBindingManager().isValidCommand("forth", true)) {
    		if (forthOn)
    			unitX = -1f;
    	} else {
    		forthOn = false;
    	}
    	if (KeyBindingManager.getKeyBindingManager().isValidCommand("back", true)) {
    		if (!forthOn)
    			unitX = 1f;
    	} else {
    		forthOn = true;
    	}
    	if (KeyBindingManager.getKeyBindingManager().isValidCommand("right", true)) {
    		if (!leftOn)
    			unitZ = 1f;
    	} else {
    		leftOn = true;
    	}
    	if (KeyBindingManager.getKeyBindingManager().isValidCommand("left", true)) {
    		if (leftOn)
    			unitZ = -1f;
    	} else {
    		leftOn = false;
    	}
    	if (KeyBindingManager.getKeyBindingManager().isValidCommand("down", true)) {
    		if (!upOn)
    			unitY = -1f;
    	} else {
    		upOn = true;
    	}
    	if (KeyBindingManager.getKeyBindingManager().isValidCommand("up", true)) {
    		if (upOn)
    			unitY = 1f;
    	} else {
    		upOn = false;
    	}
    	
    	// normalize vector
    	if (Math.abs(unitX) + Math.abs(unitY) + Math.abs(unitZ) > 1) {
    		float hyp = (float) Math.sqrt(Math.pow(unitX, 2) + Math.pow(unitY, 2) + Math.pow(unitZ, 2));
    		unitX /= hyp;
    		unitY /= hyp;
    		unitZ /= hyp;
    	}
    	
    	// calculate new angle in XZ plane
    	Vector3f cp = camera.getLocation();
    	Vector3f pp = player.getTrackPoint();
    	float angle = FastMath.atan2(cp.x-pp.x, cp.z-pp.z);
    	
    	
    	// rotate XZ components
    	float nUnitX = unitX*FastMath.sin(angle) + unitZ*FastMath.cos(angle);
    	float nUnitZ = unitX*FastMath.cos(angle) - unitZ*FastMath.sin(angle);
    	
    	// assign vector to player
    	player.setVector(new Vector3f(nUnitX, unitY, nUnitZ));
    }
}