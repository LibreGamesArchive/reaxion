package com.googlecode.reaxion.game.audio;

import java.util.ArrayList;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.jmex.audio.AudioSystem;
import com.jmex.audio.AudioTrack;
import com.jmex.audio.MusicTrackQueue;
import com.jmex.audio.MusicTrackQueue.RepeatType;

/**
 * Utility used to play and loop background music tracks with or without intros.
 * 
 * @author Khoa Ha
 * 
 */
public class BgmPlayer {

	private static String baseURL = "com/googlecode/reaxion/resources/audio/tracks/";

	private static String filename;
	private static String ext;
	private static MusicTrackQueue queue;
	private static AudioTrack intro;
	private static AudioTrack track;
	private static boolean hasBGM = false;

	/**
	 * Prepares the queue to load sounds.
	 */
	public static void prepare() {
		queue = AudioSystem.getSystem().getMusicQueue();
		queue.setCrossfadeinTime(0);
		queue.setCrossfadeoutTime(0);
		queue.setRepeatType(RepeatType.ALL);
		queue.addCurrentSongChangeListener(new SongChangeListener());
	}

	/**
	 * Plays and loops a background music track. If a file with "-intro" exists,
	 * it will be loaded first and play on the first playback.
	 * 
	 * @param f
	 *            Filename of track and track-intro file
	 */
	public static void play(String f) {
		hasBGM = true;
		stopAndReset();

		filename = f.substring(0, f.length() - 4);
		ext = f.substring(f.length() - 4);

		intro = AudioSystem.getSystem().createAudioTrack(
				BgmPlayer.class.getClassLoader().getResource(
						baseURL + filename + "-intro" + ext), false);
		intro.setVolumeChangeRate(1);

		track = AudioSystem.getSystem().createAudioTrack(
				BgmPlayer.class.getClassLoader().getResource(
						baseURL + filename + ext), false);
		track.setVolumeChangeRate(1);
		
		queue.addTrack(intro);
		queue.addTrack(track);

		queue.play();
	}

	/**
	 * Clears all tracks from the queue and frees them from memory.
	 */
	public static void stopAndReset() {
		if (hasBGM) {
			queue.stop();
			ArrayList<AudioTrack> tracks = queue.getTrackList();
			for (int i = 0; i < tracks.size(); i++) {
				queue.getTrack(i).stop();
				queue.getTrack(i).release();
			}
			queue.clearTracks();
			hasBGM = false;
		}
	}

	public static void gamePaused() {
		if (hasBGM)
			queue.getCurrentTrack().setTargetVolume(0.4f);
	}

	public static void gameUnpaused() {
		if (hasBGM)
			queue.getCurrentTrack().setTargetVolume(1);
	}

	private static class SongChangeListener implements ChangeListener {
		public void stateChanged(ChangeEvent e) {
			AudioSystem.getSystem().getMusicQueue().removeTrack(intro);
		}
	}

}