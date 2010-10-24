package com.googlecode.reaxion.game.model.attackobject;

import java.util.ArrayList;

import com.googlecode.reaxion.game.model.Model;
import com.googlecode.reaxion.game.model.character.Character;
import com.googlecode.reaxion.game.state.StageGameState;
import com.googlecode.reaxion.game.util.LoadingQueue;
import com.jme.math.FastMath;
import com.jme.math.Vector3f;

public class MagnetBomb extends AttackObject {
	
	public static final String filename = "magnet-bomb-core";
	protected static final int span = 300;
	protected static final float dpf = 10;
	
	private static final float seekSpeed = 4f;
	
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
		for (int i=0; i<glow.length; i++) {
			glow[i] = LoadingQueue.quickLoad(new Model("magnet-bomb-glow"), b);
			b.removeModel(glow[i]);
			model.attachChild(glow[i].model);
			glow[i].model.setLocalTranslation(new Vector3f((i < 2)?(.5f - (i%2)):0, (i >= 2 && i < 4)?(.5f - (i%2)):0, (i >= 4)?(.5f - (i%2)):0));
		}
		b.getRootNode().updateRenderState();
	}
	
	@Override
	public void hit(StageGameState b, Character other) {
		b.removeModel(this);
    }
	
	@ Override
    public void act(StageGameState b) {
		// set up glows if not already done
		if (glow[0] == null)
			setUpGlow(b);
		
		// billboard glows
		for (int i=0; i<glow.length; i++)
			glow[i].billboard(b.getCamera(), true);
		
		// seek other magnet bombs
		ArrayList<Vector3f> p = new ArrayList<Vector3f>();
		for (Model m : b.getModels())
			if (m instanceof MagnetBomb && m != this)
				p.add(m.model.getLocalTranslation());
		if (p.size() > 0) {
			Vector3f v = new Vector3f();
			for (Vector3f f : p)
				v = v.add(f);
			v = v.divide(p.size()).subtract(model.getLocalTranslation());
			if (v.length() > seekSpeed)
				velocity = v.normalize().mult(seekSpeed);
			else
				velocity = v;
		}
		
    	super.act(b);
    }
	
}
