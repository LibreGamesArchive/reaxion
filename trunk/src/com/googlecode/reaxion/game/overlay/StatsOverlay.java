package com.googlecode.reaxion.game.overlay;

import java.util.ArrayList;

import com.googlecode.reaxion.game.ability.Ability;
import com.googlecode.reaxion.game.attack.Attack;
import com.googlecode.reaxion.game.burstgrid.info.PlayerInfo;
import com.googlecode.reaxion.game.input.bindings.KeyBindings;
import com.googlecode.reaxion.game.util.FontUtils;
import com.jme.math.Vector3f;
import com.jme.renderer.ColorRGBA;
import com.jme.scene.Node;
import com.jme.scene.shape.Quad;
import com.jme.system.DisplaySystem;
import com.jmex.angelfont.BitmapText;
import com.jmex.angelfont.BitmapFont.Align;

public class StatsOverlay extends MenuOverlay {

	public static final String NAME = "statsOverlay";
	private static final String baseURL = "com/googlecode/reaxion/resources/icons/characterselect/";
	
	private PlayerInfo player;
	private PlayerInfo partner;
	
	private Quad bg;
	private BitmapText[] abilityText = new BitmapText[2];
	private BitmapText[] attackText = new BitmapText[2];
	private Node[] column = new Node[2];
	
	private String[][] abilities = new String[2][];
	private String[][] attacks = new String[2][];
	
	private ScrollMenu[] abilitiesMenu = new ScrollMenu[2];
	private ScrollMenu[] attacksMenu = new ScrollMenu[2];
	
	public StatsOverlay(PlayerInfo player, PlayerInfo partner) {
		super(NAME, 800, 600, false);
		this.player = player;
		this.partner = partner;		
		
		init();
	}
	
	//initializes and displays all components of stats screen
	private void init() {
		// create darkened bg
		bg = drawRect(width, height, new ColorRGBA(0,0,0,.67f));
		bg.setLocalTranslation(new Vector3f(width/2, height/2, 0));
		attachChild(bg);
		
		// create a container Node for scaling
        container = new Node("container");
        attachChild(container);
		
		// create columns
		for (int i=0; i<column.length; i++) {
			column[i] = new Node("column_"+i);
			column[i].setLocalTranslation(400*i, 0, 0);
			container.attachChild(column[i]);
		}
		
		loadPools();
		
		createText();
		
		updateFields();
		
		container.setLocalScale((float) DisplaySystem.getDisplaySystem().getHeight()/600);
	}
	
	private void createText() {
		// create static text
		PlayerInfo c;
		for(int i = 0; i < 2; i++) {
			c = (i == 0) ? player : partner;
			
			// name label
			BitmapText headerName = new BitmapText(FontUtils.neuropol, false);
			headerName.setText(((i == 0) ? "Player:  " : "Partner  ") + c.name);
			headerName.setSize(18);
			headerName.update();
			headerName.setLocalTranslation(new Vector3f(8, 600 - 8, 0));
			column[i].attachChild(headerName);		
			
			// mugshot
			Quad pic = getImage(baseURL + c.name.toLowerCase() + "96.png");
			pic.setLocalTranslation(new Vector3f(292, 600 - 64, 0));
			column[i].attachChild(pic);
			
			// stats
			BitmapText stats = new BitmapText(FontUtils.neuropol, false);
			stats.setText("HP: " + c.getMaxHp() + "\n" +
					"Strength: " + c.getStrength() + "\n" +
					"MinGauge: " + c.getMinGauge() + "\n" +
					"MaxGauge: " + c.getMaxGauge() + "\n" +
					"Rate: " + c.getGaugeRate());
			stats.setSize(16);
			stats.update();
			stats.setLocalTranslation(new Vector3f(24, 600 - 32, 0));
			column[i].attachChild(stats);
			
			// ability label
			BitmapText headerAbilities = new BitmapText(FontUtils.neuropol, false);
			headerAbilities.setText("Abilities:");
			headerAbilities.setSize(18);
			headerAbilities.update();
			headerAbilities.setLocalTranslation(new Vector3f(8, 600 - 144, 0));
			column[i].attachChild(headerAbilities);
			
			// abilities menu
			abilitiesMenu[i] = new ScrollMenu(120, 20, 6, new ColorRGBA(0,0,0,.5f), new ColorRGBA(.5f,.5f,.5f,.5f),
					new ColorRGBA(.5f,.5f,.5f,.75f), new ColorRGBA(.75f,.75f,.75f,1), new ColorRGBA(1,1,1,1), FontUtils.eurostile, 16, Align.Left,
					abilities[i]);
			abilitiesMenu[i].enableScrollBar();
			abilitiesMenu[i].setLocalTranslation(new Vector3f(24 + 60, 600 - 168 - 20*6 + 20, 0));
			column[i].attachChild(abilitiesMenu[i]);
			
			// abilities list
			abilityText[i] = new BitmapText(FontUtils.neuropol, false);
			abilityText[i].setSize(16);
			abilityText[i].setLocalTranslation(new Vector3f(24 + 188, 600 - 168, 0));
			column[i].attachChild(abilityText[i]);
			
			// attack label
			BitmapText headerAttacks = new BitmapText(FontUtils.neuropol, false);
			headerAttacks.setText("Attacks:");
			headerAttacks.setSize(18);
			headerAttacks.update();
			headerAttacks.setLocalTranslation(new Vector3f(8, 600 - 332, 0));
			column[i].attachChild(headerAttacks);
			
			// attacks menu
			attacksMenu[i] = new ScrollMenu(120, 20, 8, new ColorRGBA(0,0,0,.5f), new ColorRGBA(.5f,.5f,.5f,.5f),
					new ColorRGBA(.5f,.5f,.5f,.75f), new ColorRGBA(.75f,.75f,.75f,1), new ColorRGBA(1,1,1,1), FontUtils.eurostile, 16, Align.Left,
					attacks[i]);
			attacksMenu[i].enableScrollBar();
			attacksMenu[i].setLocalTranslation(new Vector3f(24 + 60, 600 - 366 - 20*8 + 20, 0));
			column[i].attachChild(attacksMenu[i]);
			
			// attacks list
			attackText[i] = new BitmapText(FontUtils.neuropol, false);
			attackText[i].setSize(16);
			attackText[i].setLocalTranslation(new Vector3f(24 + 188, 600 - 366, 0));
			column[i].attachChild(attackText[i]);
		}
		
		updateRenderState();		
	}
	
	/**
	 * Converts ability and attack pools to string data.
	 */
	private void loadPools() {
		PlayerInfo c;
		for(int i = 0; i < 2; i++) {
			c = (i == 0) ? player : partner;
			
			// convert abilities
			ArrayList<Ability> a = c.getAbilityPoll();
			abilities[i] = new String[a.size()];
			for (int u=0; u<abilities[i].length; u++)
				abilities[i][u] = a.get(u).name;
			
			// convert attacks
			ArrayList<Attack> t = c.getAttackPoll();
			attacks[i] = new String[t.size()];
			for (int v=0; v<attacks[i].length; v++)
				attacks[i][v] = t.get(v).name;
		}
	}
	
	/**
	 * Reloads data for both character's attacks and abilities.
	 */
	private void updateFields() {
		PlayerInfo c;
		for(int i = 0; i < 2; i++) {
			c = (i == 0) ? player : partner;
			
			// update abilities
			String abl = "";
			String[] a = c.getAbilities();
			for (int q=0; q<a.length; q++)
				abl += q+" - "+a[q]+"\n";
			abilityText[i].setText(abl);
			abilityText[i].update();
			
			// update attacks
			String atk = "";
			String[] t = c.getAttacks();
			for (int q=0; q<t.length; q++)
				atk += q+" - "+t[q]+"\n";
			attackText[i].setText(atk);
			attackText[i].update();
		}
	}
	
	@Override
	public void updateDisplay(KeyBindings k) {
		
	}

}
