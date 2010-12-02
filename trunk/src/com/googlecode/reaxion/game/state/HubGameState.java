package com.googlecode.reaxion.game.state;

import com.googlecode.reaxion.game.model.Model;
import com.googlecode.reaxion.game.overlay.MissionOverlay;
import com.googlecode.reaxion.game.util.Battle;
import com.googlecode.reaxion.game.util.LoadingQueue;
import com.jme.input.KeyBindingManager;
import com.jme.input.KeyInput;
import com.jme.math.FastMath;
import com.jme.math.Vector3f;

/**
 * {@code HubGameState} extends {@code StageGameState} with functionality
 * dedicated to the hub system, such as portals and hub controllers.
 * 
 * @author Khoa
 */
public class HubGameState extends StageGameState {
	
	public static final String NAME = "hubGameState";
    
	private Model terminal;
	
	private MissionOverlay missionOverlay;
	
	private boolean missionOverlayShowing;
    
    public HubGameState() {
    	super();
    }
    
    public HubGameState(Battle b) {
    	super(b);
    	
    	createTerminal(b.getStage().getTerminalPosition());
    }
    
    @Override
    protected void init() {
    	super.init();
    	
    	setName(NAME);
    	
    	startsBGM = true;
    	endsBGM = true;
    	
    	missionOverlay = new MissionOverlay();
    	rootNode.attachChild(missionOverlay);
    }
    
    @Override
    protected void initKeyBindings() {
    	super.initKeyBindings();
    	
    	KeyBindingManager manager = KeyBindingManager.getKeyBindingManager();
    	
    	manager.set("access_terminal", KeyInput.KEY_RETURN);
    	manager.set("menu_up", KeyInput.KEY_UP);
    	manager.set("menu_down", KeyInput.KEY_DOWN);
    }
    
    @Override
    protected void removeKeyBindings() {
    	super.removeKeyBindings();
    	
    	KeyBindingManager manager = KeyBindingManager.getKeyBindingManager();
    	
    	manager.remove("access_terminal");
    	manager.remove("menu_up");
    	manager.remove("menu_down");
    }
    
    @Override
	public void stateUpdate(float tpf) {
		super.stateUpdate(tpf);
		KeyBindingManager manager = KeyBindingManager.getKeyBindingManager();
		
		
		if (manager.isValidCommand("access_terminal", false)) {
			Vector3f playerLoc = player.getXZTranslation();
			Vector3f terminalLoc = terminal.getXZTranslation();
			
			if (playerLoc.distance(terminalLoc) <= 3) {
				toggleMissionOverlay();

				if (missionOverlayShowing)
					missionOverlay.showMenu();
				else
					missionOverlay.hideMenu();
			}
		}
		
		if (manager.isValidCommand("menu_up", false)) {
			if (missionOverlayShowing)
				missionOverlay.updateDisplay(KeyInput.KEY_UP);
		}
		
		if (manager.isValidCommand("menu_down", false)) {
			if (missionOverlayShowing)
				missionOverlay.updateDisplay(KeyInput.KEY_DOWN);
		}
		
		if (manager.isValidCommand("menu_select", false)) {
			toggleMissionOverlay();
			missionOverlay.startSelectedMission();
		}
	}

    private void toggleMissionOverlay() {				
		frozen = !frozen;
    	missionOverlayShowing = !missionOverlayShowing;
    	
    	if (missionOverlayShowing)
    		hideOverlays();
    	else
    		showOverlays();
    	
    	switchSpacebarFunction(missionOverlayShowing);
    }
    
    private void switchSpacebarFunction(boolean showing) {
    	KeyBindingManager manager = KeyBindingManager.getKeyBindingManager();
    	
    	if (showing) {
    		manager.remove("switch");
    		manager.set("menu_select", KeyInput.KEY_SPACE);
    	} else {
    		manager.remove("menu_select");
    		manager.set("switch", KeyInput.KEY_SPACE);
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