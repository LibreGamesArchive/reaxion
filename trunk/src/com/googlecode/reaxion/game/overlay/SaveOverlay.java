package com.googlecode.reaxion.game.overlay;

import java.io.File;
import java.util.ArrayList;

import com.googlecode.reaxion.game.input.bindings.KeyBindings;
import com.googlecode.reaxion.game.util.FontUtils;
import com.jme.math.Vector3f;
import com.jme.renderer.ColorRGBA;
import com.jme.scene.Node;
import com.jme.scene.shape.Quad;
import com.jmex.angelfont.BitmapText;

public class SaveOverlay extends MenuOverlay{

	public static final String NAME = "SaveOverlay";
	private ColorRGBA textColor;
	private ColorRGBA selTextColor;

	private static final String baseURL = "com/googlecode/reaxion/saves/";

	ArrayList<File> saveFiles;
	ArrayList<String> fileNames;

	public SaveOverlay(){
		super(NAME, 800, 600, true);
		// create a container Node for scaling
		container = new Node("container_saveSelect");

		// Colors
		textColor = FontUtils.unselected;
		selTextColor = FontUtils.blueSelected;

		getSaveFiles();
		
		//create the visual saves list
		Quad[] saves = new Quad[saveFiles.size()];
		for(int i = 0; i< saves.length; i++){
			saves[i] = drawRect(405, 40, ColorRGBA.black);
			saves[i].setLocalTranslation(new Vector3f(400f,100-40*i,0));
			container.attachChild(saves[i]);
		}
		
		//create the save file names
		BitmapText[] savesText = new BitmapText[saveFiles.size()];
		for(int i = 0; i< savesText.length; i++){
			savesText[i] = new BitmapText(FontUtils.neuropol, false);
			//savesText[i].setLocalTranslation(new Vector3f(400f,100-40*i,0));
			container.attachChild(savesText[i]);
		}
	}

	private void getSaveFiles(){
		File saveFolder = new File(baseURL);
		File[] filesList = saveFolder.listFiles();
		saveFiles = new ArrayList<File>();
		fileNames = new ArrayList<String>();
		
		for (int i = 0; i < filesList.length; i++) {
			if (filesList[i].isFile()) 
				if(filesList[i].getName().contains(".sav")){
					saveFiles.add(filesList[i]);
					fileNames.add(filesList[i].getName());
				}
		}
	}

	@Override
	public void updateDisplay(KeyBindings k) {
		// TODO Auto-generated method stub

	}
}