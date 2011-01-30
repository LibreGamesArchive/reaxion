package com.googlecode.reaxion.game.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

import com.googlecode.reaxion.game.burstgrid.info.PlayerInfo;
import com.googlecode.reaxion.game.burstgrid.node.BurstNode;
import com.googlecode.reaxion.game.mission.MissionManager;
import com.googlecode.reaxion.game.model.character.MajorCharacter;
import com.googlecode.reaxion.game.state.HubGameState;

public class SaveManager {

	public static final String saveDir = "saves/";

	public static void checkSaveDir() {
		File check = new File(saveDir);

		if (!check.exists())
			check.mkdir();
	}

	public static PlayerInfo loadInfo(PlayerInfo p) {
		return readGrid(p);	
	}

	public static void saveInfo(MajorCharacter p) {
		saveGrid(p.info);
		System.out.println("Saved info for " + p.name);
	}

	public static void saveGame(HubGameState hgs) {
		PlayerInfo p1 = hgs.getPlayer().info;
		PlayerInfo p2 = hgs.getPartner().info;
		ArrayList<Integer> activatedNodes = new ArrayList<Integer>();
		HashMap<String, PlayerInfo> map = PlayerInfoManager.getMap();
		
		try {
			FileOutputStream fs;
			fs = new FileOutputStream(saveDir + hgs.getSaveName() + ".sav");
			ObjectOutputStream os = new ObjectOutputStream(fs);

			String stage = hgs.getStage().getClass().getName();
			stage = stage.substring(stage.lastIndexOf(".")+1);
			os.writeObject(stage);
			
			os.writeObject(p1.name);
			os.writeObject(p2.name);

			Collection<PlayerInfo> c = map.values();
			Iterator<PlayerInfo> itr = c.iterator();
			while(itr.hasNext()){
				PlayerInfo p = itr.next();
				if(p.getBurstGrid() != null)
					for(BurstNode b: p.getBurstGrid().getNodes())
						if(b.activated)
							activatedNodes.add(b.id);

				os.writeObject(p.name);
				os.writeInt(p.exp);
				os.writeBoolean(p.unlockFlag);
				os.writeObject(activatedNodes);
			}
			
			System.out.println("Game saved!");

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		//		MissionSerializer.saveMissions();
		//		System.out.println("Saving complete");
	}

	public static void loadGame(HashMap<String, PlayerInfo> map, String saveName){
		boolean error = false;
		
		String name;
		String stage;
		String activeCharacter1, activeCharacter2;

		try {
			ObjectInputStream oi;
			oi = new ObjectInputStream(new FileInputStream(saveDir + saveName + ".sav"));
			
			stage = (String)oi.readObject();
			
			activeCharacter1 = (String)oi.readObject();
			activeCharacter2 = (String)oi.readObject();		
			
			//			String p1name = (String)oi.readObject();
			//			int p1exp = oi.readInt();
			//			boolean unlocked1 = oi.readBoolean();
			//			ArrayList<Integer> activatedNodes1 = (ArrayList<Integer>)oi.readObject();
			//			
			//			String p2name = (String)oi.readObject();
			//			int p2exp = oi.readInt();
			//			boolean unlocked2 = oi.readBoolean();
			//			ArrayList<Integer> activatedNodes2 = (ArrayList<Integer>)oi.readObject();

			for(int i =0; i < map.size(); i++){
				name = (String)oi.readObject();
				PlayerInfo temp = (PlayerInfo)map.get(name);
				temp.exp = oi.readInt();
				temp.unlockFlag = oi.readBoolean();
				ArrayList<Integer> activatedNodes = (ArrayList<Integer>)oi.readObject();
				for(int j: activatedNodes)
					temp.getBurstGrid().activateNode(j);
				temp.init();
				temp.readStatsFromGrid();
				System.out.println(name + " " + temp.unlockFlag + " Exp: " + temp.exp + " " + activatedNodes.toString());
			}

			System.out.println(stage+", "+activeCharacter1+", "+activeCharacter2);
			
			Battle.setDefaultStage(stage);
			Battle.setDefaultPlayers(activeCharacter1, activeCharacter2);
			MissionManager.startHubGameState();

			System.out.println("Read Saved Data from " + saveName);

		} catch (FileNotFoundException e) {
			error = true;			
			System.out.println("No Saved Data For: " + saveName);
		} catch (Exception e) {
			error = true;
			System.out.println("Error Reading Saved Data: " + saveName);
		}
		
		if (error) {		
			Collection<PlayerInfo> c = map.values();
			Iterator<PlayerInfo> itr = c.iterator();
			while(itr.hasNext()) {
				PlayerInfo temp = itr.next();
				temp.init();
				temp = SaveManager.loadInfo(temp);
				temp.readStatsFromGrid();
			}
			LoadingQueue.resetQueue();
			loadDefaults();
		}
	}

	public static void saveGrid(PlayerInfo p) {
		try {
			ArrayList<Integer> activatedNodes = new ArrayList<Integer>();
			FileOutputStream fs;
			fs = new FileOutputStream(saveDir + p.name + ".sav");
			ObjectOutputStream os = new ObjectOutputStream(fs);

			for(BurstNode b: p.getBurstGrid().getNodes())
				if(b.activated)
					activatedNodes.add(b.id);

			os.writeInt(p.exp);
			os.writeObject(activatedNodes);
			System.out.println("Saved Info For: " + p.name);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static PlayerInfo readGrid(PlayerInfo p) {		
		try {
			ObjectInputStream oi;
			oi = new ObjectInputStream(new FileInputStream(saveDir + p.name + ".bgs"));
			p.exp = oi.readInt();
			Object o = oi.readObject();
			ArrayList<Integer> aNodes = (ArrayList<Integer>) o;
			ArrayList<BurstNode> nodes = p.getBurstGrid().getNodes();
			for(int i: aNodes)
				nodes.get(i).activated = true;	
			System.out.println("Read Saved Data For: " + p.name);
		} catch (FileNotFoundException e) {
			System.out.println("No Saved Data For: " + p.name);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return p;
	}
	
	private static void loadDefaults() {
		// TODO: Detect defaults based on flags
		
		Battle.setDefaultStage("FlowerField");
		Battle.setDefaultPlayers("Monica", "Nilay");
		MissionManager.startHubGameState();
	}
	
	public static File newSave(){
		ArrayList<Integer> activatedNodes = new ArrayList<Integer>();
		HashMap<String, PlayerInfo> map = PlayerInfoManager.getMap();
		
		try {
			FileOutputStream fs;
			fs = new FileOutputStream(saveDir + "SaveState.sav");
			ObjectOutputStream os = new ObjectOutputStream(fs);

			String stage = "FlowerField";
			os.writeObject(stage);
			
			os.writeObject("Monica");
			os.writeObject("Nilay");

			Collection<PlayerInfo> c = map.values();
			Iterator<PlayerInfo> itr = c.iterator();
			while(itr.hasNext()){
				PlayerInfo p = itr.next();
				if(p.getBurstGrid() != null)
					for(BurstNode b: p.getBurstGrid().getNodes())
						if(b.activated)
							activatedNodes.add(b.id);

				os.writeObject(p.name);
				os.writeInt(p.exp);
				os.writeBoolean(p.unlockFlag);
				os.writeObject(activatedNodes);
			}
			
			System.out.println("Game saved!");

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new File(saveDir + "SaveState.sav");
	}
}
