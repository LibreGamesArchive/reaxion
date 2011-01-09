package com.googlecode.reaxion.game.model.enemies;

import java.util.ArrayList;
import java.util.Arrays;

import com.googlecode.reaxion.game.model.Model;
import com.googlecode.reaxion.game.model.character.Character;
import com.googlecode.reaxion.game.model.prop.LightFade;
import com.googlecode.reaxion.game.state.StageGameState;
import com.googlecode.reaxion.game.util.LoadingQueue;
import com.jme.math.Vector3f;

/**
 * Physical attack prop for {@code Mirage}.
 */
public class Specter extends Character {
	
	private boolean hadAttack = false;
	
	public Specter(Model[] m) {
    	// Load model
    	super("i_specter");
    	users = new ArrayList<Model>();
    	users.addAll(Arrays.asList(m));
    	trackable = false;
    	mass = 3;
    	
    	Character user = (Character)m[m.length-1];
    	maxHp = 999;
    	hp = 999;
    	strengthMult = user.strengthMult;
    	minGauge = 999;
    	maxGauge = 999;
    	gauge = 999;
    }
	
	@ Override
    public void act(StageGameState b) {
    	super.act(b);
    	
    	System.out.println("&&& "+currentAttack);
    	
    	if (currentAttack == null) {
    		// remove if done attacking
    		if (hadAttack) {
    			b.removeModel(this);

    			LightFade l = (LightFade)LoadingQueue.quickLoad(new LightFade(), b);
    			l.model.setLocalTranslation(model.getLocalTranslation().add(new Vector3f(0, l.charHeight, 0)));

    			b.getRootNode().updateRenderState();
    		}
    	} else {
    		hadAttack = true;
    	}
    	
    }
	
}
