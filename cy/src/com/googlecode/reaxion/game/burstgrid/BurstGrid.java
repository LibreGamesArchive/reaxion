package com.googlecode.reaxion.game.burstgrid;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Scanner;

/** 
 * This class represents the Burst Grid and handles all the functionality of it. The Burst Grid holds
 * BurstNodes of type HPNode, GaugeNode, or AbilityNode
 * 
 * @author Cy Neita
 */

public class BurstGrid
{
	private ArrayList<BurstNode> bg; // the entire Burst Grid

	public BurstGrid(){
		bg = new ArrayList<BurstNode>();
	}

	/**
	 * Returns the Burst Grid
	 * @return
	 */
	public ArrayList<BurstNode> getBurstGrid(){
		return bg;
	}

	private void readGrid(String filePath){
		String s, type, abilityName;
		int id, statboost, attachedID = 1, cost, totalNodes = 0;
		ArrayList<Integer[]> nextNodes = new ArrayList<Integer[]>();
		BurstNode b;

		try {
			Scanner read = new Scanner(new FileReader(filePath + ".txt"));
			s = read.nextLine();
			while(s.charAt(0)== '|'){
				s = read.nextLine();
			}
			totalNodes = read.nextInt();
			BurstNode[] allNodes = new BurstNode[totalNodes];
			while(read.hasNext()){
				id = read.nextInt();
				type = read.next();
				if(type.contains("Max")){
					statboost = read.nextInt();
					b = new MaxGaugeNode(statboost, id);
					allNodes[id-1] = b;
				}
				else if(type.contains("Min")){
					statboost = read.nextInt();
					b = new MinGaugeNode(statboost, id);
					allNodes[id-1] = b;
				}
				else if(type.contains("Attack")){
					abilityName = read.next();
					b = new AttackNode(abilityName, id);
					allNodes[id-1] = b;
				}
				else if(type.contains("Strength")){
					statboost = read.nextInt();
					b = new StrengthNode(statboost, id);
					allNodes[id-1] = b;
				}
				else if(type.contains("Ability")){
					abilityName = read.next();
					b = new AbilityNode(abilityName, id);
					allNodes[id-1] = b;
				}
				else{
					statboost = read.nextInt();
					b = new MinGaugeNode(statboost, id);
					allNodes[id-1] = b;
				}
				while(attachedID != 0){
					attachedID = read.nextInt();
					read.next();
					cost = read.nextInt();
					nextNodes.add(new Integer[] {attachedID, cost});
				}
			nextNodes.clear();
			}
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}