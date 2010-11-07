package com.googlecode.reaxion.game.burstgrid.info;

/**
 * This class represents Cy's statistics. Reasonable base stats are yet to be decided.
 * @author Cycofactory
 *
 */
public class CyInfo extends PlayerInfo{
	
	@Override
	public void init(){
		setAbilities(new String[] {"Chivalry", "Masochist"});
		setAttacks(new String[] {"SpikeLine", "ShadowTag", "BlackHole", "SpinLance"});
		createBurstGrid("");
	}
}