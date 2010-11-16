package com.googlecode.reaxion.game.model.attackobject;

import java.util.ArrayList;
import java.util.Stack;
import com.googlecode.reaxion.game.model.Model;
import com.googlecode.reaxion.game.model.character.Character;
import com.googlecode.reaxion.game.state.StageGameState;
import com.googlecode.reaxion.game.util.LoadingQueue;
import com.jme.math.Vector3f;

public class MagnetBomb extends AttackObject {

	public static final String filename = "magnet-bomb-core";
	protected static final int span = 600;
	protected static final float dpf = 10;

	private static final float seekSpeed = .5f;

	private Model[] glow = new Model[6];

	public MagnetBomb(Model m) {
		super(filename, dpf, m);
		flinch = true;
		lifespan = span;
	}

	public MagnetBomb(Model[] m) {
		super(filename, dpf, m);
		flinch = true;
		lifespan = span;
	}

	public void setUpGlow(StageGameState b) {
		for (int i = 0; i < glow.length; i++) {
			glow[i] = LoadingQueue.quickLoad(new Model("magnet-bomb-glow"), b);
			b.removeModel(glow[i]);
			model.attachChild(glow[i].model);
			glow[i].model.setLocalTranslation(new Vector3f(
					(i < 2) ? (.5f - (i % 2)) : 0,
					(i >= 2 && i < 4) ? (.5f - (i % 2)) : 0,
					(i >= 4) ? (.5f - (i % 2)) : 0));
		}
		b.getRootNode().updateRenderState();
	}

	@Override
	public void hit(StageGameState b, Character other) {
		b.removeModel(this);
	}

	@Override
	public void act(StageGameState b) {

		// set up glows if not already done
		if (glow[0] == null)
			setUpGlow(b);

		// billboard glows
		for (int i = 0; i < glow.length; i++)
			glow[i].billboard(b.getCamera(), true);

		int ttd = 30*(lifespan - lifeCount)/lifespan;
		
		// seek other magnet bombs
		ArrayList<Vector3f> p = new ArrayList<Vector3f>();
		Stack<MagnetBomb> closeBombs = new Stack<MagnetBomb>();
		Vector3f netAccel = new Vector3f(0f,0f,0f);
		for (Model m : b.getModels())
			if (m instanceof MagnetBomb && m != this) {
				MagnetBomb bomb = (MagnetBomb) m;

				Vector3f toBomb = bomb.model.getLocalTranslation().subtract(
						model.getLocalTranslation());
				int ttdOther = 30*(bomb.lifespan - bomb.lifeCount)/bomb.lifespan;
				
				if (toBomb.length() > 20) {
					netAccel = netAccel.add(toBomb.normalize().mult(seekSpeed*
							ttd * ttdOther / (toBomb.lengthSquared())));
				}
				else
					closeBombs.push(bomb);
			}

		// Find fakey center of mass
		Vector3f centerOfMass = new Vector3f(model.getLocalTranslation());
		boolean fakeyRotating = false;
		while (!closeBombs.isEmpty()) {
			fakeyRotating = true;
			MagnetBomb bomb = closeBombs.pop();
			
			Vector3f toBomb = model.getLocalTranslation().subtract(
					bomb.model.getLocalTranslation());
			int ttdOther = bomb.lifespan - bomb.lifeCount;
			
			centerOfMass = (centerOfMass.mult(ttd).add(
					bomb.model.getLocalTranslation().normalize()
							.mult(ttdOther)).divide(ttd + ttdOther));
		}

		if (fakeyRotating) {
			// Calculate centripetal force to make alpha = .5
			// alpha / r = a_tangential
			Vector3f distToCoM = model.getLocalTranslation().subtract(
					centerOfMass);
			netAccel.add(distToCoM.divide(distToCoM.mult(distToCoM)).mult(2f));
		}
		
		velocity = velocity.mult(.98f).add(netAccel);
		
		super.act(b);
	}
}
