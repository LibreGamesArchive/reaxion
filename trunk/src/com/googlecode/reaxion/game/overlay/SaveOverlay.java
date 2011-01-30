package com.googlecode.reaxion.game.overlay;

import java.io.File;
import java.util.ArrayList;

import com.googlecode.reaxion.game.input.bindings.KeyBindings;
import com.googlecode.reaxion.game.state.HubGameState;
import com.googlecode.reaxion.game.util.FontUtils;
import com.googlecode.reaxion.game.util.SaveManager;
import com.jme.math.Vector3f;
import com.jme.renderer.ColorRGBA;
import com.jme.scene.Node;
import com.jme.scene.shape.Quad;
import com.jmex.angelfont.BitmapText;

public class SaveOverlay extends MenuOverlay{

	public static final String NAME = "SaveOverlay";

	private HubGameState hgs;
	
	private ColorRGBA textColor;
	private ColorRGBA selTextColor;

	private Quad bg;
	private ScrollMenu savesList;

	private static final String baseURL = "saves/";

	ArrayList<File> saveFiles;
	ArrayList<String> fileNames;

	public SaveOverlay(){
		super("SaveOverlay", 800, 600, true);

		init();
	}
	
	public SaveOverlay(HubGameState hgs){
		super("SaveOverlay", 800, 600, true);

		this.hgs = hgs;
		init();
	}

	private void init(){
		bg = drawRect(width, height, new ColorRGBA(0,0,0,.67f));
		bg.setLocalTranslation(new Vector3f(width/2, height/2, 0));
		attachChild(bg);

		// create a container Node for scaling
		container = new Node("container_saveSelect");
		attachChild(container);

		// Colors
		textColor = FontUtils.unselected;
		selTextColor = FontUtils.blueSelected;

		getSaveFiles();

		BitmapText saveLabel = new BitmapText(FontUtils.neuropol, false);
		saveLabel.setText("Save Files");
		saveLabel.setSize(18);
		saveLabel.update();
		saveLabel.setLocalTranslation(new Vector3f(width*3/4 - 80, height*1/2 + 190, 0));
		container.attachChild(saveLabel);
		
		String[] fileNamesString = new String[fileNames.size()];
		for(int i = 0; i < fileNames.size(); i++)
			fileNamesString[i] = fileNames.get(i).substring(0, fileNames.get(i).lastIndexOf('.'));
		savesList = new ScrollMenu(160, 40, 5, fileNamesString);
		savesList.enableScrollBar();
		savesList.setLocalTranslation(new Vector3f(width*3/4, height*1/2, 0));
		container.attachChild(savesList);
	}

	private void getSaveFiles(){
		File saveFolder = new File(baseURL);
		if(!saveFolder.exists())
			saveFolder.mkdir();
		File[] filesList = saveFolder.listFiles();
		saveFiles = new ArrayList<File>();
		fileNames = new ArrayList<String>();

		if(filesList.length > 0)
			for (int i = 0; i < filesList.length; i++) {
				if (filesList[i].isFile()) 
					if(filesList[i].getName().contains(".sav")){
						saveFiles.add(filesList[i]);
						fileNames.add(filesList[i].getName());
					}
			}
		else{
			File f = SaveManager.newSave();
			saveFiles.add(f);
			fileNames.add(f.getName());
		}
	}

	@Override
	public void updateDisplay(KeyBindings k) {
		// TODO Auto-generated method stub

	}
}