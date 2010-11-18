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

public class GridOverlay extends Overlay {

	public int screenWidth, screenHeight;
	
	public boolean mainOverlay;

	public GridOverlay(int screenWidth, int screenHeight, boolean mainOverlay) {
		super();
		
		this.screenWidth = screenWidth;
		this.screenHeight = screenHeight;
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

		int totalGridSpaceY = rows * boxHeight + (rows - 1) * verticalSpacing;
		int yDisp = screenHeight - (screenHeight - totalGridSpaceY) / 2 - (mainOverlay ? 0 : screenHeight / 2);
		xDisp -= (mainOverlay ? 0 : screenWidth / 2);

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
		yDisp -= (mainOverlay ? 0 : screenHeight / 2);
		int xDisp = screenWidth - (screenWidth - totalGridSpaceX) / 2 - (mainOverlay ? 0 : screenWidth / 2);

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
		int xDisp = screenWidth - (screenWidth - totalGridSpaceX) / 2 - (mainOverlay ? 0 : screenWidth / 2);
		
		int totalGridSpaceY = rows * boxHeight + (rows - 1) * verticalSpacing;
		int yDisp = screenHeight - (screenHeight - totalGridSpaceY) / 2 - (mainOverlay ? 0 : screenHeight / 2);

		for (int i = 0; i < rows; i++)
			for (int j = 0; j < columns; j++)
				positions[i][j] = new Point(xDisp + (boxWidth + horizontalSpacing) * j, yDisp - (boxHeight + verticalSpacing) * i);

		return positions;
	}

}
