package com.googlecode.reaxion.game.burstgrid.info;

/**
 * This class represents Brian's statistics. Reasonable base stats are yet to be decided.
 * @author Cycofactory
 *
 */
public class BrianInfo extends PlayerInfo{
	
	@Override
	public void init(){
		setAbilities(new String[] {"GroundStriker"});
		setAttacks(new String[] {"BombingMagnet", "LightningCloud", "LightningStorm", "LanceArc", "SpawnBubble"});
		createBurstGrid("");
	}
}