package com.googlecode.reaxion.game;

import java.util.logging.Logger;

import com.jme.app.AbstractGame;
import com.jme.image.Texture;
import com.jme.input.FirstPersonHandler;
import com.jme.input.InputHandler;
import com.jme.input.KeyBindingManager;
import com.jme.input.KeyInput;
import com.jme.input.MouseInput;
import com.jme.light.PointLight;
import com.jme.math.Vector3f;
import com.jme.renderer.ColorRGBA;
import com.jme.scene.Node;
import com.jme.scene.state.LightState;
import com.jme.scene.state.WireframeState;
import com.jme.scene.state.ZBufferState;
import com.jme.system.DisplaySystem;
import com.jme.util.geom.Debugger;
import com.jmex.game.state.CameraGameState;
import com.jmex.game.state.GameStateManager;
import com.jmex.game.state.StatisticsGameState;

/**
 * {@code BattleGameState} currently mirrors much of the functionality of {@code DebugGameState},
 * but will eventually become specialized to handle in-game battles.
 * @author Khoa
 */
public class BattleGameState extends CameraGameState {
    private static final Logger logger = Logger.getLogger(BattleGameState.class
            .getName());
    
    protected InputHandler input;
    protected WireframeState wireState;
    protected LightState lightState;
    protected boolean pause;
    protected boolean showBounds = false;
    protected boolean showDepth = false;
    protected boolean showNormals = false;
    protected boolean statisticsCreated = false;
    protected AbstractGame game = null;
    
    protected PlayerInput playerInput;
    private Character player;
    
    public BattleGameState() {
    	super("battleGameState");
    	init();
    }
    
    private void init() {
        rootNode = new Node("RootNode");

        // Create a wirestate to toggle on and off. Starts disabled with default
        // width of 1 pixel.
        wireState = DisplaySystem.getDisplaySystem().getRenderer()
                .createWireframeState();
        wireState.setEnabled(false);
        rootNode.setRenderState(wireState);

        // Create ZBuffer for depth
        ZBufferState zbs = DisplaySystem.getDisplaySystem().getRenderer()
                .createZBufferState();
        zbs.setEnabled(true);
        zbs.setFunction(ZBufferState.TestFunction.LessThanOrEqualTo);
        rootNode.setRenderState(zbs);
        
        // Lighting
        /** Set up a basic, default light. */
        PointLight light = new PointLight();
        light.setDiffuse( new ColorRGBA( 0.75f, 0.75f, 0.75f, 0.75f ) );
        light.setAmbient( new ColorRGBA( 0.5f, 0.5f, 0.5f, 1.0f ) );
        light.setLocation( new Vector3f( 100, 100, 100 ) );
        light.setEnabled( true );

        /** Attach the light to a lightState and the lightState to rootNode. */
        lightState = DisplaySystem.getDisplaySystem().getRenderer().createLightState();
        lightState.setEnabled( true );
        lightState.attach( light );
        rootNode.setRenderState( lightState );
        
        // Set rootNode for transparency
        /*
        BlendState tpState = DisplaySystem.getDisplaySystem().getRenderer().createBlendState();
		tpState.setEnabled(true);
		tpState.setSourceFunction(BlendState.SourceFunction.SourceAlpha);
		tpState.setDestinationFunction(BlendState.DestinationFunction.OneMinusSourceAlpha);
		tpState.setBlendEnabled(true);
		tpState.setTestEnabled(true);
		tpState.setTestFunction(BlendState.TestFunction.GreaterThan);
		tpState.setReference(0.1f);
		rootNode.setRenderState(tpState);
		rootNode.setRenderQueueMode(Renderer.QUEUE_TRANSPARENT);
		rootNode.setLightCombineMode(Spatial.LightCombineMode.Replace);
		DisplaySystem.getDisplaySystem().getRenderer().getQueue().setTwoPassTransparency(true);
		ZBufferState zstate = DisplaySystem.getDisplaySystem().getRenderer().createZBufferState(); 
		zstate.setWritable(false);
		zstate.setEnabled(false);
		*/ 
        
        //TODO: change camera system so that it follows x distance behind the player and is in line
        //		with the player and the target (another model or point)
        // Fix up the camera, will not be needed for final camera controls
        Vector3f loc = new Vector3f( 0.0f, 2.5f, 10.0f );
        Vector3f left = new Vector3f( -1.0f, 0.0f, 0.0f );
        Vector3f up = new Vector3f( 0.0f, 1.0f, 0.0f );
        Vector3f dir = new Vector3f( 0.0f, 0f, -1.0f );
        cam.setFrame( loc, left, up, dir );
        cam.update();

        // Initial InputHandler
	    input = new FirstPersonHandler(cam, 15.0f, 0.5f);
	    initKeyBindings();

        // Finish up
        rootNode.updateRenderState();
        rootNode.updateWorldBound();
        rootNode.updateGeometricState(0.0f, true);
    }
    
    /**
     * Specifies the player character for this game state.
     * @param p Character to be designated as the player
     * @author Khoa
     *
     */
    public void assignPlayer(Character p) {
    	player = p;
    	// Create input system
    	playerInput = new PlayerInput(player);
    }
    
    @ Override
    protected void onActivate() {
    	super.onActivate();
    }
    
    public LightState getLightState() {
    	return lightState;
    }

    // duplicate the functionality of DebugGameState
    // Most of this can be commented out during finalization
    private void initKeyBindings() {
        /** Assign key P to action "toggle_pause". */
        KeyBindingManager.getKeyBindingManager().set("toggle_pause",
                KeyInput.KEY_P);
        /** Assign key T to action "toggle_wire". */
        KeyBindingManager.getKeyBindingManager().set("toggle_wire",
                KeyInput.KEY_T);
        /** Assign key L to action "toggle_lights". */
        KeyBindingManager.getKeyBindingManager().set("toggle_lights",
                KeyInput.KEY_L);
        /** Assign key B to action "toggle_bounds". */
        KeyBindingManager.getKeyBindingManager().set("toggle_bounds",
                KeyInput.KEY_B);
        /** Assign key N to action "toggle_normals". */
        KeyBindingManager.getKeyBindingManager().set("toggle_normals",
                KeyInput.KEY_N);
        /** Assign key C to action "camera_out". */
        KeyBindingManager.getKeyBindingManager().set("camera_out",
                KeyInput.KEY_C);
        KeyBindingManager.getKeyBindingManager().set("screen_shot",
                KeyInput.KEY_F1);
        KeyBindingManager.getKeyBindingManager().set("exit",
                KeyInput.KEY_ESCAPE);
        KeyBindingManager.getKeyBindingManager().set("toggle_depth",
                KeyInput.KEY_F3);
        KeyBindingManager.getKeyBindingManager().set("toggle_stats",
                KeyInput.KEY_F4);
        KeyBindingManager.getKeyBindingManager().set("mem_report",
                KeyInput.KEY_R);
        KeyBindingManager.getKeyBindingManager().set("toggle_mouse",
                        KeyInput.KEY_M);
    }

    @ Override
    public void stateUpdate(float tpf) {
        // Update the InputHandler
    	if (input != null) {
    		input.update(tpf);
    	
	        /** If toggle_pause is a valid command (via key p), change pause. */
	        if (KeyBindingManager.getKeyBindingManager().isValidCommand(
	                "toggle_pause", false)) {
	            pause = !pause;
	        }
	    	
	        if (pause)
	            return;
    	}
    	
    	// Update the PlayerInput
    	if (playerInput != null) {
    		playerInput.checkKeys();
    	}
    	// TODO: Traverse node tree and call act() where valid, not just for player
    	// Update the Player
    	if (player != null) {
    		player.act();
    	}

        // Update the geometric state of the rootNode
        rootNode.updateGeometricState(tpf, true);

        if (input != null) {
	        /** If toggle_wire is a valid command (via key T), change wirestates. */
	        if (KeyBindingManager.getKeyBindingManager().isValidCommand(
	                "toggle_wire", false)) {
	            wireState.setEnabled(!wireState.isEnabled());
	            rootNode.updateRenderState();
	        }
	        /** If toggle_lights is a valid command (via key L), change lightstate. */
	        if (KeyBindingManager.getKeyBindingManager().isValidCommand(
	                "toggle_lights", false)) {
	            lightState.setEnabled(!lightState.isEnabled());
	            rootNode.updateRenderState();
	        }
	        /** If toggle_bounds is a valid command (via key B), change bounds. */
	        if (KeyBindingManager.getKeyBindingManager().isValidCommand(
	                "toggle_bounds", false)) {
	            showBounds = !showBounds;
	        }
	        /** If toggle_depth is a valid command (via key F3), change depth. */
	        if (KeyBindingManager.getKeyBindingManager().isValidCommand(
	                "toggle_depth", false)) {
	            showDepth = !showDepth;
	        }
	        /** If toggle_stats is a valid command (via key F4), change depth. */
            if (KeyBindingManager.getKeyBindingManager().isValidCommand(
                    "toggle_stats", false)) {
            	if (statisticsCreated == false) {
	                // create a statistics game state
	                GameStateManager.getInstance().attachChild(
	                		new StatisticsGameState("stats", 1f, 0.25f, 0.75f, true));
	                statisticsCreated = true;
            	}
                GameStateManager.getInstance().getChild("stats").setActive(
                        !GameStateManager.getInstance().getChild("stats").isActive());
            }
            
	        if (KeyBindingManager.getKeyBindingManager().isValidCommand(
	                "toggle_normals", false)) {
	            showNormals = !showNormals;
	        }
	        /** If camera_out is a valid command (via key C), show camera location. */
	        if (KeyBindingManager.getKeyBindingManager().isValidCommand(
	                "camera_out", false)) {
                logger.info("Camera at: "
	                    + DisplaySystem.getDisplaySystem().getRenderer()
	                            .getCamera().getLocation());
	        }
	        if (KeyBindingManager.getKeyBindingManager().isValidCommand(
	                "screen_shot", false)) {
	            DisplaySystem.getDisplaySystem().getRenderer().takeScreenShot(
	                    "SimpleGameScreenShot");
	        }
	        if (KeyBindingManager.getKeyBindingManager().isValidCommand(
	                "mem_report", false)) {
	            long totMem = Runtime.getRuntime().totalMemory();
	            long freeMem = Runtime.getRuntime().freeMemory();
	            long maxMem = Runtime.getRuntime().maxMemory();
	
                logger.info("|*|*|  Memory Stats  |*|*|");
                logger.info("Total memory: " + (totMem >> 10) + " kb");
                logger.info("Free memory: " + (freeMem >> 10) + " kb");
                logger.info("Max memory: " + (maxMem >> 10) + " kb");
	        }
	        if (KeyBindingManager.getKeyBindingManager().isValidCommand(
	                        "toggle_mouse", false)) {
	                    MouseInput.get().setCursorVisible(!MouseInput.get().isCursorVisible());
	                    logger.info("Cursor Visibility set to " + MouseInput.get().isCursorVisible());
	                }
	
	        if (KeyBindingManager.getKeyBindingManager().isValidCommand("exit",
	                false)) {
	        	if (game != null) {
	        		game.finish();
	        	} else {
	        		System.exit(0);
	        	}
	        }
        }
    }

    public void stateRender(float tpf) {
    	
        // Render the rootNode
        DisplaySystem.getDisplaySystem().getRenderer().draw(rootNode);

        if (showBounds) {
            Debugger.drawBounds(rootNode, DisplaySystem.getDisplaySystem()
                    .getRenderer(), true);
        }

        if (showNormals) {
            Debugger.drawNormals(rootNode, DisplaySystem.getDisplaySystem()
                    .getRenderer());
        }

        if (showDepth) {
            DisplaySystem.getDisplaySystem().getRenderer().renderQueue();
            Debugger.drawBuffer(Texture.RenderToTextureType.Depth, Debugger.NORTHEAST,
                    DisplaySystem.getDisplaySystem().getRenderer());
        }
    }

    public void cleanup() {
    }
}
