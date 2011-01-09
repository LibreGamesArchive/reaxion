package com.googlecode.reaxion.game.burstgrid.info;

/**
 * This class represents Monica's statistics. Reasonable base stats are yet to be decided.
 * 
 * EDIT: I made up some base stats.
 * @author Cycofactory
 *
 */
public class MonicaInfo extends PlayerInfo {
	
	public MonicaInfo() {
		super("Monica");
	}

	@Override
	public void init(){
		setStats(80, 1, 15, 25, 1);
		//exp = 10; // testing value to buy nodes. go crazy.
		setAbilities(new String[] {"TriggerFingers", "Inheritor"});
		setAttacks(new String[] {"ShootBullet", "BulletWave", "BulletStorm", "SlideDash", "OmegaShot", "ShieldBarrier"});
		//createBurstGrid("");
		setUsableSfx();
		createBurstGrid("src/com/googlecode/reaxion/resources/burstgrid/MonicaGrid.txt");
		
		/**
		The following is to be implemented later on, once all the characters have Grids and can choose 
		their abilities
		*/
		
		//readStatsFromGrid();
		//getBurstGrid().activateNode(1);
	}
	
}