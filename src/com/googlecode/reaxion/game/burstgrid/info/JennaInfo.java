package com.googlecode.reaxion.game.burstgrid.info;

/**
 * This class represents Jenna's statistics. Reasonable base stats are yet to
 * be decided.
 * 
 * @author Khoa
 * 
 */
public class JennaInfo extends PlayerInfo {

	boolean unlocked = false;
	
	public JennaInfo() {
		super("Jenna");
	}
	
	@Override
	public void init() {
		setStats(125,0,12,38,1);
		setAbilities(new String[] { "MedusaEyes" });
		setAttacks(new String[] { "IcyWind", "SlideIce", "SlideDash", "SpawnBubble", "BlackHole", "SheerCold" });
		setUsableSfx();
		createBurstGrid("");
	}
	
}