package com.googlecode.reaxion.game.overlay;

import java.awt.Point;

import com.googlecode.reaxion.game.Reaxion;
import com.googlecode.reaxion.game.input.bindings.KeyBindings;
import com.googlecode.reaxion.game.input.bindings.MenuBindings;
import com.googlecode.reaxion.game.util.FontUtils;
import com.jme.math.Vector3f;
import com.jme.renderer.ColorRGBA;
import com.jme.scene.Node;
import com.jme.scene.shape.Quad;
import com.jmex.angelfont.BitmapFont;
import com.jmex.angelfont.BitmapText;

/**
 * Displays the main menu with choices to Character Selection, Stage Selection, Mission Selection, 
 * Stats Viewer, and Burst Grid.
 * @author Brian Clanton, Khoa Ha, Austin Hou
 *
 */

public class TerminalOverlay extends MenuOverlay {
	
	public static final String NAME = "terminalOverlay";
	
	private static final String baseURL = "com/googlecode/reaxion/resources/gui/";
	
	private static final String[] menuItemNames = {"Character Select", "Stage Select", "Mission Select", "View Stats", "Burst Grid", "Save Game"};
	
	private BitmapText[] menuItems;
	
	private int currentIndex;
	
	private Point[] menuGrid;
	
	private int fontSize;
	
	private Quad box;
	private Quad selector;
	
	private ColorRGBA selectedColor;
	private ColorRGBA unselectedColor;
	
	
	
	public TerminalOverlay() {
		super(NAME, 800, 600, false);
		
		container = new Node("terminalOverlay");
		
		selectedColor = FontUtils.blueSelected;
		unselectedColor = FontUtils.unselected;
		
		// create box
        box = getImage(baseURL+"menu-box.png");
        
        // create selector
        selector = getImage(baseURL+"selector.png");
        selector.setLocalTranslation(-94 + 71, 103 - 15, 0);
		
		currentIndex = 0;
		
		container.attachChild(box);
		container.attachChild(selector);
		
		createMenuList();
		
		container.updateRenderState();
		container.setLocalScale((float) Reaxion.getScreenHeight() / baseHeight);
		container.setLocalTranslation(Reaxion.getScreenWidth() / 2, Reaxion.getScreenHeight() / 2, 0);
		
		attachChild(container);
		
		updateRenderState();
	}
	
	private void createMenuList() {
		fontSize = 20;
		menuGrid = createCenteredTextList(menuItemNames.length, fontSize, 16);
		
		menuItems = new BitmapText[menuItemNames.length];
		
		for (int i = 0; i < menuItems.length; i++) {
			menuItems[i] = new BitmapText(FontUtils.eurostile, false);
			menuItems[i].setText(menuItemNames[i]);
			menuItems[i].setSize(fontSize);
			menuItems[i].setAlignment(BitmapFont.Align.Right);
			menuItems[i].setDefaultColor(i == 0 ? selectedColor : unselectedColor);
			menuItems[i].update();
			
			menuItems[i].setLocalTranslation(menuGrid[i].x + 80, menuGrid[i].y, 0);
			
			container.attachChild(menuItems[i]);
		}
		
		menuItems[currentIndex].setDefaultColor(selectedColor);
		menuItems[currentIndex].update();
	}
	
	public int getCurrentIndex() {
		return currentIndex;
	}
	
	public void updateDisplay(KeyBindings k) {
		int lastIndex = currentIndex;
		
		if (k == MenuBindings.UP) {
			currentIndex--;
			if (currentIndex == -1)
				currentIndex += menuItems.length;
		} else if (k == MenuBindings.DOWN) {
			currentIndex = (currentIndex + 1) % menuItems.length;
		}
		
		menuItems[lastIndex].setDefaultColor(unselectedColor);
		menuItems[lastIndex].update();
		
		menuItems[currentIndex].setDefaultColor(selectedColor);
		menuItems[currentIndex].update();
		
		selector.setLocalTranslation(-94 + 71, Math.round(103 - 15 - (float)(currentIndex*210f/menuItems.length)), 0);
		
		updateRenderState();
	}
	
}
