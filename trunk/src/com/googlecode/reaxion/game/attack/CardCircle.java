package com.googlecode.reaxion.game.attack;

import com.googlecode.reaxion.game.model.Model;
import com.googlecode.reaxion.game.model.attackobject.CircleCard;
import com.googlecode.reaxion.game.state.StageGameState;
import com.googlecode.reaxion.game.util.LoadingQueue;
import com.jme.math.FastMath;
import com.jme.math.Vector3f;

/**
 * Surrounds the opponent with a ring of cards.
 */
public class CardCircle extends Attack {	
	private static final String n = "Card Circle";
	private static final int gc = 21;
	
	private CircleCard[] card = new CircleCard[6];
	
	public CardCircle() {
		name = n;
		gaugeCost = gc;
		description = "Surrounds the opponent with a ring of cards.";
	}
	
	public CardCircle(AttackData ad) {
		super(ad, gc);
		name = n;
	}
	
	public static void load() {
		LoadingQueue.push(new Model(CircleCard.filename));
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
				card[i] = (CircleCard)LoadingQueue.quickLoad(new CircleCard(getUsers(), b.getTarget()), b);
				card[i].theta = 2*FastMath.PI/card.length*i;
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
