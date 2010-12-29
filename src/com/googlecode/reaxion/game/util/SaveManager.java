package com.googlecode.reaxion.game.util;

import java.io.File;

import com.googlecode.reaxion.game.burstgrid.info.PlayerInfo;
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
		return BurstGridSerializer.readGrid(p);	
	}
	
	public static void saveInfo(MajorCharacter p) {
		BurstGridSerializer.saveGrid(p.info);
		System.out.println("Saved info for " + p.name);
	}
	
	public static void saveGame(HubGameState hgs) {
		saveInfo(hgs.getPlayer());
		saveInfo(hgs.getPartner());
		MissionSerializer.saveMissions();
		System.out.println("Saving complete");
	}
	
}
