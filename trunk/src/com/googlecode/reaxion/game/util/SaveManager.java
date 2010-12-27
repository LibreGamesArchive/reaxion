package com.googlecode.reaxion.game.util;

import com.googlecode.reaxion.game.burstgrid.info.PlayerInfo;
import com.googlecode.reaxion.game.model.character.MajorCharacter;

public class SaveManager {
	
	public static PlayerInfo loadInfo(PlayerInfo p) {
		return BurstGridSerializer.readGrid(p);	
	}
	
	public static void saveInfo(MajorCharacter p) {
		BurstGridSerializer.saveGrid(p.info);
	}
	
}
