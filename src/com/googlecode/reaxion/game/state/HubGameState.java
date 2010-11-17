package com.googlecode.reaxion.game.state;

import com.googlecode.reaxion.game.model.Model;
import com.googlecode.reaxion.game.overlay.MissionOverlay;
import com.googlecode.reaxion.game.util.Battle;
import com.googlecode.reaxion.game.util.LoadingQueue;
import com.jme.input.KeyBindingManager;
import com.jme.input.KeyInput;
import com.jme.math.FastMath;
import com.jme.math.Vector3f;
import com.jme.scene.Node;

/**
 * {@code HubGameState} extends {@code StageGameState} with functionality
 * dedicated to the hub system, such as portals and hub controllers.
 * @author Khoa
 */
public class HubGameState extends StageGameState {
	
	public static final String NAME = "hubGameState";
    
	private Model terminal;
	
	private MissionOverlay missionOverlay;
    
    public HubGameState() {
    	super();
    	init();
    }
    
    public HubGameState(Battle b) {
    	super(b);
    	init();
    	
    	createTerminal(b.getStage().getTerminalPosition());
    }
    
    private void init() {    	
    	missionOverlay = new MissionOverlay();
    	
    	initKeyBindings();
    }
    
    private void initKeyBindings() {
    	KeyBindingManager.getKeyBindingManager().set("access_terminal", KeyInput.KEY_RETURN);
    }
    
    @Override
	public void stateUpdate(float tpf) {
		super.stateUpdate(tpf);
		
		if (KeyBindingManager.getKeyBindingManager().isValidCommand("access_terminal", false)) {
			Vector3f playerLoc = player.getXZTranslation();
			Vector3f terminalLoc = terminal.getXZTranslation();
			
			if (playerLoc.distance(terminalLoc) <= 3) {
				if (!rootNode.hasChild(missionOverlay)) {
					pause = true;
					rootNode.attachChild(missionOverlay);
				} else {
					pause = false;
					rootNode.detachChild(missionOverlay);
				}
			}
		}
	}

	@Override
    protected void act() {
    	Vector3f p = player.model.getLocalTranslation();
    	Vector3f t = terminal.model.getLocalTranslation();
    	terminal.roll = FastMath.atan2(p.x-t.x, p.z-t.z);
    	terminal.rotate();
    }
    
    private void createTerminal(Vector3f pos) {
    	terminal = LoadingQueue.quickLoad(new Model("dashboard"), this);
    	terminal.trackable = true;
    	terminal.model.setLocalTranslation(pos);
    	rootNode.updateRenderState();
    }
    
}