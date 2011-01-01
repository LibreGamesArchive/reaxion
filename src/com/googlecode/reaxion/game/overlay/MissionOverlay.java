package com.googlecode.reaxion.game.overlay;

import java.awt.Point;
import java.util.ArrayList;

import com.googlecode.reaxion.game.Reaxion;
import com.googlecode.reaxion.game.input.bindings.KeyBindings;
import com.googlecode.reaxion.game.input.bindings.MenuBindings;
import com.googlecode.reaxion.game.mission.Mission;
import com.googlecode.reaxion.game.mission.MissionManager;
import com.googlecode.reaxion.game.util.FontUtils;
import com.jme.input.KeyInput;
import com.jme.renderer.ColorRGBA;
import com.jme.scene.Node;
import com.jme.scene.shape.Quad;
import com.jmex.angelfont.BitmapFont;
import com.jmex.angelfont.BitmapText;

/**
 * {@code MissionOverlay} is the mission menu for {@code HubGameState}. It controls all of the visual elements
 * of the menu and allows for the selection and initiation of missions.
 * 
 * @author Brian Clanton
 *
 */
public class MissionOverlay extends MenuOverlay {

	public static final String NAME = "missionOverlay";
	
	private static final String baseURL = "com/googlecode/reaxion/resources/icons/missionselect/";
	private static final String baseGuiURL = "com/googlecode/reaxion/resources/gui/";
	
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
		super(NAME, 800, 600, false);
		init();
	}                         
	
	/**
	 * Initializes the mission menu.
	 */
	private void init() {
		container = new Node("container_missionSelect");
		
		missions = MissionManager.getMissions();
		
		createMissionList();
	
		cursor = getImage(baseURL + "missionselect_cursor.png");
		Point cursorPos = missionListGrid[numListItems / 2][0];
		cursor.setLocalTranslation(cursorPos.x, cursorPos.y, 0);
		container.attachChild(cursor);		
		
		container.updateRenderState();
		container.setLocalScale((float) Reaxion.getScreenHeight() / baseHeight);
		container.setLocalTranslation(Reaxion.getScreenWidth() / 2, Reaxion.getScreenHeight() / 2, 0);
		
		attachChild(container);
	}
	
	/**
	 * Generates an {@code Array} of {@code Node} objects which graphically display mission 
	 * information.
	 */
	private void createMissionList() {
		numListItems = Math.min(baseHeight / (missionListItemHeight + missionListItemSpacing) - 1, missions.size());
		missionListGrid = createVerticallyCenteredGrid(numListItems, 1, 750 - 150,
				missionListItemWidth, missionListItemHeight,
				missionListItemSpacing, missionListItemSpacing);
		
		missionList = new Node[missions.size()];
		
		for(int i = 0; i < missionList.length; i++) {
			missionList[i] = createMissionListItem(missions.get(i), i);
			missionList[i].setLocalTranslation(1000, 0, 0);
			container.attachChild(missionList[i]);
		}	
		
		currentIndex = missionList.length - numListItems / 2;
		rotateMissionList(null);
	}

	/**
	 * Creates a single mission list item.
	 * 
	 * @param m {@code Mission} object that contains information for the mission list item
	 * @param index Current index
	 * @return A mission list item
	 */
	private Node createMissionListItem(Mission m, int index) {
		Node listItem = new Node("listItem_" + m.getTitle());
		
		Quad box = new Quad("box_" + m.getMissionID(), 300, 80);
		box.setSolidColor(ColorRGBA.black);
		
		BitmapText id = new BitmapText(FontUtils.eurostile, false);
		id.setText("No. " + m.getMissionIDNum());
		id.setSize(24);
		id.setAlignment(BitmapFont.Align.Center);
		id.update();
		
		BitmapText title = new BitmapText(FontUtils.eurostile, false);
		title.setText(m.getTitle());
		title.setSize(18);
		title.setAlignment(BitmapFont.Align.Center);
		title.update();
		
//		BitmapText test = new BitmapText(FontUtils.eurostile, false);
//		test.setText("" + index);
//		test.setSize(24);
//		test.setAlignment(BitmapFont.Align.Center);
//		test.update();
		
		Quad checkbox = getImage(baseURL + "checkbox-" + (m.isCompleted() ? "1" : "0") + ".png");
		
		id.setLocalTranslation(-150 + id.getLineWidth() / 2 + 10, id.getLineHeight() / 2, 0);
		title.setLocalTranslation(20, 30, 0);
//		test.setLocalTranslation(100, 0, 0);
		checkbox.setLocalTranslation(-60 + 5 * (32 + 5), -17, 0);
		
		listItem.attachChild(box);
		listItem.attachChild(id);
		listItem.attachChild(title);
//		listItem.attachChild(test);
		listItem.attachChild(checkbox);
		
		for (int i = 0; i < m.getDifficultyRating(); i++) {
			Quad star = getImage(baseGuiURL + "star_small.png");
			star.setLocalTranslation(-60 + (star.getWidth() + 5) * i, -20, 0);
			listItem.attachChild(star);
		}
		
		return listItem;
	}
	
	/**
	 * Updates the locations of each of the mission list items based on key input.
	 * @param key The keycode for the key that was pressed
	 */
	private void rotateMissionList(KeyBindings k) {
		if (k == MenuBindings.DOWN) {
			int index = currentIndex - 1;
			if (index < 0)
				index += missionList.length;
			missionList[index].setLocalTranslation(1000, 0, 0);
		} else if (k == MenuBindings.UP) {
			missionList[(currentIndex + numListItems) % missionList.length].setLocalTranslation(1000, 0, 0);
		}
		
		for (int i = 0; i < numListItems; i++) {
			Point pos = missionListGrid[i][0];
			missionList[(currentIndex + i) % missionList.length].setLocalTranslation(pos.x, pos.y, 0);
//			System.out.print((currentIndex + i) % missionList.length + (i == numListItems - 1 ? "" : ","));
		}
	}
	
	public void startSelectedMission() {
		int index = (currentIndex + numListItems / 2) % missions.size();
		MissionManager.startMission(missions.get(index).getMissionID());
	}
	
	@Override
	public void updateDisplay(KeyBindings k) {
		if (k == MenuBindings.DOWN)
			currentIndex = (currentIndex + 1) % missionList.length;
		else if (k == MenuBindings.UP) {
			currentIndex -= 1;
			if (currentIndex < 0)
				currentIndex += missionList.length;
		}
		
		rotateMissionList(k);
		
		container.updateRenderState();
	}

}
