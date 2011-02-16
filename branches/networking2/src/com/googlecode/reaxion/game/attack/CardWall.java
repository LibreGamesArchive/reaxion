package com.googlecode.reaxion.game.attack;

import com.googlecode.reaxion.game.model.Model;
import com.googlecode.reaxion.game.model.attackobject.ScreenCard;
import com.googlecode.reaxion.game.state.StageGameState;
import com.googlecode.reaxion.game.util.LoadingQueue;
import com.jme.math.FastMath;
import com.jme.math.Vector3f;
import com.radakan.jme.mxml.anim.MeshAnimationController;

public class CardWall extends Attack {
	private static final String n = "Card Wall";
	private static final int gc = 20;
	private static final int wallLength = 25;
	
	private ScreenCard[] cardWall1 = new ScreenCard[wallLength];
	private ScreenCard[] cardWall2 = new ScreenCard[wallLength];
	
	public CardWall() {
		name = n;
		description="Summons two parallel walls of cards.";
		gaugeCost = gc;
	}
	
	public CardWall(AttackData ad) {
		super(ad, gc);
		name = n;
	}
	
	public static void load() {
		LoadingQueue.push(new Model(ScreenCard.filename));
	}
	
	@Override
	public void firstFrame(StageGameState b) {
		character.moveLock = true;
		character.jumpLock = true;
		character.animationLock = true;
		character.tagLock = true;
		
		character.play("cast", b.tpf);
	}
	
	@Override
	public void nextFrame(StageGameState b) {
		MeshAnimationController animControl = (MeshAnimationController) character.model.getController(0);
		if (phase == 0 && animControl.getCurTime() + b.tpf >= animControl.getAnimationLength("cast")/2) {
			
			// calculate transformations
			Vector3f rotation = character.rotationVector;
			float angle = FastMath.atan2(rotation.x, rotation.z);
			Vector3f translation1 = new Vector3f(4*FastMath.cos(angle), 0, -4*FastMath.sin(angle));
			Vector3f translation2 = new Vector3f(-4*FastMath.cos(angle), 0, 4*FastMath.sin(angle));
			Vector3f spacing = new Vector3f(4*FastMath.sin(angle), 0, 4*FastMath.cos(angle));
			
			for (int i=0; i<cardWall1.length; i++) {
				cardWall1[i] = (ScreenCard)LoadingQueue.quickLoad(new ScreenCard(getUsers()), b);
				cardWall2[i] = (ScreenCard)LoadingQueue.quickLoad(new ScreenCard(getUsers()), b);
				cardWall1[i].roll = FastMath.PI/2;
				cardWall2[i].roll = FastMath.PI/2;
				cardWall1[i].rotate(new Vector3f(FastMath.sin(angle),0,FastMath.cos(angle)));
				cardWall2[i].rotate(new Vector3f(FastMath.sin(angle),0,FastMath.cos(angle)));
				cardWall1[i].model.setLocalTranslation(character.model.getWorldTranslation().add(translation1).add(spacing.mult(i))); 
				cardWall2[i].model.setLocalTranslation(character.model.getWorldTranslation().add(translation2).add(spacing.mult(i))); 
				
			}
			
			b.getRootNode().updateRenderState();
			
			phase++;
		} else if (character.play("cast", b.tpf)) {
			finish();
		}
	}
	
	@Override
	public void finish() {
		super.finish();
		character.moveLock = false;
		character.jumpLock = false;
		character.animationLock = false;
		character.tagLock = false;
	}
	
}
