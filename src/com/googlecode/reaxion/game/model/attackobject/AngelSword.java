package com.googlecode.reaxion.game.model.attackobject;

import com.googlecode.reaxion.game.model.Model;
import com.googlecode.reaxion.game.model.character.Character;
import com.googlecode.reaxion.game.state.StageGameState;
import com.jme.math.Vector3f;

public class AngelSword extends AttackObject {
	
	public static final String filename = "angel-sword";
	protected static final float dpf = 25;
	protected static final float speed = .5f;
	protected static final int growFrames = 8;
	
	public AngelSword(Model m) {
    	super(filename, dpf, m);
    	flinch = true;
    }
	
	public AngelSword(Model[] m) {
    	super(filename, dpf, m);
    	flinch = true;
    }
	
	@Override
	public void hit(StageGameState b, Character other) {
		finish(b);
    }
	
	@ Override
    public void act(StageGameState b) {
        // billboarding
		billboard(b.getCamera(), false);
    	
    	if (lifeCount <= growFrames) {
    		model.setLocalScale(new Vector3f(1, (float)lifeCount/(float)growFrames, 1));
    	} else if (lifeCount >= growFrames) {
    		velocity = new Vector3f(0, -speed, 0);
    		if (model.getWorldTranslation().y <= -2.5)
    			finish(b);
    	}
        
    	super.act(b);
    }
	
}
