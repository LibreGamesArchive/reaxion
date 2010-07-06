package com.googlecode.reaxion.game;

import com.jme.input.InputHandler;
import com.jme.input.KeyBindingManager;
import com.jme.input.KeyInput;
import com.jme.math.Vector3f;

/**
 * Input Handler for the allows player to move it forward, backward, left, and right.
 * Attacks and other inputs pertaining to the player will also go here.
 * @author Khoa
 *
 */
public class PlayerInput extends InputHandler {

	private Character player;
	
	private Boolean forthOn = false;
	private Boolean leftOn = false;
	
    /**
     * Supply the node to control and the api that will handle input creation.
     * @param node the node we wish to move
     * @param api the library that will handle creation of the input.
     */
    public PlayerInput(Character p) {
    	player = p;
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
    }
    
    /**
     * Must be called during {@code update()} by the {@code GameState}. Checks the current state of
     * input commands, preserving priority by the order in which they are pressed. Sets player's unit
     * vector accordingly.
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
    	
    	// create unit vector and check for priority releases
    	float unitX = 0f;
    	float unitY = 0f;
    	float unitZ = 0f;
    	if (KeyBindingManager.getKeyBindingManager().isValidCommand("forth", true)) {
    		if (forthOn)
    			unitZ = -1f;
    	} else {
    		forthOn = false;
    	}
    	if (KeyBindingManager.getKeyBindingManager().isValidCommand("back", true)) {
    		if (!forthOn)
    			unitZ = 1f;
    	} else {
    		forthOn = true;
    	}
    	if (KeyBindingManager.getKeyBindingManager().isValidCommand("right", true)) {
    		if (!leftOn)
    			unitX = 1f;
    	} else {
    		leftOn = true;
    	}
    	if (KeyBindingManager.getKeyBindingManager().isValidCommand("left", true)) {
    		if (leftOn)
    			unitX = -1f;
    	} else {
    		leftOn = false;
    	}
    	
    	// normalize vector
    	if (Math.abs(unitX) + Math.abs(unitY) + Math.abs(unitZ) > 1) {
    		float hyp = (float) Math.sqrt(Math.pow(unitX, 2) + Math.pow(unitY, 2) + Math.pow(unitZ, 2));
    		unitX /= hyp;
    		unitY /= hyp;
    		unitZ /= hyp;
    	}
    	
    	// assign vector to player
    	player.setVector(new Vector3f(unitX, unitY, unitZ));
    }
}