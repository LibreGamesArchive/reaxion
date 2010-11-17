package com.googlecode.reaxion.game.mission.missions;

import com.googlecode.reaxion.game.mission.Mission;
import com.googlecode.reaxion.game.mission.MissionID;
import com.googlecode.reaxion.game.model.character.Monica;
import com.googlecode.reaxion.game.state.BurstGridGameState;

public class MissionBurstGrid extends Mission {

	public MissionBurstGrid() {
		super("Open BurstGrid", MissionID.OPEN_BURSTGRID.id, 0, false, "Opens BurstGrid for testing purposes.", "");
	}

	@Override
	public void init() {
		Monica m = new Monica();
		addState(new BurstGridGameState(m.info));
	}
	
}
