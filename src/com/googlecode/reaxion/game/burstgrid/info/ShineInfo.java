package com.googlecode.reaxion.game.burstgrid.info;

/**
 * This class represents Shine's statistics. Reasonable base stats are yet to be decided.
 * 
 * @author Khoa
 *
 */
public class ShineInfo extends PlayerInfo {
	
	boolean unlocked = false;

	public ShineInfo() {
		super("Shine");
	}
	
	@Override
	public void init(){
		setStats(110,0,10,30,1);
		setAbilities(new String[] {"Charity", "PassiveHealer"});
		setAttacks(new String[] {"Bastion", "Beacon", "Guardian", "ShieldMediguard", "ShieldReflega", "ShieldHoly"});
		createBurstGrid("");
	}
	
}