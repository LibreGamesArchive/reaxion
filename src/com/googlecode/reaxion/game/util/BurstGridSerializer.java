package com.googlecode.reaxion.game.util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;

import com.googlecode.reaxion.game.burstgrid.info.PlayerInfo;
import com.googlecode.reaxion.game.burstgrid.node.BurstNode;

public class BurstGridSerializer implements Serializable{
	
	public static void saveGrid(PlayerInfo p) {
		try {
			ArrayList<Integer> activatedNodes = new ArrayList<Integer>();
			FileOutputStream fs;
			fs = new FileOutputStream("src/com/googlecode/reaxion/resources/burstgrid/grids/" + p.name + ".bgs");
			ObjectOutputStream os = new ObjectOutputStream(fs);
			
			for(BurstNode b: p.getBurstGrid().getNodes())
				if(b.activated)
					activatedNodes.add(b.id);
			
			os.writeInt(p.exp);
			os.writeObject(activatedNodes);
			System.out.println("Saved Info For: " + p.name);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public static PlayerInfo readGrid(PlayerInfo p) {		
		try {
			ObjectInputStream oi;
			oi = new ObjectInputStream(new FileInputStream("src/com/googlecode/reaxion/resources/burstgrid/grids/" + p.name + ".bgs"));
			p.exp = oi.readInt();
			Object o = oi.readObject();
			ArrayList<Integer> aNodes = (ArrayList<Integer>) o;
			ArrayList<BurstNode> nodes = p.getBurstGrid().getNodes();
			for(int i: aNodes)
				nodes.get(i).activated = true;	
			System.out.println("Read Saved Data For: " + p.name);
		} catch (FileNotFoundException e) {
			System.out.println("No Saved Data For: " + p.name);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return p;
	}
}