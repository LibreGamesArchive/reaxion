package com.googlecode.reaxion.tools.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Scanner;

public class ToolUtils {

	private static final String infoDir = "src/com/googlecode/reaxion/tools/info/";
	
	private static ArrayList<String> characterNames;
	
	public static void initialize() {
		readCharacterNames();
	}
	
	private static void readCharacterNames() {
		try {
			Scanner reader = new Scanner(new File(infoDir + "character list.txt"));
			
			String[] chars = reader.nextLine().split(",");
			characterNames = new ArrayList<String>();
			
			for (String c : chars)
				characterNames.add(c);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static ArrayList<String> getCharacterNames() {
		return characterNames;
	}	
	
}
