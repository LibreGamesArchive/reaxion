package com.googlecode.reaxion.game.audio;

import paulscode.sound.SoundSystem;
import paulscode.sound.SoundSystemConfig;
import paulscode.sound.SoundSystemException;
import paulscode.sound.SoundSystemLogger;
import paulscode.sound.codecs.CodecJOrbis;
import paulscode.sound.codecs.CodecWav;
import paulscode.sound.libraries.LibraryJavaSound;

import com.googlecode.reaxion.game.state.StageGameState;
import com.jme.math.Vector3f;

/**
 * Controls all audio of the game.
 * 
 * @author Brian Clanton
 *
 */

public class AudioPlayer {

	private static String baseURL = "com/googlecode/reaxion/resources/audio/";
	private static String trackDir = "tracks/";
	private static String sfxDir = "sfx/";
	private static String loggerHeader = "Audio Player Logger: ";
	
	private static String currentBGM;
	
	private static SoundSystem sound;
	private static SoundSystemLogger logger;
	
	/**
	 * Triggers sound system initialization.
	 */
	public static void prepare() {
		try {
			SoundSystemConfig.addLibrary(LibraryJavaSound.class);
			SoundSystemConfig.setCodec("ogg", CodecJOrbis.class);
			SoundSystemConfig.setCodec("wav", CodecWav.class);
			SoundSystemConfig.setSoundFilesPackage(baseURL);
		} catch (SoundSystemException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		sound = new SoundSystem();
		
		logger = new SoundSystemLogger();
		SoundSystemConfig.setLogger(logger);
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
		
		sound.newStreamingSource(true, currentBGM, trackDir + intro + ext, true, 0, 0, 0, SoundSystemConfig.ATTENUATION_NONE, 0);
		sound.queueSound(currentBGM, trackDir + filename);
		
		logger.message(loggerHeader + "Queued BGM " + filename, 0);
	}
	
	/**
	 * Starts background music.
	 */
	public static void startBGM() {
		sound.play(currentBGM);
		logger.message(loggerHeader + "BGM " + currentBGM + " started.", 0);
	}
	
	/**
	 * Updates the listener position and orientation.
	 * 
	 * @param b
	 */
	public static void update(StageGameState b) {
		Vector3f loc = b.getPlayer().model.getLocalTranslation();
		Vector3f lookAt = b.getPlayer().rotationVector;
		sound.setListenerPosition(loc.x, loc.y, loc.z);
		sound.setListenerOrientation(lookAt.x, lookAt.y, lookAt.z, 0, 1, 0);
	}

	/**
	 * Plays a sound effect at a given position with rolloff attenuation.
	 * 
	 * @param filename
	 * @param x
	 * @param y
	 * @param z
	 */
	public static void playSoundEffect(String filename, float x, float y, float z) {
		sound.quickPlay(true, sfxDir + filename, false, x, y, z, SoundSystemConfig.ATTENUATION_ROLLOFF, SoundSystemConfig.getDefaultRolloff());
		logger.message(loggerHeader + "Quick SFX " + filename + " played.", 0);
	}
	
	/**
	 * Plays a repeating sound effect at a given position with rolloff attenuation.
	 * 
	 * @param filename
	 * @param x
	 * @param y
	 * @param z
	 */
	public static void playRepeatingSoundEffect(String filename, float x, float y, float z) {
		sound.newStreamingSource(true, filename, sfxDir + filename, true, x, y, z, SoundSystemConfig.ATTENUATION_ROLLOFF, SoundSystemConfig.getDefaultRolloff());
		sound.play(filename);
		logger.message(loggerHeader + "Repeating SFX " + filename + " played.", 0);
	}
	
	/**
	 * Stops a repeating sound effect.
	 * 
	 * @param filename
	 */
	public static void stopRepeatingSoundEffect(String filename) {
		sound.stop(filename);
		logger.message(loggerHeader + "Repeating SFX " + filename + " stopped.", 0);
	}
	
	/**
	 * Lowers master volume due to game pause.
	 */
	public static void gamePaused() {
		sound.setMasterVolume(.5f);
		logger.message(loggerHeader + "Master Volume lowered.", 0);
	}
	
	/**
	 * Returns master volume to normal due to game unpause.
	 */
	public static void gameUnpaused() {
		sound.setMasterVolume(1f);
		logger.message(loggerHeader + "Master Volume returned to normal.", 0);
	}
	
	/**
	 * Stops background music.
	 */
	public static void clearBGM() {
		sound.stop(currentBGM);
		logger.message(loggerHeader + "BGM " + currentBGM + " cleared.", 0);
	}
	
	/**
	 * Calls cleanup method of SoundSystem.
	 */
	public static void cleanup() {
		sound.cleanup();
	}
	
}
