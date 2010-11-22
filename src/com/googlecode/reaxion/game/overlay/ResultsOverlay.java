package com.googlecode.reaxion.game.overlay;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.googlecode.reaxion.game.model.character.Character;
import com.googlecode.reaxion.game.model.character.MajorCharacter;
import com.googlecode.reaxion.game.state.StageGameState;
import com.googlecode.reaxion.game.util.FontUtils;
import com.jme.math.Vector3f;
import com.jme.renderer.ColorRGBA;
import com.jme.scene.Node;
import com.jme.scene.shape.Quad;
import com.jme.system.DisplaySystem;
import com.jmex.angelfont.BitmapFont;
import com.jmex.angelfont.BitmapFontLoader;
import com.jmex.angelfont.BitmapText;
import com.jmex.game.state.GameState;

/**
 * Displays the results of a battle in {@code ResultsGameState}.
 * @author Khoa Ha
 *
 */
public class ResultsOverlay extends Overlay {
	
	private static final String baseURL = "../../resources/gui/";
	
	private final int[] point = {32, 96, 104, 110, 116, 122, 128, 134, 140};
	private int frame = 0;
	
	private double expYield;
	private int numStars;
	
	private Node bg;
	
	private Quad topFade;
	private Quad botFade;
	private Quad topBar;
	private Quad starBar;
	private Quad statBar;
	
	private BitmapText combatants;
	private Node targetTime;
	private Node clearTime;
	private Node totalHp;
	private Node remainingHp;
	private Node expBonus;
	
	public ResultsOverlay() {
		super();
		
        // create a container Node for scaling
        container = new Node("container");
        
        // create combatants text
        combatants = new BitmapText(FontUtils.neuropol, false);
        combatants.setSize(30);
        combatants.setDefaultColor(new ColorRGBA(1, 1, 1, 1));
        combatants.setLocalTranslation(new Vector3f(14, 586, 0));
        
        // create nodes for all the stats
        targetTime = new Node("targetTime");
        clearTime = new Node("clearTime");
        totalHp = new Node("totalHp");
        remainingHp = new Node("remainingHp");
        expBonus = new Node("expBonus");
        
        // create a bg container
        bg = new Node("bg");
        bg.setLocalTranslation(new Vector3f(width/2, height/2, 0));
        
        // create topFade
        topFade = getImage(baseURL+"top_fade.png");
        topFade.setLocalTranslation(new Vector3f(400, 600 + 67, 0));
        
        // create topBar
        topBar = getImage(baseURL+"top_bar.png");
        topBar.setLocalTranslation(new Vector3f(0 - 283, 600 - 42, 0));
        
        // create starBar
        starBar = getImage(baseURL+"star_bar.png");
        starBar.setLocalTranslation(new Vector3f(800 + 331, 478 - 64, 0));
        
        // create statBar
        statBar = getImage(baseURL+"score_bar.png");
        statBar.setLocalTranslation(new Vector3f(142 + 329, -157, 0));

        // create botFade
        botFade = getImage(baseURL+"bot_fade.png");
        botFade.setLocalTranslation(new Vector3f(400, 0 - 67, 0));
        
        // attach children
        attachChild(bg);
        attachChild(container);
        container.attachChild(topFade);
        container.attachChild(topBar);
        container.attachChild(starBar);
        container.attachChild(statBar);
        container.attachChild(botFade);
        
        container.setLocalScale((float) DisplaySystem.getDisplaySystem().getHeight()/600);
    }
	
	/**
	 * Function to be called during each update by the GameState.
	 */
	public void update(GameState b) {
		if (frame <= point[0]) {
			topFade.setLocalTranslation(new Vector3f(400, 600 + 67 - 133*(float)frame/point[0], 0));
			botFade.setLocalTranslation(new Vector3f(400, 0 - 67 + 133*(float)frame/point[0], 0));
		} else if (frame <= point[1]) {
			topBar.setLocalTranslation(new Vector3f(0 - 283 + 567*(float)(frame-point[0])/(point[1]-point[0]), 600 - 42, 0));
			starBar.setLocalTranslation(new Vector3f(800 + 331 - 660*(float)(frame-point[0])/(point[1]-point[0]), 478 - 64, 0));
			statBar.setLocalTranslation(new Vector3f(142 + 329, -157 + 315*(float)(frame-point[0])/(point[1]-point[0]), 0));
		} else if (frame == point[2]) {
			container.attachChild(combatants);
		} else if (frame == point[3]) {
			container.attachChild(targetTime);
		} else if (frame == point[4]) {
			container.attachChild(clearTime);
		} else if (frame == point[5]) {
			container.attachChild(totalHp);
		} else if (frame == point[6]) {
			container.attachChild(remainingHp);
		} else if (frame == point[7]) {
			container.attachChild(expBonus);
		} else if (frame >= point[8]) {
			if (frame % 4 == 0 && (frame - point[8])/4 < numStars) {
				Quad star = getImage(baseURL+"star.png");
		        star.setLocalTranslation(new Vector3f(232 + 110*(frame - point[8])/4, 478 - 64, 0));
		        container.attachChild(star);
		        container.updateRenderState();
			}
		}
		
		frame++;
	}
	
	public void setBackground(Quad q) {
		bg.attachChild(q);
		bg.updateRenderState();
	}
	
	public void setCombatants(MajorCharacter player, MajorCharacter partner, Character[] opponents) {
		String str = player.name;
		if (partner != null)
			str += " + "+ partner.name;
		str += " vs.\n";
		for (int i=0; i<opponents.length; i++) {
			str += opponents[i].name;
			if (i+1 < opponents.length)
				str += " + ";
		}
		combatants.setText(str);
		combatants.update();
	}
	
	public void setStats(double ttd, double ctd, MajorCharacter player, MajorCharacter partner, int eY) {
		
		// target time
		BitmapText ttl = new BitmapText(FontUtils.neuropol, false);
        ttl.setSize(26);
        ttl.setDefaultColor(new ColorRGBA(1, 1, 1, 1));
        ttl.setLocalTranslation(new Vector3f(180, 284, 0));
        ttl.setText("Target Time");
        ttl.update();
		
		BitmapText tt = new BitmapText(FontUtils.neuropol, false);
        tt.setSize(26);
        tt.setDefaultColor(new ColorRGBA(1, 1, 1, 1));
        tt.setAlignment(BitmapFont.Align.Right);
        tt.setLocalTranslation(new Vector3f(758, 284, 0));
        tt.setText(formatTime(ttd));
        tt.update();
        
        targetTime.attachChild(ttl);
        targetTime.attachChild(tt);       
        
        // clear time
        BitmapText ctl = new BitmapText(FontUtils.neuropol, false);
        ctl.setSize(26);
        ctl.setDefaultColor(new ColorRGBA(1, 1, 1, 1));
        ctl.setLocalTranslation(new Vector3f(180, 252, 0));
        ctl.setText("Clear Time");
        ctl.update();
        
        BitmapText ct = new BitmapText(FontUtils.neuropol, false);
        ct.setSize(26);
        ct.setDefaultColor(new ColorRGBA(1, 1, 1, 1));
        ct.setAlignment(BitmapFont.Align.Right);
        ct.setLocalTranslation(new Vector3f(758, 252, 0));
        ct.setText(formatTime(ctd));
        ct.update();
        
        clearTime.attachChild(ctl);
        clearTime.attachChild(ct); 
        
        // calculate hp stats
        double thd = Math.max(player.maxHp, 0);
        double rhd = Math.max(player.hp, 0);
        if (partner != null) {
        	thd += Math.max(partner.maxHp, 0);
        	rhd += Math.max(partner.hp, 0);
        }
        
        // total hp
        BitmapText thl = new BitmapText(FontUtils.neuropol, false);
        thl.setSize(26);
        thl.setDefaultColor(new ColorRGBA(1, 1, 1, 1));
        thl.setLocalTranslation(new Vector3f(180, 220, 0));
        thl.setText("Total HP");
        thl.update();
        
        BitmapText th = new BitmapText(FontUtils.neuropol, false);
        th.setSize(26);
        th.setDefaultColor(new ColorRGBA(1, 1, 1, 1));
        th.setAlignment(BitmapFont.Align.Right);
        th.setLocalTranslation(new Vector3f(758, 220, 0));
        th.setText(""+(int)thd);
        th.update();
        
        totalHp.attachChild(thl);
        totalHp.attachChild(th); 
        
        // remaining hp
        BitmapText rhl = new BitmapText(FontUtils.neuropol, false);
        rhl.setSize(26);
        rhl.setDefaultColor(new ColorRGBA(1, 1, 1, 1));
        rhl.setLocalTranslation(new Vector3f(180, 188, 0));
        rhl.setText("Remaining HP");
        rhl.update();
        
        BitmapText rh = new BitmapText(FontUtils.neuropol, false);
        rh.setSize(26);
        rh.setDefaultColor(new ColorRGBA(1, 1, 1, 1));
        rh.setAlignment(BitmapFont.Align.Right);
        rh.setLocalTranslation(new Vector3f(758, 188, 0));
        rh.setText(""+(int)rhd);
        rh.update();
        
        remainingHp.attachChild(rhl);
        remainingHp.attachChild(rh);
        
        // expBonus
        BitmapText ebl = new BitmapText(FontUtils.neuropol, false);
        ebl.setSize(26);
        ebl.setDefaultColor(new ColorRGBA(1, 1, 1, 1));
        ebl.setLocalTranslation(new Vector3f(180, 124, 0));
        ebl.setText("EXP Bonus");
        ebl.update();
        
        BitmapText eb = new BitmapText(FontUtils.neuropol, false);
        eb.setSize(26);
        eb.setDefaultColor(new ColorRGBA(1, 1, 1, 1));
        eb.setAlignment(BitmapFont.Align.Right);
        eb.setLocalTranslation(new Vector3f(758, 124, 0));
        
        //calculate exp
        calculateExp(ttd, ctd, thd, rhd, eY);
        
        eb.setText(""+(int)expYield);
        eb.update();
        
        expBonus.attachChild(ebl);
        expBonus.attachChild(eb);
		
	}
	
	/**
	 * Increment the participants' exp in their PlayerInfo.
	 */
	public void allotExp(MajorCharacter player, MajorCharacter partner) {
		player.info.exp += (int)expYield;
		partner.info.exp += (int)expYield;
	}
	
	private void calculateExp(double targetTime, double clearTime, double totalHp, double remainingHp, int exp) {
		// time score ranges from clearTime <= targetTime (1) to clearTime >= 3*targetTime (0)
		double timeMultiplier = Math.min(Math.max(1 - (clearTime - targetTime)/(2*targetTime), 0), 1);
		
		// hp score ranges from remainingHp = totalHp (1) to remainingHp = 0 (.5)
		double hpMultiplier = (1+ remainingHp / totalHp) / 2;
		
		expYield = timeMultiplier * hpMultiplier * exp;
		numStars = (int) Math.ceil(5 * expYield/exp);
	}
	
	private String formatTime(double seconds) {
		if (Double.isNaN(seconds))
			return "--:--:--";
		String[] time = new String[3];
		time[0] = ""+(int)(seconds/3600);
		time[1] = ""+(int)((seconds%3600)/60);
		time[2] = ""+(int)((seconds%3600)%60);
		for (int i=0; i<time.length; i++)
			if (time[i].length()<2)
				time[i] = "0"+time[i];
		return time[0]+":"+time[1]+":"+time[2];
	}
	
}
