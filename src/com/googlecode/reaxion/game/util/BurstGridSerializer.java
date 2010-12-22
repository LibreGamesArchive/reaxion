package com.googlecode.reaxion.game.util;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;

import com.googlecode.reaxion.game.burstgrid.info.PlayerInfo;
import com.googlecode.reaxion.game.burstgrid.node.BurstNode;


public class BurstGridSerializer implements Serializable{

	ArrayList<BurstNode> burstGrid = new ArrayList<BurstNode>();
	ArrayList<Integer> activatedNodes = new ArrayList<Integer>();
	int exp = 0;
	
	public BurstGridSerializer(){
		
	}
	
	public BurstGridSerializer(PlayerInfo p) throws IOException{
		saveGrid(p);
	}
	
	private void saveGrid(PlayerInfo p) throws IOException{
		FileOutputStream fs = new FileOutputStream("src/com/googlecode/reaxion/resources/burstgrid/grids/" + p.name + ".bgs");
		ObjectOutputStream os = new ObjectOutputStream(fs);
		for(BurstNode b: p.getBurstGrid().getNodes()){
			if(b.activated)
				activatedNodes.add(b.id);
			exp = p.exp;
		os.writeObject(activatedNodes);
		os.writeInt(exp);
		}
	}
	
	public void readGrid(String name) throws IOException, ClassNotFoundException{
		ObjectInputStream oi = new ObjectInputStream(new FileInputStream("src/com/googlecode/reaxion/resources/burstgrid/grids/" + name + ".bgs"));
		ArrayList<Integer> aNodes = (ArrayList<Integer>)oi.readObject();
		int ex = oi.readInt();
		System.out.println(aNodes);
		System.out.println(ex);
	}
}