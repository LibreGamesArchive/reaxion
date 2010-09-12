package com.googlecode.reaxion.game.input.ai;

import com.googlecode.reaxion.game.attack.AttackData;
import com.googlecode.reaxion.game.attack.ShootBullet;
import com.googlecode.reaxion.game.input.AIInput;
import com.googlecode.reaxion.game.model.character.Character;
import com.googlecode.reaxion.game.model.character.MajorCharacter;
import com.googlecode.reaxion.game.state.BattleGameState;

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
    public void makeCommands(BattleGameState state) {
    	MajorCharacter player = state.getPlayer();
    	
    	// attack whenever possible
    	if (character.gauge >= 6 && !character.flinching && character.currentAttack == null)
    		new ShootBullet(new AttackData(character, player));
    }
    
}