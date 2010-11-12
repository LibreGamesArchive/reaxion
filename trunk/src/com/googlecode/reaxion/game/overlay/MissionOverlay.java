package com.googlecode.reaxion.game.overlay;

import java.util.ArrayList;

import com.googlecode.reaxion.game.mission.Mission;
import com.googlecode.reaxion.game.mission.MissionManager;
import com.googlecode.reaxion.game.util.FontUtils;
import com.jme.renderer.ColorRGBA;
import com.jme.scene.Node;
import com.jme.scene.shape.Quad;
import com.jme.system.DisplaySystem;
import com.jmex.angelfont.BitmapText;

public class MissionOverlay extends GridOverlay {

	private static String baseURL = "../../resources/icons/missionselect/";
	
	private ArrayList<Mission> missions;
	
	private Node container;
	private Node[] missionList;
	
	public MissionOverlay() {
		super();
		init();
	}                         
	
	private void init() {
		container = new Node("container_missionSelect");
		missions = MissionManager.getMissions();

		screenWidth = 800;
		screenHeight = 600;
		
		createMissionList();
		createShade();
		
		container.updateRenderState();
		container.setLocalScale((float) DisplaySystem.getDisplaySystem()
				.getHeight()
				/ screenHeight);
		
		attachChild(container);
	}
	
	private void createShade() {
		Quad shade = new Quad("shade", screenWidth, screenHeight);
		shade.setSolidColor(new ColorRGBA(1f, 1f, 1f, .75f));
		shade.setZOrder(1);
		container.attachChild(shade);
	}
	
	private void createMissionList() {
		missionList = new Node[missions.size()];
		
		for(int i = 0; i < missionList.length; i++) {
			missionList[i] = createMissionListItem(missions.get(i));
			container.attachChild(missionList[i]);
		}
	}

	private Node createMissionListItem(Mission m) {
		Node listItem = new Node("listItem_" + m.getTitle());
		
		BitmapText id = new BitmapText(FontUtils.neuropol, false);
		id.setText("No. " + (m.getMissionID()));
		id.update();
		
		BitmapText title = new BitmapText(FontUtils.neuropol, false);
		title.setText(m.getTitle());
		
		Quad image = getImage(baseURL + "question_listitem.png");
		
		listItem.attachChild(id);
		listItem.attachChild(title);
		listItem.attachChild(image);
		
		return listItem;
	}
	
}
