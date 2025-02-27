package com.googlecode.reaxion.game.state;

import com.googlecode.reaxion.game.input.bindings.BattleGameStateBindings;
import com.googlecode.reaxion.game.mission.MissionManager;
import com.googlecode.reaxion.game.model.character.Character;
import com.googlecode.reaxion.game.util.Battle;
import com.jme.input.KeyBindingManager;
import com.jme.input.KeyInput;
import com.jmex.game.state.GameStateManager;

/**
 * {@code BattleGameState} extends {@code StageGameState} with functionality
 * dedicated to the battle system, such as battle parameters and opponents.
 * @author Khoa
 */
public class BattleGameState extends StageGameState {
	
	public static final String NAME = "battleGameState";
    
    public double targetTime = Double.NaN;
    public int expYield = 0;
    
    // time between final kill and results display
    public int victoryTime = 72;
    public int defeatTime = 72;
    private int resultCount = 0;
    
    private Character[] opponents;
    
    public BattleGameState() {
    	super();
    }
    
    public BattleGameState(Battle b) {
    	super(b);   	
    	
    	targetTime = b.getTargetTime();
    	expYield = b.getExpYield();
    	
    	assignOpponents(b.getOps());
    }
    
    @Override
	protected void init() {
		super.init();
		
		setName(NAME);
		
		startsBGM = true;
		endsBGM = false;
	}
    
    /**
     * Specifies the characters that must be defeated to win.
     * @param o Array of characters to be marked as opponents
     * @author Khoa
     *
     */
    public void assignOpponents(Character[] o) {
    	opponents = o;
    }
    
    /**
     * Returns pointer to opponents.
     * @author Khoa
     *
     */
    public Character[] getOpponents() {
    	return opponents;
    }

    @Override
	public void stateUpdate(float tpf) {
		super.stateUpdate(tpf);
		
		if (KeyBindingManager.getKeyBindingManager().isValidCommand(BattleGameStateBindings.RETURN_TO_HGS.toString(), false)) {
			endsBGM = true;
			setActive(false);
			MissionManager.endMission();
		}
	}

	@ Override
    protected void act() {
    	if (resultCount != 0)
    		timing = false;
    	
    	// Check winning/losing conditions
    	if (player.hp <= 0 && (partner == null || partner.hp <= 0)) {
    		System.out.println("You lose!");
			if (resultCount >= defeatTime)
				goToGameOver();
			else {
				hideOverlays();
				resultCount++;
			}
    	} else if (opponents != null) {
    		int sumHp = 0;
    		for (int i = 0; i<opponents.length; i++)
    			sumHp += Math.max(opponents[i].hp, 0);
    		if (sumHp <= 0) {
    			System.out.println("You win!");
    			if (resultCount >= victoryTime)
    				goToResults();
    			else {
    				hideOverlays();
    				resultCount++;
    			}
    		}
    	}
    	
    }
    
    /**
     * Ends this GameState and calls the {@code GameOverGameState}.
     */
    public void goToGameOver() {
    	GameOverState overState = new GameOverState(this);
		
		GameStateManager.getInstance().attachChild(overState);
		overState.setActive(true);
    	GameStateManager.getInstance().detachChild(this);
    	setActive(false);
    }
    
    /**
     * Ends this GameState and calls the {@code ResultsGameState}.
     */
    public void goToResults() {
    	ResultsGameState resultsState = new ResultsGameState(this);
		
		GameStateManager.getInstance().attachChild(resultsState);
		resultsState.setActive(true);
    	GameStateManager.getInstance().detachChild(this);
    	setActive(false);
    }
    
}