package com.googlecode.reaxion.game.overlay;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.googlecode.reaxion.game.input.bindings.BurstGridStateBindings;
import com.googlecode.reaxion.game.input.bindings.DialogueGameStateBindings;
import com.googlecode.reaxion.game.input.bindings.GameBindings;
import com.googlecode.reaxion.game.input.bindings.HubGameStateBindings;
import com.googlecode.reaxion.game.input.bindings.KeyBindings;
import com.googlecode.reaxion.game.input.bindings.MenuBindings;
import com.googlecode.reaxion.game.input.bindings.PlayerBindings;
import com.googlecode.reaxion.game.util.FontUtils;
import com.jme.input.KeyBindingManager;
import com.jme.input.KeyInput;
import com.jme.renderer.ColorRGBA;
import com.jme.scene.Node;
import com.jme.scene.shape.Quad;
import com.jmex.angelfont.BitmapText;
import com.jmex.angelfont.Rectangle;
import com.jmex.angelfont.BitmapFont.Align;

/**
 * Displays the main menu for the game.
 * @author Khoa Ha
 *
 */
public class TitleScreenOverlay extends Overlay {
	
	public static final String NAME = "titleScreenOverlay";	
	private static final String baseURL = "com/googlecode/reaxion/resources/gui/";
	
	private static final String[] allOptions = {"New Game", "Load Game", "Network Play", "Configure", "Extras"};
	private ArrayList<Integer> options;
	
	private int currentIndex = 0;
	
	private Node menu;
	private Quad selector;
	
	private Node container;
	
	public TitleScreenOverlay() {
		super(NAME);
		
		// create a container Node for scaling
        container = new Node("container");       
        
        Quad bg = getImage(baseURL+"title-wp0.png");
        bg.setLocalTranslation(400, 300, 0);
        
        // set up menu
        menu = new Node("menu");
        setOptions();
        createMenu();
        
        // set up selector
        selector = getImage(baseURL+"title-active.png");
        updateSelector();
        
        // attach children
        attachChild(container);
        container.attachChild(bg);
        container.attachChild(selector);
        container.attachChild(menu);
        
        container.setLocalScale((float) height/600);
        container.setLocalTranslation(offset*container.getLocalScale().x, 0, 0);
    }
	
	/**
	 * Populates the menu list according to certain conditions.
	 */
	private void setOptions() {
		options = new ArrayList<Integer>();
		
		// always have a New Game option
		options.add(0);
		
		// TODO: check whether save data exists
		options.add(1);
		
		// always have a Network Play option
		options.add(2);
		
		// always have a Configure option
		options.add(3);
		
		// TODO: check if extras are unlocked
		options.add(4);
	}
	
	/**
	 * Draws the visual elements for the menu.
	 */
	private void createMenu() {
		for (int i=0; i<options.size(); i++) {
			BitmapText text = new BitmapText(FontUtils.eurostile, false);
			text.setDefaultColor(new ColorRGBA(0, 0, 0, 1));
	        text.setSize(24);
	        text.setText(allOptions[options.get(i)]);
	        text.update();
	        text.setLocalTranslation(64 + 32*i, 172 + 24 - 32*i, 0);
	        
			Quad div = getImage(baseURL+"title-line.png");
			div.setLocalTranslation(64 + 138 - 48 + 32*i, 172 - 32*i, 0);
			
			menu.attachChild(text);
			menu.attachChild(div);
		}
	}
	
	/**
	 * Changes the menu selection by a specified amount.
	 */
	public void scroll(int amount) {
		currentIndex = (currentIndex + options.size() + amount) % options.size();
		updateSelector();
	}
	
	/**
	 * Updates the selector's position.
	 */
	private void updateSelector() {
		selector.setLocalTranslation(64 + 187 - 48 + 32*currentIndex, 172 + 16 - 32*currentIndex, 0);
	}
	
	/**
	 * Returns the current selection index corresponding
	 * to the item's index in {@code allOptions}.
	 */
	public int getOptionIndex() {
		return options.get(currentIndex);
	}
	
}