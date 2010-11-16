package com.googlecode.reaxion.game.overlay;

import com.googlecode.reaxion.game.burstgrid.info.PlayerInfo;
import com.googlecode.reaxion.game.util.FontUtils;
import com.jme.math.Vector3f;
import com.jme.scene.Node;
import com.jme.scene.shape.Quad;
import com.jme.system.DisplaySystem;
import com.jmex.angelfont.BitmapFont;
import com.jmex.angelfont.BitmapText;

/**
 * Facilitates the display view in {@code BurstGridGameState}.
 * @author Khoa Ha
 *
 */
public class BurstGridOverlay extends Overlay {
	
	private static final String baseURL = "../../resources/cosmos/";

	private Quad panel;
	
	private BitmapText exp;
	private BitmapText h;
	private BitmapText s;
	private BitmapText g;
	private BitmapText mi;
	private BitmapText ma;
	
	private Node container;
	
	public BurstGridOverlay() {
		super();
        
        // create a container Node for scaling
        container = new Node("container");
        attachChild(container);
        
        // create panels
        panel = getImage(baseURL+"panels.png");
        panel.setLocalTranslation(new Vector3f(120, 300, 0));
        container.attachChild(panel);
        
        // prepare dynamic text
        createDynamicText();
        
        container.setLocalScale((float) DisplaySystem.getDisplaySystem().getHeight()/600);
    }
	
	/**
	 * Changes displayed statistics.
	 */
	public void updateStats(PlayerInfo info) {
		exp.setText(""+info.exp);
		exp.update();
		
		h.setText(""+info.getMaxHp());
		h.update();
		
		g.setText(""+info.getGaugeRate());
		g.update();
		
		s.setText(""+info.getStrength());
		s.update();
		
		mi.setText(""+info.getMinGauge());
		mi.update();
		
		ma.setText(""+info.getMaxGauge());
		ma.update();
	}
	
	/**
	 * Set initial, non-changing text.
	 */
	public void setStaticText(PlayerInfo info) {
		BitmapText name = new BitmapText(FontUtils.eurostile, false);
        name.setSize(24);
        name.setLocalTranslation(new Vector3f(12, 600 - 10, 0));
        name.setText(info.name);
		name.update();
        container.attachChild(name);
        
        BitmapText exp = new BitmapText(FontUtils.eurostile, false);
        exp.setSize(18);
        exp.setLocalTranslation(new Vector3f(12, 600 - 44, 0));
        exp.setText("EXP");
		exp.update();
        container.attachChild(exp);
        
        BitmapText h = new BitmapText(FontUtils.eurostile, false);
        h.setSize(18);
        h.setLocalTranslation(new Vector3f(12, 600 - 80, 0));
        h.setText("HP");
		h.update();
        container.attachChild(h);
        
        BitmapText s = new BitmapText(FontUtils.eurostile, false);
        s.setSize(18);
        s.setLocalTranslation(new Vector3f(12, 600 - 110, 0));
        s.setText("Strength");
		s.update();
        container.attachChild(s);
        
        BitmapText g = new BitmapText(FontUtils.eurostile, false);
        g.setSize(18);
        g.setLocalTranslation(new Vector3f(12, 600 - 140, 0));
        g.setText("GaugeRate");
		g.update();
        container.attachChild(g);
        
        BitmapText mi = new BitmapText(FontUtils.eurostile, false);
        mi.setSize(18);
        mi.setLocalTranslation(new Vector3f(12, 600 - 170, 0));
        mi.setText("MinGauge");
        mi.update();
        container.attachChild(mi);
        
        BitmapText ma = new BitmapText(FontUtils.eurostile, false);
        ma.setSize(18);
        ma.setLocalTranslation(new Vector3f(12, 600 - 200, 0));
        ma.setText("MaxGauge");
        ma.update();
        container.attachChild(ma);
        
        BitmapText ph = new BitmapText(FontUtils.eurostile, false);
        ph.setSize(18);
        ph.setLocalTranslation(new Vector3f(12, 600 - 248, 0));
        ph.setText("HP %");
		ph.update();
        container.attachChild(ph);
        
        BitmapText ps = new BitmapText(FontUtils.eurostile, false);
        ps.setSize(18);
        ps.setLocalTranslation(new Vector3f(12, 600 - 278, 0));
        ps.setText("Str %");
		ps.update();
        container.attachChild(ps);
        
        BitmapText pg = new BitmapText(FontUtils.eurostile, false);
        pg.setSize(18);
        pg.setLocalTranslation(new Vector3f(12, 600 - 308, 0));
        pg.setText("GRate %");
		pg.update();
        container.attachChild(pg);
        
        BitmapText pmi = new BitmapText(FontUtils.eurostile, false);
        pmi.setSize(18);
        pmi.setLocalTranslation(new Vector3f(12, 600 - 338, 0));
        pmi.setText("MinG %");
        pmi.update();
        container.attachChild(pmi);
        
        BitmapText pma = new BitmapText(FontUtils.eurostile, false);
        pma.setSize(18);
        pma.setLocalTranslation(new Vector3f(12, 600 - 368, 0));
        pma.setText("MaxG %");
        pma.update();
        container.attachChild(pma);
	}
	
	/**
	 * Creates dynamic text fields.
	 */
	private void createDynamicText() {    
        exp = new BitmapText(FontUtils.eurostile, false);
        exp.setSize(18);
        exp.setAlignment(BitmapFont.Align.Right);
        exp.setLocalTranslation(new Vector3f(240 - 12, 600 - 44, 0));
        container.attachChild(exp);
        
        h = new BitmapText(FontUtils.eurostile, false);
        h.setSize(18);
        h.setAlignment(BitmapFont.Align.Right);
        h.setLocalTranslation(new Vector3f(240 - 12, 600 - 80, 0));
        container.attachChild(h);
        
        s = new BitmapText(FontUtils.eurostile, false);
        s.setSize(18);
        s.setAlignment(BitmapFont.Align.Right);
        s.setLocalTranslation(new Vector3f(240 - 12, 600 - 110, 0));
        container.attachChild(s);
        
        g = new BitmapText(FontUtils.eurostile, false);
        g.setSize(18);
        g.setAlignment(BitmapFont.Align.Right);
        g.setLocalTranslation(new Vector3f(240 - 12, 600 - 140, 0));
        container.attachChild(g);
        
        mi = new BitmapText(FontUtils.eurostile, false);
        mi.setSize(18);
        mi.setAlignment(BitmapFont.Align.Right);
        mi.setLocalTranslation(new Vector3f(240 - 12, 600 - 170, 0));
        container.attachChild(mi);
        
        ma = new BitmapText(FontUtils.eurostile, false);
        ma.setSize(18);
        ma.setAlignment(BitmapFont.Align.Right);
        ma.setLocalTranslation(new Vector3f(240 - 12, 600 - 200, 0));
        container.attachChild(ma);
	}
	
}
