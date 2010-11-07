package com.googlecode.reaxion.game.util;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

import com.googlecode.reaxion.game.burstgrid.info.AndrewInfo;
import com.googlecode.reaxion.game.burstgrid.info.AustinInfo;
import com.googlecode.reaxion.game.burstgrid.info.BrianInfo;
import com.googlecode.reaxion.game.burstgrid.info.CyInfo;
import com.googlecode.reaxion.game.burstgrid.info.KhoaInfo;
import com.googlecode.reaxion.game.burstgrid.info.MonicaInfo;
import com.googlecode.reaxion.game.burstgrid.info.NilayInfo;
import com.googlecode.reaxion.game.burstgrid.info.PlayerInfo;
import com.googlecode.reaxion.game.burstgrid.info.ShineInfo;

/** 
 * This class handles the creation of retrieval of {@code PlayerInfo}.
 * 
 * @author Khoa
 */

public class PlayerInfoManager {

	private static HashMap<String, PlayerInfo> map = new HashMap<String, PlayerInfo>();
	
	/**
	 * Initialize character info and mappings
	 */
	public static void init() {
		// TODO: read in info externally
		
		map.put("Andrew", new AndrewInfo());
		map.put("Austin", new AustinInfo());
		map.put("Brian", new BrianInfo());
		map.put("Cy", new CyInfo());
		map.put("Khoa", new KhoaInfo());
		map.put("Monica", new MonicaInfo());
		map.put("Nilay", new NilayInfo());
		map.put("Shine", new ShineInfo());
		
		Collection<PlayerInfo> c = map.values();
		Iterator<PlayerInfo> itr = c.iterator();
		while(itr.hasNext()) {
			itr.next().init();
		}
	}
	
	/**
	 * Returns the {@code PlayerInfo} corresponding to {@code name}.
	 */
	public static PlayerInfo get(String name) {
		return map.get(name);
	}
	
}
