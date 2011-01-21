package com.googlecode.reaxion.game.attack;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;

import com.googlecode.reaxion.game.audio.AudioPlayer;
import com.googlecode.reaxion.game.audio.SoundEffectType;
import com.googlecode.reaxion.game.model.Model;
import com.googlecode.reaxion.game.model.character.Character;
import com.googlecode.reaxion.game.model.character.MajorCharacter;
import com.googlecode.reaxion.game.state.StageGameState;
import com.jme.math.Vector3f;

/**
 * Temporarily acts upon the attacking {@code Character} for the duration of the
 * attack, controlling animations and creating {@code AttackObjects}. All
 * attacks should extend this class.
 * 
 * @author Khoa Ha
 * 
 */
public class Attack {

	public AttackDisplayInfo info;

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

	/**
	 * The target of the attack
	 */
	protected Model target;

	// creating an Attack with these constructors is useless, as they only
	// exist to facilitate backend stuff
	public Attack() {
		info = new AttackDisplayInfo();
	}

	public Attack(AttackData ad) {
		info = new AttackDisplayInfo();
	}

	public Attack(AttackData ad, int gc) {
		character = ad.character;
		friends = ad.friends;
		target = ad.target;
		character.currentAttack = this;

		info = new AttackDisplayInfo();
		info.gaugeCost = gc;
		checkGauge();
	}

	public Attack(AttackData ad, int gc, String name) {
		this(ad, gc);
		info.name = name;
	}

	/**
	 * Contains all the commands to load required AttackObjects, override to add
	 * functionality.
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
	 * Called when the attacking character is interrupted by Model m, override
	 * to add functionality. Default functions as if character was hit as
	 * normal.
	 */
	public void interrupt(StageGameState b, Model m) {
		character.reactHit(b, m);
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
	 * deduct it accordingly. Otherwise, finish the attack before it starts.
	 * Return whether deduction was successful or not.
	 */
	protected boolean checkGauge() {
		if (character.gauge >= info.gaugeCost) {
			character.gauge = Math.max(Math.min(character.minGauge, character.gauge)
					- info.gaugeCost, 0);
			return true;
		} else {
			finish();
			return false;
		}
	}

	/**
	 * Convenience function that returns a list of users, consisting of the
	 * casting character and any friends.
	 */
	protected Character[] getUsers() {
		Character[] users = new Character[friends.length + 1]; // Arrays.copyOf(friends,
																// friends.length+1);
		for (int i = 0; i < friends.length; i++)
			users[i] = friends[i];
		users[users.length - 1] = character;
		return users;
	}

	/**
	 * Convenience function to ensure character is grounded, otherwise finishes
	 * attack and restores GP.
	 */
	protected void validateGround() {
		if (character.model.getWorldTranslation().y > 0) {
			character.gauge += info.gaugeCost;
			finish();
		}
	}

	/**
	 * Finds which sound effect to play given the character that triggered it.
	 */
	protected void triggerSoundEffect(SoundEffectType[] sfxTypes) {
		if (character instanceof MajorCharacter) {
			MajorCharacter temp = (MajorCharacter) character;
			for (int i = 0; i < sfxTypes.length; i++) {
				if (temp.info.hasSoundEffectType(sfxTypes[i])) {
					Vector3f loc = temp.model.getLocalTranslation();
					AudioPlayer.playSoundEffect(temp.info.getSoundEffect(sfxTypes[i]), loc.x,
							loc.y, loc.z);
					break;
				}
			}
		}
	}

	public static AttackDisplayInfo[] toAttackDisplayInfoArray(Class[] attacks) {
		AttackDisplayInfo[] adi = new AttackDisplayInfo[attacks.length];
		try {
			for (int i = 0; i < adi.length; i++)
				adi[i] = ((Attack) attacks[i].
						getConstructors()[0].
						newInstance()).
						info;
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("ADI: "+Arrays.toString(adi));
		return adi;
	}
}
