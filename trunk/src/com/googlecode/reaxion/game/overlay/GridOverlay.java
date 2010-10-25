package com.googlecode.reaxion.game.overlay;

import java.awt.Point;

public class GridOverlay extends Overlay {

	public int screenHeight, screenWidth;

	public GridOverlay() {
		super();
	}

	protected Point[][] createVerticallyCenteredGrid(int rows, int columns, int xDisp, int boxHeight, int boxWidth, int horizontalSpacing, int verticalSpacing) {
		Point[][] positions = new Point[rows][columns];

		int totalGridSpaceY = rows * boxHeight + (rows - 1) * verticalSpacing;
		int yDisp = screenHeight - (screenHeight - totalGridSpaceY) / 2;

		for (int i = 0; i < rows; i++)
			for (int j = 0; j < columns; j++)
				positions[i][j] = new Point(xDisp + (boxWidth + horizontalSpacing) * j, yDisp - (boxHeight + verticalSpacing) * i);

		return positions;
	}

	protected Point[][] createHorizontallyCenteredGrid(int rows, int columns, int yDisp, int boxHeight, int boxWidth, int horizontalSpacing, int verticalSpacing) {
		Point[][] positions = new Point[rows][columns];

		int totalGridSpaceX = columns * boxWidth + (columns - 1) * horizontalSpacing;
		int xDisp = screenWidth - (screenWidth - totalGridSpaceX) / 2;

		for (int i = 0; i < rows; i++)
			for (int j = 0; j < columns; j++)
				positions[i][j] = new Point(xDisp + (boxWidth + horizontalSpacing) * j, yDisp - (boxHeight + verticalSpacing) * i);

		return positions;
	}

	protected Point[][] createCenteredGrid(int rows, int columns, int boxHeight, int boxWidth, int horizontalSpacing, int verticalSpacing) {
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
