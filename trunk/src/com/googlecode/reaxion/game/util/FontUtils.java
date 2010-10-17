package com.googlecode.reaxion.game.util;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.jmex.angelfont.BitmapFont;
import com.jmex.angelfont.BitmapFontLoader;

public class FontUtils {

	private static final File fontFile = new File(
			"src/com/googlecode/reaxion/resources/fonts/neuropol.fnt");
	private static final File glyphFile = new File(
			"src/com/googlecode/reaxion/resources/fonts/neuropol_0.png");

	public static BitmapFont neuropol;
	
	public static void loadFonts() {
		try {
			neuropol = BitmapFontLoader.load(fontFile.toURI().toURL(), glyphFile
					.toURI().toURL());
		} catch (Exception ex) {
			Logger.getLogger("FontUtils").log(Level.SEVERE,
					"Unable to load font: " + ex);
			System.exit(1);
		}
	}
}
