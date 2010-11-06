package com.googlecode.reaxion.game.burstgrid;

/**
 * This class represents Austin's statistics. Reasonable base stats are yet to be decided.
 * @author Cycofactory
 *
 */
public class AustinInfo extends PlayerInfo{
	
	public AustinInfo(){
		super();
		createBurstGrid("");
	}
	
	public AustinInfo(int hp, int str, int minG, int maxG, int r){ //HP, Strength, MinGauge, MaxGauge, Rate
		super(hp, str, minG, maxG, r);
		createBurstGrid("");
	}
}