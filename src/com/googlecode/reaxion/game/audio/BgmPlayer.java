package com.googlecode.reaxion.game.audio;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.jmex.audio.AudioSystem;
import com.jmex.audio.AudioTrack;
import com.jmex.audio.MusicTrackQueue;
import com.jmex.audio.MusicTrackQueue.RepeatType;

/**
 * Utility used to play and loop background music tracks with or without intros.
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
	 * @param f Filename of track and track-intro file
	 */
	public static void play(String f) {
		queue.clearTracks();
		
		filename = f.substring(0, f.length()-4);
		ext = f.substring(f.length()-4);
		
		intro = AudioSystem.getSystem().createAudioTrack(
				BgmPlayer.class.getClassLoader().getResource(
						baseURL+filename+"-intro"+ext), false);

		track = AudioSystem.getSystem().createAudioTrack(
				BgmPlayer.class.getClassLoader().getResource(
						baseURL+filename+ext), false);

		queue.addTrack(intro);
		queue.addTrack(track);

		queue.play();
	}
	
	public static void stopAndReset() {
		queue.stop();
		queue.clearTracks();
	}
	
	private static class SongChangeListener implements ChangeListener {
	    public void stateChanged(ChangeEvent e) {
	    	AudioSystem.getSystem().getMusicQueue().removeTrack(intro); 
	    }
	}

}