package com.googlecode.reaxion.game.mission.missions;

import com.googlecode.reaxion.game.mission.Mission;
import com.googlecode.reaxion.game.mission.MissionID;
import com.googlecode.reaxion.game.state.HubGameState;
import com.googlecode.reaxion.game.util.Battle;

public class MissionHGS extends Mission {
	
	public MissionHGS() {
		super("Open HubGameState", MissionID.OPEN_HUBGAMESTATE.id, 0, false, "");
	}

	@Override
	public void init() {
		addState(new HubGameState(Battle.getCurrentBattle()));
	}
	
}
