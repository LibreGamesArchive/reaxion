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
	PlayerInfo pinf;
	int ex = 0;
	
	public BurstGridSerializer(){
		
	}
	
	public BurstGridSerializer(PlayerInfo p) throws IOException{
		pinf = p;
		saveGrid(pinf);
	}
	
	private void saveGrid(PlayerInfo p) throws IOException{
		FileOutputStream fs = new FileOutputStream("src/com/googlecode/reaxion/resources/burstgrid/grids/" + p.name + ".bgs");
		ObjectOutputStream os = new ObjectOutputStream(fs);
		for(BurstNode b: p.getBurstGrid().getNodes()){
			if(b.activated)
				activatedNodes.add(b.id);
		}
		ex = p.exp;
		os.writeInt(ex);
		os.writeObject(activatedNodes);
	}
	
	public void readGrid(String name) throws IOException, ClassNotFoundException{
		ObjectInputStream oi = new ObjectInputStream(new FileInputStream("src/com/googlecode/reaxion/resources/burstgrid/grids/" + name + ".bgs"));
		ex = oi.readInt();
		Object o = oi.readObject();
		ArrayList<Integer> aNodes = (ArrayList<Integer>)o;
		
		pinf.exp = ex;
		ArrayList<BurstNode> nodes = pinf.getBurstGrid().getNodes();
		for(int i: aNodes)
			nodes.get(i).activated = true;			
	}
}