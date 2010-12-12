package com.googlecode.reaxion.game.overlay;

import java.awt.Point;

/**
 * {@code GridOverlay} extends the functionality of {@code Overlay} in order to provide grid layout support.
 * Grid properties can be entered into methods which return two-dimensional arrays containing the appopriate
 * positions for each item in the grid.
 * 
 * @author Brian Clanton
 *
 */

public abstract class GridOverlay extends Overlay {

	public int baseWidth, baseHeight;
	
	public boolean mainOverlay;

	public GridOverlay(int baseWidth, int baseHeight, boolean mainOverlay) {
		super();
		
		this.baseWidth = baseWidth;
		this.baseHeight = baseHeight;
		this.mainOverlay = mainOverlay;
	}

	/**
	 * Returns the positions for grid items. The grid area is vertically centered.
	 * 
	 * @param rows
	 * @param columns
	 * @param xDisp
	 * @param boxWidth
	 * @param boxHeight
	 * @param horizontalSpacing
	 * @param verticalSpacing
	 * @return positions
	 */
	protected Point[][] createVerticallyCenteredGrid(int rows, int columns, int xDisp, int boxWidth, int boxHeight, int horizontalSpacing, int verticalSpacing) {
		Point[][] positions = new Point[rows][columns];

		xDisp -= (mainOverlay ? 0 : baseWidth / 2);

		int totalGridSpaceY = rows * boxHeight + (rows - 1) * verticalSpacing;
		int yDisp = baseHeight - (baseHeight - totalGridSpaceY) / 2 - (mainOverlay ? 0 : baseHeight / 2);

		for (int i = 0; i < rows; i++)
			for (int j = 0; j < columns; j++)
				positions[i][j] = new Point(xDisp + (boxWidth + horizontalSpacing) * j, yDisp - (boxHeight + verticalSpacing) * i);

		return positions;
	}

	/**
	 * Returns the positions for grid items. The grid area is horizontally centered.
	 * 
	 * @param rows
	 * @param columns
	 * @param xDisp
	 * @param boxWidth
	 * @param boxHeight
	 * @param horizontalSpacing
	 * @param verticalSpacing
	 * @return positions
	 */
	protected Point[][] createHorizontallyCenteredGrid(int rows, int columns, int yDisp, int boxWidth, int boxHeight, int horizontalSpacing, int verticalSpacing) {
		Point[][] positions = new Point[rows][columns];

		int totalGridSpaceX = columns * boxWidth + (columns - 1) * horizontalSpacing;
		int xDisp = baseWidth - (baseWidth - totalGridSpaceX) / 2 - (mainOverlay ? 0 : baseWidth / 2);
		
		yDisp -= (mainOverlay ? 0 : baseHeight / 2);

		for (int i = 0; i < rows; i++)
			for (int j = 0; j < columns; j++)
				positions[i][j] = new Point(xDisp + (boxWidth + horizontalSpacing) * j, yDisp - (boxHeight + verticalSpacing) * i);

		return positions;
	}

	/**
	 * Returns the positions for grid items. The grid area is horizontally and vertically centered.
	 * 
	 * @param rows
	 * @param columns
	 * @param xDisp
	 * @param boxWidth
	 * @param boxHeight
	 * @param horizontalSpacing
	 * @param verticalSpacing
	 * @return positions
	 */
	protected Point[][] createCenteredGrid(int rows, int columns, int boxWidth, int boxHeight, int horizontalSpacing, int verticalSpacing) {
		Point[][] positions = new Point[rows][columns];

		int totalGridSpaceX = columns * boxWidth + (columns - 1) * horizontalSpacing;
		int xDisp = baseWidth - (baseWidth - totalGridSpaceX) / 2 - (mainOverlay ? 0 : baseWidth / 2);
		
		int totalGridSpaceY = rows * boxHeight + (rows - 1) * verticalSpacing;
		int yDisp = baseHeight - (baseHeight - totalGridSpaceY) / 2 - (mainOverlay ? 0 : baseHeight / 2);

		for (int i = 0; i < rows; i++)
			for (int j = 0; j < columns; j++)
				positions[i][j] = new Point(xDisp + (boxWidth + horizontalSpacing) * j, yDisp - (boxHeight + verticalSpacing) * i);

		return positions;
	}
	
	protected Point[] createCenteredTextList(int rows, int fontSize, int verticalSpacing) {
		Point[] positions = new Point[rows];
		
		int xDisp = (mainOverlay ? baseWidth / 2 : 0);
		
		int totalGridSpaceY = fontSize * rows + verticalSpacing * (rows - 1);
		int yDisp = baseHeight - (baseHeight - totalGridSpaceY) / 2 - (mainOverlay ? 0 : baseHeight / 2);
		
		for (int i = 0; i < rows; i++)
			positions[i] = new Point(xDisp, yDisp - (fontSize + verticalSpacing) * i);
		
		return positions;
	}

}
