package com.googlecode.reaxion.game.util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;

import com.googlecode.reaxion.game.mission.Mission;
import com.googlecode.reaxion.game.mission.MissionID;
import com.googlecode.reaxion.game.mission.MissionManager;

public class MissionSerializer implements Serializable {

	public static void saveMissions() {
		try {
			ArrayList<MissionID> completedMissions = new ArrayList<MissionID> ();
			
			for (Mission m : MissionManager.getMissions()) 
				if (m.isCompleted())
					completedMissions.add(m.getMissionID());
			
			SaveManager.checkSaveDir();
			
			ObjectOutputStream os = new ObjectOutputStream(new FileOutputStream(SaveManager.saveDir + "missions.sav"));
			os.writeObject(completedMissions);			
			os.close();	
			
			System.out.println("Saved missions");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void readMissions() {
		try {
			ObjectInputStream oi = new ObjectInputStream(new FileInputStream(SaveManager.saveDir + "missions.sav"));
			MissionManager.setCompletedMissions((ArrayList<MissionID>) oi.readObject());			
			oi.close();
		} catch (FileNotFoundException e) {
			System.out.println("No Saved Data For Missions");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
