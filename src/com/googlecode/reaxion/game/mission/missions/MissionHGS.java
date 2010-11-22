package com.googlecode.reaxion.game.mission.missions;

import com.googlecode.reaxion.game.mission.Mission;
import com.googlecode.reaxion.game.mission.MissionID;
import com.googlecode.reaxion.game.util.Battle;
import com.jme.math.Vector3f;

public class MissionHGS extends Mission {
	
	public MissionHGS() {
		super("Open HubGameState", MissionID.OPEN_HUBGAMESTATE, 0, false, "Opens HubGameState. For testing purposes.", "");
	}

	@Override
	public void init() {
		Battle temp = Battle.getCurrentBattle();
		temp.setP1Position(new Vector3f(0, 0, 10));
		Battle.setCurrentBattle(temp);
		
		addState(Battle.createHubGameState());
	}
	
}
