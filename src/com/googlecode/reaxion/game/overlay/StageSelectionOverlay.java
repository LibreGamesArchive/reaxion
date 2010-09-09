package com.googlecode.reaxion.game.overlay;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.jme.renderer.ColorRGBA;
import com.jme.scene.Node;
import com.jme.scene.shape.Quad;
import com.jme.system.DisplaySystem;
import com.jmex.angelfont.BitmapFont;
import com.jmex.angelfont.BitmapFontLoader;
import com.jmex.angelfont.BitmapText;

/**
 * {@code StageSelectionOverlay} extends the functionality of {@code Overlay} in order to create a stage selection menu.
 * An image of the stage is displayed on the left of the overlay, which corresponds to the currently selected stage name
 * on the right of the overlay.
 * @author Brian
 */

public class StageSelectionOverlay extends Overlay {

	private static final File fontFile = new File(
			"src/com/googlecode/reaxion/resources/fonts/neuropol.fnt");
	private static final File glyphFile = new File(
			"src/com/googlecode/reaxion/resources/fonts/neuropol_0.png");

	private BitmapFont font;

	private String baseURL = "../../resources/stages/renders/";

	private Node container;

	private String[] stageNames = { "Flower Field", "Checkerboard" };
	private Node[] stageBoxes;
	private BitmapText[] stageList;
	private int currentIndex;
	
	private ColorRGBA selectedText;
	private ColorRGBA unselectedText;

	private int fontSize;

	private int totalWidth = 800;
	private int totalHeight = 600;

	public StageSelectionOverlay() {
		super();
		init();
	}

	private void init() {
		container = new Node("container");
		currentIndex = 0;
		selectedText = new ColorRGBA(0, 1, 0, 1);
		unselectedText = new ColorRGBA(1, 1, 1, 1);
		fontSize = 24;

		try {
			font = BitmapFontLoader.load(fontFile.toURI().toURL(), glyphFile
					.toURI().toURL());
		} catch (Exception ex) {
			Logger.getLogger(this.getClass().getName()).log(Level.SEVERE,
					"Unable to load font: " + ex);
			System.exit(1);
		}

		createStageBoxes();
		createStageList();

		container.attachChild(stageBoxes[currentIndex]);

		container.updateRenderState();
		container.setLocalScale((float) DisplaySystem.getDisplaySystem()
				.getHeight()
				/ totalHeight);

		attachChild(container);
	}

	private String getImageURL(String s) {
		return s.toLowerCase().replace(" ", "-") + ".png";
	}

	private void createStageBoxes() {
		stageBoxes = new Node[stageNames.length];

		for (int i = 0; i < stageNames.length; i++)
			stageBoxes[i] = createStageBox(stageNames[i]);
	}

	private Node createStageBox(String name) {
		Node stageBox = new Node("stageBox_" + name);

		Quad image = getImage(baseURL + getImageURL(name));
		image.setName("image_" + name);

		Quad border = new Quad("border_" + name, image.getWidth() + 20, image
				.getHeight() + 16);
		border.setSolidColor(new ColorRGBA(1, 1, 1, 1));

		stageBox.attachChild(border);
		stageBox.attachChild(image);
		stageBox.setLocalTranslation(border.getWidth() / 2 + 40,
				totalHeight / 2, 0);

		return stageBox;
	}

	private void createStageList() {
		stageList = new BitmapText[stageNames.length];
		for (int i = 0; i < stageNames.length; i++) {
			stageList[i] = createStageListItem(stageNames[i], i);

			if (i == 0) {
				stageList[i].setDefaultColor(selectedText);
				stageList[i].update();
			}

			container.attachChild(stageList[i]);
		}
	}

	private BitmapText createStageListItem(String name, int index) {
		int topY, textHeightAndSpacing;

		BitmapText text = new BitmapText(font, false);
		text.setSize(fontSize);
		text.setDefaultColor(unselectedText);
		text.setText(name);
		text.setAlignment(BitmapFont.Align.Left);

		topY = totalHeight / 2 + (stageNames.length / 2)
				* (int) text.getLineHeight();
		textHeightAndSpacing = (int) text.getLineHeight() + 10;
		// position = (stageNames.length / 2) + index;

		text.setLocalTranslation(totalWidth - 300, topY - textHeightAndSpacing
				* index, 0);
		text.update();

		return text;
	}

	public void updateDisplay(boolean up) {
		int lastIndex = currentIndex;
		if (up) {
			if (currentIndex == 0)
				return;
			else
				currentIndex--;
		} else {
			if (currentIndex == stageNames.length - 1)
				return;
			else
				currentIndex++;
		}

		container.detachChild(stageBoxes[lastIndex]);
		container.attachChild(stageBoxes[currentIndex]);

		stageList[lastIndex].setDefaultColor(unselectedText);
		stageList[lastIndex].update();

		stageList[currentIndex].setDefaultColor(selectedText);
		stageList[currentIndex].update();

		container.updateRenderState();
	}

	public String getSelectedStageClass() {
		return stageNames[currentIndex].replace(" ", "");
	}
	
	public String getSelectedStageName() {
		return stageNames[currentIndex];
	}
	
	public BitmapFont getTextFont(){
		return font;
	}

}
