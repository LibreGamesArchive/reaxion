package com.googlecode.reaxion.game.burstgrid;

/**
 * This class represents Nilay's statistics. Reasonable base stats are yet to be decided.
 * @author Cycofactory
 *
 */
public class NilayInfo extends PlayerInfo{
	
	public NilayInfo(){
		super(200, 1, 20, 30, 1);
		createBurstGrid("");
	}
	
	public NilayInfo(int hp, int str, int minG, int maxG, int r){ //HP, Strength, MinGauge, MaxGauge, Rate
		super(hp, str, minG, maxG, r);
		createBurstGrid("");
	}
}