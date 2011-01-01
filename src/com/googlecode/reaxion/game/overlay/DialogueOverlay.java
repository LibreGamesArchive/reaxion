package com.googlecode.reaxion.game.overlay;

import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.googlecode.reaxion.game.attack.Attack;
import com.googlecode.reaxion.game.model.character.Character;
import com.googlecode.reaxion.game.state.StageGameState;
import com.googlecode.reaxion.game.util.FontUtils;
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
 * Handles text output and image movement during text cutscenes.
 * @author Khoa Ha
 *
 */
public class DialogueOverlay extends Overlay {
	
	public static final String NAME = "dialogueOverlay";
	
	private static final String baseURL = "com/googlecode/reaxion/resources/gui/";
	private static final String resourceURL = "com/googlecode/reaxion/resources/cutscene/";

	public Quad box;
	public Quad pointer;
	
	public BitmapText name;
	public BitmapText text;
	public BitmapText testText;
	
	public Node[] actors;
	
	private Node actorsContainer;
	private Node bgContainer;
	
	public DialogueOverlay() {
		super(NAME);
        
        // create test text
        testText = new BitmapText(FontUtils.eurostile, false);
        testText.setSize(24);
		
        // create a container Node for scaling
        container = new Node("container");
        attachChild(container);
        
        // create bg container
        bgContainer = new Node("bgContainer");
        bgContainer.setLocalTranslation(new Vector3f(400, 300, 0));
        
        // create actors container
        actorsContainer = new Node("actorsContainer");
        
        // create name
        name = new BitmapText(FontUtils.neuropol, false);
        name.setSize(24);
        name.setDefaultColor(new ColorRGBA(1, 1, 1, 1));
        name.setLocalTranslation(new Vector3f(20, 172, 0));
        
        // create text box
        text = new BitmapText(FontUtils.eurostile, false);
        text.setSize(24);
        text.setDefaultColor(new ColorRGBA(1, 1, 1, 1));
        text.setLocalTranslation(new Vector3f(20, 136, 0));
        
        // create text field
        box = getImage(baseURL+"textbox.png");
        box.setLocalTranslation(new Vector3f(400, 88, 0));
        
        // create pointer
        pointer = getImage(baseURL+"arrow.png");
        pointer.setLocalTranslation(new Vector3f(-6, -9, 0));        
        
        // attach children
        attachChild(container);
        container.attachChild(bgContainer);
        container.attachChild(actorsContainer);
        container.attachChild(box);
        container.attachChild(pointer);
        container.attachChild(name);
        container.attachChild(text);
        
        container.setLocalScale((float) DisplaySystem.getDisplaySystem().getHeight()/600);
    }
	
	/**
	 * Prepares {@code num} number of nodes for actors.
	 * @param num
	 */
	public void setupActors(int num) {
		actors = new Node[num];
		for (int i=0; i<actors.length; i++) {
			actors[i] = new Node("actor"+i);
			actorsContainer.attachChild(actors[i]);
		}
		actorsContainer.updateRenderState();
	}
	
	/**
	 * Sets the {@code Actor} at index {@code ind} to portrait {@code img}.
	 * @param ind
	 * @param img
	 */
	public void loadPortrait(int ind, String img) {
		actors[ind].detachAllChildren();
		actors[ind].attachChild(getImage(resourceURL+img));
		actors[ind].updateRenderState();
	}
	
	/**
	 * Moves the {@code Actor} at index {@code ind} to point {@code pt}.
	 * @param ind
	 * @param pt
	 */
	public void movePortrait(int ind, Point pt) {
		actors[ind].setLocalTranslation(pt.x, pt.y, 0);
	}
	/**
	 * Returns the position of the {@code Actor} at {@code ind}.
	 * @param ind
	 * @return
	 */
	public Point getPoint(int ind) {
		Point pt = new Point();
		pt.x = (int)actors[ind].getLocalTranslation().x;
		pt.y = (int)actors[ind].getLocalTranslation().y;
		return pt;
	}
	
	/**
	 * Show or hide the pointer based on the parameter.
	 * @param show
	 */
	public void showArrow(boolean show) {
		if (show)
			pointer.setLocalTranslation(new Vector3f(204, 160, 0));
		else
			pointer.setLocalTranslation(new Vector3f(-6, -9, 0));
	}
	
	/**
	 * Sets the background image to the parameter.
	 * @param img
	 */
	public void setBg(String img) {
		bgContainer.detachAllChildren();
		bgContainer.attachChild(getImage(resourceURL+img));
		bgContainer.updateRenderState();
	}
	
	/**
	 * Sets the name label to the parameter.
	 */
	public void setName(String str) {
		if (str == null)
			str = "";
		name.setText(str);
		name.update();
	}
	
	/**
	 * Sets the text field to the parameter.
	 * @param str
	 */
	public void setText(String str) {
		text.setText(str);
		text.update();
	}
	
	/**
	 * Tests the rendered width of a given string.
	 * @param str
	 * @return width in pixels of text
	 */
	public float testText(String str) {
		testText.setText(str);
		testText.update();
		return testText.getLineWidth();
	}
}
