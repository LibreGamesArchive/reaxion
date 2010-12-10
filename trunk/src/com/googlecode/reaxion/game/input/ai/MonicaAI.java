package com.googlecode.reaxion.game.input.ai;

import java.util.ArrayList;

import com.googlecode.reaxion.game.attack.AttackData;
import com.googlecode.reaxion.game.attack.BulletStorm;
import com.googlecode.reaxion.game.attack.SlideDash;
import com.googlecode.reaxion.game.model.Model;
import com.googlecode.reaxion.game.model.character.Character;
import com.googlecode.reaxion.game.model.character.MajorCharacter;
import com.googlecode.reaxion.game.state.StageGameState;
import com.jme.math.Vector3f;

/**
 * Monica's encounter AI.
 *  <br>* Slide Dash
 *  <br>* Bullet Storm
 * @author Khoa
 *
 */
public class MonicaAI extends SmartAI {
	
	private final float radius = 80;
	private Vector3f lastSidestep = new Vector3f();
	
	
    public MonicaAI(Character c) {
    	super(c);
    }

    @Override
    public void makeCommands(StageGameState state) {
    	
    	if (character.currentAttack == null) {
    		
    		MajorCharacter player = state.getPlayer();
    		Vector3f toPlayer = character.model.getWorldTranslation().subtract(player.model.getWorldTranslation());

    		ArrayList<Model> threats = findThreats(state, radius, 12, .03f);
    		if (threats.size() == 0) {
    			velocity = fleeDistance(state, -1, false, true, true);
    			lastSidestep = new Vector3f();
    		} else {
    			Vector3f flee = fleeVelocity(state, radius, true, true, false);
    			ArrayList<Model> jump = checkJump(state, character.jump, 4, .05f);
    			System.out.println(threats.size()+" : "+flee.length()+" :: "+jump);
    			if (threats.size() >= 4 && flee.length() < 16 && jump.size() == 0) {
    				jump(4);
    			} else {
    				ArrayList<Model> homingThreats = new ArrayList<Model>();
    				if (!lastSidestep.equals(new Vector3f()))
    					homingThreats = findThreats(state, radius, 6, .9f);
    				if (homingThreats.size() != threats.size()) {
    					Vector3f bias = lastSidestep.add(flee);
    					velocity = sidestep(state, threats, 360, .001f, bias);
    					lastSidestep = velocity;
    				}
    			}
    			if (lastSidestep == null)
    				lastSidestep = new Vector3f();
    		}

    		Vector3f aW = avoidWall(state, .5f);
    		if (aW != null)
    			velocity = aW;

    		if (toPlayer.length() <= radius/3 && character.model.getWorldTranslation().y <= 0) {
    			if (character.gauge >= 10 && !character.flinching) {
    				velocity = toPlayer.mult(new Vector3f(-1, 0, -1)).normalize();
    				new SlideDash(new AttackData(character, player));
    			}
    		} else if (threats.size() < 2) {
    			if (character.gauge >= 10 && !character.flinching)
    				new BulletStorm(new AttackData(character, player));
    		}
    		
    		character.getVelocity().y = 0;

    	}
    	
    	super.makeCommands(state);
    }
    
}