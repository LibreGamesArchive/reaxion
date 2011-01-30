package com.googlecode.reaxion.game.overlay;

import java.util.ArrayList;
import java.util.Arrays;

import com.googlecode.reaxion.game.ability.Ability;
import com.googlecode.reaxion.game.attack.Attack;
import com.googlecode.reaxion.game.burstgrid.info.PlayerInfo;
import com.googlecode.reaxion.game.input.bindings.KeyBindings;
import com.googlecode.reaxion.game.input.bindings.MenuBindings;
import com.googlecode.reaxion.game.util.FontUtils;
import com.jme.math.Vector3f;
import com.jme.renderer.ColorRGBA;
import com.jme.scene.Node;
import com.jme.scene.shape.Quad;
import com.jme.system.DisplaySystem;
import com.jmex.angelfont.BitmapText;
import com.jmex.angelfont.BitmapFont.Align;

/**
 * {@code StatsOverlay} displays information for both player characters,
 * and allows the player to assign their abilities and attacks from the
 * set of those unlocked via the BurstGrid.
 * 
 * @author Khoa Ha
 */

public class StatsOverlay extends MenuOverlay {

	public static final String NAME = "statsOverlay";
	private static final String baseURL = "com/googlecode/reaxion/resources/icons/characterselect/";
	private static final String guiURL = "com/googlecode/reaxion/resources/gui/";
	private static final String attackBaseLocation = "com.googlecode.reaxion.game.attack.";
	private static final String abilityBaseLocation = "com.googlecode.reaxion.game.ability.";
	
	private PlayerInfo player;
	private PlayerInfo partner;
	
	private int controlIndex = 0;
	
	private Quad bg;
	private Quad pointer;
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
		
		// create pointer
        pointer = getImage(guiURL+"arrow.png");
        container.attachChild(pointer);
        updatePointer();
		
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
			headerName.setText(((i == 0) ? "Player:  " : "Partner:  ") + c.name);
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
			abilitiesMenu[i].update();
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
			attacksMenu[i].update();
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
			ArrayList<Ability> a = c.getAbilityPool();
			abilities[i] = new String[a.size()];
			for (int u=0; u<abilities[i].length; u++)
				abilities[i][u] = a.get(u).name;
			
			// convert attacks
			ArrayList<Attack> t = c.getAttackPool();
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
				try {
					abl += (q+1)+" - "+((Ability) Class.forName(abilityBaseLocation + a[q]).newInstance()).name+"\n";
				} catch (Exception e) {
					e.printStackTrace();
				}
			abilityText[i].setText(abl);
			abilityText[i].update();
			
			// update attacks
			String atk = "";
			String[] t = c.getAttacks();
			for (int q=0; q<t.length; q++)
				try {
					atk += (q+1)+" - "+((Attack) Class.forName(attackBaseLocation + t[q]).newInstance()).name+"\n";
				} catch (Exception e) {
					e.printStackTrace();
				}
			attackText[i].setText(atk);
			attackText[i].update();
		}
	}
	
	private void updatePointer() {
		pointer.setLocalTranslation((int)(controlIndex/2)*400 + 12, 600 - 168 - (controlIndex%2)*198 - 10, 0);
	}
	
	private ScrollMenu getCurrentMenu() {
		if (controlIndex % 2 == 0)
			return abilitiesMenu[(int)controlIndex/2];
		return attacksMenu[(int)controlIndex/2];
	}
	
	/**
	 * Moves the element in the {@code ScrollMenu} denoted by
	 * {@code controlIndex} at index {@code item} into the usable
	 * slots for the character in question.
	 * @param item Index in the current {@code ScrollMenu}
	 * @param ind Index of the list to insert item at
	 */
	private void assignItem(int item, int ind) {
		PlayerInfo c = (controlIndex <= 1) ? player : partner;
		
		if (controlIndex % 2 == 0) {
			// assign ability
			ind = Math.min(ind, 1);
			String abl = c.getAbilityPool().get(item).getClass().getName();
			abl = abl.substring(abl.lastIndexOf('.')+1);
			ArrayList<String> currentList = new ArrayList<String>(Arrays.asList(c.getAbilities()));
			if (!currentList.contains(abl)) {
				if (ind > currentList.size())
					if (currentList.size() < 2)
						currentList.add(abl);
				currentList.set(ind, abl);
				c.setAbilities(currentList.toArray(new String[0]));
			}
			
		} else {
			// assign attack
			String atk = c.getAttackPool().get(item).getClass().getName();
			atk = atk.substring(atk.lastIndexOf('.')+1);
			ArrayList<String> currentList = new ArrayList<String>(Arrays.asList(c.getAttacks()));
			if (!currentList.contains(atk)) {
				if (ind > currentList.size())
					if (currentList.size() < 6)
						currentList.add(atk);
				currentList.set(ind, atk);
				c.setAttacks(currentList.toArray(new String[0]));
			}
		}
	}
	
	@Override
	public void updateDisplay(KeyBindings k) {
		if (k == MenuBindings.SELECT_ITEM) {
			// switch scroll menus
			controlIndex = (controlIndex + 1) % 4;
			updatePointer();
			
		} else if (k.toString().contains("MenuBindings_CHOOSE_")) {
			// assign new item
			int val = k.toString().charAt(k.toString().length()-1) - 48;
			int ind = getCurrentMenu().getCurrentIndex();
			if (ind >= 0) {
				assignItem(ind, val-1);
				updateFields();
			}
			
		} else {
			// scroll menu
			if (k == MenuBindings.UP) {
				getCurrentMenu().changeIndex(-1, true);
				getCurrentMenu().remainVisible();
				getCurrentMenu().update();
			}
			if (k == MenuBindings.DOWN) {
				getCurrentMenu().changeIndex(1, true);
				getCurrentMenu().remainVisible();
				getCurrentMenu().update();
			}
		}
	}

}
