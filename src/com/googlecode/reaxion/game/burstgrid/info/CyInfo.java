package com.googlecode.reaxion.game.burstgrid.info;

import com.googlecode.reaxion.game.audio.SoundEffectType;

/**
 * This class represents Cy's statistics. Reasonable base stats are yet to be decided.
 * 
 * @author Cycofactory
 *
 */
public class CyInfo extends PlayerInfo {
	
	boolean unlocked = false;

	public CyInfo() {
		super("Cy");
	}
	
	@Override
	public void init(){
		setStats(100,1,15,20,0);
		setAbilities(new String[] {"Chivalry", "Masochist"});
		setAttacks(new String[] {"CometPunch", "SpikeLine", "ShadowTag", "BlackHole", "DarkHand", "ShadowAura"});
		setUsableSfx();
		createBurstGrid("");
	}

	@Override
	protected void setUsableSfx() {
		super.setUsableSfx();
		//usableSfx.put(SoundEffectType.ATTACK_BLACK_HOLE, "test3.ogg");
	}
	
}