package com.googlecode.reaxion.game.model.attackobject;

import com.googlecode.reaxion.game.model.Model;
import com.googlecode.reaxion.game.model.character.Character;
import com.googlecode.reaxion.game.state.StageGameState;
import com.googlecode.reaxion.game.util.LoadingQueue;
import com.jme.math.FastMath;
import com.jme.math.Vector3f;

public class RoamingCloud extends AttackObject {
	
	public static final String filename = "bound-sphere";
	protected static final int span = 600;
	protected static final float dpf = 0;
	
	private int strikeDelay = 4;
	private final int sizeTime = 40;
	private final float speed = .5f;
	
	private boolean strike = false;
	
	private Model[] clouds = new Model[5];
	
	public RoamingCloud(Model m) {
    	super(filename, dpf, m);
    	lifespan = span;
    }
	
	public RoamingCloud(Model[] m) {
    	super(filename, dpf, m);
    	lifespan = span;
    }
	
	public void setUpClouds(StageGameState b) {
		for (int i=0; i<clouds.length; i++) {
			clouds[i] = LoadingQueue.quickLoad(new Model("cloud"), b);
			b.removeModel(clouds[i]);
			model.attachChild(clouds[i].model);
			clouds[i].model.setLocalTranslation(new Vector3f((i==1 || i==2)?(float)(i*2-3)/2:0, 0, (i==3 || i==4)?(float)(i*2-7)/2:0));
		}
		b.getRootNode().updateRenderState();
	}
	
	@Override
	public void hit(StageGameState b, Character other) {
		
    }
	
	@ Override
    public void act(StageGameState b) {
		// set up clouds if not already done
		if (clouds[0] == null)
			setUpClouds(b);
		
		// billboard clouds
		for (int i=0; i<clouds.length; i++)
			clouds[i].billboard(b.getCamera(), true);
		
		// grow/shrink
		if (lifeCount <= sizeTime)
			model.setLocalScale((float)(lifeCount+1)/(sizeTime+1));
		else if (lifeCount >= lifespan - sizeTime)
			model.setLocalScale((float)(lifespan-lifeCount+1)/(sizeTime+1));
		
		if (strike) {
			// count down
			velocity = new Vector3f();
			if (strikeDelay == 0) {
				Lightning l = (Lightning)LoadingQueue.quickLoad(new Lightning(users.toArray(new Model[0])), b);
				l.model.setLocalTranslation(model.getLocalTranslation());
				b.getRootNode().updateRenderState();
			}
			strikeDelay--;
			
		} else {
	    	if (model.getLocalScale().x == 1) {
	    		//roam
	    		if (lifeCount % 30 == 0 || FastMath.nextRandomFloat() > .9f) {
	    			float angle = FastMath.nextRandomFloat()*FastMath.PI*2;
	    			velocity = new Vector3f(speed*FastMath.cos(angle), 0, speed*FastMath.sin(angle));
	    		}
	    		
	    		// check if target is underneath
	    		Vector3f pos = model.getLocalTranslation();
	    		for (Model m : b.getModels())
	    			if (m instanceof Character && pos.distance(m.model.getLocalTranslation())<=3) {
	    				boolean flag = false;
	    				for (Model u : users)
	    					if (m == u) {
	    						flag = true;
	    						break;
	    					}
	    				if (!flag) {
	    					strike = true;
	    					break;
	    				}
	    			}
	    	} else
	    		velocity = new Vector3f();
		}
		
    	super.act(b);
    }
	
}
