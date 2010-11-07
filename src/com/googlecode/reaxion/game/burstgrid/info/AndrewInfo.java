package com.googlecode.reaxion.game.burstgrid.info;

/**
 * This class represents Andrew's statistics. Reasonable base stats are yet to be decided.
 * @author Khoa
 *
 */
public class AndrewInfo extends PlayerInfo{
	
	@Override
	public void init(){
		setAbilities(new String[] {"RandomInstantGauge", "HighJump"});
		setAttacks(new String[] {"SlideIce", "TriLance", "LightningCloud", "BlackHole"});
		createBurstGrid("");
	}
}