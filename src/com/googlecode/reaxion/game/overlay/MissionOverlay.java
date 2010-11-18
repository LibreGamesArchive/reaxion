package com.googlecode.reaxion.game.overlay;

import java.awt.Point;
import java.util.ArrayList;

import com.googlecode.reaxion.game.mission.Mission;
import com.googlecode.reaxion.game.mission.MissionManager;
import com.googlecode.reaxion.game.util.FontUtils;
import com.jme.renderer.ColorRGBA;
import com.jme.scene.Node;
import com.jme.scene.shape.Quad;
import com.jme.system.DisplaySystem;
import com.jmex.angelfont.BitmapFont;
import com.jmex.angelfont.BitmapText;

public class MissionOverlay extends GridOverlay {

	private static String baseURL = "../../resources/icons/missionselect/";
	
	private ArrayList<Mission> missions;
	
	private Node container;
	private Node[] missionList;
	
	private Point[][] missionListGrid;
	
	public MissionOverlay() {
		super(800, 600, false);
		init();
	}                         
	
	private void init() {
		container = new Node("container_missionSelect");
		missions = MissionManager.getMissions();
		
		createMissionList();
		
		container.updateRenderState();
		container.setLocalScale((float) DisplaySystem.getDisplaySystem()
				.getHeight()
				/ screenHeight);
		container.setLocalTranslation(screenWidth / 2, screenHeight / 2, 0);
		
		attachChild(container);
	}
	
	private void createMissionList() {
		missionListGrid = createVerticallyCenteredGrid(missions.size(), 1, 750 - 150, 300, 100, 15, 15);
		missionList = new Node[missions.size()];
		
//		for (Mission m : missions)
//			System.out.println(m);
		
		for(int i = 0; i < missionList.length; i++) {
			missionList[i] = createMissionListItem(missions.get(i));
			Point pos = missionListGrid[i][0];
			missionList[i].setLocalTranslation(pos.x, pos.y, 0);
			container.attachChild(missionList[i]);
		}
	}

	private Node createMissionListItem(Mission m) {
		Node listItem = new Node("listItem_" + m.getTitle());
		
		Quad box = new Quad("box_" + m.getMissionID(), 300, 80);
		box.setSolidColor(ColorRGBA.black);
		
		BitmapText id = new BitmapText(FontUtils.eurostile, false);
		id.setText("No. " + (m.getMissionID()));
		id.setSize(24);
		id.setAlignment(BitmapFont.Align.Center);
		id.update();
		
		BitmapText title = new BitmapText(FontUtils.eurostile, false);
		title.setText(m.getTitle());
		title.setSize(18);
		title.setAlignment(BitmapFont.Align.Center);
		title.update();
		
//		Quad image = getImage(baseURL + "question_listitem.png");
		
//		image.setLocalTranslation((box.getHeight() - image.getHeight()) / 2 + image.getWidth() / 2 - 150, 0, 0);
		id.setLocalTranslation(-150 + id.getLineWidth() / 2 + 20, 30 - id.getLineHeight() / 2, 0);
		title.setLocalTranslation(id.getLocalTranslation().x + 100, 25 - title.getLineHeight() / 2, 0);
		
		listItem.attachChild(box);
		listItem.attachChild(id);
		listItem.attachChild(title);
//		listItem.attachChild(image);
		
		for (int i = 0; i < m.getDifficultyRating(); i++) {
			Quad star = getImage(baseURL + "star_small.png");
			star.setLocalTranslation(id.getLocalTranslation().x + id.getLineWidth() / 2 + 55 * i, -20, 0);
			listItem.attachChild(star);
		}
		
		return listItem;
	}
	
}
