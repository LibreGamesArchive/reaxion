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
	private static final String neuropolFile = "com/googlecode/reaxion/resources/fonts/neuropol-c.fnt";
	private static final String neuropolGlyph = "com/googlecode/reaxion/resources/fonts/neuropol-c_0.png";
	
	public static BitmapFont eurostile;
	private static final String eurostileFile = "com/googlecode/reaxion/resources/fonts/eurostile.fnt";
	private static final String eurostileGlyph = "com/googlecode/reaxion/resources/fonts/eurostile_0.png";
	
	// Font Colors
	public static ColorRGBA blueSelected = new ColorRGBA(0, .7f, 1, 1);
	public static ColorRGBA greenSelected = ColorRGBA.green;
	public static ColorRGBA unselected = ColorRGBA.white;
	
	public static void loadFonts() {
		try {
			neuropol = BitmapFontLoader.load(ClassLoader.getSystemClassLoader().getResource(neuropolFile),
					ClassLoader.getSystemClassLoader().getResource(neuropolGlyph));
			eurostile = BitmapFontLoader.load(ClassLoader.getSystemClassLoader().getResource(eurostileFile),
					ClassLoader.getSystemClassLoader().getResource(eurostileGlyph));
		} catch (Exception ex) {
			Logger.getLogger("FontUtils").log(Level.SEVERE,
					"Unable to load font: " + ex);
			System.exit(1);
		}
	}
}
