package com.googlecode.reaxion.game.state;

import com.googlecode.reaxion.game.model.character.MajorCharacter;
import com.googlecode.reaxion.game.util.Battle;

public class ServerBattleGameState extends BattleGameState {

	public static final String NAME = "serverBattleGameState";
	
	protected MajorCharacter player2;
	protected Class[] player2Attacks;
	protected MajorCharacter partner2;
	protected Class[] partner2Attacks;
	
	public ServerBattleGameState() {
		super();
	}
	
	public ServerBattleGameState(Battle b) {
	}
	
	@ Override
    protected void act() {
    	// Check winning/losing conditions
    	if (player.hp <= 0 && partner.hp <= 0) {
    		System.out.println("You lose!");
			if (resultCount >= defeatTime)
				goToGameOver();
			else {
				hideOverlays();
				resultCount++;
			}
    	} else if (player2.hp <= 0 && partner2.hp <= 0) {
    		System.out.println("You win!");
    		if (resultCount >= victoryTime)
    			goToResults();
    		else {
    			hideOverlays();
    			resultCount++;
    		}
    	}
    	
    }
	
	@Override
	protected void checkPause() {
		
	}

	public void assignTeam2(MajorCharacter p1, Class[] q1, MajorCharacter p2, Class[] q2) {
    	// Assign players to local objects
    	player2 = p1;
    	player2Attacks = q1;
    	partner2 = p2;
    	partner2Attacks = q2;
    	
    	// Update player stats
    	player2.updateStats();
    	partner2.updateStats();
    	
    	// Remove the inactive character
    	removeModel(partner2);
    }
	
	public void tagSwitch2() {
    	if (partner2 != null && partner2.hp > 0) {
    		// Tell the current attack a swap is in progress
    		if (player2.currentAttack != null)
    			player2.currentAttack.switchOut(this);
    		MajorCharacter p = player2;
    		player2 = partner2;
    		partner2 = p;
    		Class[] a = player2Attacks;
    		player2Attacks = partner2Attacks;
    		partner2Attacks = a;
    		
    		// Attach the active character
    		addModel(player2);
    		// Synchronize position
    		player2.model.setLocalTranslation(partner2.model.getLocalTranslation().clone());
    		player2.model.setLocalRotation(partner2.model.getLocalRotation().clone());
    		player2.rotationVector = partner2.rotationVector;
    		player2.gravVel = partner2.gravVel;
    		// Remove the inactive character
    		removeModel(partner2);

    		rootNode.updateRenderState();
    	}
    }
	
}
