package com.googlecode.reaxion.game.model.attackobject;

import com.googlecode.reaxion.game.model.Model;
import com.googlecode.reaxion.game.model.character.Character;
import com.googlecode.reaxion.game.model.prop.LightFade;
import com.googlecode.reaxion.game.state.StageGameState;
import com.googlecode.reaxion.game.util.ListFilter;
import com.googlecode.reaxion.game.util.LoadingQueue;
import com.jme.math.Vector3f;

/**
 * An angel that protects the user and heals their HP.
 */
public class Angel extends AttackObject {
	
	public static final String filename = "i_angel";
	protected static final float dpf = 0;
	
	private final double hpf = .07;
	
	private final float speed = .3f;
	
	private int hp;
	
	private Character user;
	
	public Angel(Model m) {
    	super(filename, dpf, m);
    	flinch = true;
    	user = ((Character)m);
    	hp = user.maxHp / 10;
    }
	
	public Angel(Model[] m) {
    	super(filename, dpf, m);
    	flinch = true;
    	user = ((Character)m[m.length - 1]);
    	hp = user.maxHp / 10;
    }
	
	@ Override
    public void act(StageGameState b) {
		if (lifeCount == 0)
			createLight(b);
		
		play("jump");
		
		// move between player and target
		Vector3f midpoint = b.getTarget().model.getWorldTranslation().add(b.getPlayer().model.getWorldTranslation()).divide(2);
		velocity = midpoint.subtract(model.getWorldTranslation());
		if (velocity.length() > speed) {
			velocity = velocity.normalize().mult(speed);
			rotate(velocity.mult(new Vector3f(1, 0, 1)));
		} else {
			rotate(b.getPlayer().rotationVector.mult(new Vector3f(1, 0, 1)));
		}
		
		// heal user
		b.getPlayer().heal(b, hpf);
		
		// check if a hit by another attack with linear approximation
    	Model[] collisions = getLinearModelCollisions(b, velocity, .5f, ListFilter.Filter.AttackObject, users);
        for (Model c : collisions) {
        	if (c instanceof AttackObject) {
        		// check if users include the other attack's users
        		boolean flag = false;
    			for (Model u : users) {
    				boolean flag2 = false;
    				for (Model o : c.users)
    					if (u == o) {
    						flag2 = true;
    						break;
    					}
    				if (!flag2)
    					flag = true;
    				break;
    			}
        		// lower hp
        		if (flag) {
        			((AttackObject)c).hit(b, user);
        			hp -= c.getDamage();
        			// check if dead
        			if (hp <= 0) {
        				createLight(b);
        				finish(b);
        			}
        		}
        	}
        }
        
    	super.act(b);
    }
	
	private void createLight(StageGameState b) {
		LightFade l = (LightFade)LoadingQueue.quickLoad(new LightFade(), b);
		l.model.setLocalTranslation(model.getLocalTranslation().add(0, l.charHeight/2, 0));
		b.getRootNode().updateRenderState();
	}
}
