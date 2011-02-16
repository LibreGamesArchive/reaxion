package com.googlecode.reaxion.game.overlay;

import com.googlecode.reaxion.game.burstgrid.BurstGrid;
import com.googlecode.reaxion.game.burstgrid.info.PlayerInfo;
import com.googlecode.reaxion.game.burstgrid.node.AbilityNode;
import com.googlecode.reaxion.game.burstgrid.node.AttackNode;
import com.googlecode.reaxion.game.burstgrid.node.BurstNode;
import com.googlecode.reaxion.game.burstgrid.node.HPNode;
import com.googlecode.reaxion.game.burstgrid.node.MaxGaugeNode;
import com.googlecode.reaxion.game.burstgrid.node.MinGaugeNode;
import com.googlecode.reaxion.game.burstgrid.node.RateNode;
import com.googlecode.reaxion.game.burstgrid.node.StrengthNode;
import com.googlecode.reaxion.game.util.FontUtils;
import com.jme.math.Vector3f;
import com.jme.renderer.ColorRGBA;
import com.jme.scene.Node;
import com.jme.scene.Spatial;
import com.jme.scene.shape.Quad;
import com.jme.system.DisplaySystem;
import com.jmex.angelfont.BitmapFont;
import com.jmex.angelfont.BitmapText;
import com.jmex.angelfont.Rectangle;

/**
 * Facilitates the display view in {@code BurstGridGameState}.
 * @author Khoa Ha
 *
 */
public class BurstGridOverlay extends Overlay {
	
	public static final String NAME = "burstGridOverlay";
	
	private static final String baseURL = "com/googlecode/reaxion/resources/icons/cosmos/";
	
	private final ColorRGBA plain = new ColorRGBA(1, 1, 1, 1);
	private final ColorRGBA blocked = new ColorRGBA(2/3f, 2/3f, 2/3f, 1);
	private final ColorRGBA buy = new ColorRGBA(1, 3/4f, 0, 1);
	
	private Quad panel;
	
	private BitmapText exp;
	private BitmapText h;
	private BitmapText s;
	private BitmapText g;
	private BitmapText mi;
	private BitmapText ma;
	private BitmapText ph;
	private BitmapText ps;
	private BitmapText pg;
	private BitmapText pmi;
	private BitmapText pma;
	private BitmapText pat;
	private BitmapText pal;
	private BitmapText type;
	private BitmapText detail;
	private BitmapText c;
	private BitmapText caption;
	
	private Node descriptors;
	
	public BurstGridOverlay() {
		super(NAME);
        
        // create a container Node for scaling
        container = new Node("container");
        attachChild(container);
        
        // create panels
        panel = getImage(baseURL+"panels.png");
        panel.setLocalTranslation(new Vector3f(120, 300, 0));
        container.attachChild(panel);
        
        // create a container Node for the current descriptors
        descriptors = new Node("descriptors");
        container.attachChild(descriptors);
        
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
	 * Update the grid count info.
	 */
	public void updateCount(BurstGrid grid) {		
		ph.setText(grid.getNodeCount("HP", true)+"/"+grid.getNodeCount("HP", false));
		ph.update();
		
		ps.setText(grid.getNodeCount("Strength", true)+"/"+grid.getNodeCount("Strength", false));
		ps.update();
		
		pg.setText(grid.getNodeCount("Rate", true)+"/"+grid.getNodeCount("Rate", false));
		pg.update();
		
		pmi.setText(grid.getNodeCount("MinGauge", true)+"/"+grid.getNodeCount("MinGauge", false));
		pmi.update();
		
		pma.setText(grid.getNodeCount("MaxGauge", true)+"/"+grid.getNodeCount("MaxGauge", false));
		pma.update();
		
		pat.setText(grid.getNodeCount("MaxGauge", true)+"/"+grid.getNodeCount("MaxGauge", false));
		pat.update();
		
		pal.setText(grid.getNodeCount("MaxGauge", true)+"/"+grid.getNodeCount("MaxGauge", false));
		pal.update();
	}
	
	/**
	 * Update the node's descriptors.
	 */
	public void updateDescriptors(PlayerInfo info, BurstNode b, int cost) {
		String t = "";
		String d = "";
		String cp = "";
		
		if (b instanceof HPNode) {
			t = "HP Bonus";
			d = "+ "+((HPNode) b).hpPlus+"HP";
			cp = "Raises maximum HP.";
		} else if (b instanceof MaxGaugeNode) {
			t = "MaxGauge Bonus";
			d = "+ "+((MaxGaugeNode) b).maxGPlus+"G";
			cp = "Raises upper Gauge limit.";
		} else if (b instanceof MinGaugeNode) {
			t = "MinGauge Bonus";
			d = "+ "+((MinGaugeNode) b).minGPlus+"G";
			cp = "Raises lower Gauge limit.";
		} else if (b instanceof AttackNode) {
			t = "Attack";
			d = ((AttackNode)b).at.name;
			cp = ((AttackNode)b).at.description;
		} else if (b instanceof StrengthNode) {
			t = "Strength Bonus";
			d = "+ "+((StrengthNode) b).strengthPlus;
			cp = "Raises attack damage.";
		} else if (b instanceof AbilityNode) {
			t = "Ability";
			d = ((AbilityNode)b).ab.name;
			cp = ((AbilityNode)b).ab.description;
		} else if (b instanceof RateNode) {
			t = "GaugeRate Bonus";
			d = "+ "+((RateNode) b).rate;
			cp = "Raises Gauge refill rate.";
		}
		
		type.setText(t);
		type.update();
		detail.setText(d);
		detail.update();
		caption.setText(cp);
		caption.update();
		
		c.setText(""+ ((cost < Integer.MAX_VALUE)? cost : "--"));
		c.update();
		
		// change colors accordingly
		for (Spatial s : descriptors.getChildren())
			if (s instanceof BitmapText)
				((BitmapText) s).setDefaultColor((b.activated)? plain : ((cost <= info.exp)? buy : blocked));
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
        
        BitmapText pat = new BitmapText(FontUtils.eurostile, false);
        pat.setSize(18);
        pat.setLocalTranslation(new Vector3f(12, 600 - 398, 0));
        pat.setText("Attack %");
        pat.update();
        container.attachChild(pat);
        
        BitmapText pal = new BitmapText(FontUtils.eurostile, false);
        pal.setSize(18);
        pal.setLocalTranslation(new Vector3f(12, 600 - 428, 0));
        pal.setText("Ability %");
        pal.update();
        container.attachChild(pal);
        
        BitmapText c = new BitmapText(FontUtils.eurostile, false);
        c.setSize(18);
        c.setLocalTranslation(new Vector3f(12, 600 - 496, 0));
        c.setText("Cost");
        c.update();
        descriptors.attachChild(c);
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
        
        ph = new BitmapText(FontUtils.eurostile, false);
        ph.setSize(18);
        ph.setAlignment(BitmapFont.Align.Right);
        ph.setLocalTranslation(new Vector3f(240 - 12, 600 - 248, 0));
        container.attachChild(ph);
        
        ps = new BitmapText(FontUtils.eurostile, false);
        ps.setSize(18);
        ps.setAlignment(BitmapFont.Align.Right);
        ps.setLocalTranslation(new Vector3f(240 - 12, 600 - 278, 0));
        container.attachChild(ps);
        
        pg = new BitmapText(FontUtils.eurostile, false);
        pg.setSize(18);
        pg.setAlignment(BitmapFont.Align.Right);
        pg.setLocalTranslation(new Vector3f(240 - 12, 600 - 308, 0));
        container.attachChild(pg);
        
        pmi = new BitmapText(FontUtils.eurostile, false);
        pmi.setSize(18);
        pmi.setAlignment(BitmapFont.Align.Right);
        pmi.setLocalTranslation(new Vector3f(240 - 12, 600 - 338, 0));
        container.attachChild(pmi);
        
        pma = new BitmapText(FontUtils.eurostile, false);
        pma.setSize(18);
        pma.setAlignment(BitmapFont.Align.Right);
        pma.setLocalTranslation(new Vector3f(240 - 12, 600 - 368, 0));
        container.attachChild(pma);
        
        pat = new BitmapText(FontUtils.eurostile, false);
        pat.setSize(18);
        pat.setAlignment(BitmapFont.Align.Right);
        pat.setLocalTranslation(new Vector3f(240 - 12, 600 - 398, 0));
        container.attachChild(pat);
        
        pal = new BitmapText(FontUtils.eurostile, false);
        pal.setSize(18);
        pal.setAlignment(BitmapFont.Align.Right);
        pal.setLocalTranslation(new Vector3f(240 - 12, 600 - 428, 0));
        container.attachChild(pal);
        
        type = new BitmapText(FontUtils.eurostile, false);
        type.setSize(20);
        type.setLocalTranslation(new Vector3f(12, 600 - 466, 0));
        descriptors.attachChild(type);
        
        detail = new BitmapText(FontUtils.eurostile, false);
        detail.setSize(20);
        detail.setAlignment(BitmapFont.Align.Right);
        detail.setLocalTranslation(new Vector3f(240 - 12, 600 - 466, 0));
        descriptors.attachChild(detail);
        
        c = new BitmapText(FontUtils.eurostile, false);
        c.setSize(18);
        c.setAlignment(BitmapFont.Align.Right);
        c.setLocalTranslation(new Vector3f(240 - 12, 600 - 496, 0));
        descriptors.attachChild(c);
        
        caption = new BitmapText(FontUtils.eurostile, false);
        caption.setSize(14);
        caption.setBox(new Rectangle(12, 72, 216, 48));
        descriptors.attachChild(caption);
	}
	
}
