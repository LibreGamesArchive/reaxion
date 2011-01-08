package com.googlecode.reaxion.game.overlay;

import com.googlecode.reaxion.game.util.FontUtils;
import com.jme.math.Vector3f;
import com.jme.scene.Node;
import com.jme.scene.shape.Quad;
import com.jme.system.DisplaySystem;
import com.jmex.angelfont.BitmapFont.Align;
import com.jmex.angelfont.BitmapFont;
import com.jmex.angelfont.BitmapText;
import com.jmex.angelfont.Rectangle;
import com.jmex.game.state.GameState;

/**
 * Displays alerts from different game states.
 * @author Khoa Ha
 *
 */
public class InfoOverlay extends Overlay {
	
	public static final String NAME = "infoOverlay";
	
	private static final String baseURL = "com/googlecode/reaxion/resources/gui/";
	
	private final int slideTime = 8;
	private int duration = 0;
	private int messagePriority = 0;
	
	private Node container;
	
	private Node box;
	private Quad info;
	private BitmapText infoText;
	
	private int height;
	private int width;
	
	private int frame = 0;
	
	public InfoOverlay() {
		super(NAME);
		
		// set resolution variables
		height = DisplaySystem.getDisplaySystem().getHeight();
		width = DisplaySystem.getDisplaySystem().getWidth();
        
        // create a container Node for scaling
        container = new Node("container");
        
        // create a container for the info
        box = new Node("box");
        
        // create box bg
        info = getImage(baseURL+"info.png");
        info.setLocalTranslation(new Vector3f(800 - 100, 600 - 28, 0));
        box.attachChild(info);
        
        // create info text
        infoText = new BitmapText(FontUtils.eurostile, false);
        infoText.setSize(16);
        infoText.setAlignment(Align.Right);
        infoText.setBox(new Rectangle(610, 590, 180, 36));
        box.attachChild(infoText);
        
        // hide box
        box.setLocalTranslation(200 + offset*2, 0, 0);
        
        // attach children
        attachChild(container);
        container.attachChild(box);
        
        container.setLocalScale((float) height/600);
    }
	
	/**
	 * Function to be called during each update by the GameState.
	 */
	public void update(GameState b) {
		if (duration > 0) {
			if (frame > duration) {
				duration = 0;
				messagePriority = 0;
				frame = 0;
			} else if (frame <= slideTime)
				box.setLocalTranslation(200*(1 - (float)1/slideTime*frame) + offset*2, 0, 0);
			else if (frame > duration - slideTime)
				box.setLocalTranslation((float)200/slideTime*(frame - duration + slideTime) + offset*2, 0, 0);
			frame++;
		}
	}
	
	/**
	 * Displays an info message if available.
	 * @param message String to be displayed
	 * @param duration Duration of the message
	 * @param priority The higher the priority, the more likely to display
	 * 
	 * @return If message set up successfully
	 */
	public boolean alert(String message, BitmapFont.Align alignment, int duration, int priority) {
		if (this.duration == 0  || priority >= messagePriority) {
			
			this.duration = duration + slideTime*2;
			messagePriority = priority;

			infoText.setText(message);
			infoText.setAlignment(alignment);
			infoText.update();
			container.updateRenderState();
			
			return true;
			
		} else {
			return false;
		}
	}
	
}