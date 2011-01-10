package com.googlecode.reaxion.game.overlay;

import com.googlecode.reaxion.game.util.FontUtils;
import com.jme.scene.Node;
import com.jme.scene.shape.Quad;
import com.jme.system.DisplaySystem;
import com.jmex.angelfont.BitmapFont.Align;
import com.jmex.angelfont.BitmapText;
import com.jmex.angelfont.Rectangle;

/**
 * Displays full-screen alerts from different game states.
 * @author Khoa Ha
 *
 */
public class PromptOverlay extends Overlay {
	
	public static final String NAME = "alertOverlay";
	
	private static final String baseURL = "com/googlecode/reaxion/resources/gui/";
	
	private final int border = 8;
	
	private Node container;
	
	private Node cover;
	private Quad prompt;
	private BitmapText alertText;
	
	public PromptOverlay() {
		super(NAME);
		
		// set resolution variables
		height = DisplaySystem.getDisplaySystem().getHeight();
		width = DisplaySystem.getDisplaySystem().getWidth();
        
        // create a container Node for scaling
        container = new Node("container");
        
        // create a container for the info
        cover = new Node("cover");
        
        // create box bg
        prompt = getImage(baseURL+"prompt.png");
        prompt.setLocalTranslation(400, 300, 0);
        cover.attachChild(prompt);
        
        // create info text
        alertText = new BitmapText(FontUtils.eurostile, false);
        alertText.setAlignment(Align.Center);
        alertText.setBox(new Rectangle(400 - 192 + border, 300, 384 - border*2, 192));
        cover.attachChild(alertText);
        
        // attach children
        attachChild(container);
        
        container.setLocalScale((float) DisplaySystem.getDisplaySystem().getHeight()/600);
    }
	
	/**
	 * Displays a message
	 * @param str Message to display
	 * @param size Font size
	 */
	public void showMessage(String str, int size) {
		alertText.setText(str);
		alertText.setSize(size);
		alertText.update();
		alertText.setBox(new Rectangle(400 - 192 + border, 300 + alertText.getHeight()/2, 384 - border*2, 192));
		alertText.update();
		container.attachChild(cover);
		updateRenderState();
	}
	
	/**
	 * Concludes the current message
	 */
	public void finishMessage() {
		container.detachChild(cover);
		updateRenderState();			
	}
	
}