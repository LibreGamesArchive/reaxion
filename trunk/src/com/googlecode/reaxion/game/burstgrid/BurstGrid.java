package com.googlecode.reaxion.game.burstgrid;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

import com.googlecode.reaxion.game.burstgrid.node.AbilityNode;
import com.googlecode.reaxion.game.burstgrid.node.AttackNode;
import com.googlecode.reaxion.game.burstgrid.node.BurstNode;
import com.googlecode.reaxion.game.burstgrid.node.HPNode;
import com.googlecode.reaxion.game.burstgrid.node.MaxGaugeNode;
import com.googlecode.reaxion.game.burstgrid.node.MinGaugeNode;
import com.googlecode.reaxion.game.burstgrid.node.RateNode;
import com.googlecode.reaxion.game.burstgrid.node.StrengthNode;
import com.jme.math.FastMath;
import com.jme.math.Vector3f;

/** 
 * This class represents the Burst Grid and handles all the functionality of it. The Burst Grid holds
 * BurstNodes of type HPNode, GaugeNode, or AbilityNode
 * 
 * @author Cy Neita
 */

public class BurstGrid
{
	private ArrayList<BurstNode> bg; // the entire Burst Grid
	String gridType; // refers to the 3-dimensional organization of the grid's nodes. The type is the name of the person. 

	public BurstGrid(){
		bg = new ArrayList<BurstNode>();
	}

	public BurstGrid(String filePath){
		bg = new ArrayList<BurstNode>();
		readGrid(filePath);
	}

	/**
	 * Returns an ArrayList of the nodes in BurstGrid
	 * @return
	 */
	public ArrayList<BurstNode> getNodes(){
		return bg;
	}
	
	/**
	 * Counts the number of nodes that fit the specified parameters.
	 * @param type HP, Max, Min, Attack, Strength, Ability, or Rate
	 * @param active Whether node must be activated or not to count
	 * @return Integer number of nodes that fit criteria
	 */
	public int getNodeCount(String type, boolean active) {
		int count = 0;
		
		for(BurstNode a: bg) {
			if ((type == "HP" && a instanceof HPNode) ||
					(type == "Max" && a instanceof MaxGaugeNode) ||
					(type == "Min" && a instanceof MinGaugeNode) ||
					(type == "Attack" && a instanceof AttackNode) ||
					(type == "Strength" && a instanceof StrengthNode) ||
					(type == "Ability" && a instanceof AbilityNode) ||
					(type == "Rate" && a instanceof RateNode))
				count += ((active && a.activated) || !active)? 1 : 0;
		}
		
		return count;
	}

	private void readGrid(String filePath){
		String line;
		ArrayList<String> conns = new ArrayList<String>();
		BurstNode b;

		try {
			File f = new File(filePath);
			if (f.exists()) {

				Scanner read = new Scanner(f);
				line = read.nextLine();
				while(line.charAt(0)== '|'){
					line = read.nextLine();
				}
				gridType = line;
				while(read.hasNext()){
					line = read.nextLine();

					if(!line.equals("")){
						String[] temp = line.split("-");
						String[] node = temp[0].split(" ");
						conns.add(temp[1]);

						
						if(node[1].contains("Max")){
							b = new MaxGaugeNode(Integer.parseInt(node[2]), Integer.parseInt(node[0]));
						}
						else if(node[1].contains("HP")){
							b = new HPNode(Integer.parseInt(node[2]), Integer.parseInt(node[0]));
						}
						else if(node[1].contains("Min")){
							b = new MinGaugeNode(Integer.parseInt(node[2]), Integer.parseInt(node[0]));
						}
						else if(node[1].contains("Attack")){
							b = new AttackNode(node[2], Integer.parseInt(node[0]));
						}
						else if(node[1].contains("Strength")){
							b = new StrengthNode(Integer.parseInt(node[2]), Integer.parseInt(node[0]));
						}
						else if(node[1].contains("Ability")){
							b = new AbilityNode(node[2], Integer.parseInt(node[0]));
						}
						else{
							b = new RateNode(Integer.parseInt(node[2]), Integer.parseInt(node[0]));
						}
						
						// Checks for root node
						if (Integer.parseInt(node[0]) == 1)
							b.activated = true;
						
						bg.add(b);	
					}
				}
				for(int i = 0; i < conns.size(); i++){
					String[] temp = conns.get(i).split(" ");
					for(int j = 0; j < temp.length; j++){
						String[] c = temp[j].split(",");
						bg.get(i).addConnection(bg.get(Integer.parseInt(c[0])-1), Integer.parseInt(c[1]));
					}
				}
			} 
			
			//This section creates the vectors for the 3D representation of the grid
			//The first node of each grid is located at <0,0,0>
			for(BurstNode a: bg){
				if(gridType.contains("Monica")){
					float ang1 = 2*FastMath.PI/5; //The angular increment for first tier nodes
					float ang2 = FastMath.PI/6; //The angular increment for second tier nodes
					float ang3 = FastMath.PI/9; //The angular increment for third tier nodes
					int l1 = 2; //The length of the first tier paths
					int l2 = 2; //The length of the second tier paths
					int l3 = 2; //The length of the third tier paths
					
					if(a.id==1)
						a.setVect(new Vector3f(0f,0f,0f));
					
					else if(a.id<=6)
						a.setVect(new Vector3f(FastMath.cos(ang1*(a.id-2))*l1,FastMath.sin(ang1*(a.id-2))*l1,1));
					
					else if(a.id<=16){
						Vector3f prevVec = bg.get((int)FastMath.ceil(a.id/2f)-3).vect;
						float prevAng = FastMath.atan2(prevVec.y, prevVec.x);
						
						if(a.id%2==1)
							a.setVect(new Vector3f(prevVec.getX()+FastMath.cos(prevAng-ang2)*l2, prevVec.getY()+FastMath.sin(prevAng-ang2)*l2,3));
						else
							a.setVect(new Vector3f(prevVec.getX()+FastMath.cos(prevAng+ang2)*l2, prevVec.getY()+FastMath.sin(prevAng+ang2)*l2,3));
					}
					else{
						Vector3f prevVec = bg.get((int)FastMath.ceil(a.id/3f-.5f)).vect;
						float prevAng = FastMath.atan2(prevVec.y, prevVec.x);
						
						if(a.id%3==2){
							a.setVect(new Vector3f(prevVec.getX()+FastMath.cos(prevAng-ang3)*l3, prevVec.getY()+FastMath.sin(prevAng-ang3)*l3,3));
						}
						else if(a.id%3==0){
							a.setVect(new Vector3f(prevVec.getX()+FastMath.cos(prevAng)*l3, prevVec.getY()+FastMath.sin(prevAng)*l3,3));
						}
						else{
							a.setVect(new Vector3f(prevVec.getX()+FastMath.cos(prevAng+ang3)*l3, prevVec.getY()+FastMath.sin(prevAng+ang3)*l3,3));
						}
					}
				}
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public String toString(){
		String s = "";
		for(BurstNode b: bg){
			s += "" + b.id + ": ";
			for(BurstNode c:b.nodes){
				s += c.toString();
				s += "(" + b.costs.get(b.nodes.indexOf(c)) + ") ";
			}
			s += "\n";
		}
		return s;
	}
}