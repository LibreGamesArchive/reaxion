package com.googlecode.reaxion.game.model.attackobject;

import com.googlecode.reaxion.game.model.Model;
import com.googlecode.reaxion.game.model.character.Character;
import com.googlecode.reaxion.game.state.StageGameState;
import com.googlecode.reaxion.game.util.LoadingQueue;
import com.jme.math.FastMath;
import com.jme.math.Vector3f;

public class SoundWall extends AttackObject {
	
	public static final String filename = "sound-barrier";
	protected static final int span = 600;
	protected static final int growTime = 12;
	protected static final float dpf = 9;
	
	private final float timeScale = .1f;
	
	private final float innerSize = .8f;
	private final float lowSize = .6f;
	
	private Model wave;
	
	public SoundWall(Model m) {
    	super(filename, dpf, m);
    	flinch = true;
    	lifespan = span;
    }
	
	public SoundWall(Model[] m) {
    	super(filename, dpf, m);
    	flinch = true;
    	lifespan = span;
    }
	
	public void setUpWave(StageGameState b) {
		wave = LoadingQueue.quickLoad(new Model("sound-barrier"), null);
		model.attachChild(wave.model);
		b.getRootNode().updateRenderState();
	}
	
	@Override
	public void hit(StageGameState b, Character other) {
		b.removeModel(this);
    }
	
	@ Override
    public void act(StageGameState b) {
		// set up glows if not already done
		if (wave == null)
			setUpWave(b);
		
		// scale
		float outerScale = FastMath.cos(lifeCount*timeScale)*(1-lowSize)+lowSize;
		
		// grow/shrink
		if (lifeCount < growTime)
			outerScale *= ((float)(lifeCount+1)/(float)(growTime+1));
		else if (lifeCount > lifespan - growTime)
			outerScale *= ((float)(lifespan - lifeCount + 1)/(float)(growTime + 1));
		
		float innerScale = 1/outerScale*innerSize*(FastMath.sin(lifeCount*timeScale)*(1-lowSize)+lowSize);
		model.setLocalScale(new Vector3f(outerScale, outerScale, 1));
		wave.model.setLocalScale(new Vector3f(innerScale, innerScale, 1));
		wave.model.setLocalTranslation(0, 0, FastMath.sin(lifeCount*timeScale));
		
    	super.act(b);
    }
	
}
