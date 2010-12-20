package com.googlecode.reaxion.game.burstgrid.info;

import com.googlecode.reaxion.game.audio.SoundEffectType;

/**
 * This class represents Andrew's statistics. Reasonable base stats are yet to
 * be decided.
 * 
 * @author Khoa
 * 
 */
public class AndrewInfo extends PlayerInfo {

	boolean unlocked = false;
	
	public AndrewInfo() {
		super("Andrew");
	}
	
	@Override
	public void init() {
		setStats(125,0,12,38,1);
		setAbilities(new String[] { "RandomInstantGauge", "HighJump" });
		setAttacks(new String[] { "CardScreen", "CardCircle", "Cartomancy", "CardThrow", "Psywave", "LightningCloud" });
		createBurstGrid("");
		setUsableSfx();
	}

	@Override
	protected void setUsableSfx() {
		usableSfx.put(SoundEffectType.ATTACK_LIGHTNING_CLOUD, "lightning bolt.ogg");
	}
	
}