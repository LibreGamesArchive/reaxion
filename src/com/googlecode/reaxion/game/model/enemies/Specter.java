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
	
	private Model target;
	
	public Specter(Model[] m, Model t) {
    	// Load model
    	super("i_specter");
    	users = new ArrayList<Model>();
    	users.addAll(Arrays.asList(m));
    	trackable = false;
    	mass = 3;
    	
    	Character user = (Character)m[m.length-1];
    	target = t;
    	maxHp = 999;
    	hp = 999;
    	strengthMult = user.strengthMult;
    	minGauge = 999;
    	maxGauge = 999;
    	gauge = 999;
    	speed = user.speed;
    }
	
	@ Override
    public void act(StageGameState b) {
		// move towards opponent, if able
		velocity = b.getTarget().model.getWorldTranslation().subtract(model.getWorldTranslation()).normalize().mult(speed);
		velocity.y = 0;
		
    	super.act(b);
    	
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
