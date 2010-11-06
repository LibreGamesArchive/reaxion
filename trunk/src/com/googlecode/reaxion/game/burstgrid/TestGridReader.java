package com.googlecode.reaxion.game.burstgrid;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Scanner;

/** 
 * This class just tests to see if the BurstGrid can be read from a text file correctly
 * 
 * @author Cy Neita
 */

public class TestGridReader{
	
	BurstGrid bg;
	
	public static void main(String[] args){
		new TestGridReader();
	}
	
	public TestGridReader(){
		bg = new BurstGrid("src/com/googlecode/reaxion/resources/burstgrid/MonicaGrid.txt");
		bg.printGrid();
	}
}