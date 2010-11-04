package com.googlecode.reaxion.game.audio;

import paulscode.sound.SoundSystem;
import paulscode.sound.SoundSystemConfig;
import paulscode.sound.SoundSystemException;
import paulscode.sound.codecs.CodecJOrbis;
import paulscode.sound.libraries.LibraryJavaSound;

/**
 * Controls all audio of the game.
 * 
 * @author Brian
 *
 */

public class AudioPlayer {

	private static String baseURL = "com/googlecode/reaxion/resources/audio/tracks/";
	private static SoundSystem sound;
	private static String currentBGM;
	
	/**
	 * Triggers sound system initialization.
	 */
	public static void prepare() {
		try {
			SoundSystemConfig.addLibrary(LibraryJavaSound.class);
			SoundSystemConfig.setCodec("ogg", CodecJOrbis.class);
			SoundSystemConfig.setSoundFilesPackage(baseURL);
		} catch (SoundSystemException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		sound = new SoundSystem();
	}
	
	/**
	 * Places background music in the audio queue.
	 * 
	 * @param filename
	 */
	public static void queueBGM(String filename) {
		String bgmName = filename.substring(0, filename.indexOf("."));
		String ext = filename.substring(filename.indexOf("."));
		String intro = bgmName + "-intro";
		currentBGM = bgmName;
		
		sound.newStreamingSource(true, currentBGM, intro + ext, true, 0, 0, 0, SoundSystemConfig.ATTENUATION_NONE, 0);
		sound.queueSound(currentBGM, filename);	
	}
	
	/**
	 * Starts background music.
	 */
	public static void startBGM() {
		sound.play(currentBGM);
	}
	
	/**
	 * Lowers master volume due to game pause.
	 */
	public static void gamePaused() {
		sound.setMasterVolume(.5f);
	}
	
	/**
	 * Returns master volume to normal due to game unpause.
	 */
	public static void gameUnpaused() {
		sound.setMasterVolume(1f);
	}
	
	/**
	 * Stops background music and removes the background music source from memory.
	 */
	public static void clearBGM() {
		sound.stop(currentBGM);
		sound.removeSource(currentBGM);
	}
	
	/**
	 * Calls cleanup method of SoundSystem.
	 */
	public static void cleanup() {
		sound.cleanup();
	}
	
}
