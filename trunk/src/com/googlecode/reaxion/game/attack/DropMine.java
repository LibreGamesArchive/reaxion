package com.googlecode.reaxion.game.attack;

import com.googlecode.reaxion.game.model.Model;
import com.googlecode.reaxion.game.model.attackobject.IceMine;
import com.googlecode.reaxion.game.model.prop.IceTrail;
import com.googlecode.reaxion.game.state.StageGameState;
import com.googlecode.reaxion.game.util.LoadingQueue;

public class DropMine extends Attack {
	private static final String n = "Icicle Mine";
	private static final int gc = 9;
	
	public DropMine() {
		name = n;
		description="Drops an ice mine that explodes upon anyone who walks on it.";
		gaugeCost = gc;
	}

	public DropMine(AttackData ad) {
		super(ad, gc);
		name = n;
	}
	
	public static void load() {
		LoadingQueue.push(new Model(IceTrail.filename));
	}
	
	@Override
	public void firstFrame(StageGameState b) {
		// make sure there aren't already six mines
		int count = 0;
		for (int i=0; i< b.getModels().size(); i++)
			if (b.getModels().get(i) instanceof IceMine && b.getModels().get(i).users.contains(getUsers()[getUsers().length-1]))
				count++;
		if (count >= 6) {
			character.gauge += gaugeCost;
			finish();
		} else {
			character.moveLock = true;
			character.jumpLock = true;
			character.animationLock = true;
			character.tagLock = true;
			character.play("smite", b.tpf);
		}
	}
	
	@Override
	public void nextFrame(StageGameState b) {
		if (phase == 0 && character.play("smite", b.tpf)) {		
			// calculate transformations			
			IceMine s = (IceMine)LoadingQueue.quickLoad(new IceMine(getUsers()), b);
			s.model.setLocalTranslation(character.model.getWorldTranslation());
			
			b.getRootNode().updateRenderState();			
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
