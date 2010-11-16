package com.googlecode.reaxion.game.burstgrid.info;

/**
 * This class represents Shine's statistics. Reasonable base stats are yet to be decided.
 * 
 * @author Khoa
 *
 */
public class ShineInfo extends PlayerInfo {
	
	public ShineInfo() {
		super("Shine");
	}
	
	@Override
	public void init(){
		setStats(110,0,10,18,1);
		setAbilities(new String[] {"AfterImage", "PassiveHealer"});
		setAttacks(new String[] {"ShootBullet", "Beacon", "BubbleBath", "ShieldMediguard", "ShieldReflega", "ShieldHoly"});
		createBurstGrid("");
	}
	
}