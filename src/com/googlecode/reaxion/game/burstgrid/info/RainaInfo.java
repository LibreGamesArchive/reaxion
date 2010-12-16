package com.googlecode.reaxion.game.burstgrid.info;

import com.googlecode.reaxion.game.audio.SoundEffectType;

/**
 * This class represents Raina's statistics. Reasonable base stats are yet to
 * be decided.
 * 
 * @author Khoa
 * 
 */
public class RainaInfo extends PlayerInfo {

	boolean unlocked = false;
	
	public RainaInfo() {
		super("Raina");
	}
	
	@Override
	public void init() {
		setStats(125,0,12,38,1);
		setAbilities(new String[] { "Charity", "CriticalPoint" });
		setAttacks(new String[] { "SpawnBubble", "BubbleBath", "Psywave", "Beacon", "LightningStorm" });
		createBurstGrid("");
		setUsableSfx();
	}
	
	@Override
	protected void setUsableSfx() {
		usableSfx.put(SoundEffectType.ATTACK_LIGHTNING_STORM, "sm64_thank_you.ogg");
	}
	
}