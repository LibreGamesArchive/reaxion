package com.googlecode.reaxion.game.attack;

import com.googlecode.reaxion.game.model.Model;
import com.googlecode.reaxion.game.model.attackobject.Bullet;
import com.googlecode.reaxion.game.model.attackobject.ScreenCard;
import com.googlecode.reaxion.game.state.StageGameState;
import com.googlecode.reaxion.game.util.LoadingQueue;
import com.jme.math.FastMath;
import com.jme.math.Vector3f;

/**
 * Conceals the user behind a screen of cards.
 */
public class CardScreen extends Attack {	
	private static final String n = "Card Screen";
	private static final int gc = 7;
	
	private ScreenCard[] card = new ScreenCard[5];
	
	public CardScreen() {
		name = n;
		gaugeCost = gc;
		description = "Conceals the user behind a screen of cards.";
	}
	
	public CardScreen(AttackData ad) {
		super(ad, gc);
		name = n;
	}
	
	public static void load() {
		LoadingQueue.push(new Model(ScreenCard.filename));
	}
	
	@Override
	public void firstFrame(StageGameState b) {
		character.jumpLock = true;
		character.animationLock = true;
		character.play("shootUp", b.tpf);
	}
	
	@Override
	public void nextFrame(StageGameState b) {
		if (phase == 0 && character.play("shootUp", b.tpf)) {
			
			// calculate transformations
			Vector3f rotation = character.rotationVector;
			float angle = FastMath.atan2(rotation.x, rotation.z);
			Vector3f translation = new Vector3f(4*FastMath.sin(angle), 0, 4*FastMath.cos(angle));
			Vector3f spacing = new Vector3f(4*FastMath.sin(angle+FastMath.PI/2), 0, 4*FastMath.cos(angle+FastMath.PI/2));
			
			for (int i=0; i<card.length; i++) {
				card[i] = (ScreenCard)LoadingQueue.quickLoad(new ScreenCard(getUsers()), b);

				card[i].rotate(rotation);
				card[i].setVelocity(rotation.mult(character.speed));
				card[i].model.setLocalTranslation(character.model.getWorldTranslation().add(translation).add(spacing.mult(card.length/2-i)));
			}
			
			b.getRootNode().updateRenderState();
			
			character.play("shootDown", b.tpf);
			phase++;
		} else if (phase == 1 && character.play("shootDown", b.tpf)) {
			finish();
		}
	}
	
	@Override
	public void finish() {
		super.finish();
		character.jumpLock = false;
		character.animationLock = false;
	}
	
}
