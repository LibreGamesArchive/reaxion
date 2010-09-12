package com.googlecode.reaxion.game.overlay;

import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.googlecode.reaxion.game.attack.Attack;
import com.googlecode.reaxion.game.model.character.Character;
import com.googlecode.reaxion.game.state.BattleGameState;
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


public class CharSelectOverlay extends Overlay {
	
	private static final String baseURL = "../../resources/gui/";
	
	private static final File fontFile = new File("src/com/googlecode/reaxion/resources/fonts/neuropol.fnt");
    private static final File glyphFile = new File("src/com/googlecode/reaxion/resources/fonts/neuropol_0.png");
    

	private BitmapFont font = null;
    
	private Class[] attacks;
	
	private Node container;
	private Node bg;
	private Quad screenshot;
	
	private String[]charNames;
	private Quad[]p1Fill;
	private Quad[]p2Fill;
	private Quad[]opFill;
	private BitmapText[]p1Display;
	private BitmapText[]p2Display;
	private BitmapText[]opDisplay;
	private BitmapText Menu;
	
	private ColorRGBA textColor;
	private ColorRGBA boxColor;
	private ColorRGBA selTextColor;
	private ColorRGBA selBoxColor;
	
	private int[]CurrentIndex = new int[2];
	private int[]selectedChars = new int[3];
	
	public CharSelectOverlay() {
		super();
		
		// try to load the bitmap font
		try {
            font = BitmapFontLoader.load(fontFile.toURI().toURL(), glyphFile.toURI().toURL());
        } catch(Exception ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "Unable to load font: " + ex);
            System.exit(1);
        }
        
        // create a container Node for scaling
        container = new Node("container");
        attachChild(container);
       
        
        // White
        textColor = new ColorRGBA(1, 1, 1, 1);
        // Dark Gray
        boxColor = new ColorRGBA(.25f, .25f, .25f, 1);
        selTextColor = new ColorRGBA(0, 1, 0, 1);
        selBoxColor = new ColorRGBA(0, .67f, .67f, 1);
        
     // create a bg container
        bg = new Node("bg");
        bg.setLocalTranslation(new Vector3f(width/2, height/2, 0));
        
		p1Fill = new Quad[6];
		p2Fill = new Quad[6];
		opFill = new Quad[6];
		/*for (int i=0; i<p1Fill.length; i++) {
			p1Fill[i] = drawRect(162, 18, boxColor);
			p1Fill[i].setLocalTranslation(new Vector3f(-22 + 98, 100 - 20*i + 10, 0));
			container.attachChild(p1Fill[i]);
		}*/
        
        
        int numchars = 6;
        charNames = new String[numchars];
        charNames[0] = "Khoa";
        charNames[1] = "Cy";
        charNames[2] = "Nilay";
        charNames[3] = "Monica";
        charNames[4] = "Austin";
        charNames[5] = "Brian";
        p1Display = new BitmapText[numchars];
        p2Display = new BitmapText[numchars];
        opDisplay = new BitmapText[numchars];
        for (int i = 0; i < numchars; i++)
        {
        	p1Display[i] = new BitmapText(font, false);
        	p2Display[i] = new BitmapText(font, false);
        	opDisplay[i] = new BitmapText(font, false);
        	p1Display[i].setText(charNames[i]);
        	p2Display[i].setText(charNames[i]);
        	opDisplay[i].setText(charNames[i]);
        }
        Menu = new BitmapText(font, false);
        Menu.setText("Character Select. Use arrow keys to move, space to choose, and enter to play.");
        
        for(int i = 0; i < 3; i++)
        	selectedChars[i] = 0;
        
        //for (int i = 0; i < p1Display.length; i++)
        /*{
        	BitmapText txt = p1Display[i];
        	txt.setLocalTranslation(new Vector3f(-22 + 98, 100 - 20*i + 10, 0));
			container.attachChild(txt);
		
        }*/
        
        container.setLocalScale((float) DisplaySystem.getDisplaySystem().getHeight()/600);
        
	}
        
    public void update(BattleGameState b) {
       	
       	for (int i=0; i<charNames.length; i++) {
       		p1Display[i].setLocalTranslation(new Vector3f(22 + 4, 100 - 20*i + 18, 0));
       		p1Display[i].update();
       	}
       	
       	
       	
       	
    }
    
    public void updateDisplay(int dir){
    	int []lastIndex = new int[2];
    	lastIndex[0] = CurrentIndex[0];
    	lastIndex[1] = CurrentIndex[1];
		if (dir == 1) {
			if (CurrentIndex[1] == 0)
				return;
			else
				CurrentIndex[1]--;
		} else {
			if(dir == 2)
			{
			if (CurrentIndex[0] == 2)
				return;
			else
				CurrentIndex[0]++;
			}
			else
				if(dir == 3)
				{
					if (CurrentIndex[1] == p1Fill.length-1)
						return;
					else
						CurrentIndex[1]++;
				}
				else
				{
					if (CurrentIndex[0] == 0)
						return;
					else
						CurrentIndex[0]--;
				}
		}
		System.out.println(lastIndex[0] + " " + lastIndex[1]);
		System.out.println(CurrentIndex[0] + " " + CurrentIndex[1]);
		
		p1Display[selectedChars[0]].setDefaultColor(selBoxColor);
		p2Display[selectedChars[1]].setDefaultColor(selBoxColor);
		opDisplay[selectedChars[2]].setDefaultColor(selBoxColor);
		p1Display[selectedChars[0]].update();
		p2Display[selectedChars[0]].update();
		opDisplay[selectedChars[0]].update();
		
		if(CurrentIndex[0] == 0)
		{
			p1Display[CurrentIndex[1]].setDefaultColor(selTextColor);
			p1Display[CurrentIndex[1]].update();
		}
		else
			if(CurrentIndex[0] == 1)
			{
				p2Display[CurrentIndex[1]].setDefaultColor(selTextColor);
				p2Display[CurrentIndex[1]].update();
			}
			else
				if(CurrentIndex[0] == 2)
				{
					opDisplay[CurrentIndex[1]].setDefaultColor(selTextColor);
					opDisplay[CurrentIndex[1]].update();
				}
		if(lastIndex[0] == 0)
		{
			p1Display[lastIndex[1]].setDefaultColor(textColor);
			if(lastIndex[1] == selectedChars[0])
				p1Display[lastIndex[1]].setDefaultColor(selBoxColor);
			p1Display[lastIndex[1]].update();
		}
		else
			if(lastIndex[0] == 1)
			{
				p2Display[lastIndex[1]].setDefaultColor(textColor);
				if(lastIndex[1] == selectedChars[1])
					p2Display[lastIndex[1]].setDefaultColor(selBoxColor);
				p2Display[lastIndex[1]].update();
			}
			else
				if(lastIndex[0] == 2)
				{
					opDisplay[lastIndex[1]].setDefaultColor(textColor);
					if(lastIndex[1] == selectedChars[2])
						opDisplay[lastIndex[1]].setDefaultColor(selBoxColor);
					opDisplay[lastIndex[1]].update();
				}
		

    }
    
    public void updateSel()
    {
    	int last = selectedChars[CurrentIndex[0]];
    	selectedChars[CurrentIndex[0]] = CurrentIndex[1];
    	if(CurrentIndex[0] == 0)
		{
			p1Display[last].setDefaultColor(textColor);
			p1Display[last].update();
		}
		else
			if(CurrentIndex[0] == 1)
			{
				p2Display[last].setDefaultColor(textColor);
				p2Display[last].update();
			}
			else
				if(CurrentIndex[0] == 2)
				{
					opDisplay[last].setDefaultColor(textColor);
					opDisplay[last].update();
				}
    	
    	
    }
    
    
        
	public void pause() {
		screenshot = getScreenshot();
		screenshot.setLocalTranslation(new Vector3f(width/2, height/2, 0));
		container.attachChild(screenshot);
		

		for (int i=0; i<p1Fill.length; i++) {
		p1Fill[i] = drawRect(162, 18, boxColor);
		p1Fill[i].setLocalTranslation(new Vector3f(-22 + 205, 250 - 20*i + 10, 0));
		container.attachChild(p1Fill[i]);
		
		p2Fill[i] = drawRect(162, 18, boxColor);
		p2Fill[i].setLocalTranslation(new Vector3f(-22 + 405, 250 - 20*i + 10, 0));
		container.attachChild(p2Fill[i]);
		
		opFill[i] = drawRect(162, 18, boxColor);
		opFill[i].setLocalTranslation(new Vector3f(-22 + 605, 250 - 20*i + 10, 0));
		container.attachChild(opFill[i]);
		}
		
		Menu.setLocalTranslation(new Vector3f(-22+78,450,0));
		Menu.setSize(18);
       	Menu.update();
       	container.attachChild(Menu);
       	
       	
       	//the following lines can be removed when brian is created.
       	BitmapText warning = new BitmapText(font, false);
       	warning.setSize(18);
       	warning.setDefaultColor(textColor);
       	warning.setLocalTranslation(-22+78,410,0);
       	warning.setText("Note: do not choose brian until his model has been created.");
       	warning.update();
       	container.attachChild(warning);
       	
       	BitmapText[]labels = new BitmapText[3];
       	String[]temp = {"Player 1", "Player 2", "Opponent"};
       	for(int i = 0; i < 3; i++)
       	{
       		labels[i] = new BitmapText(font, false);
       		labels[i].setSize(17);
       		labels[i].setDefaultColor(textColor);
       		labels[i].setLocalTranslation(-62 + 205 + 200*i, 250 + 50, 0);
       		labels[i].setText(temp[i]);
       		labels[i].update();
       		container.attachChild(labels[i]);
       	}
       	
		
        for (int i = 0; i < p1Display.length; i++)
        {
        	p1Display[i].setSize(16);
        	p1Display[i].setDefaultColor(textColor);
        	p1Display[i].setText(charNames[i]);
        	p1Display[i].setLocalTranslation(new Vector3f(-22+175, 250 - 20*i + 18, 0));
        	p1Display[i].update();
			container.attachChild(p1Display[i]);
			
			p2Display[i].setSize(16);
        	p2Display[i].setDefaultColor(textColor);
        	p2Display[i].setText(charNames[i]);
        	p2Display[i].setLocalTranslation(new Vector3f(-22+375, 250 - 20*i + 18, 0));
        	p2Display[i].update();
			container.attachChild(p2Display[i]);
			
			opDisplay[i].setSize(16);
        	opDisplay[i].setDefaultColor(textColor);
        	opDisplay[i].setText(charNames[i]);
        	opDisplay[i].setLocalTranslation(new Vector3f(-22+575, 250 - 20*i + 18, 0));
        	opDisplay[i].update();
			container.attachChild(opDisplay[i]);
		
        }
		this.updateRenderState();
	}  
	
	public void unpause() {
        for (int i = 0; i < p1Display.length; i++)
        {
			
			container.detachChild(p1Fill[i]);
			container.detachChild(p2Fill[i]);
			container.detachChild(opFill[i]);
			container.detachChild(p1Display[i]);
			container.detachChild(p2Display[i]);
			container.detachChild(opDisplay[i]);
        }
		container.detachChild(screenshot);
		this.updateRenderState();
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
	
	public void setBackground(Quad q) {
		bg.attachChild(q);
		bg.updateRenderState();
	}
	
	public int[] getSelectedChars()
	{
		for(int i = 0; i < 3; i++)
			System.out.println(selectedChars[i]+" ");
		return selectedChars;
	}
	
	
}