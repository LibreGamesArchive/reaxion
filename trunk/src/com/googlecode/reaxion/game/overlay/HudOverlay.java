package com.googlecode.reaxion.game.overlay;

import com.googlecode.reaxion.game.attack.Attack;
import com.googlecode.reaxion.game.model.character.Character;
import com.googlecode.reaxion.game.state.StageGameState;
import com.googlecode.reaxion.game.util.ColorUtils;
import com.googlecode.reaxion.game.util.FontUtils;
import com.jme.math.Vector3f;
import com.jme.renderer.ColorRGBA;
import com.jme.scene.Node;
import com.jme.scene.shape.Quad;
import com.jme.system.DisplaySystem;
import com.jmex.angelfont.BitmapFont;
import com.jmex.angelfont.BitmapText;

/**
 * Facilitates the creation and maintains of the in-battle HUD.
 * @author Khoa Ha
 *
 */
public class HudOverlay extends Overlay {
	
	private static final String baseURL = "../../resources/gui/";
    
	private Class[] attacks;
	private int gaugeCap;
	
	private Node container;
	
	private Quad[] attackFill;
	private Quad[] attackBar;
	private BitmapText[] attackText;
	private BitmapText[] gaugeCostText;
	private int[] gaugeCosts;
	private Quad opHealthFill;
	private Quad opHealth;
	private BitmapText opName;
	private BitmapText opHealthText;
	private Quad healthFill;
	private Quad health;
	private BitmapText healthText;
	private BitmapText name;
	private Quad ptHealthFill;
	private Quad ptHealth;
	private BitmapText ptHealthText;
	private BitmapText ptName;
	private Quad gaugeLowFill;
	private Quad gaugeHighFill;
	private Quad gauge;
	private BitmapText gaugeCount;
	
	private ColorRGBA textColor;
	private ColorRGBA[] gaugeColors = {new ColorRGBA(0, .67f, .67f, 1), new ColorRGBA(1, .5f, 0, 1)};
	private ColorRGBA[] attackUsed;
	private ColorRGBA[] zPressedColors;
	private ColorRGBA attackUnavailable;
	
	public boolean zPressed;
	
	public HudOverlay() {
		super();
		
        // create a container Node for scaling
        container = new Node("container");
		
        // White
        textColor = new ColorRGBA(1, 1, 1, 1);
        // Dark Gray
        attackUnavailable = new ColorRGBA(.25f, .25f, .25f, 1);
        
        attackUsed = new ColorRGBA[gaugeColors.length];
        zPressedColors = new ColorRGBA[gaugeColors.length];
        for(int i = 0; i < gaugeColors.length; i++) {
        	attackUsed[i] = ColorUtils.lighter(gaugeColors[i], .5);
        	zPressedColors[i] = ColorUtils.darker(gaugeColors[i], .5);
        }
        
		attackFill = new Quad[6];
		for (int i=0; i<attackFill.length; i++) {
			attackFill[i] = drawRect(162, 18, attackUnavailable);
			attackFill[i].setLocalTranslation(new Vector3f(-22 + 98, 100 - 20*i + 10, 0));
			container.attachChild(attackFill[i]);
		}
		
		attackBar = new Quad[6];
		for (int i=0; i<attackBar.length; i++) {
			attackBar[i] = getImage(baseURL+"attack.png");
			attackBar[i].setLocalTranslation(new Vector3f(-22 + 98, 100 - 20*i + 10, 0));
			container.attachChild(attackBar[i]);
		}
		
		attackText = new BitmapText[6];
		for (int i=0; i<attackText.length; i++) {
			attackText[i] = new BitmapText(FontUtils.neuropol, false);
			attackText[i].setSize(16);
			attackText[i].setDefaultColor(textColor);
			attackText[i].setLocalTranslation(new Vector3f(4, 100 - 20*i + 18, 0));
			container.attachChild(attackText[i]);
		}
		
		gaugeCostText = new BitmapText[6];
		for (int i = 0; i < gaugeCostText.length; i++) {
			gaugeCostText[i] = new BitmapText(FontUtils.neuropol, false);
			gaugeCostText[i].setSize(16);
			gaugeCostText[i].setDefaultColor(textColor);
			gaugeCostText[i].setAlignment(BitmapFont.Align.Right);
			gaugeCostText[i].setLocalTranslation(new Vector3f(attackFill[i].getWidth() - 25, 100 - 20 * i + 18, 0));
			container.attachChild(gaugeCostText[i]);
		}
		
		gaugeCosts = new int[6];
		
		opHealthFill = drawRect(576, 8, new ColorRGBA(0, 1, 0, 1));
		opHealthFill.setLocalTranslation(new Vector3f(6 + 9 + 283, 576 + 5, 0));
		container.attachChild(opHealthFill);
		
		opHealth = getImage(baseURL+"gauge.png");
		opHealth.setLocalTranslation(new Vector3f(6 + 292, 576 + 5, 0));
		container.attachChild(opHealth);
		
		opName = new BitmapText(FontUtils.neuropol, false);
        opName.setSize(16);
        opName.setDefaultColor(textColor);
        opName.setLocalTranslation(new Vector3f(18, 572, 0));
        container.attachChild(opName);
        
        opHealthText = new BitmapText(FontUtils.neuropol, false);
        opHealthText.setSize(16);
        opHealthText.setAlignment(BitmapFont.Align.Right);
        opHealthText.setDefaultColor(textColor);
        opHealthText.setLocalTranslation(new Vector3f(580, 572, 0));
        container.attachChild(opHealthText);
        
        ptHealthFill = drawRect(180, 8, new ColorRGBA(0, 1, 0, 1));
		ptHealthFill.setLocalTranslation(new Vector3f(208 + 9 + 90, 50 + 5, 0));
		container.attachChild(ptHealthFill);
        
        ptHealth = getImage(baseURL+"partner.png");
		ptHealth.setLocalTranslation(new Vector3f(208 + 98, 50 + 5, 0));
		container.attachChild(ptHealth);
		
		ptHealthText = new BitmapText(FontUtils.neuropol, false);
        ptHealthText.setSize(16);
        ptHealthText.setDefaultColor(textColor);
        ptHealthText.setLocalTranslation(new Vector3f(222, 76, 0));
        container.attachChild(ptHealthText);
        
        ptName = new BitmapText(FontUtils.neuropol, false);
        ptName.setSize(16);
        ptName.setAlignment(BitmapFont.Align.Right);
        ptName.setDefaultColor(textColor);
        ptName.setLocalTranslation(new Vector3f(390, 76, 0));
        container.attachChild(ptName);
		
		healthFill = drawRect(342, 8, new ColorRGBA(0, 1, 0, 1));
		healthFill.setLocalTranslation(new Vector3f(431 + 9 + 171, 50 + 5, 0));
		container.attachChild(healthFill);
		
		health = getImage(baseURL+"health.png");
		health.setLocalTranslation(new Vector3f(431 + 180, 50 + 5, 0));
		container.attachChild(health);
		
		healthText = new BitmapText(FontUtils.neuropol, false);
        healthText.setSize(16);
        healthText.setDefaultColor(textColor);
        healthText.setLocalTranslation(new Vector3f(442, 76, 0));
        container.attachChild(healthText);
        
        name = new BitmapText(FontUtils.neuropol, false);
        name.setSize(16);
        name.setAlignment(BitmapFont.Align.Right);
        name.setDefaultColor(textColor);
        name.setLocalTranslation(new Vector3f(778, 76, 0));
        container.attachChild(name);
		
		gaugeHighFill = drawRect(576, 8, gaugeColors[1]);
		gaugeHighFill.setLocalTranslation(new Vector3f(208 + 9 + 283, 34 + 5, 0));
		container.attachChild(gaugeHighFill);
		gaugeLowFill = drawRect(576, 8, gaugeColors[0]);
		gaugeLowFill.setLocalTranslation(new Vector3f(208 + 9 + 283, 34 + 5, 0));
		container.attachChild(gaugeLowFill);
		
		gauge = getImage(baseURL+"gauge.png");
		gauge.setLocalTranslation(new Vector3f(208 + 292, 34 + 5, 0));
		container.attachChild(gauge);
		
		gaugeCount = new BitmapText(FontUtils.neuropol, false);
		gaugeCount.setSize(16);
		gaugeCount.setAlignment(BitmapFont.Align.Center);
		gaugeCount.setDefaultColor(textColor);
        gaugeCount.setLocalTranslation(new Vector3f(210, 34, 0));
        container.attachChild(gaugeCount);
        
        attachChild(container);
        
        container.setLocalScale((float) DisplaySystem.getDisplaySystem().getHeight()/600);
    }
	
	/**
	 * Function to be called during each update by the GameState.
	 */
	public void update(StageGameState b) {
		// update attacks
		for (int i=0; i<attacks.length; i++) {
			if (attacks[i] != null && b.getPlayer().currentAttack != null && attacks[i].isInstance(b.getPlayer().currentAttack)) {
				attackFill[i].setLocalTranslation(new Vector3f(98, 100 - 20*i + 10, 0));
				attackFill[i].setSolidColor(gaugeCosts[i] >= gaugeCap ? attackUsed[1] : attackUsed[0]);
				attackBar[i].setLocalTranslation(new Vector3f(98, 100 - 20*i + 10, 0));
				attackText[i].setLocalTranslation(new Vector3f(22 + 4, 100 - 20*i + 18, 0));
				gaugeCostText[i].setLocalTranslation(new Vector3f(22 + attackFill[i].getWidth() - 10, 100 - 20 * i + 18, 0));
			} else {
				attackFill[i].setLocalTranslation(new Vector3f(-22 + 98, 100 - 20*i + 10, 0));
				if(gaugeCosts[i] != -1 && b.getPlayer().gauge >= gaugeCosts[i]) {
					ColorRGBA[] temp1 = zPressed ? zPressedColors : gaugeColors;
					ColorRGBA[] temp2 = zPressed ? gaugeColors : zPressedColors;
					if(i <= 2)
						attackFill[i].setSolidColor(gaugeCosts[i] >= gaugeCap ? temp1[1] : temp1[0]);
					else
						attackFill[i].setSolidColor(gaugeCosts[i] >= gaugeCap ? temp2[1] : temp2[0]);
				}
				else
					attackFill[i].setSolidColor(attackUnavailable);
				attackBar[i].setLocalTranslation(new Vector3f(-22 + 98, 100 - 20*i + 10, 0));
				attackText[i].setLocalTranslation(new Vector3f(4, 100 - 20*i + 18, 0));
				gaugeCostText[i].setLocalTranslation(new Vector3f(attackFill[i].getWidth() - 10, 100 - 20 * i + 18, 0));
			}
		}
		
		// update opHealth
		float percentOpHp = 0;
		if (b.getTarget() instanceof Character)
			percentOpHp = (float) Math.max(((Character)(b.getTarget())).hp/((Character)(b.getTarget())).maxHp, 0);
		opHealthFill.setLocalScale(new Vector3f(percentOpHp, 1, 1));
		opHealthFill.setLocalTranslation(new Vector3f(6 + 9 + 283 - (1-percentOpHp)*283, 576 + 5, 0));
		opHealthFill.setSolidColor(new ColorRGBA((percentOpHp<.5)?1:0, (percentOpHp>=.25)?1:0, 0, 1));
		
		// update opName
		opName.setText(b.getTarget().name);
		opName.update();
		
		// update opHealthText
		if (b.getTarget() instanceof Character)
			opHealthText.setText((int)Math.max(((Character)(b.getTarget())).hp, 0) +"/"+ (int)((Character)(b.getTarget())).maxHp);
		else
			opHealthText.setText("-- / --");
		opHealthText.update();
		
		// update ptHealth
		float percentPtHp = (float) Math.max((b.getPartner().hp/b.getPartner().maxHp), 0);
		ptHealthFill.setLocalScale(new Vector3f(percentPtHp, 1, 1));
		ptHealthFill.setLocalTranslation(new Vector3f(208 + 9 + 90 + (1-percentPtHp)*90, 50 + 5, 0));
		ptHealthFill.setSolidColor(new ColorRGBA((percentPtHp<.5)?1:0, (percentPtHp>=.25)?1:0, 0, 1));
		
		// update ptHealthText
		ptHealthText.setText((int)Math.max(b.getPartner().hp, 0) +"/"+ (int)(b.getPartner().maxHp));
		ptHealthText.update();
		
		// update ptName
		ptName.setText(b.getPartner().name);
		ptName.update();
		
		// update health
		float percentHp = (float) Math.max((b.getPlayer().hp/b.getPlayer().maxHp), 0);
		healthFill.setLocalScale(new Vector3f(percentHp, 1, 1));
		healthFill.setLocalTranslation(new Vector3f(431 + 9 + 171 + (1-percentHp)*171, 50 + 5, 0));
		healthFill.setSolidColor(new ColorRGBA((percentHp<.5)?1:0, (percentHp>=.25)?1:0, 0, 1));
		
		// update healthText
		healthText.setText((int)Math.max(b.getPlayer().hp, 0) +"/"+ (int)(b.getPlayer().maxHp));
		healthText.update();
		
		// update name
		name.setText(b.getPlayer().name);
		name.update();
		
		//update gauge
		float lowerFraction = (float) b.getPlayer().minGauge/b.getPlayer().maxGauge;
		float percentGauge = (float) b.getPlayer().gauge/b.getPlayer().maxGauge;
		gaugeHighFill.setLocalScale(new Vector3f(percentGauge, 1, 1));
		gaugeHighFill.setLocalTranslation(new Vector3f(208 + 9 + 283 - (1-percentGauge)*283, 34 + 5, 0));
		gaugeLowFill.setLocalScale(new Vector3f((float) Math.min(percentGauge, lowerFraction), 1, 1));
		gaugeLowFill.setLocalTranslation(new Vector3f(208 + 9 + 283 - (1-Math.min(percentGauge, lowerFraction))*283, 34 + 5, 0));
		
		// update gaugeCount
		gaugeCount.setText(""+(int)b.getPlayer().gauge);
		gaugeCount.update();
		gaugeCount.setLocalTranslation(new Vector3f(210 + 576*percentGauge, 34, 0));
	}
	
	public void passCharacterInfo(Class[] c, int cap) {
		attacks = c;
		gaugeCap = cap;
		
		// read in attacks
		for (int i=0; i<attackText.length; i++) {
			try {
				if (attacks[i] != null) {
					Attack temp = (Attack) attacks[i].getConstructors()[0].newInstance();
					attackText[i].setText(temp.name);
					gaugeCostText[i].setText("" + temp.gaugeCost);
					gaugeCosts[i] = temp.gaugeCost;
				} else {
					attackText[i].setText("---");
					gaugeCostText[i].setText("--");
					gaugeCosts[i] = -1;
				}
				attackText[i].update();
				gaugeCostText[i].update();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
}
