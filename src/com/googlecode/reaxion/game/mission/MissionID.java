package com.googlecode.reaxion.game.mission;

/**
 * {@code MissionID} is a set of mission names each containing an {@code int} id.
 * This {@code enum} is used in lieu of an {@code int} in the {@code MissionManager} 
 * {@code startMission} method for programmer clarity.
 * 
 * @author Brian Clanton
 *
 */

public enum MissionID {

	OPEN_BURSTGRID(-2),
	OPEN_HUBGAMESTATE(-1),
	DEFEAT_LIGHT_USER(1),
	VS_TOYBOX (2);
	
	public int id;
	
	private MissionID(int id) {
		this.id = id;
	}
	
}
