package com.googlecode.reaxion.game.burstgrid.info;

/**
 * This class represents Khoa's statistics. Reasonable base stats are yet to be decided.
 * 
 * @author Cycofactory
 *
 */
public class KhoaInfo extends PlayerInfo {
	
	@Override
	public void init(){
		setStats(100,2,20,30,0);
		setAbilities(new String[] {"AfterImage", "FinalHour"});
		setAttacks(new String[] {"ShootBullet", "Beacon", "AngelRain", "ShieldMediguard", "TriLance"});
		createBurstGrid("");
	}
	
}