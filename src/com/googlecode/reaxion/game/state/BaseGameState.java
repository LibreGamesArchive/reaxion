package com.googlecode.reaxion.game.state;

import com.googlecode.reaxion.game.audio.AudioPlayer;
import com.googlecode.reaxion.game.audio.BackgroundMusic;
import com.googlecode.reaxion.game.overlay.InfoOverlay;
import com.googlecode.reaxion.game.overlay.LoadingOverlay;
import com.googlecode.reaxion.game.overlay.PromptOverlay;
import com.jmex.angelfont.BitmapFont;
import com.jmex.game.state.CameraGameState;

/**
 * The base {@code GameState} class for most of Reaxion. Integrates the addition and removal of key bindings
 * and the beginning and ending of background music with the {@code setActive} method. Also contains methods
 * used in most of the Reaxion {@code GameState} classes.
 * 
 * @author Brian Clanton
 *
 */
public abstract class BaseGameState extends CameraGameState {

	private static final String NAME = "baseGameState";
	
	protected boolean startsBGM;
	protected boolean endsBGM;
	
	protected BackgroundMusic bgm;
	
	protected LoadingOverlay loading;
	protected PromptOverlay prompt;
	protected InfoOverlay info;
	
	public BaseGameState(boolean preinitialize) {
		super(NAME);
		
		if (preinitialize)
			init();
		
		// create global overlays
		loading = new LoadingOverlay();
		prompt = new PromptOverlay();
		info = new InfoOverlay();
		rootNode.attachChild(loading);
		rootNode.attachChild(prompt);
		rootNode.attachChild(info);
		rootNode.updateRenderState();
	}

	/**
	 * Initializes the {@code BaseGameState}.
	 */
	protected abstract void init();
	
	protected void showMusicAlert() {
		String title = bgm.getTitle();
		String album = bgm.getAlbum();
		info.alert(title + "\n" + album, BitmapFont.Align.Center, 100, 1);
	}
	
	@Override
	public void setActive(boolean active) {
		super.setActive(active);
		
		if (active) {
			if (startsBGM) {
				AudioPlayer.queueBGM(bgm);
				AudioPlayer.startBGM();
				if (this instanceof StageGameState)
					showMusicAlert();
			}
			
			rootNode.updateRenderState();
		} else if (!active && endsBGM)
			AudioPlayer.clearBGM();
	}
	
	@Override
	public void stateUpdate(float _tpf) {
		info.update(this);
	}
	
    public boolean isStartsBGM() {
		return startsBGM;
	}

	public void setStartsBGM(boolean startsBGM) {
		this.startsBGM = startsBGM;
	}

	public boolean isEndsBGM() {
		return endsBGM;
	}

	public void setEndsBGM(boolean endsBGM) {
		this.endsBGM = endsBGM;
	}

	public BackgroundMusic getBgm() {
		return bgm;
	}

	public void setBgm(BackgroundMusic bgm) {
		this.bgm = bgm;
	}
	
}
