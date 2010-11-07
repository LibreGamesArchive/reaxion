package com.googlecode.reaxion.game.burstgrid.info;

/**
 * This class represents Brian's statistics. Reasonable base stats are yet to be decided.
 * @author Cycofactory
 *
 */
public class BrianInfo extends PlayerInfo{
	
	public BrianInfo(){
		super();
		createBurstGrid("");
	}
	
	public BrianInfo(int hp, int str, int minG, int maxG, int r){ //HP, Strength, MinGauge, MaxGauge, Rate
		super(hp, str, minG, maxG, r);
		createBurstGrid("");
	}
}