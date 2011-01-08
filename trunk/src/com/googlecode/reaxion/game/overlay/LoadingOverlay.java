package com.googlecode.reaxion.game.overlay;

import com.jme.scene.Node;
import com.jme.scene.shape.Quad;
import com.jme.system.DisplaySystem;

/**
 * Displays a loading overlay while the game adds data.
 * @author Khoa Ha
 *
 */
public class LoadingOverlay extends Overlay {
	
	public static final String NAME = "infoOverlay";
	
	private static final String baseURL = "com/googlecode/reaxion/resources/gui/";
	
	private Node container;
	
	private Quad loading;
	
	public LoadingOverlay() {
		super(NAME);
        
        // create a container Node for scaling
        container = new Node("container");
        
        // create loading overlay
        loading = getImage(baseURL+"loading.png");
        loading.setLocalTranslation(400, 300, 0);
        
        // attach children
        attachChild(container);
        
        container.setLocalScale((float) DisplaySystem.getDisplaySystem().getHeight()/600);
        container.setLocalTranslation(offset*container.getLocalScale().x, 0, 0);
    }
	
	/**
	 * Displays or hides the loading overlay
	 * @param show Whether to show or not
	 */
	public void show(boolean show) {
		if (show) {
			container.attachChild(loading);
			updateRenderState();
		} else {
			container.detachChild(loading);
			updateRenderState();
		}
			
	}
	
}