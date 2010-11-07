package com.googlecode.reaxion.game.audio;

import java.io.File;
import java.util.HashMap;
import java.util.logging.Logger;

/**
 * {@code SoundEffectsManager} contains a {@code HashMap} of sound effect file names. 
 * The class provides centralization for all sound effects, since sound effects can be shared between characters.
 * 
 * @author Brian
 *
 */
public class SoundEffectManager {

	protected static final Logger logger = Logger.getLogger(SoundEffectManager.class.getName());

	private static HashMap<Integer, String> sfx;

	/**
	 * Initializes sound effect {@code HashMap}.
	 */
	public static void initialize() {
		sfx = new HashMap<Integer, String> ();

		File sfxDirectory = new File("src/com/googlecode/reaxion/resources/audio/sfx/");
		String[] soundEffects = sfxDirectory.list();
		
		if(soundEffects == null)
			logger.info("Sound effects could not be loaded.");
		else {
			int count = 0;
			for(int i = 0; i < soundEffects.length; i++) {
				if(soundEffects[i].contains(".ogg") || soundEffects[i].contains(".wav")) {
					sfx.put(i, soundEffects[i]);
					count++;
				}
			}

			logger.info("Successfully loaded " + count + " sound effects.");
		}
	}

	/**
	 * Returns a sound effect file name given its index.
	 * 
	 * @param index
	 * @return
	 */
	public static String getSfx(int index) {
		return sfx.get(index);
	}

}
