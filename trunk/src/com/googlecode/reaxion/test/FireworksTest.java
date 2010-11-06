package com.googlecode.reaxion.test;

import java.awt.Font;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.googlecode.reaxion.game.model.prop.Firework;
import com.jme.app.SimpleGame;
import com.jme.bounding.OrientedBoundingBox;
import com.jme.math.Vector3f;
import com.jme.renderer.ColorRGBA;
import com.jmex.font3d.Font3D;
import com.jmex.font3d.Text3D;
import com.jmex.font3d.effects.Font3DGradient;
 
public class FireworksTest extends SimpleGame {
    /** 3DText font and text */
    private Font3D myfont;
    private Text3D text;
    private float textwidth;
    
    /**
     * Set up all the necessary stuff.
     * Init the Explosionfactory, create the 3DText.
     */
    @Override
    protected void simpleInitGame() {
        display.setTitle("Happy New Year");
        // create a 3DFont from a system font (Arial) */
        myfont = new Font3D(new Font("Arial", Font.PLAIN, 2), 0.1, true, true,true);
        // color the Text
        Font3DGradient gradient = new Font3DGradient(Vector3f.UNIT_Y, ColorRGBA.white, ColorRGBA.red);
        gradient.applyEffect(myfont);
        text = myfont.createText("Fireworks Test!", 2, 0);
        // scale Z-Axis of the font down to make it slimmer 
        text.setLocalScale(new Vector3f(1, 1, 0.1f));
        text.updateWorldBound();
        // get the width of our 3DText, we need it later to spawn the Firework
        OrientedBoundingBox bb = (OrientedBoundingBox) text.getWorldBound();
        textwidth = bb.extent.x;
        rootNode.attachChild(text);
        Firework firework = new Firework();
        rootNode.attachChild(firework);
 
        // move the cam a bit to the left and backwards
        cam.setLocation(new Vector3f(5, 0, 15));
    }
 
 
    /**
     * The Entry point.
     * Creates and starts the game.
     */
    public static void main(String[] args) {
        // we only want to see important messages
        Logger.getLogger("com.jme").setLevel(Level.SEVERE);
        Logger.getLogger("com.jmex").setLevel(Level.SEVERE);
        
        FireworksTest happy = new FireworksTest();
        happy.setConfigShowMode(ConfigShowMode.AlwaysShow);
        happy.start();
    }
}
