package com.googlecode.reaxion.game.burstgrid.info;

import com.googlecode.reaxion.game.audio.SoundEffectType;

/**
 * This class represents Brian's statistics. Reasonable base stats are yet to be decided.
 * 
 * @author Cycofactory
 *
 */
public class BrianInfo extends PlayerInfo {
	
	@Override
	public void init(){
		setStats(75,1,16,22,1);
		setAbilities(new String[] {"GroundStriker"});
		setAttacks(new String[] {"BombingMagnet", "LightningCloud", "LightningStorm", "LanceArc", "SpawnBubble"});
		createBurstGrid("");
		setUsableSfx();
	}

	@Override
	protected void setUsableSfx() {
		usableSfx.put(SoundEffectType.ATTACK_LIGHTNING_CLOUD, "m&lss_l-thunder.wav");
		usableSfx.put(SoundEffectType.ATTACK_LIGHTNING_STORM, "sm64_thank_you.wav");
	}
	
}