package com.googlecode.reaxion.game.state;

import com.googlecode.reaxion.game.mission.MissionManager;
import com.googlecode.reaxion.game.model.Model;
import com.googlecode.reaxion.game.overlay.CharacterSelectionOverlay;
import com.googlecode.reaxion.game.overlay.MenuOverlay;
import com.googlecode.reaxion.game.overlay.MissionOverlay;
import com.googlecode.reaxion.game.overlay.Overlay;
import com.googlecode.reaxion.game.overlay.StageSelectionOverlay;
import com.googlecode.reaxion.game.overlay.TerminalOverlay;
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
    
	public static final String ACCESS_TERMINAL = NAME + "_access_terminal";
	public static final String CLOSE_TERMINAL = NAME + "_close_terminal";
	
	private Model terminal;
	
	private TerminalOverlay terminalOverlay;
	private CharacterSelectionOverlay characterOverlay;
	private StageSelectionOverlay stageOverlay;
	private MissionOverlay missionOverlay;
	private MenuOverlay currentMenu;
	
	private final float activationDistance = 8f;
	
	private boolean menuShowing;
    
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
    	
    	terminalOverlay = new TerminalOverlay();
    	characterOverlay = new CharacterSelectionOverlay();
    	stageOverlay = new StageSelectionOverlay();
    	missionOverlay = new MissionOverlay();
    	
    	changeCurrentMenu(terminalOverlay);
    }
    
    @Override
    protected void initKeyBindings() {
    	super.initKeyBindings();
    	
    	KeyBindingManager manager = KeyBindingManager.getKeyBindingManager();
    	
    	manager.set(ACCESS_TERMINAL, KeyInput.KEY_RETURN);
    	manager.set(CLOSE_TERMINAL, KeyInput.KEY_BACK);
    }
    
    @Override
    protected void removeKeyBindings() {
    	super.removeKeyBindings();
    	
    	KeyBindingManager manager = KeyBindingManager.getKeyBindingManager();
    	
    	manager.remove(ACCESS_TERMINAL);
    	manager.remove(CLOSE_TERMINAL);
    }
    
    @Override
	public void stateUpdate(float tpf) {
		super.stateUpdate(tpf);
		
		KeyBindingManager manager = KeyBindingManager.getKeyBindingManager();
		
		if (manager.isValidCommand(ACCESS_TERMINAL, false)) {
			Vector3f playerLoc = player.getXZTranslation();
			Vector3f terminalLoc = terminal.getXZTranslation();
			
			if (playerLoc.distance(terminalLoc) <= activationDistance) {
				toggleMenu(true);
				changeCurrentMenu(terminalOverlay);
				rootNode.attachChild((Overlay) currentMenu);
			}
		}
		
		if (manager.isValidCommand(CLOSE_TERMINAL, false)) {
			Vector3f playerLoc = player.getXZTranslation();
			Vector3f terminalLoc = terminal.getXZTranslation();
			
			if (playerLoc.distance(terminalLoc) <= activationDistance) {
				toggleMenu(false);
				rootNode.detachChild((Overlay) currentMenu);
			}
		}
		
		if (manager.isValidCommand(MenuOverlay.UP, false)) {
			if (menuShowing)
				currentMenu.updateDisplay(KeyInput.KEY_UP);
		}
		
		if (manager.isValidCommand(MenuOverlay.DOWN, false)) {
			if (menuShowing)
				currentMenu.updateDisplay(KeyInput.KEY_DOWN);
		}
		
		if (manager.isValidCommand(MenuOverlay.LEFT, false)) {
			if (menuShowing)
				currentMenu.updateDisplay(KeyInput.KEY_LEFT);
		}
		
		if (manager.isValidCommand(MenuOverlay.RIGHT, false)) {
			if (menuShowing)
				currentMenu.updateDisplay(KeyInput.KEY_RIGHT);
		}
		
		if (manager.isValidCommand(MenuOverlay.SELECT, false)) {
			if (menuShowing)
				currentMenu.updateDisplay(KeyInput.KEY_SPACE);
		}
		
		if (manager.isValidCommand(MenuOverlay.SELECT_FINAL, false)) {
			if (currentMenu instanceof MissionOverlay) {
				missionOverlay.startSelectedMission();
			} else if (currentMenu instanceof TerminalOverlay) {
				rootNode.detachChild(currentMenu);
				
				switch (((TerminalOverlay) currentMenu).getCurrentIndex(true)) {
				case 0:
					changeCurrentMenu(characterOverlay);
					break;
				case 1:
					changeCurrentMenu(stageOverlay);
					break;
				case 2:
					changeCurrentMenu(missionOverlay);
				}
				
				((Overlay) currentMenu).updateRenderState();
				rootNode.attachChild((Overlay) currentMenu);
				toggleMenu(true);
			} else if (currentMenu instanceof StageSelectionOverlay) {
				Battle.setDefaultStage(((StageSelectionOverlay) currentMenu).getSelectedStageClass(true));
				
				MissionManager.endHubGameState();
				MissionManager.startHubGameState();
			} else if (currentMenu instanceof CharacterSelectionOverlay) {
				String[] characters = ((CharacterSelectionOverlay) currentMenu).getSelectedChars(true);
				
				Battle.setDefaultPlayers(characters[0], characters[1]);
				
				MissionManager.endHubGameState();
				MissionManager.startHubGameState();
			}
			
		}
		
		if (manager.isValidCommand(CharacterSelectionOverlay.UNDO_CHOICE,
				false)) {
			currentMenu.updateDisplay(KeyInput.KEY_DELETE);
		}
		
		if (manager.isValidCommand(CharacterSelectionOverlay.CHOOSE_1,
				false)) {
			currentMenu.updateDisplay(KeyInput.KEY_1);
		}
		
		if (manager.isValidCommand(CharacterSelectionOverlay.CHOOSE_2,
				false)) {
			currentMenu.updateDisplay(KeyInput.KEY_2);
		}
		
	}

    private void toggleMenu(boolean showing) {				
		frozen = showing;
    	menuShowing = showing;
    	
    	if (menuShowing) {
    		KeyBindingManager.getKeyBindingManager().remove(ACCESS_TERMINAL);
    		hideOverlays();
    	}
    	else {
    		KeyBindingManager.getKeyBindingManager().set(ACCESS_TERMINAL, KeyInput.KEY_RETURN);
    		currentMenu.deactivate();
    		showOverlays();
    	}
    }
    
    private void changeCurrentMenu(MenuOverlay m) {
    	if (currentMenu != null)
    		currentMenu.deactivate();
    	
    	m.activate();
    	
    	if (m instanceof StageSelectionOverlay)
    		((StageSelectionOverlay) m).setSelectedStage(getStage().name);
    	
    	currentMenu = m;
    }
    
	@Override
	public void setActive(boolean active) {
		super.setActive(active);
		
		if (active) {
			player.renew();
			partner.renew();
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