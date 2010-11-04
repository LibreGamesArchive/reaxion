package com.googlecode.reaxion.game.state;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.logging.Logger;

import com.googlecode.reaxion.game.Reaxion;
import com.googlecode.reaxion.game.burstgrid.BurstGrid;
import com.googlecode.reaxion.game.burstgrid.BurstNode;
import com.googlecode.reaxion.game.input.PlayerInput;
import com.googlecode.reaxion.game.model.Model;
import com.googlecode.reaxion.game.model.character.Character;
import com.googlecode.reaxion.game.model.character.MajorCharacter;
import com.jme.app.AbstractGame;
import com.jme.bounding.BoundingBox;
import com.jme.image.Texture;
import com.jme.input.AbsoluteMouse;
import com.jme.input.InputHandler;
import com.jme.input.KeyBindingManager;
import com.jme.input.KeyInput;
import com.jme.input.MouseInput;
import com.jme.math.FastMath;
import com.jme.math.Vector3f;
import com.jme.scene.shape.Sphere;
import com.jme.scene.state.BlendState;
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

/**
 * {@code BurstGridState} is the state used to render the BurstGrid
 * @author Cy
 */
public class BurstGridState extends CameraGameState {
    private static final Logger logger = Logger.getLogger(BattleGameState.class
            .getName());
    
    public float tpf;
    
    protected InputHandler input;
    protected WireframeState wireState;
    protected LightState lightState;
   
    protected boolean pause;
    protected boolean showBounds = false;
    protected boolean showDepth = false;
    protected boolean showNormals = false;
    protected boolean statisticsCreated = false;
    protected AbstractGame game = null;
    
    /**
     * Contains an ArrayList of the BurstNodes
     */
    private ArrayList<BurstNode> burstGrid;
    
	private AbsoluteMouse mouse;
    
    public BurstGridState() {
    	super("burstGridState");
    	init();
    }
    
    private void init() {
        // Create ZBuffer for depth
        ZBufferState zbs = DisplaySystem.getDisplaySystem().getRenderer()
                .createZBufferState();
        zbs.setEnabled(true);
        zbs.setFunction(ZBufferState.TestFunction.LessThanOrEqualTo);
        rootNode.setRenderState(zbs);

        Sphere s = new Sphere("My sphere", 10, 10, 1f);
		// Do bounds for the sphere, but we'll use a BoundingBox this time
		s.setModelBound(new BoundingBox());
		s.updateModelBound();
		
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
    }
    
    private void readBurstGrid(String filename){
		try{
			FileInputStream fs = new FileInputStream(filename + ".txt");
			DataInputStream in = new DataInputStream(fs);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			String s;
			while ((s = br.readLine()) != null){
				System.out.println (s);
			}
			//Close the input stream
			in.close();
		}catch (Exception e){//Catch exception if any
			System.err.println("Error: " + e.getMessage());
		}
	}
        
    /**
     * Returns the BurstGrid.
     */
    public ArrayList<BurstNode> getGrid() {
    	return burstGrid;
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
        KeyBindingManager.getKeyBindingManager().set("screen_shot",
                KeyInput.KEY_F1);
        KeyBindingManager.getKeyBindingManager().set("exit",
                KeyInput.KEY_ESCAPE);
        KeyBindingManager.getKeyBindingManager().set("mem_report",
                KeyInput.KEY_R);
        KeyBindingManager.getKeyBindingManager().set("toggle_mouse",
                        KeyInput.KEY_M);
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
    	
    	// Update the PlayerInput
    	if (playerInput != null) {
    		playerInput.checkKeys();
    	}
    	
        // Update the geometric state of the rootNode
        rootNode.updateGeometricState(tpf, true);

        if (input != null) {
        	/** If camera controls are valid commands (via WASD keys), change camera angle. */
        	if (KeyBindingManager.getKeyBindingManager().isValidCommand(
	                "cam_left", true)) {
        		camRotXZ -= camRotXZSpd;
        		if (cameraMode != "free")
        			camRotXZ = Math.max(camRotXZ, camRotXZLimit[0]);
	        }
        	if (KeyBindingManager.getKeyBindingManager().isValidCommand(
	                "cam_right", true)) {
	            camRotXZ += camRotXZSpd;
	            if (cameraMode != "free")
        			camRotXZ = Math.min(camRotXZ, camRotXZLimit[1]);
	        }
        	if (KeyBindingManager.getKeyBindingManager().isValidCommand(
	                "cam_up", true)) {
        		if (cameraMode == "free")
        			camRotY = Math.min(camRotY + camRotYSpd, camRotYLimit[1]);
        		else
        			camRotY = Math.min(camRotY + camRotYSpd, camRotYLimit[2]);
	        }
        	if (KeyBindingManager.getKeyBindingManager().isValidCommand(
	                "cam_down", true)) {
        		camRotY = Math.max(camRotY - camRotYSpd, camRotYLimit[0]);
	        }
        	/** If target_near is a valid command (via key 1), switch to next closest target.
	        if (KeyBindingManager.getKeyBindingManager().isValidCommand(
	                "target_near", false) && cameraMode == "lock") {
	            nextTarget(-1);
	            rootNode.updateRenderState();
	        }
	        /** If target_far is a valid command (via key 2), switch to next furthest target. 
	        if (KeyBindingManager.getKeyBindingManager().isValidCommand(
	                "target_far", false) && cameraMode == "lock") {
	            nextTarget(1);
	            rootNode.updateRenderState();
	        }
	        */
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
	        /** If camera_out is a valid command (via key O), show camera location. */
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
        }
    }
    
    /**
     * Toggles camera mode, maintaining viewpoint when switching to free, and auto-locking
     * closest target when switching to lock
    private void swapCameraMode() {
    	if (cameraMode == "lock") {
    		cameraMode = "free";
    		Vector3f c = cam.getLocation();
    		Vector3f p = player.getTrackPoint();
    		//camRotY = (float)Math.asin((double)((c.y-p.y)/cameraDistance));
    		camRotXZ = (float)Math.atan2((double)(c.x-p.x), (double)(c.z-p.z));
    	} else if (cameraMode == "free") {
    		// make sure there are trackable objects
    		boolean noTargets = true;
    		for (int i=0; i<models.size(); i++) {
        		if (models.get(i) != player && models.get(i).trackable) {
        			noTargets = false;
        			break;
        		}
        	}
    		if (!noTargets) {
    			cameraMode = "lock";
    			camRotXZ = 0;
    			nextTarget(0);
    		}
    	}
    	System.out.println("Camera switch to "+cameraMode);
    }
     */
    
    /**
     * Sets the target to the specified model and returns whether it was in the model list
    public Boolean setTarget(Model m) {
    	int i = models.indexOf(m);
    	if (i != -1)
    		currentTarget = m;
    	cam.update();
    	return (i != -1);
    }
     */
    
    /**
     * Sets the currentTarget to another model, according to the value of {@code k}
     * @param k -1 for next closest from current target, 1 for next further,
     * and 0 for closest to player
    public void nextTarget(int k) {
    	// do nothing if there are no models
    	if (burstGrid.size() == 0)
    		return;
    	
    	ArrayList<Object[]> o = new ArrayList<Object[]>();
    	
    	// Add models and distances to 2D ArrayList
    	for (int i=0; i<models.size(); i++) {
    		Model m = models.get(i);
    		if (m != player && m.trackable) {
    			Object[] a = new Object[2];
    			a[0] = new Float(player.model.getWorldTranslation().distance(m.model.getWorldTranslation()));
    			a[1] = m;
    			o.add(a);
    		}
    	}
    	
    	// Make it an array
    	Object[] t = o.toArray();
    	
    	// Sort 2D array by distances
    	Arrays.sort(t, new Comparator<Object>() {
        	public int compare(Object one, Object two){
        		Object[] first = (Object[]) one;
        		Object[] secnd = (Object[]) two;
        		//System.out.println((Float)(first[0])+" - "+(Float)(secnd[0]));
        		return (int)((Float)(first[0]) - (Float)(secnd[0]));
        	}
        });
    	/*
    	System.out.print("[ ");
    	for (Object f : t) {System.out.print(Arrays.toString((Object[])f));}
    	System.out.println(" ]");
    	*/
/*    	
        // Locate the currentTarget's index
        int ind = -1;
        for (int i=0; i<t.length; i++) {
    		if (((Object[])t[i])[1] == currentTarget) {
    			ind = i;
    			break;
    		}
    	}
        
    	// Set the new target
        switch (k) {
        	case 0: currentTarget = (Model)(((Object[])t[0])[1]); break;
        	case -1: currentTarget = (Model)(((Object[])t[(t.length+ind-1)%t.length])[1]); break;
        	case 1: currentTarget = (Model)(((Object[])t[(ind+1)%t.length])[1]);
        }
        */
        
        /*
        // move test cylinders to lock point
        Vector3f tp = currentTarget.getTrackPoint();
        for (int i=0; i<cyl.length; i++) {
        	cyl[i].setLocalTranslation(tp.x, tp.y, tp.z);
        }
        
        // Update camera
        cam.update();
    }
         */

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
