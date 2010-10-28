package com.googlecode.reaxion.game.overlay;

import java.awt.Point;

/**
 * {@code GridOverlay} extends the functionality of {@code Overlay} in order to provide grid layout support.
 * Grid properties can be entered into methods which return two-dimensional arrays containing the appopriate
 * positions for each item in the grid.
 * 
 * @author Brian
 *
 */

public class GridOverlay extends Overlay {

	public int screenHeight, screenWidth;

	public GridOverlay() {
		super();
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
		int yDisp = screenHeight - (screenHeight - totalGridSpaceY) / 2;

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
		int xDisp = screenWidth - (screenWidth - totalGridSpaceX) / 2;

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
		int xDisp = screenWidth - (screenWidth - totalGridSpaceX) / 2;
		
		int totalGridSpaceY = rows * boxHeight + (rows - 1) * verticalSpacing;
		int yDisp = screenHeight - (screenHeight - totalGridSpaceY) / 2;

		for (int i = 0; i < rows; i++)
			for (int j = 0; j < columns; j++)
				positions[i][j] = new Point(xDisp + (boxWidth + horizontalSpacing) * j, yDisp - (boxHeight + verticalSpacing) * i);

		return positions;
	}

}
