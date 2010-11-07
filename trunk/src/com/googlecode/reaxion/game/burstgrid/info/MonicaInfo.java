package com.googlecode.reaxion.game.burstgrid.info;

/**
 * This class represents Monica's statistics. Reasonable base stats are yet to be decided.
 * 
 * EDIT: I made up some base stats.
 * @author Cycofactory
 *
 */
public class MonicaInfo extends PlayerInfo{
	public MonicaInfo(){
		super(200, 2, 15, 30, 1);
		createBurstGrid("src/com/googlecode/reaxion/resources/burstgrid/MonicaGrid.txt");
	}
	
	public MonicaInfo(int hp, int str, int minG, int maxG, int r){ //HP, Strength, MinGauge, MaxGauge, Rate
		super(hp, str, minG, maxG, r);
		createBurstGrid("src/com/googlecode/reaxion/resources/burstgrid/MonicaGrid.txt");
	}
}