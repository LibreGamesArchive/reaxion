package com.googlecode.reaxion.game.model.prop;

import com.googlecode.reaxion.game.model.Model;
import com.googlecode.reaxion.game.state.BattleGameState;
import com.jme.math.FastMath;
import com.jme.math.Vector3f;

public class Petal extends Model {
	private static final String filename = "flower_petal";
	
	private final float shrinkRate = .008f;
	private final float rpfLimit = FastMath.PI/8;
	private float size = 1;
	
	private Vector3f rpf;
	
    public Petal() {
    	// Load model
    	super(filename);
    	trackable = false;
    	init();
    }
    
    @Override
    protected void init() {
    	super.init();
    	rpf = new Vector3f();
    	rpf.x = rpfLimit*FastMath.nextRandomFloat() - rpfLimit/2;
    	rpf.y = rpfLimit*FastMath.nextRandomFloat() - rpfLimit/2;
    	rpf.z = rpfLimit*FastMath.nextRandomFloat() - rpfLimit/2;
    }
    
    // Standard animated states, only override if specific states differ
    @ Override
    public void act(BattleGameState b) {
    	super.act(b);
    	if (size <= 0) {
    		b.removeModel(this);
    	} else {
    		rotate(rotationVector.add(rpf));
    		size -= shrinkRate;
    		model.setLocalScale(size);
    	}
    	
		model.setLocalTranslation(model.getLocalTranslation().addLocal(velocity));
    }
    
}
