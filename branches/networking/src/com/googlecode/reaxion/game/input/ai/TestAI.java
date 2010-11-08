package com.googlecode.reaxion.game.input.ai;

import java.awt.geom.Point2D;

import com.googlecode.reaxion.game.attack.AngelRain;
import com.googlecode.reaxion.game.attack.AttackData;
import com.googlecode.reaxion.game.attack.SpinLance;
import com.googlecode.reaxion.game.input.AIInput;
import com.googlecode.reaxion.game.model.character.Character;
import com.googlecode.reaxion.game.model.character.MajorCharacter;
import com.googlecode.reaxion.game.state.StageGameState;
import com.jme.math.FastMath;
import com.jme.math.Vector3f;

/**
 * AI input made for demo purposes. Only has two attacks, <i>Spin Lance</i> and
 * <i>Angel Rain</i>. Works best with the <i>After Image</i> ability.
 * @author Khoa
 *
 */
public class TestAI extends AIInput {
	
	private boolean nextLance;
	private Vector3f vel;
	
    public TestAI(Character c) {
    	super(c);
    	nextLance = randomBoolean();
    }

    @Override
    public void makeCommands(StageGameState state) {
    	MajorCharacter player = state.getPlayer();
    	
    	// reset velocity so as to not interfere with bounds checking
    	character.setVelocity(new Vector3f());
    	
    	if (character.hp/character.maxHp > .67) {
    		// change direction at random
    		if (timer % 80 == 0) {
    			vel = randomVector();
    			vel.y = 0;
    		}
    		
    		// attack whenever possible
    		if (character.gauge >= 5 && !character.flinching && character.currentAttack == null)
    			new SpinLance(new AttackData(character, player));
    		
    	} else if (character.hp/character.maxHp > .33) {
    		Vector3f t = character.model.getWorldTranslation();
    		Point2D.Float[] p = state.getStage().bound(character);
    		
    		// check for wall collision
    		if (p.length >= 2)
    			avoidWall(t, p);
    		
    		// change direction at random
    		if (timer % 80 == 0) {
    			vel = randomVector();
    			vel.y = 0;
    		}
    		
    		// attack whenever possible
    		if (nextLance) {
    			if (character.gauge >= 5 && !character.flinching && character.currentAttack == null) {
    				new SpinLance(new AttackData(character, player));
    				nextLance = randomBoolean();
    			}
    		} else {
    			if (character.gauge >= 22 && !character.flinching && character.currentAttack == null) {
    				new AngelRain(new AttackData(character));
    				nextLance = randomBoolean();
    			}
    		}
    		
    	} else {
    		Vector3f t = character.model.getWorldTranslation();
    		Point2D.Float[] p = state.getStage().bound(character);
    		
    		// check for wall collision
    		if (p.length >= 2)
    			avoidWall(t, p);
    		
    		// run away from player, with slight offset
    		if (timer  == 0) {
    			float angle = FastMath.nextRandomFloat()*FastMath.PI*2;
    			Vector3f offset = new Vector3f(2*FastMath.cos(angle), 0, 2*FastMath.sin(angle));
    			vel = player.getVelocity().add(offset);
    		}
    		
    		// attack whenever possible
    		if (nextLance) {
    			if (character.gauge >= 5 && !character.flinching && character.currentAttack == null) {
    				new SpinLance(new AttackData(character, player));
    				nextLance = (Math.random() >= .8);
    			}
    		} else {
    			if (character.gauge >= 22 && !character.flinching && character.currentAttack == null) {
    				new AngelRain(new AttackData(character));
    				nextLance = (Math.random() >= .8);
    			}
    		}
    		
    	}
    	
    	//actually set the velocity
    	move(vel);
    	
    	super.makeCommands(state);
    }
    
    private void avoidWall(Vector3f t, Point2D.Float[] p) {
    	// find range of departure angles
		float minAngle = FastMath.PI/2;
		float maxAngle = 0;
		for (Point2D.Float o : p) {
			float a = FastMath.atan2(o.y, o.x);
			minAngle = Math.min(minAngle, a);
			maxAngle = Math.max(maxAngle, a);
		}
		// set velocity to a random angle within range
		float angle = FastMath.nextRandomFloat()*(maxAngle - minAngle) + minAngle;
		vel = new Vector3f(FastMath.cos(angle), 0, FastMath.sin(angle));
    }
    
}