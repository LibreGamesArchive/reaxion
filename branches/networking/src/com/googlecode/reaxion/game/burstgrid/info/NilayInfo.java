package com.googlecode.reaxion.game.burstgrid.info;

/**
 * This class represents Nilay's statistics. Reasonable base stats are yet to be decided.
 * 
 * @author Cycofactory
 *
 */
public class NilayInfo extends PlayerInfo {
	
	@Override
	public void init(){
		setStats(200, 1, 20, 30, 1);
		setAbilities(new String[] {"RapidGauge"});
		setAttacks(new String[] {"SpinLance", "LanceWheel", "TriLance", "LanceArc", "LanceGuard"});
		createBurstGrid("");
	}
	
}