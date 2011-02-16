package com.googlecode.reaxion.game.model.prop;

import com.googlecode.reaxion.game.model.Model;
import com.googlecode.reaxion.game.state.StageGameState;
import com.jme.math.FastMath;
import com.jme.math.Vector3f;

public class Pointer extends Model {
	private static final String filename = "pointer";
	
	private final float[] offset = {1.5f, -10000};
	private final float angleInc = FastMath.PI/45;

	private boolean visible = false;
	
	private Model terminal;
	
    public Pointer(Model c) {
    	// Load model
    	super(filename);
    	trackable = false;
    	
    	terminal = c;
    }
    
    // Animate
    @ Override
    public void act(StageGameState b) {
    	super.act(b);
    	
    	if (visible) {
    		model.setLocalTranslation(terminal.model.getLocalTranslation().add(
    				new Vector3f(0, offset[0]+FastMath.cos((float)(4*b.getTotalTime())%FastMath.PI)/4, 0)));
    		roll = (roll + angleInc) % (FastMath.PI * 2);
    		rotate();
    		
    	} else {
    		model.setLocalTranslation(new Vector3f(0, offset[1], 0));
    	}
    }
    
    public void show(boolean f) {
    	visible = f;
    }
    
}
