package com.googlecode.reaxion.game.burstgrid.info;

/**
 * This class represents Monica's statistics. Reasonable base stats are yet to be decided.
 * 
 * EDIT: I made up some base stats.
 * @author Cycofactory
 *
 */
public class MonicaInfo extends PlayerInfo{
	
	@Override
	public void init(){
		setStats(200, 2, 15, 30, 1);
		setAbilities(new String[] {"EvasiveStart"});
		setAttacks(new String[] {"ShootBullet", "ShieldBarrier", "SpinLance", "SpikeLine"});
		createBurstGrid("");
		//createBurstGrid("src/com/googlecode/reaxion/resources/burstgrid/MonicaGrid.txt");
	}
	
}