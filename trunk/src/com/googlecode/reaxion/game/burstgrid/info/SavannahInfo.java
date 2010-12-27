package com.googlecode.reaxion.game.burstgrid.info;

/**
 * This class represents Savannah's statistics. Reasonable base stats are yet to
 * be decided.
 * 
 * @author Khoa
 * 
 */
public class SavannahInfo extends PlayerInfo {

	boolean unlocked = false;

	public SavannahInfo() {
		super("Savannah");
	}
	
	@Override
	public void init() {
		setStats(100,3,10,15,0);
		setAbilities(new String[] { "HighJump", "Inheritor" });
		setAttacks(new String[] { "Whirlwind", "Teleport", "TriLance", "CrossCut", "SpikeLine" });
		createBurstGrid("");
		setUsableSfx();
	}
	
}