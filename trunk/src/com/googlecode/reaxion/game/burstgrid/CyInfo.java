package com.googlecode.reaxion.game.burstgrid;

/**
 * This class represents Cy's statistics. Reasonable base stats are yet to be decided.
 * @author Cycofactory
 *
 */
public class CyInfo extends PlayerInfo{
	
	public CyInfo(){
		super();
		createBurstGrid("");
	}
	
	public CyInfo(int hp, int str, int minG, int maxG, int r){ //HP, Strength, MinGauge, MaxGauge, Rate
		super(hp, str, minG, maxG, r);
		createBurstGrid("");
	}
}