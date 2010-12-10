package com.googlecode.reaxion.game.overlay;

import com.googlecode.reaxion.game.util.FontUtils;
import com.jme.math.Vector3f;
import com.jme.renderer.ColorRGBA;
import com.jme.scene.Node;
import com.jme.scene.shape.Quad;
import com.jme.system.DisplaySystem;
import com.jmex.angelfont.BitmapFont;
import com.jmex.angelfont.BitmapText;
import com.jmex.game.state.GameState;

/**
 * Displays continuation options for a battle in {@code ResultsGameState}.
 * @author Khoa Ha
 *
 */
public class GameOverOverlay extends ScreenshotOverlay {
	
	private static final String baseURL = "../../resources/gui/";
	
	private final ColorRGBA selectedText = FontUtils.blueSelected;
	private final ColorRGBA unselectedText = FontUtils.unselected;
	
	private final int[] point = {80, 132, 138, 182, 188, 200};
	private int frame = 0;
	
	private Node container;
	
	private Quad bg;
	
	private Quad topFade;
	private Quad botFade;
	private Quad topBar;
	private Quad botBar;
	private Quad exitBar;
	
	private BitmapText gameOverText;
	private BitmapText retryText;
	private BitmapText exitText;
	
	public GameOverOverlay() {
		super();
        
        // create a container Node for scaling
        container = new Node("container");
        
        // create gameOver text
        gameOverText = new BitmapText(FontUtils.neuropol, false);
        gameOverText.setAlignment(BitmapFont.Align.Center);
        gameOverText.setSize(40);
        gameOverText.setDefaultColor(new ColorRGBA(1, 1, 1, 1));
        gameOverText.setLocalTranslation(new Vector3f(400, 328, 0));
        gameOverText.setText("G A M E   O V E R");
		gameOverText.update();
		
		// create retry text
        retryText = new BitmapText(FontUtils.neuropol, false);
        retryText.setAlignment(BitmapFont.Align.Center);
        retryText.setSize(20);
        retryText.setDefaultColor(selectedText);
        retryText.setLocalTranslation(new Vector3f(400 - 84, 224, 0));
        retryText.setText("Retry");
        retryText.update();
        
        // create exit text
        exitText = new BitmapText(FontUtils.neuropol, false);
        exitText.setAlignment(BitmapFont.Align.Center);
        exitText.setSize(20);
        exitText.setDefaultColor(unselectedText);
        exitText.setLocalTranslation(new Vector3f(400 + 84, 224, 0));
        exitText.setText("Exit");
        exitText.update();
        
        // create a bg container
        bg = getScreenshot();
        bg.setLocalTranslation(new Vector3f(width/2, height/2, 0));
        
        // create topFade
        topFade = getImage(baseURL+"top_fade2.png");
        topFade.setLocalTranslation(new Vector3f(400, 600 + 59, 0));
        
        // create topBar
        topBar = getImage(baseURL+"sharp_bar_top.png");
        topBar.setLocalTranslation(new Vector3f(0 - 366, 310 + 26, 0));
        
        // create botBar
        botBar = getImage(baseURL+"sharp_bar_bot.png");
        botBar.setLocalTranslation(new Vector3f(800 + 366, 310 - 26, 0));
        
        // create exitBar
        exitBar = getImage(baseURL+"choice_bar.png");
        exitBar.setLocalTranslation(new Vector3f(400, 216, 0));
        
        // create botFade
        botFade = getImage(baseURL+"bot_fade2.png");
        botFade.setLocalTranslation(new Vector3f(400, 0 - 59, 0));
        
        // attach children
        attachChild(bg);
        attachChild(container);
        container.attachChild(topFade);
        container.attachChild(topBar);
        container.attachChild(botBar);
        container.attachChild(botFade);
        
        container.setLocalScale((float) DisplaySystem.getDisplaySystem().getHeight()/600);
    }
	
	/**
	 * Function to be called during each update by the GameState.
	 */
	public void update(GameState b) {
		if (frame <= point[0]) {
			topFade.setLocalTranslation(new Vector3f(400, 600 + 59 - 118*(float)frame/point[0], 0));
			botFade.setLocalTranslation(new Vector3f(400, 0 - 59 + 118*(float)frame/point[0], 0));
		} else if (frame <= point[1]) {
			topBar.setLocalTranslation(new Vector3f(0 - 366 + 732*(float)(frame-point[0])/(point[1]-point[0]), 310 + 26, 0));
			botBar.setLocalTranslation(new Vector3f(800 + 366 - 732*(float)(frame-point[0])/(point[1]-point[0]), 310 - 26, 0));
		} else if (frame == point[2]) {
			container.attachChild(exitBar);
			exitBar.setLocalScale(new Vector3f(1, (float)(frame-point[2] + 1)/(point[3]-point[2] + 1), 1));
			container.updateRenderState();
		} else if (frame <= point[3]) {
			exitBar.setLocalScale(new Vector3f(1, (float)(frame-point[2] + 1)/(point[3]-point[2] + 1), 1));
		} else if (frame == point[4]) {
			container.attachChild(gameOverText);
		} else if (frame == point[5]) {
			container.attachChild(retryText);
			container.attachChild(exitText);
		}
		
		frame++;
	}
	
	public void toggleChoice(boolean retry) {
		retryText.setDefaultColor(retry ? selectedText:unselectedText);
		retryText.update();

		exitText.setDefaultColor(retry ? unselectedText:selectedText);
		exitText.update();

		container.updateRenderState();
	}
	
}