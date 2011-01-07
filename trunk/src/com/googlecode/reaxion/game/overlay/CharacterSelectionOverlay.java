package com.googlecode.reaxion.game.overlay;

import java.awt.Point;
import java.util.Arrays;

import com.googlecode.reaxion.game.input.bindings.CharacterSelectionOverlayBindings;
import com.googlecode.reaxion.game.input.bindings.KeyBindings;
import com.googlecode.reaxion.game.input.bindings.MenuBindings;
import com.googlecode.reaxion.game.util.FontUtils;
import com.jme.math.FastMath;
import com.jme.math.Matrix3f;
import com.jme.math.Vector3f;
import com.jme.renderer.ColorRGBA;
import com.jme.scene.Node;
import com.jme.scene.shape.Quad;
import com.jme.system.DisplaySystem;
import com.jmex.angelfont.BitmapText;
import com.jmex.game.state.GameState;

/**
 * {@code CharacterSelectionOverlay} extends the functionality of {@code GridOverlay} in
 * order to create a character selection menu with grid elements. Images of the characters
 * are arranged in a grid format, with cursors to select and choose the characters.
 * 
 * @author Austin Hou
 */

public class CharacterSelectionOverlay extends MenuOverlay {

	public static final String NAME = "characterSelectionOverlay";
	
	private static final String baseURL = "com/googlecode/reaxion/resources/icons/characterselect/";
	private static final String baseGuiURL = "com/googlecode/reaxion/resources/gui/";
	private static final String cursorURL = "com/googlecode/reaxion/resources/cursors/";
	
	private String[] charNames = {"Khoa", "Cy", "Nilay", "Monica", "Austin", "Brian", "Andrew", "Jenna", "Raina", "Savannah", "Polina", "Shine"};
	private Node grid;
	private Quad[] p1Fill;
	private Quad p1c;
	private Quad p2c;
	private BitmapText[] p1Display;
	
	private boolean showBg;
	private float bgAngle = 0;
	
	private Quad bg;
	private Quad front;

	//table dimensions
	private int tblL = 4;
	private int tblW = 4;
	
	//round number (see method updateSel)
	private int round = 0;
	
	private ColorRGBA textColor;
	private ColorRGBA selTextColor;

	private int[] currentIndex = new int[2];
	private int[] selectedChars = new int[2];
	
	private int[][] takenPos = new int[tblL][tblW];
	
	

	/**
	 * This method initializes both visible and background elements of {@code CharacterSelectionOverlay}.
	 */
	public CharacterSelectionOverlay(boolean showBg) {
		super(NAME, 800, 600, true);
		// create a container Node for scaling
		container = new Node("container_characterSelect");
		
		this.showBg = showBg;
		
		// Colors
		textColor = FontUtils.unselected;
		selTextColor = FontUtils.blueSelected;
		
		p1Fill = new Quad[charNames.length];

		// Create visuals
		bg = getImage(baseGuiURL + "stage-select-bg.png");
		bg.setLocalTranslation(new Vector3f(400, 300, 0));
		bg.setZOrder(2);
		front = getImage(baseGuiURL + "char-select-front.png");
		front.setLocalTranslation(new Vector3f(400, 300, 0));
		front.setZOrder(1);
		
		//Character List initiation
		p1Display = new BitmapText[charNames.length];
		for (int i = 0; i < charNames.length; i++) {
			p1Display[i] = new BitmapText(FontUtils.neuropol, false);
			p1Display[i].setText(charNames[i]);
		}
		
		for (int i = 0; i < selectedChars.length; i++)
			selectedChars[i] = -1;

		//initiates display
		initGUI();

		if (showBg)
			container.attachChild(bg);
		container.attachChild(front);
		
		container.updateRenderState();
		container.setLocalScale((float) DisplaySystem.getDisplaySystem()
				.getHeight() / 600);
		
		attachChild(container);
	}

	/**
	 * Function to be called during each update by the GameState.
	 */
	public void update(GameState b) {
		bgAngle = (bgAngle - FastMath.PI/600)%(FastMath.PI*2);
		Matrix3f m = new Matrix3f();
		m.fromAngleNormalAxis(bgAngle, new Vector3f(0, 0, 1));
		bg.setLocalRotation(m);
	}
	
	/**
	 * Function to be called during each selection by the player.
	 * 
	 */
	public void updateSel() {
		
		int picked = takenPos[currentIndex[1]][currentIndex[0]];
		
		boolean flag = false;
		for (int i=0; i<selectedChars.length; i++)
			if (selectedChars[i] == picked) {
				flag = true;
				break;
			}
		
		//round of selection - 0 = player 1, 1 = player 2
		if (!flag) {
			switch(round)
			{
			case 0:
				selectedChars[0] = picked;
				p1c.setLocalTranslation(grid.getLocalTranslation().add(p1Fill[picked].getLocalTranslation()).mult(container.getLocalScale()));
				currentIndex[0] = 0;
				currentIndex[1] = 0;
				round++;
				break;
			case 1:
				selectedChars[1] = picked;
				p2c.setLocalTranslation(grid.getLocalTranslation().add(p1Fill[picked].getLocalTranslation()).mult(container.getLocalScale()));
				round++;
				break;
			default:
				break;
			}
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
		
		String s = cursorURL + "p1-2.png";
		p1c = getImage(s);
		s = cursorURL + "p2-2.png";
		p2c = getImage(s);
		hide(p1c);
		hide(p2c);
		
		//retrieves character images
    	String [] charLoc = new String[charNames.length];
    	for(int j = 0; j<charNames.length; j++)
    	{
    		charLoc[j] = baseURL+charNames[j].toLowerCase()+"96.png";
    		p1Fill[j] = getImage(charLoc[j]);
    	}
    	
    	//grid creation
    	Point[][] pos = createHorizontallyCenteredGrid(tblW, tblL, 400, 70, 70, 40, 60);
    	grid = new Node("grid");
    	grid.setLocalTranslation(-400 + 32, 32, 0);
    	
    	int cntr = 0;
		for (int i = 0; i < tblW; i++) 
			for (int j = 0; j < tblL; j++){
				if(cntr >= p1Fill.length)
					break;
				p1Fill[cntr].setLocalTranslation(new Vector3f(pos[i][j].x,
						pos[i][j].y, 0));
				p1Display[cntr].setLocalTranslation(new Vector3f(pos[i][j].x - 64*.75f,
						pos[i][j].y - 48, 0));
				takenPos[j][i] = cntr;
				grid.attachChild(p1Fill[cntr]);
				cntr++;
		}

		//instructions
		container.attachChild(grid);
		container.attachChild(p1c);
		container.attachChild(p2c);
		
		//player name list 
		for (int i = 0; i < p1Display.length; i++) {
			p1Display[i].setSize(16);
			p1Display[i].setDefaultColor(textColor);
			p1Display[i].setText(charNames[i]);
			p1Display[i].update();
			grid.attachChild(p1Display[i]);
		}
		
		p1c.setLocalTranslation(grid.getLocalTranslation().add(p1Fill[0].getLocalTranslation()).mult(container.getLocalScale()));
		
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
				hide(p1c);
				round = 0;
				//currentIndex[0] = 0;
				//currentIndex[1] = 0;
				this.updateRenderState();
				return;
			}
			else
				if(round == 2)
				{
					hide(p2c);
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
		hide(p1c);
		round = 0;
		this.updateRenderState();
		return;
		
	}
	
	
	/**
	 * Function to be called to choose Player 2
	 * 
	 */
	public void choose2() {
		hide(p2c);
		round = 1;

		this.updateRenderState();
		return;
	}
	
	
	/**
	 * Function to be called at the conclusion of character selection.
	 * 
	 * @param closingOverlay {@code boolean} indicating if menu will be disposed of after getting
	 * the selected stage class name.
	 */
	public String[] getSelectedChars(boolean closingOverlay) {		
		String[] temp = new String[selectedChars.length];
		for (int i = 0; i < selectedChars.length; i++) {
			if (selectedChars[i] < 0)
				return null;
			temp[i] = charNames[selectedChars[i]];
		}
		return temp;
	}
	
	@Override
	public void updateDisplay(KeyBindings k) {
		int[] lastIndex = currentIndex.clone();
		
		if (k == CharacterSelectionOverlayBindings.CHOOSE_1)
			choose1();
		else if (k == CharacterSelectionOverlayBindings.CHOOSE_2)
			choose2();
		else if (k == CharacterSelectionOverlayBindings.UNDO_CHOICE)
			undo();
		else if (k == MenuBindings.SELECT_ITEM)
			updateSel();
		
		if (k == MenuBindings.UP) {

			if (currentIndex[0] == 0)
			{
				currentIndex[0] = tblW - 1;
				if(takenPos[currentIndex[1]][currentIndex[0]] == 123)
					currentIndex[0]--;
			}
				//return;
			else
				currentIndex[0]--;
		} else if (k == MenuBindings.RIGHT) {
				if (currentIndex[1] == tblL-1 || takenPos[currentIndex[1]+1][currentIndex[0]] == 123)
					currentIndex[1] = 0;
					//return;
				else
					currentIndex[1]++;

		} else if (k == MenuBindings.DOWN) {
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
						currentIndex[0] = 0;
				}
				else
					currentIndex[0]++;

		} else if (k == MenuBindings.LEFT) {
				if (currentIndex[1] == 0)
				{
					currentIndex[1] = tblL - 1;
					while(takenPos[currentIndex[1]][currentIndex[0]] == 123)
						currentIndex[1]--;
				}
				else
					currentIndex[1]--;

		}

		int selCur = takenPos[currentIndex[1]][currentIndex[0]];
		
		//p1Display[selCur].setDefaultColor(selTextColor);
		
		int selBef = takenPos[lastIndex[1]][lastIndex[0]];
		
		//p1Display[selBef].setDefaultColor(textColor);
		
		if(round == 0)
		    p1c.setLocalTranslation(grid.getLocalTranslation().add(p1Fill[selCur].getLocalTranslation()).mult(container.getLocalScale()));
		else if(round == 1)
			p2c.setLocalTranslation(grid.getLocalTranslation().add(p1Fill[selCur].getLocalTranslation()).mult(container.getLocalScale()));
		
		/*int picked = 0;
		picked = takenPos[currentIndex[1]][currentIndex[0]];
		p1c.setLocalTranslation(p1Fill[picked].getLocalTranslation());
		container.detachChild(p1c);
		container.attachChild(p1c);*/

	}
	
	private void hide(Quad q) {
		q.setLocalTranslation(new Vector3f(-1000, -1000, 0));
	}

}