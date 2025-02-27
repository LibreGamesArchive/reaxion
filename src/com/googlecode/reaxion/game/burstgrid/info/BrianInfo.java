package com.googlecode.reaxion.game.burstgrid.info;

import com.googlecode.reaxion.game.audio.SoundEffectType;

/**
 * This class represents Brian's statistics. Reasonable base stats are yet to be decided.
 * 
 * @author Cycofactory
 *
 */
public class BrianInfo extends PlayerInfo {
	
	boolean unlocked = false;

	public BrianInfo() {
		super("Brian");
	}
	
	@Override
	public void init(){
		setStats(75,1,16,34,1);
		setAbilities(new String[] {"GroundStriker"});
		setAttacks(new String[] {"MagneticWorld", "LightningCloud", "LightningStorm", "Railgun", "BombingMagnet", "Jolt"});
		createBurstGrid("");
		setUsableSfx();
	}

	@Override
	protected void setUsableSfx() {
		super.setUsableSfx();
		usableSfx.put(SoundEffectType.ATTACK_LIGHTNING_CLOUD, "m&lss_l-thunder.ogg");
		usableSfx.put(SoundEffectType.ATTACK_LIGHTNING_STORM, "sm64_thank_you.ogg");
	}
	
}