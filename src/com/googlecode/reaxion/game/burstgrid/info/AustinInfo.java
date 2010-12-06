package com.googlecode.reaxion.game.burstgrid.info;

import com.googlecode.reaxion.game.audio.SoundEffectType;

/**
 * This class represents Austin's statistics. Reasonable base stats are yet to
 * be decided.
 * 
 * @author Cycofactory
 * 
 */
public class AustinInfo extends PlayerInfo {

	boolean unlocked = false;

	public AustinInfo() {
		super("Austin");
	}
	
	@Override
	public void init() {
		setStats(100,3,10,15,0);
		setAbilities(new String[] { "RapidGauge" });
		setAttacks(new String[] { "BombingMagnet", "ShootFireball",
				"SpawnBubble", "LanceGuard" });
		createBurstGrid("");
		setUsableSfx();
	}

	@Override
	protected void setUsableSfx() {
		usableSfx.put(SoundEffectType.ATTACK_FIREBALL, "test3.ogg");
	}
	
}