package com.googlecode.reaxion.game.overlay;

import java.awt.Point;
import java.util.ArrayList;

import com.googlecode.reaxion.game.mission.Mission;
import com.googlecode.reaxion.game.mission.MissionManager;
import com.googlecode.reaxion.game.util.FontUtils;
import com.jme.math.Vector3f;
import com.jme.renderer.ColorRGBA;
import com.jme.scene.Node;
import com.jme.scene.shape.Quad;
import com.jme.system.DisplaySystem;
import com.jmex.angelfont.BitmapFont;
import com.jmex.angelfont.BitmapText;

public class MissionOverlay extends GridOverlay {

	private static final String baseURL = "../../resources/icons/missionselect/";
	private static final String baseGuiURL = "../../resources/gui/";
	
	private static final int missionListItemWidth = 300;
	private static final int missionListItemHeight = 80;
	private static final int missionListItemSpacing = 15;
	
	private static int numListItems;
	
	private ArrayList<Mission> missions;
	
	private Node[] missionList;
	
	private int currentIndex;
	
	private Quad cursor;
	
	private Point[][] missionListGrid;
	
	public MissionOverlay() {
		super(800, 600, false);
		init();
	}                         
	
	private void init() {
		System.out.println("initializing mission overlay");
		numListItems = baseHeight / (missionListItemHeight + missionListItemSpacing);
		
		container = new Node("container_missionSelect");
		missions = MissionManager.getMissions();
		
		System.out.println("##### " + container);
		
		createMissionList();
		
		currentIndex = 0;
		
		cursor = getImage(baseURL + "missionselect_cursor.png");
		Point cursorPos = missionListGrid[numListItems / 2][0];
		cursor.setLocalTranslation(cursorPos.x, cursorPos.y, 0);
		
		container.updateRenderState();
		container.setLocalScale((float) DisplaySystem.getDisplaySystem()
				.getHeight()
				/ baseHeight);
		container.setLocalTranslation(baseWidth / 2, baseHeight / 2, 0);
		
		attachChild(container);
	}
	
	private void createMissionList() {
		missionListGrid = createVerticallyCenteredGrid(numListItems, 1, 750 - 150,
				missionListItemWidth, missionListItemHeight,
				missionListItemSpacing, missionListItemSpacing);
		
		missionList = new Node[missions.size()];
		
//		for (Mission m : missions)
//			System.out.println(m);
		
		for(int i = 0; i < missionList.length; i++) {
			missionList[i] = createMissionListItem(missions.get(i));
			Point pos = missionListGrid[(i + numListItems / 2) % numListItems][0];
			missionList[i].setLocalTranslation(pos.x, pos.y, 0);
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
		
		float titleX = (150 - 10 - id.getLineWidth() - 10) / 2;
		
		id.setLocalTranslation(-150 + id.getLineWidth() / 2 + 10, id.getLineHeight() / 2, 0);
		title.setLocalTranslation(titleX, 30, 0);
		
		listItem.attachChild(box);
		listItem.attachChild(id);
		listItem.attachChild(title);
		
		for (int i = 0; i < m.getDifficultyRating(); i++) {
			Quad star = getImage(baseGuiURL + "star_small.png");
			star.setLocalTranslation(-60 + (star.getWidth() + 5) * i, -20, 0);
			listItem.attachChild(star);
		}
		
		return listItem;
	}

	public void showMenu() {
		container.attachChild(cursor);
		
		for (Node n : missionList)
			container.attachChild(n);
		
		updateRenderState();
	}

	public void hideMenu() {
		container.detachChild(cursor);
		
		for (Node n : missionList)
			container.detachChild(n);
		
		updateRenderState();
	}
	
	public void updateDisplay(int key) {
		
	}
	
	private void rotateMissionList() {
		
	}

}
