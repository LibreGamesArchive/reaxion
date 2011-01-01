package com.googlecode.reaxion.game.overlay;

import java.awt.Point;

import com.googlecode.reaxion.game.Reaxion;
import com.googlecode.reaxion.game.input.bindings.KeyBindings;
import com.googlecode.reaxion.game.input.bindings.MenuBindings;
import com.googlecode.reaxion.game.model.stage.CityOfDreams;
import com.googlecode.reaxion.game.model.stage.CloudNine;
import com.googlecode.reaxion.game.model.stage.CrystalPalace;
import com.googlecode.reaxion.game.model.stage.DataCore;
import com.googlecode.reaxion.game.model.stage.Flipside;
import com.googlecode.reaxion.game.model.stage.FlowerField;
import com.googlecode.reaxion.game.model.stage.HeavensAscent;
import com.googlecode.reaxion.game.model.stage.LavaValley;
import com.googlecode.reaxion.game.model.stage.MikoLake;
import com.googlecode.reaxion.game.model.stage.SeasRepose;
import com.googlecode.reaxion.game.model.stage.TwilightKingdom;
import com.googlecode.reaxion.game.model.stage.WorldsEdge;
import com.googlecode.reaxion.game.util.FontUtils;
import com.jme.math.FastMath;
import com.jme.math.Matrix3f;
import com.jme.math.Vector3f;
import com.jme.renderer.ColorRGBA;
import com.jme.scene.Node;
import com.jme.scene.shape.Quad;
import com.jmex.angelfont.BitmapText;
import com.jmex.game.state.GameState;

/**
 * {@code StageSelectionOverlay} extends the functionality of {@code GridOverlay} in
 * order to create a stage selection menu with grid elements. An image of the stage is 
 * displayed on the left of the overlay, which corresponds to the currently selected 
 * stage grid item on the right of the overlay. The name of the currently selected stage
 * is displayed under the stage preview image.
 * 
 * @author Brian Clanton
 */

public class StageSelectionOverlay extends MenuOverlay {

	public static final String NAME = "stageSelectionOverlay";
	
	private static final String baseIconURL = "com/googlecode/reaxion/resources/icons/stageselect/";
	private static final String baseGuiURL = "com/googlecode/reaxion/resources/gui/";

	private static final String[] stageNames = { FlowerField.NAME, WorldsEdge.NAME, MikoLake.NAME,
		Flipside.NAME, TwilightKingdom.NAME, SeasRepose.NAME,
		CityOfDreams.NAME, CloudNine.NAME, LavaValley.NAME,
		CrystalPalace.NAME, DataCore.NAME, HeavensAscent.NAME };
	
	private boolean showBg;
	
	private Quad bg;
	private Quad back;
	private Quad front;
	private float bgAngle = 0;
	
	private Node previews;
	private Quad[] previewBoxes;
	private BitmapText[] stageTitles;
	private Quad[][] stageGrid;
	private Quad cursor;
	
	private Point[][] stageGridLayout;

	private int currentRow, currentColumn;
	private int fontSize;
	private int stageGridRows, stageGridColumns;

	public StageSelectionOverlay(boolean showBg) {
		super(NAME, 800, 600, true);
		this.showBg = showBg;
		init();
	}

	/**
	 * This method initializes both visible and background elements of {@code StageSelectionOverlay}.
	 */
	private void init() {
		container = new Node("container_stageSelect");
		
		// Settings Initialization
		currentRow = 0;
		currentColumn = 0;
		fontSize = 32;
		
		stageGridRows = 4;
		stageGridColumns = 3;

		stageGridLayout = createVerticallyCenteredGrid(stageGridRows, stageGridColumns, baseWidth - 250,
				64, 48, 10, 10);
		
		// Visual Element Initialization
		createPreviewBoxes();
		createStageTitles();
		createStageGrid();
		
		cursor = getImage(baseIconURL + "stageselect_cursor.png");
		cursor.setLocalTranslation(stageGridLayout[currentRow][currentColumn].x,
				stageGridLayout[currentRow][currentColumn].y, 0);
		
		bg = getImage(baseGuiURL + "stage-select-bg.png");
		bg.setLocalTranslation(new Vector3f(400, 300, 0));
		bg.setZOrder(3);
		back = getImage(baseGuiURL + "stage-select-back.png");
		back.setLocalTranslation(new Vector3f(210, 300, 0));
		back.setZOrder(2);
		front = getImage(baseGuiURL + "stage-select-front.png");
		front.setLocalTranslation(new Vector3f(400, 300, 0));
		
		// Create Preview Holder
		previews = new Node("previews");
		previews.attachChild(previewBoxes[currentRow * stageGridColumns + currentColumn]);
		previews.setZOrder(1);
		
		// Visual Element Attachment
		if (showBg)
			container.attachChild(bg);
		container.attachChild(back);
		container.attachChild(cursor);
		container.attachChild(previews);
		container.attachChild(front);
		container.attachChild(stageTitles[currentRow * stageGridColumns + currentColumn]);

		container.updateRenderState();
		container.setLocalScale((float) Reaxion.getScreenHeight() / baseHeight);

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
	 * Returns the file URL of a stage image. The {@code isPreview} parameter is used to discern between
	 * preview images and grid item images.
	 * 
	 * @param stageName Name of the stage
	 * @param isPreview Indicates whether or not the image will be used as a preview image or as part
	 * of the stage grid
	 * @return A {@code String} containing the requested filename
	 */
	private String getImageURL(String stageName, boolean isPreview) {
		String str = stageName.replace("'", "").replace(" ", "-").toLowerCase();
		return baseIconURL + str + (isPreview ? "_preview" : "_griditem") + ".png";
	}

	/**
	 * Creates all of the preview boxes and stores them in an {@code Array}.
	 */
	private void createPreviewBoxes() {
		previewBoxes = new Quad[stageNames.length];

		for (int i = 0; i < stageNames.length; i++) {
			previewBoxes[i] = getImage(getImageURL(stageNames[i], true));
			previewBoxes[i].setLocalTranslation(210, 330, 0);
			//previewBoxes[i] = createPreviewBox(stageNames[i]);
		}
	}

	/**
	 * Creates an individual preview box.
	 * 
	 * @param name Name of the stage
	 * @return Preview box for the stage specified by the {@code name} parameter
	 */
	private Node createPreviewBox(String name) {
		Node previewBox = new Node("previewBox_" + name);

		Quad image = getImage(getImageURL(name, true));

		Quad border = new Quad("border_preview_" + name, image.getWidth() + 20, image
				.getHeight() + 16);
		border.setSolidColor(ColorRGBA.white);

		previewBox.attachChild(border);
		previewBox.attachChild(image);
		previewBox.setLocalTranslation(border.getWidth() / 2 + 40,
				baseHeight / 2 + 30, 0);

		return previewBox;
	}
	
	/**
	 * Creates all of the stage titles and stores them in an {@code Array}.
	 */
	private void createStageTitles() {
		stageTitles = new BitmapText[stageNames.length];
		
		for (int i = 0; i < stageNames.length; i++)
			stageTitles[i] = createStageTitle(stageNames[i]);
	}
	
	/**
	 * Creates an individual stage title. Text, name, color, size, and local translation are defined.
	 * 
	 * @param name Name of the stage
	 * @return {@code BitmapText} object with text set to {@code name}
	 */
	private BitmapText createStageTitle(String name) {
		BitmapText stage = new BitmapText(FontUtils.neuropol, false);
		stage.setText(name);
		stage.setName("stageTitle_" + name);
		stage.setDefaultColor(ColorRGBA.white);
		stage.setSize(fontSize);
		stage.update();
		stage.setLocalTranslation(210 - stage.getLineWidth() / 2, baseHeight / 2 - 256 / 2, 0);
		
		return stage;
	}

	/**
	 * Creates the navigable grid of stages.
	 */
	private void createStageGrid() {
		stageGrid = new Quad[stageGridRows][stageGridColumns];
		for (int i = 0; i < stageNames.length; i++) {
			int r = i / stageGridColumns;
			int c = i % stageGridColumns;
			
			stageGrid[r][c] = getImage(getImageURL(stageNames[i], false));
			stageGrid[r][c].setName("stageGridItem_" + stageNames[i]);

			stageGrid[r][c].setLocalTranslation(
					stageGridLayout[r][c].x,
					stageGridLayout[r][c].y, 0);
			container.attachChild(stageGrid[r][c]);
		}
	}
	
	/**
	 * Removes apostrophes and spaces in the currently selected stage name in order to generate a
	 * {@code String} corresponding to its class name. 
	 * 
	 * @param closingOverlay {@code boolean} indicating if menu will be disposed of after getting
	 * the selected stage class name.
	 * @return Class name of the currently selected stage
	 */
	public String getSelectedStageClass() {
		String str = stageNames[currentRow * stageGridColumns + currentColumn].replace("'", "");
		return str.replace(" ", "");
	}

	/**
	 * Returns the selected stage name.
	 * 
	 * @return Name of the currently selected stage
	 */
	public String getSelectedStageName() {
		return stageNames[currentRow * stageGridColumns + currentColumn];
	}
	
	/**
	 * Changes the selected stage.
	 * 
	 * @param name Name of the stage to be set selected
	 */
	public void setSelectedStage(String name) {
		System.out.println("# Changing Selected Stage To: " + name);
		
		int index = -1;
		int lastRow = currentRow;
		int lastColumn = currentColumn;
		
		for (int i = 0; i < stageNames.length; i++) {
			if (stageNames[i].equals(name)) {
				index = i;
				break;
			}
		}
		
		if (index != -1) {
			currentColumn = index % stageGridColumns;
			index -= currentColumn;
			currentRow = index / (stageGridRows - 1);
			
			System.out.println("# Current Row: " + currentRow + " || Max Row: " + stageGridRows);
			System.out.println("# Current Column: " + currentColumn + " || Max Column: " + stageGridColumns);
			
			updateMenuElements(lastRow, lastColumn);
		} else {
			System.out.println("# Name not found.");
		}
	}

	@Override
	public void updateDisplay(KeyBindings k) {
		int lastRow = currentRow;
		int lastColumn = currentColumn;
		
		/*
		 * lastItem, uneven, and onLastRow are use to handle cases when the grid is uneven.
		 * If the user presses up on the top row, the last row is uneven, and the grid item selected is in a 
		 * column that does not exist on the last row, then the last item in the last row is selected.
		 * If the user presses down on the second to last row, the last row is uneven, and the grid item selected
		 * is in a column that does not exits on the last row, then the last item in the last row is selected.
		 * Wrap around on the last row is also handled correctly.
		 */
		int lastItem = 	(stageNames.length - 1) % stageGridColumns;
		
		boolean uneven = lastItem != (stageGridColumns - 1);
		boolean onLastRow = currentRow == (stageGridRows - 1);
		
		//Key Input Checking
		if (k == MenuBindings.UP) {
			if (currentRow == 0) {
				if(uneven && currentColumn > lastItem)
					currentColumn = lastItem;
				currentRow = stageGridRows - 1;
			}
			else
				currentRow--;
		} else if (k == MenuBindings.DOWN) {
			if (currentRow == stageGridRows - 1)
				currentRow = 0;				
			else {
				if (currentRow == stageGridRows - 2 && uneven && currentColumn > lastItem)
					currentColumn = lastItem;
				currentRow ++;
			}
		} else if (k == MenuBindings.LEFT) {
			if (currentColumn == 0)
				currentColumn = onLastRow? lastItem : stageGridColumns - 1;
			else
				currentColumn--;
		} else if (k == MenuBindings.RIGHT) {
			if (onLastRow && currentColumn == lastItem || currentColumn == stageGridColumns - 1)
				currentColumn = 0;
			else
				currentColumn++;
		}
		
		if (!(lastRow == currentRow && lastColumn == currentColumn)) {
			updateMenuElements(lastRow, lastColumn);
			container.updateRenderState();
		}
	}

	private void updateMenuElements(int lastRow, int lastColumn) {
		// Cursor location changed
		cursor.setLocalTranslation(stageGridLayout[currentRow][currentColumn].x,
				stageGridLayout[currentRow][currentColumn].y, 0);
		
		// Changes currently displayed preview box
		previews.detachChild(previewBoxes[lastRow * stageGridColumns + lastColumn]);
		previews.attachChild(previewBoxes[currentRow * stageGridColumns + currentColumn]);
		
		// Changes current displayed stage title
		container.detachChild(stageTitles[lastRow * stageGridColumns + lastColumn]);
		container.attachChild(stageTitles[currentRow * stageGridColumns + currentColumn]);
	}
	
}
