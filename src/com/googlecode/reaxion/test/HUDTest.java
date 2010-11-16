package com.googlecode.reaxion.test;

import java.nio.FloatBuffer;

import com.jme.app.SimpleGame;
import com.jme.bounding.BoundingBox;
import com.jme.image.Texture;
import com.jme.math.Quaternion;
import com.jme.math.Vector3f;
import com.jme.renderer.ColorRGBA;
import com.jme.renderer.Renderer;
import com.jme.scene.Node;
import com.jme.scene.Spatial;
import com.jme.scene.TexCoords;
import com.jme.scene.shape.Cylinder;
import com.jme.scene.shape.Quad;
import com.jme.scene.state.BlendState;
import com.jme.scene.state.MaterialState;
import com.jme.scene.state.TextureState;
import com.jme.util.TextureManager; 
import com.jme.util.geom.BufferUtils;

public class HUDTest extends SimpleGame {
	
	private Quaternion rotQuat = new Quaternion();
    private Vector3f axis = new Vector3f(1, 1, 0);
    private Cylinder cylinder;
    private float angle = 0;

    private Node hudNode;

    public static void main(String[] args) {
        HUDTest app = new HUDTest();
        app.setConfigShowMode(SimpleGame.ConfigShowMode.AlwaysShow);
        app.start();
    }
    @Override
	protected void simpleInitGame() {
        display.setTitle("HUD Tutorial");
        
        /* create a rotating cylinder so we have something in the background */
        cylinder = new Cylinder("Cylinder", 6, 18, 5, 10);
        cylinder.setModelBound(new BoundingBox());
        cylinder.updateModelBound();

        MaterialState ms = display.getRenderer().createMaterialState();
        ms.setAmbient(new ColorRGBA(1f, 0f, 0f, 1f));
        ms.setDiffuse(new ColorRGBA(1f, 0f, 0f, 1f));

        /* has been depricated */
        //ms.setAlpha(1f);

        ms.setEnabled(true);
        cylinder.setRenderState(ms);
        cylinder.updateRenderState();

        rootNode.attachChild(cylinder);


        hudNode = new Node("hudNode");
        hudNode.setRenderQueueMode(Renderer.QUEUE_ORTHO);        
        hudNode.setLightCombineMode(Spatial.LightCombineMode.Off);
        
        // to handle texture transparency:
        // create a blend state
        final BlendState bs = display.getRenderer().createBlendState();
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
        hudNode.setRenderState(bs);
        
        Quad hudQuad = getImage("../resources/gui/gauge.png");
        hudQuad.setLocalTranslation(new Vector3f(display.getWidth()/2,display.getHeight()/2,0));
        
        Quad hudQuad2 = getImage("../resources/gui/attack.png");
        hudQuad2.setLocalTranslation(new Vector3f(display.getWidth()/2+0,display.getHeight()/2+0,0));
        
        hudNode.attachChild(hudQuad);
        hudNode.attachChild(hudQuad2);
        rootNode.attachChild(hudNode);

    }

    @Override
	protected void simpleUpdate() {
    	/* recalculate rotation for the cylinder */
        if (timer.getTimePerFrame() < 1) {
            angle = angle + timer.getTimePerFrame();
        }

        rotQuat.fromAngleAxis(angle, axis);
        cylinder.setLocalRotation(rotQuat);
    }
    
    protected Quad getImage(String str) {
        // create the texture state to handle the texture
        final TextureState ts = display.getRenderer().createTextureState();
        // load the image bs a texture (the image should be placed in the same directory bs this class)
        final Texture texture = TextureManager.loadTexture(
                getClass().getResource(str),
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
    
    private float getUForPixel(int xPixel, int textureWidth) {
        return (float) xPixel / textureWidth;
    }

    private float getVForPixel(int yPixel, int textureHeight) {
        return 1f - (float) yPixel / textureHeight;
    }
	
}
