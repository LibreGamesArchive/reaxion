package com.googlecode.reaxion.game.overlay;

import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;

import com.jme.image.Image;
import com.jme.image.Texture;
import com.jme.math.Vector3f;
import com.jme.scene.TexCoords;
import com.jme.scene.shape.Quad;
import com.jme.scene.state.TextureState;
import com.jme.system.DisplaySystem;
import com.jme.util.TextureManager;
import com.jme.util.geom.BufferUtils;

/**
 * This class should be extended if an overlay involves the use of a screenshot as a
 * background image.
 * 
 * @author Brian Clanton
 *
 */
public abstract class ScreenshotOverlay extends GridOverlay {

	protected Quad screenshot;
	
	public ScreenshotOverlay() {
		super(0, 0, false);
	}
	
	public ScreenshotOverlay(int baseWidth, int baseHeight, boolean mainOverlay) {
		super(baseWidth, baseHeight, mainOverlay);
	}
	
	/**
	 * Called when freezing to create the overlays
	 */
	public void freeze() {
		screenshot = getScreenshot();
		screenshot.setLocalTranslation(new Vector3f(width/2, height/2, 0));
		screenshot.setZOrder(Integer.MAX_VALUE);
		container.attachChild(screenshot);
	}
	
	/**
	 * Called when unfreezing to remove the overlays
	 */
	public void unfreeze() {
		container.detachChild(screenshot);
	}
	
	public Quad getScreenshot() {
		// Create a pointer to the image info and create a buffered image to
        // hold it.
        final ByteBuffer buff = BufferUtils.createByteBuffer(width * height * 3);
        DisplaySystem.getDisplaySystem().getRenderer().grabScreenContents(buff, Image.Format.RGB8, 0, 0, width, height);
        final int w = width;
        final int h = height;
        BufferedImage img = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);

        // Grab each pixel information and set it to the BufferedImage info.
        for (int x = 0; x < w; x++) {
            for (int y = 0; y < h; y++) {
                
                int index = 3 * ((h- y - 1) * w + x);
                //if (index < 0) { System.out.println(); }
                int argb = (((int) (buff.get(index+0)) & 0xFF) << 16) //r
                         | (((int) (buff.get(index+1)) & 0xFF) << 8)  //g
                         | (((int) (buff.get(index+2)) & 0xFF));      //b

                img.setRGB(x, y, argb);
            }
        }
        
        // create the texture state to handle the texture
        final TextureState ts = DisplaySystem.getDisplaySystem().getRenderer().createTextureState();
        // load the image bs a texture (the image should be placed in the same directory bs this class)
        final Texture texture = TextureManager.loadTexture(
                img,
                Texture.MinificationFilter.Trilinear, // of no use for the quad
                Texture.MagnificationFilter.Bilinear, // of no use for the quad
                1.0f,
                true);
        // set the texture for this texture state
        ts.setTexture(texture);
        // activate the texture state
        ts.setEnabled(true);

        Quad hudQuad = new Quad("hud", w, h);
        
        // correct texture application:
        final FloatBuffer texCoords = BufferUtils.createVector2Buffer(4);
        // coordinate lower-left
        texCoords.put(getUForPixel(0, w)).put(getVForPixel(0, h));
        // coordinate upper-left
        texCoords.put(getUForPixel(0, w)).put(getVForPixel(h, h));
        // coordinate upper-right
        texCoords.put(getUForPixel(w, w)).put(getVForPixel(h, h));
        // coordinate lower-right
        texCoords.put(getUForPixel(w, w)).put(getVForPixel(0, h));
        // assign texture coordinates to the quad
        hudQuad.setTextureCoords(new TexCoords(texCoords));
        // apply the texture state to the quad
        hudQuad.setRenderState(ts);
        
        return hudQuad;
	}
	
}
