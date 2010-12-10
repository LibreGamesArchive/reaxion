package com.googlecode.reaxion.game.util;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.jme.renderer.ColorRGBA;
import com.jmex.angelfont.BitmapFont;
import com.jmex.angelfont.BitmapFontLoader;

public class FontUtils {

	// Fonts
	public static BitmapFont neuropol;
	private static final File neuropolFile = new File(
			"src/com/googlecode/reaxion/resources/fonts/neuropol-c.fnt");
	private static final File neuropolGlyph = new File(
			"src/com/googlecode/reaxion/resources/fonts/neuropol-c_0.png");
	
	public static BitmapFont eurostile;
	private static final File eurostileFile = new File(
		"src/com/googlecode/reaxion/resources/fonts/eurostile.fnt");
	private static final File eurostileGlyph = new File(
		"src/com/googlecode/reaxion/resources/fonts/eurostile_0.png");
	
	// Font Colors
	public static ColorRGBA blueSelected = new ColorRGBA(0, .7f, 1, 1);
	public static ColorRGBA greenSelected = ColorRGBA.green;
	public static ColorRGBA unselected = ColorRGBA.white;
	
	public static void loadFonts() {
		try {
			neuropol = BitmapFontLoader.load(neuropolFile.toURI().toURL(), neuropolGlyph
					.toURI().toURL());
			eurostile = BitmapFontLoader.load(eurostileFile.toURI().toURL(), eurostileGlyph
					.toURI().toURL());
		} catch (Exception ex) {
			Logger.getLogger("FontUtils").log(Level.SEVERE,
					"Unable to load font: " + ex);
			System.exit(1);
		}
	}
}
