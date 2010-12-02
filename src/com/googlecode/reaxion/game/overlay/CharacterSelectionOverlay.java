package com.googlecode.reaxion.game.overlay;

import com.googlecode.reaxion.game.util.FontUtils;
import com.jme.math.Vector3f;
import com.jme.renderer.ColorRGBA;
import com.jme.scene.Node;
import com.jme.scene.shape.Quad;
import com.jme.system.DisplaySystem;
import com.jmex.angelfont.BitmapText;

public class CharacterSelectionOverlay extends Overlay {

	private static final String baseURL = "../../resources/gui/";

	private final int numchars = 8;
	
	private Node container;

	private String[] charNames;
	private Quad[] p1Fill;
	private Quad[] p2Fill;
	private Quad[] opFill;
	private BitmapText[] p1Display;
	private BitmapText[] p2Display;
	private BitmapText[] opDisplay;
	private BitmapText menu;

	private ColorRGBA textColor;
	private ColorRGBA boxColor;
	private ColorRGBA selTextColor;
	private ColorRGBA selBoxColor;

	private int[] currentIndex = new int[2];
	private int[] selectedChars = new int[3];

	public CharacterSelectionOverlay() {
		super();

		// create a container Node for scaling
		container = new Node("container");
		attachChild(container);

		// White
		textColor = new ColorRGBA(1, 1, 1, 1);
		// Dark Gray
		boxColor = new ColorRGBA(.25f, .25f, .25f, 1);
		selTextColor = new ColorRGBA(0, 1, 0, 1);
		selBoxColor = new ColorRGBA(0, .67f, .67f, 1);

		p1Fill = new Quad[numchars];
		p2Fill = new Quad[numchars];
		opFill = new Quad[numchars];

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
		p2Display = new BitmapText[numchars];
		opDisplay = new BitmapText[numchars];
		for (int i = 0; i < numchars; i++) {
			p1Display[i] = new BitmapText(FontUtils.neuropol, false);
			p2Display[i] = new BitmapText(FontUtils.neuropol, false);
			opDisplay[i] = new BitmapText(FontUtils.neuropol, false);
			p1Display[i].setText(charNames[i]);
			p2Display[i].setText(charNames[i]);
			opDisplay[i].setText(charNames[i]);
		}
		menu = new BitmapText(FontUtils.neuropol, false);
		menu.setText("Character Select. Use arrow keys to move, space to choose, and enter to play.");

		for (int i = 0; i < 3; i++)
			selectedChars[i] = 0;

		initGUI();

		container.setLocalScale((float) DisplaySystem.getDisplaySystem()
				.getHeight() / 600);
	}

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
				if (currentIndex[1] == p1Fill.length - 1)
					return;
				else
					currentIndex[1]++;

			} else if (dir == 3) {
				if (currentIndex[0] == 2)
					return;
				else
					currentIndex[0]++;

			} else {
				if (currentIndex[1] == 0)
					return;
				else
					currentIndex[1]--;

			}
		}
		/*
		 * System.out.println(lastIndex[0] + " " + lastIndex[1]);
		 * System.out.println(currentIndex[0] + " " + currentIndex[1]);
		 */

		p1Display[selectedChars[0]].setDefaultColor(selBoxColor);
		p2Display[selectedChars[1]].setDefaultColor(selBoxColor);
		opDisplay[selectedChars[2]].setDefaultColor(selBoxColor);

		if (currentIndex[0] == 0) {
			p1Display[currentIndex[1]].setDefaultColor(selTextColor);
			p1Display[currentIndex[1]].update();
		} else if (currentIndex[0] == 1) {
			p2Display[currentIndex[1]].setDefaultColor(selTextColor);
			p2Display[currentIndex[1]].update();
		} else if (currentIndex[0] == 2) {
			opDisplay[currentIndex[1]].setDefaultColor(selTextColor);
			opDisplay[currentIndex[1]].update();
		}
		if (lastIndex[0] == 0) {
			p1Display[lastIndex[1]].setDefaultColor(textColor);
			if (lastIndex[1] == selectedChars[0])
				p1Display[lastIndex[1]].setDefaultColor(selBoxColor);
			p1Display[lastIndex[1]].update();
		} else if (lastIndex[0] == 1) {
			p2Display[lastIndex[1]].setDefaultColor(textColor);
			if (lastIndex[1] == selectedChars[1])
				p2Display[lastIndex[1]].setDefaultColor(selBoxColor);
			p2Display[lastIndex[1]].update();
		} else if (lastIndex[0] == 2) {
			opDisplay[lastIndex[1]].setDefaultColor(textColor);
			if (lastIndex[1] == selectedChars[2])
				opDisplay[lastIndex[1]].setDefaultColor(selBoxColor);
			opDisplay[lastIndex[1]].update();
		}

	}

	public void updateSel() {
		int last = selectedChars[currentIndex[0]];
		selectedChars[currentIndex[0]] = currentIndex[1];
		if (currentIndex[0] == 0) {
			p1Display[last].setDefaultColor(textColor);
			p1Display[last].update();
		} else if (currentIndex[0] == 1) {
			p2Display[last].setDefaultColor(textColor);
			p2Display[last].update();
		} else if (currentIndex[0] == 2) {
			opDisplay[last].setDefaultColor(textColor);
			opDisplay[last].update();
		}
	}

	public void initGUI() {

		for (int i = 0; i < p1Fill.length; i++) {
			p1Fill[i] = drawRect(70, 70, boxColor);
			p1Fill[i].setLocalTranslation(new Vector3f(-100 + 225 + 90 * i,
					410 - /*20 * i + */10, 0));
			container.attachChild(p1Fill[i]);

			p2Fill[i] = drawRect(70,70, boxColor);
			p2Fill[i].setLocalTranslation(new Vector3f(-100 + 225 + 90 * i,
					280 -10, 0));
			container.attachChild(p2Fill[i]);

			opFill[i] = drawRect(70,70, boxColor);
			opFill[i].setLocalTranslation(new Vector3f(-100 + 225 + 90 * i,
					150 -  10, 0));
			container.attachChild(opFill[i]);
		}

		menu.setLocalTranslation(new Vector3f(-22 + 78, 550, 0));
		menu.setSize(18);
		menu.update();
		container.attachChild(menu);

		// the following lines can be removed when brian is created.
		/*
		BitmapText warning = new BitmapText(FontUtils.neuropol, false);
		warning.setSize(18);
		warning.setDefaultColor(textColor);
		warning.setLocalTranslation(-22 + 78, 410, 0);
		warning
				.setText("Note: do not choose brian until his model has been created.");
		warning.update();
		container.attachChild(warning);
		*/

		BitmapText[] labels = new BitmapText[3];
		String[] temp = { "Player 1", "Player 2", "Opponent" };
		for (int i = 0; i < 3; i++) {
			labels[i] = new BitmapText(FontUtils.neuropol, false);
			labels[i].setSize(17);
			labels[i].setDefaultColor(textColor);
			labels[i].setLocalTranslation(-62 + 152, 405 + 50 - 130*i, 0);
			labels[i].setText(temp[i]);
			labels[i].update();
			container.attachChild(labels[i]);
		}

		for (int i = 0; i < p1Display.length; i++) {
			p1Display[i].setSize(16);
			p1Display[i].setDefaultColor(i == 0? selTextColor : textColor);
			p1Display[i].setText(charNames[i]);
			p1Display[i].setLocalTranslation(new Vector3f(-130 + 225 + 90 * i,
					360, 0));
			p1Display[i].update();
			container.attachChild(p1Display[i]);

			p2Display[i].setSize(16);
			p2Display[i].setDefaultColor(i == 0 ? selBoxColor : textColor);
			p2Display[i].setText(charNames[i]);
			p2Display[i].setLocalTranslation(new Vector3f(-130 + 225 + 90 * i,
					230, 0));
			p2Display[i].update();
			container.attachChild(p2Display[i]);

			opDisplay[i].setSize(16);
			opDisplay[i].setDefaultColor(i == 0 ? selBoxColor : textColor);
			opDisplay[i].setText(charNames[i]);
			opDisplay[i].setLocalTranslation(new Vector3f(-130 + 225 + 90 * i,
					100, 0));
			opDisplay[i].update();
			container.attachChild(opDisplay[i]);

		}
		this.updateRenderState();
	}

	public String[] getSelectedChars() {
		String[] temp = new String[selectedChars.length];
		for (int i = 0; i < selectedChars.length; i++)
			temp[i] = charNames[selectedChars[i]];
		return temp;
	}
	
	public int[] getSelectedCharsIndex() {
		return new int[]{currentIndex[0],currentIndex[1]};
	}

}