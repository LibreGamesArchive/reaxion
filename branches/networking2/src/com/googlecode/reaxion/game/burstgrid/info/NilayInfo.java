package com.googlecode.reaxion.game.burstgrid.info;

/**
 * This class represents Nilay's statistics. Reasonable base stats are yet to be decided.
 * 
 * @author Cycofactory
 *
 */
public class NilayInfo extends PlayerInfo {
	
	boolean unlocked = false;

	public NilayInfo() {
		super("Nilay");
	}
	
	@Override
	public void init(){
		setStats(100, 1, 14, 22, 1);
		setAbilities(new String[] {"RapidGauge"});
		setAttacks(new String[] {"CrossCut", "SpinLance", "LanceWheel", "TriLance", "LanceArc", "LanceGuard"});
		setUsableSfx();
		createBurstGrid("");
	}
	
}