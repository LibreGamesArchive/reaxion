package com.googlecode.reaxion.game.state;

import com.googlecode.reaxion.game.audio.AudioPlayer;
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
	
	protected String bgm;
	
	public BaseGameState(boolean preinitialize) {
		super(NAME);
		
		if (preinitialize)
			init();
	}

	/**
	 * Initializes the {@code BaseGameState}.
	 */
	protected abstract void init();
	/**
	 * Initializes key bindings.
	 */
	protected abstract void initKeyBindings();
	/**
	 * Removes all key bindings initialized by the current {@code BaseGameState}.
	 */
	protected abstract void removeKeyBindings();
	
	@Override
	public void setActive(boolean active) {
		super.setActive(active);
		
		if (active) {
			initKeyBindings();
			
			if (startsBGM) {
				AudioPlayer.queueBGM(bgm);
				AudioPlayer.startBGM();
			}
			
			rootNode.updateRenderState();
		} else {
			removeKeyBindings();
			
			if (endsBGM)
				AudioPlayer.clearBGM();
		}
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

	public String getBgm() {
		return bgm;
	}

	public void setBgm(String bgm) {
		this.bgm = bgm;
	}
	
}
