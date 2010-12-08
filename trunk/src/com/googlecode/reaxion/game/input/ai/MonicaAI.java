package com.googlecode.reaxion.game.input.ai;

import com.googlecode.reaxion.game.model.character.Character;
import com.googlecode.reaxion.game.model.character.MajorCharacter;
import com.googlecode.reaxion.game.state.StageGameState;
import com.jme.math.Vector3f;

/**
 * Monica's encounter AI.
 *  <br>* Bullet Wave
 *  <br>* Slide Dash
 *  <br>* Bullet Storm
 * @author Khoa
 *
 */
public class MonicaAI extends SmartAI {
	
	private final float radius = 100;
	
    public MonicaAI(Character c) {
    	super(c);
    }

    @Override
    public void makeCommands(StageGameState state) {
    	MajorCharacter player = state.getPlayer();
    
    	Vector3f r = fleeVelocity(state, -1, true, true, false);
    	if (!checkVector(r)) {
    		if (findThreats(state, radius, 0).size() > 0)
    			r = fleeDistance(state, radius, false, true, true);
    	}
    	
    	if (checkVector(r))
    		velocity = r;
    	
    	Vector3f aW = avoidWall(state, .5f);
    	if (aW != null)
    		velocity = aW;
    	
    	super.makeCommands(state);
    }
    
}