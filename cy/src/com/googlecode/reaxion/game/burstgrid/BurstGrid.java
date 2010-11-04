package com.googlecode.reaxion.game.burstgrid;

import java.io.File;
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
		readGrid("../../com/googlecode/reaxion/resources/burstgrid/BurstGrid1.txt");
	}

	/**
	 * Returns the Burst Grid
	 * @return
	 */
	public ArrayList<BurstNode> getBurstGrid(){
		return bg;
	}

	private void readGrid(String filePath){
		String line, type, abilityName;
		int id, statboost, attachedID = 1, cost, totalNodes = 0;
		ArrayList<BurstNode> nextNodes = new ArrayList<BurstNode>();
		ArrayList<String> conns = new ArrayList<String>();
		BurstNode b;

		try {
			Scanner read = new Scanner(new File(filePath));
			line = read.nextLine();
			while(line.charAt(0)== '|'){
				line = read.nextLine();
			}
			totalNodes = read.nextInt();
			BurstNode[] allNodes = new BurstNode[totalNodes];
			while(read.hasNext()){
				line = read.nextLine();
				type = read.next();

				if(!line.equals("")){
					String[] temp = line.split("|");
					String[] node = temp[0].split(" ");
					conns.add(temp[1]);
					
					if(node[1].contains("Max")){
						b = new MaxGaugeNode(Integer.parseInt(node[3]), Integer.parseInt(node[0]));
					}
					if(node[1].contains("HP")){
						b = new MaxGaugeNode(Integer.parseInt(node[3]), Integer.parseInt(node[0]));
					}
					else if(node[1].contains("Min")){
						b = new MinGaugeNode(Integer.parseInt(node[3]), Integer.parseInt(node[0]));
					}
					else if(node[1].contains("Attack")){
						b = new AttackNode(node[3], Integer.parseInt(node[0]));
					}
					else if(node[1].contains("Strength")){
						b = new StrengthNode(Integer.parseInt(node[3]), Integer.parseInt(node[0]));
					}
					else if(node[1].contains("Ability")){
						b = new AbilityNode(node[3], Integer.parseInt(node[0]));
					}
					else{
						b = new RateNode(Integer.parseInt(node[3]), Integer.parseInt(node[0]));
					}
					bg.add(b);					
				}
				for(int i = 0; i < conns.size(); i++){
					String[] temp = conns.get(i).split(" ");
					for(int j = 0; j < temp.length; j++){
						String[] c = temp[j].split(",");
						bg.get(i).addConnection(bg.get(Integer.parseInt(c[0])), Integer.parseInt(c[1]));
					}
				}
			}

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void printGrid(){
		for(BurstNode b: bg){
			for(BurstNode c:b.nodes){
				c.print();
			}
			System.out.println();
		}
	}
}