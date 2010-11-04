package com.googlecode.reaxion.game.burstgrid;

/**
 * This class represents Monica's statistics. Reasonable base stats are yet to be decided.
 * @author Cycofactory
 *
 */
public class MonicaInfo extends PlayerInfo{
	
	public MonicaInfo(){
		super();
	}
	
	public MonicaInfo(int hp, int str, int minG, int maxG, int r){
		super(hp, str, minG, maxG, r);
	}
	
	private void createBurstGrid(){
		BurstNode b1 = new AttackNode("!", 1);
		BurstNode b2 = new HPNode(100, 2);
		BurstNode b3 = new RateNode(1, 3);
		BurstNode b4 = new StrengthNode(1, 4);
		BurstNode b5 = new AttackNode("!", 5);
		BurstNode b6 = new MinGaugeNode(2, 6);
		BurstNode b7 = new AbilityNode("!", 7);
		BurstNode b8 = new MaxGaugeNode(2, 8);
		BurstNode b9 = new StrengthNode(1, 9);
		BurstNode b10 = new HPNode(150, 10);
		BurstNode b11 = new HPNode(200, 11);
		BurstNode b12 = new AttackNode("!", 12);
		BurstNode b13 = new MaxGaugeNode(3, 13);
		BurstNode b14 = new MinGaugeNode(3, 14);
		BurstNode b15 = new AttackNode("!", 15);
		BurstNode b16 = new RateNode(1, 16);
		BurstNode b17 = new HPNode(200, 17);
		BurstNode b18 = new StrengthNode(1, 18);
		BurstNode b19 = new RateNode(1, 19);
		BurstNode b20 = new MinGaugeNode(4, 20);
		BurstNode b21 = new AttackNode("!", 21);
		BurstNode b22 = new AbilityNode("!", 22);
		BurstNode b23 = new MaxGaugeNode(4, 23);
		BurstNode b24 = new MinGaugeNode(3, 24);
		BurstNode b25 = new HPNode(200, 25);
	}
}