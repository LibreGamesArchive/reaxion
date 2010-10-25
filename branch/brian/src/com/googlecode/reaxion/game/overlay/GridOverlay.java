package com.googlecode.reaxion.game.overlay;

import java.awt.Point;

public class GridOverlay extends Overlay {

	public int screenHeight, screenWidth;

	public GridOverlay() {
		super();
	}

	protected Point[][] createVerticallyCenteredGrid(int rows, int columns, int xDisp, int boxHeight, int boxWidth, int horizontalSpacing, int verticalSpacing) {
		Point[][] positions = new Point[rows][columns];
		
		int totalGridSpace = rows * boxHeight + (rows - 1) * verticalSpacing;
		int yDisp = screenHeight - (screenHeight - totalGridSpace) / 2;
		
		for (int i = 0; i < rows; i++)
			for (int j = 0; j < columns; j++)
				positions[i][j] = new Point(xDisp + (boxWidth + horizontalSpacing) * j, yDisp - (boxHeight + verticalSpacing) * i);
		
		return positions;
	}
	
}
