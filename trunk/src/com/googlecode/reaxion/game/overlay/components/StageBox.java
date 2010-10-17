package com.googlecode.reaxion.game.overlay.components;

import com.googlecode.reaxion.game.util.ColorUtils;
import com.googlecode.reaxion.game.util.FontUtils;
import com.jme.renderer.ColorRGBA;
import com.jme.scene.Node;
import com.jme.scene.shape.Quad;
import com.jmex.angelfont.BitmapText;

public class StageBox extends Node {

	private static final int boxWidth = 300, boxHeight = 50;
	private static final int fontSize = 20;
	
	public StageBox(String name) {
		super(name);
		
		Quad box = new Quad(name, boxWidth, boxHeight);
		box.setSolidColor(ColorUtils.darker(ColorRGBA.blue, .2));
		
		Quad fill = new Quad(name, boxWidth - 10, boxHeight - 10);
		fill.setSolidColor(ColorRGBA.blue);
		
		BitmapText stageName = new BitmapText(FontUtils.neuropol, false);
		stageName.setText(name);
		stageName.setSize(fontSize);
	}
	
}
