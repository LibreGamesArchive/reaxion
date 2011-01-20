package com.googlecode.reaxion.game.overlay;

import java.awt.Color;

import com.googlecode.reaxion.game.burstgrid.info.PlayerInfo;
import com.googlecode.reaxion.game.input.bindings.KeyBindings;
import com.googlecode.reaxion.game.util.FontUtils;
import com.jme.math.Vector3f;
import com.jme.renderer.ColorRGBA;
import com.jme.scene.shape.Quad;
import com.jmex.angelfont.BitmapText;

public class StatsOverlay extends MenuOverlay {

	public static final String NAME = "statsOverlay";
	
	private PlayerInfo player;
	private PlayerInfo partner;
	
	private String [][] abilities;
	private String[][] attacks;
	private String [] hp;
	private String [] strength;
	private String [] gauge;
	private String [] refill;
	
	private BitmapText[] names;
	private BitmapText[][] abT;
	private BitmapText[][] atT;
	private BitmapText[] hpT;
	private BitmapText[] stT;
	private BitmapText[] gaT;
	private BitmapText[] reT;
	private BitmapText[] abHeader;
	private BitmapText[] atHeader;
	private ColorRGBA white;
	private Quad asd;
	
	
	
	public StatsOverlay(PlayerInfo player, PlayerInfo partner) {
		super(NAME, 800, 600, false);
		this.player = player;
		this.partner = partner;
		
		
		init();
	}
	
	private void init() {
		abilities = new String[2][Math.max(player.getAbilities().length, partner.getAbilities().length)];
		attacks = new String[2][Math.max(player.getAttacks().length, partner.getAttacks().length)];
		hp = new String[2];
		strength = new String[2];
		gauge = new String[2];
		refill = new String[2];
		hpT = new BitmapText[2];
		stT = new BitmapText[2];
		gaT = new BitmapText[2];
		reT = new BitmapText[2];
		abT = new BitmapText[2][Math.max(player.getAbilities().length, partner.getAbilities().length)];
		atT = new BitmapText[2][Math.max(player.getAttacks().length, partner.getAttacks().length)];
		abHeader = new BitmapText[2];
		atHeader = new BitmapText[2];
		
		for(int i = 0; i<player.getAbilities().length; i++)
			abilities[0][i] = player.getAbilities()[i];
		for(int i = 0; i<player.getAttacks().length; i++)
			attacks[0][i] = player.getAttacks()[i];
		for(int i = 0; i<partner.getAbilities().length; i++)
			abilities[1][i] = partner.getAbilities()[i];
		for(int i = 0; i<partner.getAttacks().length; i++)
			attacks[1][i] = partner.getAttacks()[i];
		
		hp[0] = "HP: " + player.getMaxHp();
		hp[1] = "HP: " + partner.getMaxHp();
		strength[0] = "Strength: " + player.getStrength();
		strength[1] = "Strength: " + partner.getStrength();
		gauge[0] = "Gauge: " + player.getMaxGauge();
		gauge[1] = "Gauge: " + partner.getMaxGauge();
		refill[0] = "Refill rate: " + player.getGaugeRate();
		refill[1] = "Refill rate: " + partner.getGaugeRate();
		
		white = new ColorRGBA(255,255,255,100);
		
		names = new BitmapText[2];
		names[0] = new BitmapText(FontUtils.neuropol, false);
		names[0].setDefaultColor(white);
		names[0].setText("Player: " + player.name);
		names[1] = new BitmapText(FontUtils.neuropol, false);
		names[1].setDefaultColor(white);
		names[1].setText("Partner: " + partner.name);
		
		if(container == null)
			System.out.println("asdasdasdasudgfgfgfgfgfasd");
		asd = drawRect(width, height, new ColorRGBA(0,0,0,80));
		asd.setLocalTranslation(new Vector3f(width/2, height/2, 0));
		attachChild(asd);
		
		
		for(int i = 0; i < 2; i++)
		{
			hpT[i] = new BitmapText(FontUtils.neuropol, false);
			stT[i] = new BitmapText(FontUtils.neuropol, false);
			gaT[i] = new BitmapText(FontUtils.neuropol, false);
			reT[i] = new BitmapText(FontUtils.neuropol, false);
			abHeader[i] = new BitmapText(FontUtils.neuropol, false);
			atHeader[i] = new BitmapText(FontUtils.neuropol, false);
			hpT[i].setDefaultColor(white);
			stT[i].setDefaultColor(white);
			gaT[i].setDefaultColor(white);
			reT[i].setDefaultColor(white);
			abHeader[i].setDefaultColor(white);
			atHeader[i].setDefaultColor(white);
			hpT[i].setText(hp[i]);
			stT[i].setText(strength[i]);
			gaT[i].setText(gauge[i]);
			reT[i].setText(refill[i]);
			abHeader[i].setText("Abilities:");
			atHeader[i].setText("Attacks:");
			hpT[i].setLocalTranslation(new Vector3f(15+i*width/2, height-25, 0));
			stT[i].setLocalTranslation(new Vector3f(15+i*width/2, height-45, 0));
			gaT[i].setLocalTranslation(new Vector3f(15+i*width/2, height-65, 0));
			reT[i].setLocalTranslation(new Vector3f(15+i*width/2, height-85, 0));
			abHeader[i].setLocalTranslation(new Vector3f(5+i*width/2, height-130, 0));
			atHeader[i].setLocalTranslation(new Vector3f(5+i*width/2, height-280, 0));
			names[i].setLocalTranslation(new Vector3f(5+i*width/2, height-5, 0));
			hpT[i].setSize(16);
			stT[i].setSize(16);
			gaT[i].setSize(16);
			reT[i].setSize(16);
			abHeader[i].setSize(18);
			atHeader[i].setSize(18);
			hpT[i].update();
			stT[i].update();
			gaT[i].update();
			reT[i].update();
			abHeader[i].update();
			atHeader[i].update();
			names[i].setSize(18);
			names[i].update();
			
			attachChild(abHeader[i]);
			attachChild(atHeader[i]);
			attachChild(names[i]);
			attachChild(hpT[i]);
			attachChild(stT[i]);
			attachChild(gaT[i]);
			attachChild(reT[i]);
		}
		
		for(int i = 0; i < 2; i++)
			for(int j = 0; j < abT[0].length; j++)
			{
				abT[i][j] = new BitmapText(FontUtils.neuropol, false);
				abT[i][j].setDefaultColor(white);
				if(abilities[i][j] != null)
					abT[i][j].setText(abilities[i][j]);
				abT[i][j].setLocalTranslation(new Vector3f(20+i*width/2, height-150-j*16, 0));
				abT[i][j].setSize(14);
				abT[i][j].update();
				attachChild(abT[i][j]);
			}
		
		for(int i = 0; i < 2; i++)
			for(int j = 0; j < atT[0].length; j++)
			{
				atT[i][j] = new BitmapText(FontUtils.neuropol, false);
				atT[i][j].setDefaultColor(white);
				if(attacks[i][j] != null)
					atT[i][j].setText(attacks[i][j]);
				atT[i][j].setLocalTranslation(new Vector3f(20+i*width/2, height-300-j*16, 0));
				atT[i][j].setSize(14);
				atT[i][j].update();
				attachChild(atT[i][j]);
			}
		
		

		updateRenderState();

		
	}


	
	@Override
	public void updateDisplay(KeyBindings k) {
		
	}

}
