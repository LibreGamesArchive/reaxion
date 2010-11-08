package com.googlecode.reaxion.game.state;

import com.googlecode.reaxion.game.model.Model;
import com.googlecode.reaxion.game.util.Battle;
import com.googlecode.reaxion.game.util.LoadingQueue;
import com.jme.math.FastMath;
import com.jme.math.Vector3f;

/**
 * {@code HubGameState} extends {@code StageGameState} with functionality
 * dedicated to the hub system, such as portals and hub controllers.
 * @author Khoa
 */
public class HubGameState extends StageGameState {
	
	public static final String NAME = "hubGameState";
    
	private Model terminal;
    
    public HubGameState() {
    	super();
    }
    
    public HubGameState(Battle b) {
    	super(b);
    	createTerminal(b.getStage().getTerminalPosition());
    }
    
    @Override
    protected void act() {
    	Vector3f p = player.model.getLocalTranslation();
    	Vector3f t = terminal.model.getLocalTranslation();
    	terminal.roll = FastMath.atan2(p.x-t.x, p.z-t.z);
    	terminal.rotate();
    }
    
    private void createTerminal(Vector3f pos) {
    	terminal = LoadingQueue.quickLoad(new Model("dashboard"), null);
    	rootNode.attachChild(terminal.model);
    	terminal.model.setLocalTranslation(pos);
    	rootNode.updateRenderState();
    }
    
}