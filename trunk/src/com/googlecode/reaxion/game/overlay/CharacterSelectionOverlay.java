package com.googlecode.reaxion.game.overlay;

import java.awt.Point;

import com.googlecode.reaxion.game.util.FontUtils;
import com.jme.math.Vector3f;
import com.jme.renderer.ColorRGBA;
import com.jme.scene.Node;
import com.jme.scene.shape.Quad;
import com.jme.system.DisplaySystem;
import com.jmex.angelfont.BitmapText;

/**
 * {@code CharacterSelectionOverlay} extends the functionality of {@code GridOverlay} in
 * order to create a character selection menu with grid elements. Images of the characters
 * are arranged in a grid format, with cursors to select and choose the characters.
 * 
 * @author Austin Hou
 */

public class CharacterSelectionOverlay extends GridOverlay {

	private static final String baseURL = "../../resources/icons/characterselect/";
	private static final String cursorURL = "../../resources/cursors/";

	private final int numchars = 8;
	
	private String[] charNames;
	private Quad[] p1Fill;
	private Quad p1c;
	private Quad p2c;
	private BitmapText[] p1Display;
	private BitmapText menu;
	private BitmapText menu2;

	//table dimensions
	private int tblL = 6;
	private int tblW = 4;
	
	//round number (see method updateSel)
	private int round = 0;
	
	private ColorRGBA textColor;
	private ColorRGBA selTextColor;

	private int[] currentIndex = new int[2];
	private int[] selectedChars = new int[3];
	
	private int[][] takenPos = new int[tblL][tblW];


	/**
	 * This method initializes both visible and background elements of {@code CharacterSelectionOverlay}.
	 */
	public CharacterSelectionOverlay() {
		super(800, 600, true);
		// create a container Node for scaling
		container = new Node("container");
		attachChild(container);

		// Colors
		textColor = new ColorRGBA(1, 1, 1, 1);
		selTextColor = new ColorRGBA(0, 1, 0, 1);
		
		p1Fill = new Quad[numchars];

		//Character List initiation
		charNames = new String[numchars];
		charNames[0] = "Khoa";
		charNames[1] = "Cy";
		charNames[2] = "Nilay";
		charNames[3] = "Monica";
		charNames[4] = "Austin";
		charNames[5] = "Brian";
		charNames[6] = "Andrew";
		charNames[7] = "Shine";
		p1Display = new BitmapText[numchars];
		for (int i = 0; i < numchars; i++) {
			p1Display[i] = new BitmapText(FontUtils.neuropol, false);
			p1Display[i].setText(charNames[i]);
		}
		
		menu = new BitmapText(FontUtils.neuropol, false);
		menu.setText("Character Select. Use arrow keys to move, space to choose, and enter to play.");
		menu2 = new BitmapText(FontUtils.neuropol, false);
		menu2.setText("Press 1 to choose player 1 and 2 to choose player 2.");
		
		for (int i = 0; i < 3; i++)
			selectedChars[i] = 0;

		//initiates display
		initGUI();

		container.updateRenderState();
		container.setLocalScale((float) DisplaySystem.getDisplaySystem()
				.getHeight() / 600);
		
		attachChild(container);
	}

	/**
	 * Function to be called during each movement by the player.
	 * @param dir direction of movement
	 */
	public void updateDisplay(int dir) {
		int[] lastIndex = new int[2];
		lastIndex[0] = currentIndex[0];
		lastIndex[1] = currentIndex[1];
		if (dir == 1) {

			if (currentIndex[0] == 0)
				return;
			else
				currentIndex[0]--;
		} else {
			if (dir == 2) {
				if (currentIndex[1] == tblL-1 || takenPos[currentIndex[1]+1][currentIndex[0]] == 123)
					return;
				else
					currentIndex[1]++;

			} else if (dir == 3) {
				if (currentIndex[0] == tblW-1 || takenPos[currentIndex[1]][currentIndex[0]+1] == 123)
				{
					if(takenPos[0][currentIndex[0]+1] != 123){
						int a = 0;
						while(takenPos[a][currentIndex[0]+1] != 123)
							a++;
						a--;
						currentIndex[0]++;
						currentIndex[1] = a;
					}
					else
						return;
				}
				else
					currentIndex[0]++;

			} else {
				if (currentIndex[1] == 0)
					return;
				else
					currentIndex[1]--;

			}
		}

		int selCur = takenPos[currentIndex[1]][currentIndex[0]];
		
		p1Display[selCur].setDefaultColor(selTextColor);
		
		int selBef = takenPos[lastIndex[1]][lastIndex[0]];
		
		p1Display[selBef].setDefaultColor(textColor);
		
		/*int picked = 0;
		picked = takenPos[currentIndex[1]][currentIndex[0]];
		p1c.setLocalTranslation(p1Fill[picked].getLocalTranslation());
		container.detachChild(p1c);
		container.attachChild(p1c);*/

	}

	/**
	 * Function to be called during each selection by the player.
	 * 
	 */
	public void updateSel() {
		
		int picked = 0;
		picked = takenPos[currentIndex[1]][currentIndex[0]];
		System.out.println(picked);
		
		//round of selection - 0 = player 1, 1 = player 2, 2 (optional) = opponent
			switch(round)
			{
				case 0:
					selectedChars[0] = picked;
					//round ++;
					p1c.setLocalTranslation(p1Fill[picked].getLocalTranslation());
					container.detachChild(p1c);
					container.attachChild(p1c);
					this.updateRenderState();
					break;
				case 1:
					selectedChars[1] = picked;
					//round ++;
					p2c.setLocalTranslation(p1Fill[picked].getLocalTranslation());
					container.detachChild(p2c);
					container.attachChild(p2c);
					this.updateRenderState();
					break;
				case 2:
					selectedChars[2] = picked;
					//round ++;
					break;
				default:
					break;
		}
	}

	/**
	 * Function to be called at Overlay initiation
	 * 
	 */
	public void initGUI() {

		//initiates table values to set bounds on cursor movement
		for(int i = 0; i < tblL; i++)
			for(int j = 0; j < tblW; j++)
				takenPos[i][j] = 123;
		
		String s = cursorURL + "p1.png";
		p1c = getImage(s);
		s = cursorURL + "p2.png";
		p2c = getImage(s);
		
		//retrieves character images
    	String [] charLoc = new String[numchars];
    	for(int j = 0; j<numchars; j++)
    	{
    		charLoc[j] = baseURL+charNames[j].toLowerCase()+".png";
    		p1Fill[j] = getImage(charLoc[j]);
    		p1Fill[j].setLocalScale(35f/64f);
    	}
    	
    	//grid creation
    	Point[][] pos = createHorizontallyCenteredGrid(tblW, tblL, 400, 70, 70, 10, 40);
		
    	int cntr = 0;
		for (int i = 0; i < tblW; i++) 
			for (int j = 0; j < tblL; j++){
				if(cntr >= p1Fill.length)
					break;
				p1Fill[cntr].setLocalTranslation(new Vector3f(-425 + pos[i][j].x,
						pos[i][j].y, 0));
				p1Display[cntr].setLocalTranslation(new Vector3f(-460 + pos[i][j].x,
						pos[i][j].y - 35, 0));
				takenPos[j][i] = cntr;
				container.attachChild(p1Fill[cntr]);
				cntr++;
		}

		//instructions
		menu.setLocalTranslation(new Vector3f(-22 + 38, 550, 0));
		menu.setSize(18);
		menu.update();
		menu2.setLocalTranslation(new Vector3f(-22 + 38, 535, 0));
		menu2.setSize(18);
		menu2.update();
		container.attachChild(menu);
		container.attachChild(menu2);

		//player name list 
		for (int i = 0; i < p1Display.length; i++) {
			p1Display[i].setSize(16);
			p1Display[i].setDefaultColor(i == 0? selTextColor : textColor);
			p1Display[i].setText(charNames[i]);
			p1Display[i].update();
			container.attachChild(p1Display[i]);
		}
		this.updateRenderState();
	}

	/**
	 * Function to be called at when the player presses 'backspace' to undo a selection
	 * 
	 */
	public void undo() {
		if(round == 0)
			return;
		else
			if(round == 1)
			{
				container.detachChild(p1c);
				round = 0;
				this.updateRenderState();
				return;
			}
			else
				if(round == 2)
				{
					container.detachChild(p2c);
					round = 1;
					this.updateRenderState();
					return;
				}
				else
					if(round > 2)
					{
						round = 2;
						//this.updateRenderState();
						return;
					}
	}
	
	
	/**
	 * Function to be called to choose Player 1
	 * 
	 */
	public void choose1() {
		container.detachChild(p1c);
		round = 0;
		this.updateRenderState();
		return;
		
	}
	
	
	/**
	 * Function to be called to choose Player 2
	 * 
	 */
	public void choose2() {
		container.detachChild(p2c);
		round = 1;
		this.updateRenderState();
		return;
	}
	
	
	/**
	 * Function to be called at the conclusion of character selection.
	 * 
	 */
	public String[] getSelectedChars() {
		String[] temp = new String[selectedChars.length];
		for (int i = 0; i < selectedChars.length; i++)
			temp[i] = charNames[selectedChars[i]];
		return temp;
	}

}