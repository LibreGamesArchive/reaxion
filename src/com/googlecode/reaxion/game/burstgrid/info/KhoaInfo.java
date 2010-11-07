package com.googlecode.reaxion.game.burstgrid.info;

/**
 * This class represents Khoa's statistics. Reasonable base stats are yet to be decided.
 * @author Cycofactory
 *
 */
public class KhoaInfo extends PlayerInfo{
	
	public KhoaInfo(){
		super();
		createBurstGrid("");
	}
	
	public KhoaInfo(int hp, int str, int minG, int maxG, int r){ //HP, Strength, MinGauge, MaxGauge, Rate
		super(hp, str, minG, maxG, r);
		createBurstGrid("");
	}
}