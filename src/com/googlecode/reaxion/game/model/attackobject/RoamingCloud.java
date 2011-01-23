package com.googlecode.reaxion.game.model.attackobject;

import com.googlecode.reaxion.game.model.Model;
import com.googlecode.reaxion.game.model.Model.Billboard;
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
	private final float speed = 1f;
	
	private int seekRange = 32;
	private boolean strike = false;
	
	private final float overhead = 7;
	
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
			clouds[i].billboarding = Billboard.Free;
			
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
		
		// make clouds follow
		for (int i=0; i<clouds.length; i++)
			clouds[i].model.setLocalTranslation(model.getWorldTranslation().add((i==1 || i==2)?(float)(i*2-3)/2:0, 0, (i==3 || i==4)?(float)(i*2-7)/2:0));
		
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
				lifeCount = lifespan - sizeTime;
			}
			strikeDelay--;
			
		} else {
	    	if (model.getLocalScale().x == 1) {
	    		// check enemies
	    		Vector3f pos = model.getLocalTranslation();
	    		for (Model m : b.getModels()) {
	    			Vector3f mpos = m.model.getLocalTranslation();
	    			if (m instanceof Character && pos.distance(mpos) <= seekRange) {
	    				boolean flag = false;
	    				for (Model u : users)
	    					if (m == u) {
	    						flag = true;
	    						break;
	    					}
	    				if (!flag) {
	    					if (FastMath.sqrt(FastMath.pow(pos.x-mpos.x, 2) + FastMath.pow(pos.z-mpos.z, 2)) <= 2)
	    						strike = true;
	    					else
	    						velocity = m.model.getLocalTranslation().add(new Vector3f(0, overhead, 0)).subtract(pos).normalize().mult(speed);
	    					break;
	    				}
	    			}
	    		}
	    		
	    		//roam
	    		if (lifeCount % 30 == 0 || FastMath.nextRandomFloat() > .9f) {
	    			float angle = FastMath.nextRandomFloat()*FastMath.PI*2;
	    			velocity = new Vector3f(speed*FastMath.cos(angle), 0, speed*FastMath.sin(angle));
	    		}
	    		
	    	} else
	    		velocity = new Vector3f();
		}
		
    	super.act(b);
    }
	
	@Override
	protected void finish(StageGameState b) {
		b.removeModel(this);
		for (int i = 0; i < clouds.length; i++)
			b.removeModel(clouds[i]);
	}
	
}
