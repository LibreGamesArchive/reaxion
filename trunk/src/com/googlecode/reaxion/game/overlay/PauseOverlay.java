package com.googlecode.reaxion.game.overlay;

import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.googlecode.reaxion.game.attack.Attack;
import com.googlecode.reaxion.game.model.character.Character;
import com.googlecode.reaxion.game.state.StageGameState;
import com.jme.image.Image;
import com.jme.image.Texture;
import com.jme.math.Vector3f;
import com.jme.renderer.ColorRGBA;
import com.jme.scene.Node;
import com.jme.scene.TexCoords;
import com.jme.scene.shape.Quad;
import com.jme.scene.state.TextureState;
import com.jme.system.DisplaySystem;
import com.jme.util.TextureManager;
import com.jme.util.geom.BufferUtils;
import com.jmex.angelfont.BitmapFont;
import com.jmex.angelfont.BitmapFontLoader;
import com.jmex.angelfont.BitmapText;

/**
 * Facilitates the creation of and maintains in-battle pausing.
 * @author Khoa Ha
 *
 */
public class PauseOverlay extends ScreenshotOverlay {
	
	private static final String baseURL = "../../resources/gui/";
	
	private Quad pauseText;
	
	public PauseOverlay() {
		super();
        
        // create a container Node for scaling
        container = new Node("container");
        attachChild(container);
        
        // create pauseText
        pauseText = getImage(baseURL+"pause.png");
    }
	
	/**
	 * Called when pausing to create the overlays
	 */
	public void pause() {
		freeze();
		pauseText.setLocalTranslation(new Vector3f(width/2, height/2, 0));
		pauseText.setLocalScale((float) DisplaySystem.getDisplaySystem().getHeight()/600);
		container.attachChild(pauseText);
		this.updateRenderState();
	}
	
	/**
	 * Called when unpausing to remove the overlays
	 */
	public void unpause() {
		unfreeze();
		container.detachChild(pauseText);
		this.updateRenderState();
	}	
	
}
