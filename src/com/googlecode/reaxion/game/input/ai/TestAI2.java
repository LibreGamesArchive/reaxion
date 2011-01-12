package com.googlecode.reaxion.game.input.ai;

import com.googlecode.reaxion.game.attack.AttackData;
import com.googlecode.reaxion.game.attack.MagneticWorld;
import com.googlecode.reaxion.game.input.AIInput;
import com.googlecode.reaxion.game.model.character.Character;
import com.googlecode.reaxion.game.model.character.MajorCharacter;
import com.googlecode.reaxion.game.state.StageGameState;
import com.jme.math.Vector3f;

/**
 * AI input made to do nothing but continuously shoot bullets.
 * @author Khoa
 *
 */
public class TestAI2 extends AIInput {

	
    public TestAI2(Character c) {
    	super(c);
    }

    @Override
    public void makeCommands(StageGameState state) {
    	MajorCharacter player = state.getPlayer();
    	character.setVelocity(new Vector3f());
    	// attack whenever possible
    	if (character.gauge >= 6 && !character.flinching && character.currentAttack == null)
    		new MagneticWorld(new AttackData(character, player));
    }
    
}