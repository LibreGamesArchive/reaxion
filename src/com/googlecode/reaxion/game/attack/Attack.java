package com.googlecode.reaxion.game.attack;

import com.googlecode.reaxion.game.audio.AudioPlayer;
import com.googlecode.reaxion.game.audio.SoundEffectType;
import com.googlecode.reaxion.game.model.Model;
import com.googlecode.reaxion.game.model.character.Character;
import com.googlecode.reaxion.game.model.character.MajorCharacter;
import com.googlecode.reaxion.game.state.StageGameState;
import com.jme.math.Vector3f;

/**
 * Temporarily acts upon the attacking {@code Character} for the duration
 * of the attack, controlling animations and creating {@code AttackObjects}.
 * All attacks should extend this class.
 * 
 * @author Khoa Ha
 *
 */
public class Attack {
	
	public String name;
	public String description;
	
	public static int gaugeCost;
	
	// Some useful variables
	protected int frameCount = 0;
	protected int phase = 0;
	
	/**
	 * The one that initiated the attack
	 */
	protected Character character;
	
	/**
	 * Friendly characters that are immune
	 */
	protected Character[] friends;
	
	// creating an Attack with these constructors is useless, as they only
	// exist to facilitate backend stuff
	public Attack() {name=""; description="";}
	public Attack(AttackData ad) {}
	
	protected int sfxIndex = -1;
	
	public Attack(AttackData ad, int gc) {
		character = ad.character;
		friends = ad.friends;
		character.currentAttack = this;
		gaugeCost = gc;
		checkGauge();
	}
	
	/**
	 * Contains all the commands to load required AttackObjects, override to
	 * add functionality.
	 */
	public static void load() {
	}
	
	/**
	 * Events to be executed on each act() call for character
	 */
	public void enterFrame(StageGameState b) {
		if (frameCount == 0) {
			firstFrame(b);
		} else {
			nextFrame(b);
		}
		frameCount++;
	}
	
	/**
	 * Events to be executed on the first frame of the attack, override to add
	 * functionality.
	 */
	public void firstFrame(StageGameState b) {
		
	}
	
	/**
	 * Events to be executed on subsequent frames of the attack, override to add
	 * functionality.
	 */
	public void nextFrame(StageGameState b) {
		
	}
	
	/**
	 * Called when the attacking character is interrupted by Model m, override to
	 * add functionality. Default functions as if character was hit as normal.
	 */
	public void interrupt(StageGameState b, Model m) {
		character.reactHit(b, m);
	}
	
	/**
	 * Called when a tag switch is executed while the attack is in progress.
	 * Override to add functionality. Attack finishes by default.
	 */
	public void switchOut(StageGameState b) {
		finish();
	}
	
	/**
	 * Called to dismiss the attack, usually only done by the attack itself.
	 * Override to add functionality.
	 */
	public void finish() {
		character.currentAttack = null;
	}
	
	/**
	 * Checks if the character's gauge is high enough to use the attack. If so,
	 * deduct it accordingly. Otherwise, finish the attack before it starts. Return
	 * whether deduction was successful or not.
	 */
	protected boolean checkGauge() {
		if (character.gauge >= gaugeCost) {
			character.gauge = Math.max(Math.min(character.minGauge, character.gauge) - gaugeCost, 0);
			return true;
		} else {
			finish();
			return false;
		}
	}
	
	/**
	 * Convenience function that returns a list of users, consisting of the casting
	 * character and any friends.
	 */
	protected Character[] getUsers() {
		Character[] users = new Character[friends.length+1]; //Arrays.copyOf(friends, friends.length+1);
		for(int i = 0; i < friends.length; i++)
			users[i] = friends[i];
		users[users.length-1] = character;
		return users;
	}
	
	/**
	 * Convenience function to ensure character is grounded, otherwise finishes
	 * attack and restores GP.
	 */
	protected void validateGround() {
		if (character.model.getWorldTranslation().y > 0) {
			character.gauge += gaugeCost;
			finish();
		}
	}
	
	/**
	 * Finds which sound effect to play given the character that triggered it.
	 */
	protected void triggerSoundEffect(SoundEffectType[] sfxTypes, boolean repeating) {
		if(character instanceof MajorCharacter) {
			MajorCharacter temp = (MajorCharacter) character;
			for(int i = 0; i < sfxTypes.length; i++) {
				if(temp.info.hasSoundEffectType(sfxTypes[i])) {
					Vector3f loc = temp.model.getLocalTranslation();
					String filename = temp.info.getSoundEffect(sfxTypes[i]);
					if(repeating)
						AudioPlayer.playRepeatingSoundEffect(filename, loc.x, loc.y, loc.z);
					else
						AudioPlayer.playSoundEffect(filename, loc.x, loc.y, loc.z);
					sfxIndex = i;
					break;
				}
			}			
		}
	}
	
	protected void stopSoundEffect(SoundEffectType[] sfxTypes) {
		if(sfxIndex != -1) {
			MajorCharacter temp = (MajorCharacter) character;
			AudioPlayer.stopRepeatingSoundEffect(temp.info.getSoundEffect(sfxTypes[sfxIndex]));
		}
	}
	
}
