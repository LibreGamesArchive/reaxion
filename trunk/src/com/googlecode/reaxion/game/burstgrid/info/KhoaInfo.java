package com.googlecode.reaxion.game.burstgrid.info;

/**
 * This class represents Khoa's statistics. Reasonable base stats are yet to be decided.
 * 
 * @author Cycofactory
 *
 */
public class KhoaInfo extends PlayerInfo {
	
	boolean unlocked = false;

	public KhoaInfo() {
		super("Khoa");
	}
	
	@Override
	public void init(){
		setStats(100,2,20,42,0);
		setAbilities(new String[] {"AfterImage", "FinalHour"});
		setAttacks(new String[] {"Rapture", "Floodlight", "AngelRain", "Oblivion", "Stopga", "Teleport"});
		setUsableSfx();
		createBurstGrid("");
	}
	
}