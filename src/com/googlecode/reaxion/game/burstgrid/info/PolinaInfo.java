package com.googlecode.reaxion.game.burstgrid.info;

import com.googlecode.reaxion.game.audio.SoundEffectType;

/**
 * This class represents Polina's statistics. Reasonable base stats are yet to
 * be decided.
 * 
 * @author Khoa
 * 
 */
public class PolinaInfo extends PlayerInfo {

	boolean unlocked = false;

	public PolinaInfo() {
		super("Polina");
	}
	
	@Override
	public void init() {
		setStats(100,3,10,27,0);
		setAbilities(new String[] { "Insurance", "FleetFooted" });
		setAttacks(new String[] { "ShootFireball", "Firestorm", "Rapture", "ShieldBarrier" });
		setUsableSfx();
		createBurstGrid("");
	}

	@Override
	protected void setUsableSfx() {
		super.setUsableSfx();
		usableSfx.put(SoundEffectType.ATTACK_FIREBALL, "test3.ogg");
	}
	
}