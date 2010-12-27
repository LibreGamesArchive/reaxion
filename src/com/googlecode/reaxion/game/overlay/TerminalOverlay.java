package com.googlecode.reaxion.game.overlay;

import java.awt.Point;

import com.googlecode.reaxion.game.Reaxion;
import com.googlecode.reaxion.game.input.bindings.KeyBindings;
import com.googlecode.reaxion.game.input.bindings.MenuBindings;
import com.googlecode.reaxion.game.util.FontUtils;
import com.jme.renderer.ColorRGBA;
import com.jme.scene.Node;
import com.jmex.angelfont.BitmapFont;
import com.jmex.angelfont.BitmapText;

public class TerminalOverlay extends MenuOverlay {
	
	public static final String NAME = "terminalOverlay";
	
	private static final String[] menuItemNames = {"Character Select", "Stage Select", "Mission Select", "Burst Grid", "Save Game"};
	
	private BitmapText[] menuItems;
	
	private int currentIndex;
	
	private Point[] menuGrid;
	
	private int fontSize;
	
	private ColorRGBA selectedColor;
	private ColorRGBA unselectedColor;
	
	public TerminalOverlay() {
		super(NAME, 800, 600, false);
		
		container = new Node("terminalOverlay");
		
		selectedColor = FontUtils.greenSelected;
		unselectedColor = FontUtils.unselected;
		
		currentIndex = 0;
		
		createMenuList();
		
		container.updateRenderState();
		container.setLocalScale((float) Reaxion.getScreenHeight() / baseHeight);
		container.setLocalTranslation(Reaxion.getScreenWidth() / 2, Reaxion.getScreenHeight() / 2, 0);
		
		attachChild(container);
	}
	
	private void createMenuList() {
		fontSize = 36;
		menuGrid = createCenteredTextList(menuItemNames.length, fontSize, 10);
		
		menuItems = new BitmapText[menuItemNames.length];
		
		for (int i = 0; i < menuItems.length; i++) {
			BitmapText temp = new BitmapText(FontUtils.eurostile, false);
			temp.setText(menuItemNames[i]);
			temp.setSize(fontSize);
			temp.setAlignment(BitmapFont.Align.Center);
			temp.setDefaultColor(i == 0 ? selectedColor : unselectedColor);
			temp.update();
			
			temp.setLocalTranslation(menuGrid[i].x, menuGrid[i].y, 0);
			
			menuItems[i] = temp;
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
		
		updateRenderState();
	}
	
}
