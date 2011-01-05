package com.googlecode.reaxion.game.attack;

import com.googlecode.reaxion.game.model.Model;
import com.googlecode.reaxion.game.model.attackobject.Angel;
import com.googlecode.reaxion.game.model.attackobject.Bullet;
import com.googlecode.reaxion.game.model.attackobject.Starlight;
import com.googlecode.reaxion.game.model.prop.LightFade;
import com.googlecode.reaxion.game.state.StageGameState;
import com.googlecode.reaxion.game.util.LoadingQueue;
import com.jme.math.FastMath;
import com.jme.math.Vector3f;

public class Guardian extends Attack {
	
	private static final String n = "Guardian";
	private static final int gc = 20;
	
	private final float radius = 7;
	
	public Guardian() {
		name = n;
		gaugeCost = gc;
		description = "Concentrates light into a solid form to protect the user.";
	}
	
	public Guardian(AttackData ad) {
		super(ad, gc);
		name = n;
	}
	
	public static void load() {
		LoadingQueue.push(new Model(Angel.filename));
	}
	
	@Override
	public void firstFrame(StageGameState b) {
		// make sure there isn't already an angel
		boolean flag = false;
		for (int i=0; i< b.getModels().size(); i++)
			if (b.getModels().get(i) instanceof Angel && b.getModels().get(i).users.contains(getUsers()[getUsers().length-1])) {
				flag = true;
				break;
			}
		if (flag) {
			character.gauge += gaugeCost;
		} else {
			// create the angel
			float angle = FastMath.nextRandomFloat()*FastMath.PI*2;
			Angel angel = (Angel)LoadingQueue.quickLoad(new Angel(getUsers()), b);
			angel.model.setLocalTranslation(b.getPlayer().model.getLocalTranslation().add(radius*FastMath.sin(angle), 0, radius*FastMath.cos(angle)));
			b.getRootNode().updateRenderState();
		}
		finish();
	}
	
	@Override
	public void finish() {
		super.finish();
	}
	
}
