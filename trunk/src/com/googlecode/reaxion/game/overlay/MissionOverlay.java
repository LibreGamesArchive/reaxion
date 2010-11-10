package com.googlecode.reaxion.game.overlay;

import java.awt.GridLayout;
import java.util.ArrayList;

import com.googlecode.reaxion.game.mission.Mission;
import com.googlecode.reaxion.game.mission.MissionManager;
import com.googlecode.reaxion.game.util.FontUtils;
import com.jme.scene.Node;
import com.jmex.angelfont.BitmapText;

public class MissionOverlay extends GridLayout {

	private ArrayList<Mission> missions;
	
	public MissionOverlay() {
		missions = MissionManager.getMissions();
	}                         
	
	private Node createMissionListItem(Mission m) {
		Node listItem = new Node("listItem_" + m.getTitle());
		
		BitmapText id = new BitmapText(FontUtils.neuropol, false);
		id.setText("No. " + m.getMissionID());
		
		BitmapText title = new BitmapText(FontUtils.neuropol, false);
		title.setText(m.getTitle());
		
		return listItem;
	}
}
