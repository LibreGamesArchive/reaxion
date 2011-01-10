package com.googlecode.reaxion.game.state;

import com.googlecode.reaxion.game.Reaxion;
import com.googlecode.reaxion.game.input.bindings.GlobalBindings;
import com.googlecode.reaxion.game.input.bindings.KeyBindings;
import com.googlecode.reaxion.game.input.bindings.MenuBindings;
import com.googlecode.reaxion.game.overlay.KeyPreferencesOverlay;
import com.jme.app.AbstractGame;
import com.jme.input.InputHandler;
import com.jme.input.KeyBindingManager;
import com.jme.input.KeyInput;
import com.jme.input.KeyInputListener;
import com.jme.scene.Node;
import com.jmex.angelfont.BitmapFont;

/**
 * {@code KeyPreferencesState} provides an interface to assign key functions
 * to commands for all states in the game. This data can then be saved to a
 * file and reread to restore these preferences.
 * 
 * @author Khoa Ha
 * 
 */

public class KeyPreferencesState extends BaseGameState {

	private KeyPreferencesOverlay keyPreferencesNode;

	protected InputHandler input;
	protected KeyInput keyInput;
	private KeyBindingManager manager;

	protected AbstractGame game = null;
	
	private boolean awaitingInput = false;
	private int bindingIndex;
	
	public KeyPreferencesState() {
		super(true);
	}

	@Override
	protected void init() {
		rootNode = new Node("RootNode");

		keyPreferencesNode = new KeyPreferencesOverlay();
		rootNode.attachChild(keyPreferencesNode);

		input = new InputHandler();
		manager = KeyBindingManager.getKeyBindingManager();
		
		// register listener
		keyInput = KeyInput.get();
		keyInput.addListener(new KeyInputListener(){
		     public void onKey(char character, int keyCode, boolean pressed){
		          if (awaitingInput && pressed) {
		        	  KeyBindings binding = keyPreferencesNode.getBinding(bindingIndex);
		        	  
		        	  // check authenticity
		        	  if (manager.getAlwaysRepeatableKeyCodes().contains(keyCode)) {
		        		  info.alert("Cannot assign commands to meta keys.", BitmapFont.Align.Right, 60, 1);
		        	  } else if (keyCode == manager.getKeyCodesForCommand(binding.toString()).get(0).keys[0] ||
		        					  keyPreferencesNode.verifyUnqiue(binding, keyCode)) {
		        		  awaitingInput = false;
		        		  manager.set(binding.toString(), keyCode);
		        		  keyPreferencesNode.refreshKeys();
		        		  prompt.finishMessage();
		        		  info.alert(binding.toString()+" assigned to "+keyInput.getKeyName(keyCode)+"!", BitmapFont.Align.Right, 60, 1);
		        	  } else {
		        		  info.alert(keyInput.getKeyName(keyCode)+" conflicts with another command.", BitmapFont.Align.Right, 60, 1);
		        	  }
		          }
		     }
		});
		
		rootNode.updateRenderState();
		rootNode.updateWorldBound();
		rootNode.updateGeometricState(0.0f, true);
	}

	@Override
	public void stateUpdate(float tpf) {
		super.stateUpdate(tpf);
		
		if (input != null) {
			input.update(tpf);

			if (manager.isValidCommand(GlobalBindings.EXIT.toString(), false)) {
				if (game != null)
					game.finish();
				else
					Reaxion.terminate();
			}
		}

		if (!awaitingInput)
			checkKeyInput();
		
		rootNode.updateGeometricState(tpf, true);
	}
	
	/**
	 * Checks key input.
	 */
	private void checkKeyInput() {
		if (input != null) {
			if (manager.isValidCommand(MenuBindings.UP.toString(), false)) {
				keyPreferencesNode.scroll(-1);
			}
			if (manager.isValidCommand(MenuBindings.DOWN.toString(), false)) {
				keyPreferencesNode.scroll(1);
			}
			if (manager.isValidCommand(MenuBindings.SELECT_ITEM.toString(), false)) {
				awaitingInput = true;
				String binding = keyPreferencesNode.menu.getSelectedEntry(true);
				String key = keyPreferencesNode.keysMenu.getSelectedEntry(true);
				bindingIndex = keyPreferencesNode.keysMenu.getCurrentIndex();
				prompt.showMessage(binding+" is currently assigned to "+key+"\nPress the new key you want this action mapped to.", 20);
				
				System.out.println("Selected Item: "+binding);
			}
			if(manager.isValidCommand(MenuBindings.BACK.toString(), false)) {
				//returnToCharSelectState();
			}
		}

	}

	@Override
	public void cleanup() {
	}

}