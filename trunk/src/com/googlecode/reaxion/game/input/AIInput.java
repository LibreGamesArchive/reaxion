package com.googlecode.reaxion.game.input;

import com.googlecode.reaxion.game.model.character.Character;
import com.googlecode.reaxion.game.state.StageGameState;
import com.jme.math.FastMath;
import com.jme.math.Vector3f;

/**
 * An alternative, computer-controlled input system that allows characters to move,
 * attack, etc. It should closely mirror {@code characterInput}.
 * @author Khoa
 *
 */
public abstract class AIInput {
	
	/**
	 * Min and max levels of jump strength, as fractions of character's jump,
	 * dictated by how long the character holds the key
	 */
	private final int[] jumpLevels = {2, 4};

	protected Character character;
	
	protected int timer = 0;
	
    /**
     * Supply the node to control and the api that will handle input creation.
     * @param b the current BattleGameState
     * @param q the array of classes of attacks
     */
    public AIInput(Character c) {
    	character = c;
    }

    /**
     * Must be called during {@code act()} by the affected {@code Character}.
     * Override to add specific logic.
     * @author Khoa
     */
    public void makeCommands(StageGameState state) {
    	timer++;
    }
    
    protected void move(Vector3f v) {
    	// assign vector to character
    	if (!character.moveLock && !character.flinching)
    		character.setVelocity(v.normalize().mult(character.speed));
    }
    
    protected void jump(int jumpCount) {
    	if (!character.jumpLock && !character.flinching && character.model.getWorldTranslation().y <= 0) {
    		jumpCount = Math.max(Math.min(jumpCount, jumpLevels[1]), jumpLevels[0]);
    		character.gravVel = character.jump * jumpLevels[0]/jumpLevels[1];
		}
    }
    
    /**
     * Convenience method to check whether a vector is non-null and non-zero.
     */
    protected boolean checkVector(Vector3f v) {
    	return v != null && !v.equals(new Vector3f());
    }
    
    /**
     * Convenience method to return a random boolean.
     */
    protected boolean randomBoolean() {
    	return (Math.random() >= .5);
    }
    
    /**
     * Convenience method to create a normalized vector with random components.
     */
    protected Vector3f randomVector() {
    	return (new Vector3f(FastMath.nextRandomFloat()*2-1, FastMath.nextRandomFloat()*2-1, FastMath.nextRandomFloat()*2-1).normalize());
    }
    
    /**
     * Execute attack index in parameter
     */
    /*
    private void executeAttack(int ind) {
    	if (!character.flinching && character.currentAttack == null) {
			try {
				if (attacks[ind] != null)
					attacks[ind].getConstructors()[0].newInstance(new AttackData(character, state.getTarget()));
			} catch (Exception e) {
				System.out.println("Fatal error: Attack array parameter was not an Attack.");
				e.printStackTrace();
			}
		}
    }
    */
}