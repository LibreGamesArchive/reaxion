package com.googlecode.reaxion.game.model.attackobject;

import com.googlecode.reaxion.game.model.Model;
import com.googlecode.reaxion.game.model.character.Character;
import com.googlecode.reaxion.game.state.StageGameState;
import com.jme.math.Vector3f;

public class ScreenCard extends AttackObject {
	
	public static final String filename = "card";
	protected static final float dpf = 0;
	
	public ScreenCard(Model m) {
    	super(filename, dpf, m);
    	flinch = true;
    }
	
	public ScreenCard(Model[] m) {
    	super(filename, dpf, m);
    	flinch = true;
    }
	
	@Override
	public void hit(StageGameState b, Character other) {
		
    }
	
	@ Override
    public void act(StageGameState b) {
        
        // check if a hit by another attack with linear approximation
    	Model[] collisions = getLinearModelCollisions(b, velocity, .5f);
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
        		// kill this card
        		if (flag) {
        			((AttackObject)c).hit(b, (Character)users.get(users.size()-1));
        			b.removeModel(this);
        		}
        		
        	// check for character hits
        	} else if (c instanceof Character && !users.contains(c))
        		((Character)c).hit(b, this);
        }
        
        // actually move
        Vector3f loc = model.getLocalTranslation();
        loc.addLocal(velocity);
        model.setLocalTranslation(loc);
        
    }
	
}
