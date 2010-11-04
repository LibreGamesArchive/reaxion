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
		BurstNode b26 = new AttackNode("!", 26);
		BurstNode b27 = new MaxGaugeNode(4, 27);
		BurstNode b28 = new RateNode(1, 28);
		BurstNode b29 = new AttackNode("!", 29);
		BurstNode b30 = new RateNode(1, 30);
		BurstNode b31 = new AttackNode("!", 31);
		BurstNode b32 = new MaxGaugeNode(3, 32);
		BurstNode b33 = new MinGaugeNode(3, 33);
		BurstNode b34 = new HPNode(150, 34);
		BurstNode b35 = new AbilityNode("!", 35);
		BurstNode b36 = new HPNode(300, 36);
		BurstNode b37 = new StrengthNode(2, 37);
		BurstNode b38 = new HPNode(250, 38);
		BurstNode b39 = new MaxGaugeNode(4, 39);
		BurstNode b40 = new AttackNode("!", 40);
		BurstNode b41 = new StrengthNode(2, 41);
		BurstNode b42 = new HPNode(250, 42);
		BurstNode b43 = new MinGaugeNode(3, 43);
		BurstNode b44 = new MaxGaugeNode(3, 44);
		BurstNode b45 = new AttackNode("!", 45);
		BurstNode b46 = new HPNode(200, 46);
		
		b1.nodes.add(b2);
		b1.cost.add(1);
		b1.nodes.add(b3);
		b1.cost.add(3);
		b1.nodes.add(b4);
		b1.cost.add(1);
		b1.nodes.add(b5);
		b1.cost.add(3);
		b1.nodes.add(b6);
		b1.cost.add(2);
		b2.nodes.add(b7);
		b2.cost.add(3);
		b2.nodes.add(b8);
		b2.cost.add(2);
		b2.nodes.add(b3);
		b2.cost.add(2);
		b2.nodes.add(b6);
		b2.cost.add(2);
		b3.nodes.add(b9);
		b3.cost.add(2);
		b3.nodes.add(b10);
		b3.cost.add(2);
		b3.nodes.add(b2);
		b3.cost.add(1);
		b3.nodes.add(b2);
		b3.cost.add(1);
		
	}
}