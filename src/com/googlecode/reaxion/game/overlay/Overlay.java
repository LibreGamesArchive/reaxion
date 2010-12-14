package com.googlecode.reaxion.game.overlay;

import java.nio.FloatBuffer;

import com.jme.image.Texture;
import com.jme.renderer.ColorRGBA;
import com.jme.renderer.Renderer;
import com.jme.scene.Node;
import com.jme.scene.Spatial;
import com.jme.scene.TexCoords;
import com.jme.scene.shape.Quad;
import com.jme.scene.state.BlendState;
import com.jme.scene.state.TextureState;
import com.jme.system.DisplaySystem;
import com.jme.util.TextureManager;
import com.jme.util.geom.BufferUtils;

/**
 * Provides orthogonal rendering capabilities and simple functions that mirror
 * the drawing functions of Java2D. All classes that draw to the screen above the
 * 3d rendering should extend this class.
 * @author Khoa Ha
 *
 */
public class Overlay extends Node {

	protected int width;
	protected int height;
	
	protected Node container;
	
	public Overlay(String name) {
		super(name);
		
		width = DisplaySystem.getDisplaySystem().getWidth();
		height = DisplaySystem.getDisplaySystem().getHeight();
		
        this.setRenderQueueMode(Renderer.QUEUE_ORTHO);        
        this.setLightCombineMode(Spatial.LightCombineMode.Off);
        
        // to handle texture transparency:
        // create a blend state
        final BlendState bs = DisplaySystem.getDisplaySystem().getRenderer().createBlendState();
        // activate blending
        bs.setBlendEnabled(true);
        // set the source function
        bs.setSourceFunctionAlpha(BlendState.SourceFunction.OneMinusDestinationAlpha);
        // set the destination function
        bs.setDestinationFunctionAlpha(BlendState.DestinationFunction.DestinationAlpha);
        bs.setTestEnabled(false);
        // activate the blend state
        bs.setEnabled(true);
        // assign the blender state to the quad
        this.setRenderState(bs);

    }
    
	/**
	 * Convenience method to mirror the Java2D {@code getImage()} function.
	 * Returns a scaled {@code Quad} textured with the file provided by {@code str}.
	 * @param str Filepath to image
	 * @return {@code Quad} containing image
	 */
    protected Quad getImage(String str) {
        // create the texture state to handle the texture
        final TextureState ts = DisplaySystem.getDisplaySystem().getRenderer().createTextureState();
        // load the image bs a texture (the image should be placed in the same directory bs this class)
        final Texture texture = TextureManager.loadTexture(
                Overlay.class.getResource(str),
                Texture.MinificationFilter.Trilinear, // of no use for the quad
                Texture.MagnificationFilter.Bilinear, // of no use for the quad
                1.0f,
                true);
        // set the texture for this texture state
        ts.setTexture(texture);
        // initialize texture width
        int textureWidth = ts.getTexture().getImage().getWidth();
        // initialize texture height
        int textureHeight = ts.getTexture().getImage().getHeight();
        // activate the texture state
        ts.setEnabled(true); 

        Quad hudQuad = new Quad("hud", textureWidth, textureHeight);
        
        // correct texture application:
        final FloatBuffer texCoords = BufferUtils.createVector2Buffer(4);
        // coordinate lower-left
        texCoords.put(getUForPixel(0, textureWidth)).put(getVForPixel(0, textureHeight));
        // coordinate upper-left
        texCoords.put(getUForPixel(0, textureWidth)).put(getVForPixel(textureHeight, textureHeight));
        // coordinate upper-right
        texCoords.put(getUForPixel(textureWidth, textureWidth)).put(getVForPixel(textureHeight, textureHeight));
        // coordinate lower-right
        texCoords.put(getUForPixel(textureWidth, textureWidth)).put(getVForPixel(0, textureHeight));
        // assign texture coordinates to the quad
        hudQuad.setTextureCoords(new TexCoords(texCoords));
        // apply the texture state to the quad
        hudQuad.setRenderState(ts);
        
        return hudQuad;
    }
    
    /**
	 * Convenience method to mirror the Java2D {@code drawRect()} function.
	 * Returns a {@code Quad} scaled to {@code rectWidth} and {@code rectHeight}
	 * with provided {@code color}.
	 * @param rectWidth Width of rectangle
	 * @param rectHeight Height of rectangle
	 * @param color Color of rectangle
	 * @return {@code Quad} containing image
	 */
    protected Quad drawRect(int rectWidth, int rectHeight, ColorRGBA color) {

        Quad hudQuad = new Quad("hud", rectWidth, rectHeight);
        
        hudQuad.setSolidColor(color);
        
        return hudQuad;
    }
    
    protected float getUForPixel(int xPixel, int textureWidth) {
        return (float) xPixel / textureWidth;
    }

    protected float getVForPixel(int yPixel, int textureHeight) {
        return 1f - (float) yPixel / textureHeight;
    }
	
}
