package com.googlecode.reaxion.game.state;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.logging.Logger;

import com.googlecode.reaxion.game.Reaxion;
import com.googlecode.reaxion.game.ability.Ability;
import com.googlecode.reaxion.game.ability.AfterImage;
import com.googlecode.reaxion.game.ability.EvasiveStart;
import com.googlecode.reaxion.game.audio.BgmPlayer;
import com.googlecode.reaxion.game.input.PlayerInput;
import com.googlecode.reaxion.game.input.ai.TestAI;
import com.googlecode.reaxion.game.model.Model;
import com.googlecode.reaxion.game.model.character.Austin;
import com.googlecode.reaxion.game.model.character.Character;
import com.googlecode.reaxion.game.model.character.Cy;
import com.googlecode.reaxion.game.model.character.Khoa;
import com.googlecode.reaxion.game.model.character.MajorCharacter;
import com.googlecode.reaxion.game.model.character.Monica;
import com.googlecode.reaxion.game.model.character.Nilay;
import com.googlecode.reaxion.game.model.stage.FlowerField;
import com.googlecode.reaxion.game.model.stage.Stage;
import com.googlecode.reaxion.game.overlay.CharSelectOverlay;
import com.googlecode.reaxion.game.overlay.HudOverlay;
import com.googlecode.reaxion.game.overlay.PauseOverlay;
import com.googlecode.reaxion.game.overlay.ResultsOverlay;
import com.googlecode.reaxion.game.util.LoadingQueue;
import com.jme.app.AbstractGame;
import com.jme.image.Texture;
import com.jme.input.AbsoluteMouse;
import com.jme.input.InputHandler;
import com.jme.input.KeyBindingManager;
import com.jme.input.KeyInput;
import com.jme.input.MouseInput;
import com.jme.math.FastMath;
import com.jme.math.Vector3f;
import com.jme.scene.Node;
import com.jme.scene.shape.Quad;
import com.jme.scene.state.BlendState;
import com.jme.scene.state.ClipState;
import com.jme.scene.state.LightState;
import com.jme.scene.state.TextureState;
import com.jme.scene.state.WireframeState;
import com.jme.scene.state.ZBufferState;
import com.jme.system.DisplaySystem;
import com.jme.util.TextureManager;
import com.jme.util.geom.Debugger;
import com.jmex.game.state.CameraGameState;
import com.jmex.game.state.GameStateManager;
import com.jmex.game.state.StatisticsGameState;

public class CharSelectState extends CameraGameState {

	
	private CharSelectOverlay charSelectNode;
    protected InputHandler input;
	private static final String stageClassURL = "com.googlecode.reaxion.game.model.stage.";
	private static final String attackBaseLocation = "com.googlecode.reaxion.game.attack.";
    /**
     * Contains all scene model elements
     */
    private ArrayList<Model> models;
    
    /**
     * Contains the stage used for the battle
     */
    private Stage stage;
    
	private AbsoluteMouse mouse;
    
    public float tpf;
    
    protected AbstractGame game = null;
    private BattleGameState battle;
    
    
	public CharSelectState(BattleGameState b) {
		super("charSelectState");
		battle = b;
		init();
	}

	private void init() {
		//Initial charSelect
		rootNode = new Node("RootNode");
        charSelectNode = new CharSelectOverlay();
        rootNode.attachChild(charSelectNode);
            //resultsNode.setCombatants(battle.getPlayer(), battle.getPartner(), battle.getOpponents());
           // resultsNode.setStats(battle.targetTime, battle.getTotalTime(), battle.getPlayer(), battle.getPartner(), battle.expYield);
            
            // Fix up the camera, will not be needed for final camera controls
            Vector3f loc = new Vector3f( 0.0f, 2.5f, 10.0f );
            Vector3f left = new Vector3f( -1.0f, 0.0f, 0.0f );
            Vector3f up = new Vector3f( 0.0f, 1.0f, 0.0f );
            Vector3f dir = new Vector3f( 0.0f, 0f, -1.0f );
            cam.setFrame( loc, left, up, dir );
            cam.setFrustumPerspective(45f, (float) DisplaySystem.getDisplaySystem().getWidth()/DisplaySystem.getDisplaySystem().getHeight(), .01f, 1000);
            cam.update();

            // Initial InputHandler
    	    //input = new FirstPersonHandler(cam, 15.0f, 0.5f);
            input = new InputHandler();
    	    initKeyBindings();
    	    
    	    //Setup software mouse
    		mouse = new AbsoluteMouse("Mouse Input", DisplaySystem.getDisplaySystem().getWidth(), DisplaySystem.getDisplaySystem().getHeight());
    		mouse.registerWithInputHandler(input);
    		TextureState cursor = DisplaySystem.getDisplaySystem().getRenderer().createTextureState();
    		cursor.setTexture(TextureManager.loadTexture(
    				Reaxion.class.getClassLoader().getResource("com/googlecode/reaxion/resources/cursors/cursor.png"),
    				Texture.MinificationFilter.NearestNeighborNoMipMaps, Texture.MagnificationFilter.NearestNeighbor));
    		mouse.setRenderState(cursor);
    		BlendState as1 = DisplaySystem.getDisplaySystem().getRenderer().createBlendState();
    		as1.setBlendEnabled(true);
    		as1.setSourceFunction(BlendState.SourceFunction.SourceAlpha);
    		as1.setDestinationFunction(BlendState.DestinationFunction.OneMinusSourceAlpha);
    		as1.setTestEnabled(true);
    		as1.setTestFunction(BlendState.TestFunction.GreaterThan);
    		mouse.setRenderState(as1);
    		rootNode.attachChild(mouse);

            // Finish up
            rootNode.updateRenderState();
            rootNode.updateWorldBound();
            rootNode.updateGeometricState(0.0f, true);
            charSelectNode.pause();
            
         
        }

        // duplicate the functionality of DebugGameState
        // Most of this can be commented out during finalization
        private void initKeyBindings() {
            KeyBindingManager.getKeyBindingManager().set("screen_shot",
                    KeyInput.KEY_F1);
            KeyBindingManager.getKeyBindingManager().set("exit",
                    KeyInput.KEY_ESCAPE);
            KeyBindingManager.getKeyBindingManager().set("mem_report",
                    KeyInput.KEY_R);
            KeyBindingManager.getKeyBindingManager().set("toggle_mouse",
                            KeyInput.KEY_M);
            KeyBindingManager.getKeyBindingManager().set("charselect",
                    KeyInput.KEY_O);
            
            KeyBindingManager.getKeyBindingManager().set("go",
                    KeyInput.KEY_RETURN);
            KeyBindingManager.getKeyBindingManager().set("p11",
                    KeyInput.KEY_1);
            KeyBindingManager.getKeyBindingManager().set("p12",
                    KeyInput.KEY_2);
            KeyBindingManager.getKeyBindingManager().set("p13",
                    KeyInput.KEY_3);
            KeyBindingManager.getKeyBindingManager().set("p14",
                    KeyInput.KEY_4);
            KeyBindingManager.getKeyBindingManager().set("p15",
                    KeyInput.KEY_5);
            KeyBindingManager.getKeyBindingManager().set("p16",
                    KeyInput.KEY_6);
            KeyBindingManager.getKeyBindingManager().set("p21",
                    KeyInput.KEY_A);
            
            KeyBindingManager.getKeyBindingManager().set("arrow_up", KeyInput.KEY_UP);
            KeyBindingManager.getKeyBindingManager().set("arrow_down", KeyInput.KEY_DOWN);
            KeyBindingManager.getKeyBindingManager().set("arrow_left", KeyInput.KEY_LEFT);
            KeyBindingManager.getKeyBindingManager().set("arrow_right", KeyInput.KEY_RIGHT);
            KeyBindingManager.getKeyBindingManager().set("select", KeyInput.KEY_SPACE);
            
            
        }

        @ Override
        public void stateUpdate(float _tpf) {
        	tpf = _tpf;
        	
            // Update the InputHandler
        	if (input != null) {
        		input.update(tpf);
        	
        		/** If exit is a valid command (via key Esc), exit game */
        		if (KeyBindingManager.getKeyBindingManager().isValidCommand("exit",
    	                false)) {
    	        	if (game != null) {
    	        		game.finish();
    	        	} else {
    	        		System.exit(0);
    	        	}
    	        }
        	}
        	
        	if (KeyBindingManager.getKeyBindingManager().isValidCommand(
	                "charselect", false)) {
        		BattleGameState battleState = new BattleGameState();
        		GameStateManager.getInstance().attachChild(battleState);
	    		battleState.setActive(true);
	        	setActive(false);
        	}
        	
        	if (KeyBindingManager.getKeyBindingManager().isValidCommand(
	                "arrow_up", false)) {
        		System.out.println("up");
        		charSelectNode.updateDisplay(1);
        	}
           	if (KeyBindingManager.getKeyBindingManager().isValidCommand(
	                "arrow_right", false)) {
        		charSelectNode.updateDisplay(2);
        	}
           	if (KeyBindingManager.getKeyBindingManager().isValidCommand(
	                "arrow_down", false)) {
        		charSelectNode.updateDisplay(3);
        	}
           	if (KeyBindingManager.getKeyBindingManager().isValidCommand(
	                "arrow_left", false)) {
        		charSelectNode.updateDisplay(4);
        	}
        	
           	
           	if (KeyBindingManager.getKeyBindingManager().isValidCommand(
	                "select", false)) {
           		charSelectNode.updateSel();
           	}
           	
           	
        	if (KeyBindingManager.getKeyBindingManager().isValidCommand(
	                "go", false)) {
        		try {
					gotoBattleState();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
        	}
        	
        	// Update the overlay
        	//charSelectNode.update(this);
        		
        	// Update the geometric state of the rootNode
            rootNode.updateGeometricState(tpf, true);

            if (input != null) {
    	        if (KeyBindingManager.getKeyBindingManager().isValidCommand(
    	                "screen_shot", false)) {
    	            DisplaySystem.getDisplaySystem().getRenderer().takeScreenShot(
    	                    "SimpleGameScreenShot");
    	        }

            }
        }
        
	
        
        
        public void setBackground(Quad q) {
    		charSelectNode.setBackground(q);
    	}
    
        
    	private void gotoBattleState() {
    		int[]selected = charSelectNode.getSelectedChars();
    		
    		StageSelectionState s = new StageSelectionState(selected);
    		GameStateManager.getInstance().attachChild(s);
			s.setActive(true);
			setActive(false);

    	}
    
    public boolean charSelected()
    {
    	return true;
    }
    
    public int[] selectedChars()
    {
    	return new int[3];
    }
    

    
}