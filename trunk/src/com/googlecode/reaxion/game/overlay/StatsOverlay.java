package com.googlecode.reaxion.game.overlay;

import com.googlecode.reaxion.game.burstgrid.info.PlayerInfo;
import com.googlecode.reaxion.game.input.bindings.KeyBindings;

public class StatsOverlay extends MenuOverlay {

	public static final String NAME = "statsOverlay";
	
	private PlayerInfo player;
	private PlayerInfo partner;
	
	public StatsOverlay(PlayerInfo player, PlayerInfo partner) {
		super(NAME, 800, 600, false);
		this.player = player;
		this.partner = partner;
		
		init();
	}
	
	private void init() {
		
	}

	@Override
	public void updateDisplay(KeyBindings k) {
		
	}

}
