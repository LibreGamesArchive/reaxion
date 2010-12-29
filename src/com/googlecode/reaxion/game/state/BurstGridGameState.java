package com.googlecode.reaxion.game.state;

import java.util.ArrayList;
import java.util.logging.Logger;

import com.googlecode.reaxion.game.Reaxion;
import com.googlecode.reaxion.game.burstgrid.BurstGrid;
import com.googlecode.reaxion.game.burstgrid.info.PlayerInfo;
import com.googlecode.reaxion.game.burstgrid.node.AbilityNode;
import com.googlecode.reaxion.game.burstgrid.node.AttackNode;
import com.googlecode.reaxion.game.burstgrid.node.BurstNode;
import com.googlecode.reaxion.game.burstgrid.node.HPNode;
import com.googlecode.reaxion.game.burstgrid.node.MaxGaugeNode;
import com.googlecode.reaxion.game.burstgrid.node.MinGaugeNode;
import com.googlecode.reaxion.game.burstgrid.node.RateNode;
import com.googlecode.reaxion.game.burstgrid.node.StrengthNode;
import com.googlecode.reaxion.game.input.bindings.BurstGridStateBindings;
import com.googlecode.reaxion.game.input.bindings.DebugBindings;
import com.googlecode.reaxion.game.input.bindings.GlobalBindings;
import com.googlecode.reaxion.game.mission.MissionManager;
import com.googlecode.reaxion.game.overlay.BurstGridOverlay;
import com.jme.app.AbstractGame;
import com.jme.image.Texture;
import com.jme.input.InputHandler;
import com.jme.input.KeyBindingManager;
import com.jme.math.FastMath;
import com.jme.math.Matrix3f;
import com.jme.math.Vector3f;
import com.jme.renderer.Renderer;
import com.jme.scene.Node;
import com.jme.scene.Spatial;
import com.jme.scene.shape.Quad;
import com.jme.scene.state.BlendState;
import com.jme.scene.state.TextureState;
import com.jme.system.DisplaySystem;
import com.jme.util.TextureManager;
import com.jmex.game.state.GameStateManager;
import com.jmex.game.state.StatisticsGameState;

/**
 * GameState that allows for the viewing and editing of the BurstGrid.
 * @author Khoa Ha
 *
 */
public class BurstGridGameState extends BaseGameState {
	
	public static final String NAME = "burstGridGameState";
	protected static final Logger logger = Logger.getLogger(StageGameState.class
            .getName());
	
	private static final Vector3f[] cameraLevel = {new Vector3f(22/3f, 0, -48), new Vector3f(7/3f, 0, -16)};
	
	private final float scale = 4;
	private Vector3f camOffset = cameraLevel[0];
	private final Vector3f bgOffset = new Vector3f(0, 0, 725);
	private final float camSpeed = 1f;
	private final float zoomSpeed = 3f;
	private int zoom = 0;
	
	private InputHandler input;
	private AbstractGame game = null;
	private boolean statisticsCreated = false;
	
	private float time = 0;
	
	private BurstGridOverlay burstOverlay;
	
	private PlayerInfo info;
	private BurstGrid grid;
	private Texture ci;
	private Texture ca;
	
	private ArrayList<BurstNode> nodeStack = new ArrayList<BurstNode>();
	
	private BurstNode prevNode;
	private BurstNode currentNode;
	private Vector3f focus;
	private Vector3f destination;
	
	private int currentCost;
	
	private Node gridNode;
	private Node cursorNode;
	private Node bgNode;
	private Quad[] clouds = new Quad[2];
	
	private Quad prevRing;
	private Quad currentRing;
	
	public BurstGridGameState(PlayerInfo info) {
    	super(false);
    	this.info = info;
    	this.grid = info.getBurstGrid();
    	init();
    }
	
	protected void init() {
		setName(NAME);
		
		startsBGM = true;
		endsBGM = true;
		bgm = "the_sunleth_waterscape.ogg";
		
		// Prepare results node
		burstOverlay = new BurstGridOverlay();
		burstOverlay.setStaticText(info);
		burstOverlay.updateStats(info);
		burstOverlay.updateCount(grid);
		rootNode.attachChild(burstOverlay);
		
        // create a blend state to handle transparency
        final BlendState bs = DisplaySystem.getDisplaySystem().getRenderer().createBlendState();
        bs.setBlendEnabled(true);
        bs.setSourceFunctionAlpha(BlendState.SourceFunction.OneMinusDestinationAlpha);
        bs.setDestinationFunctionAlpha(BlendState.DestinationFunction.DestinationAlpha);
        bs.setEnabled(true);
        rootNode.setRenderState(bs);
        
        rootNode.setRenderQueueMode(Renderer.QUEUE_TRANSPARENT);
        rootNode.setLightCombineMode(Spatial.LightCombineMode.Off);
        
        // create background
        bgNode = createBg();
        rootNode.attachChild(bgNode);
        
        // create gridNode
        gridNode = new Node("gridNode");
        cursorNode = new Node("cursorNode");
        rootNode.attachChild(gridNode);
        rootNode.attachChild(cursorNode);
        
        // create textures
        ci = createTexture("connector-inactive");
        ca = createTexture("connector-active");
        
        // createRings
        createRings();
        
        // base node is always active
        grid.getNodes().get(0).activated = true;
        
        // read in burstgrid
        readNodes();
        
        // set initials
        currentNode = prevNode = grid.getNodes().get(0);
        destination = focus = currentNode.vect;
        
        // initialize input handler
        input = new InputHandler();
        
        System.out.println(grid);
    }

	@Override
	public void stateUpdate(float tpf) {
		super.stateUpdate(tpf);
		
		// Update the InputHandler
    	if (input != null) {
    		input.update(tpf);
    		
    		/** If exit is a valid command (via key Esc), exit game */
    		if (KeyBindingManager.getKeyBindingManager().isValidCommand(GlobalBindings.EXIT.toString(),
    				false)) {
    			
    			if (game != null) {
    				game.finish();
    			} else {
    				Reaxion.terminate();
    			}
    		}
    		
            	/** If traversal controls are valid commands (via arrow keys), change camera movement. */
    			if (focus == destination) {
    				
    				if (KeyBindingManager.getKeyBindingManager().isValidCommand(BurstGridStateBindings.ZOOM.toString(), false)) {
    					zoom = (zoom + 1) % 2;
    				}
    				if (KeyBindingManager.getKeyBindingManager().isValidCommand(BurstGridStateBindings.RETURN_TO_HGS.toString(), false)) {
    					//TODO: Confirmation Overlay
    					setActive(false);
    					GameStateManager.getInstance().detachChild(this);
    					MissionManager.startHubGameState();
    				}
    				
    				if (KeyBindingManager.getKeyBindingManager().isValidCommand(
    						BurstGridStateBindings.TRAVERSE_COUNTERCLOCKWISE.toString(), false)) {
    					if (prevNode != currentNode) {
    						ArrayList<BurstNode> nodes = prevNode.nodes;
    						currentNode = nodes.get((nodes.indexOf(currentNode)+nodes.size()-1) % nodes.size());
    						destination = currentNode.vect.mult(scale);
    					}
    				}
    				if (KeyBindingManager.getKeyBindingManager().isValidCommand(
    						BurstGridStateBindings.TRAVERSE_CLOCKWISE.toString(), false)) {
    					if (prevNode != currentNode) {
    						ArrayList<BurstNode> nodes = prevNode.nodes;
    						currentNode = nodes.get((nodes.indexOf(currentNode)+1) % nodes.size());
    						destination = currentNode.vect.mult(scale);
    					}
    				}
    				if (KeyBindingManager.getKeyBindingManager().isValidCommand(
    						BurstGridStateBindings.TRAVERSE_NEXT.toString(), false)) {
    					ArrayList<BurstNode> nodes = currentNode.nodes;
    					stackNode(currentNode);
    					prevNode = currentNode;
    					// try to find unactivated node
    					boolean found = false;
    					for (BurstNode n : nodes) {
    						if (n != prevNode && !n.activated) {
    							currentNode = n;
    							found = true;
    						}
    					}
    					// settle for an activated one
    					if (!found) {
    						if (prevNode == nodes.get(0) && nodes.size() > 1)
    							currentNode = nodes.get(1);
    						else
    							currentNode = nodes.get(0);
    					}
    					destination = currentNode.vect.mult(scale);
    				}
    				if (KeyBindingManager.getKeyBindingManager().isValidCommand(
    						BurstGridStateBindings.TRAVERSE_BACK.toString(), false)) {
    					if (nodeStack.size() > 0) {
    						currentNode = nodeStack.remove(nodeStack.size()-1);
    						if (nodeStack.size() > 0)
    							prevNode = nodeStack.get(nodeStack.size()-1);
    						else
    							prevNode = currentNode;
    						destination = currentNode.vect.mult(scale);
    					}
    				}
    				
    				/** If buy_node is a valid command (via Enter key), activate node. */
                    if (KeyBindingManager.getKeyBindingManager().isValidCommand(
                    		BurstGridStateBindings.BUY_NODE.toString(), false)) {
                    	buyNode(currentNode);
                    }
                    
    			}
            	
    	        /** If toggle_stats is a valid command (via key F4), change depth. */
                if (KeyBindingManager.getKeyBindingManager().isValidCommand(
                        DebugBindings.TOGGLE_STATS.toString(), false)) {
                	if (statisticsCreated == false) {
    	                // create a statistics game state
    	                GameStateManager.getInstance().attachChild(
    	                		new StatisticsGameState("stats", 1f, 0.25f, 0.75f, true));
    	                statisticsCreated = true;
                	}
                    GameStateManager.getInstance().getChild("stats").setActive(
                            !GameStateManager.getInstance().getChild("stats").isActive());
                }
                
                /** If screen_shot is a valid command (via key F1), take snapshot. */
    	        if (KeyBindingManager.getKeyBindingManager().isValidCommand(
    	        		GlobalBindings.SCREENSHOT.toString(), false)) {
    	        	Reaxion.takeScreenshot("BurstGrid");
    	        }
    	        
    	        /** If mem_report is a valid command (via key R), display report. */
    	        if (KeyBindingManager.getKeyBindingManager().isValidCommand(
    	                GlobalBindings.MEM_REPORT.toString(), false)) {
    	            long totMem = Runtime.getRuntime().totalMemory();
    	            long freeMem = Runtime.getRuntime().freeMemory();
    	            long maxMem = Runtime.getRuntime().maxMemory();
    	
                    logger.info("|*|*|  Memory Stats  |*|*|");
                    logger.info("Total memory: " + (totMem >> 10) + " kb");
                    logger.info("Free memory: " + (freeMem >> 10) + " kb");
                    logger.info("Max memory: " + (maxMem >> 10) + " kb");
    	        }
    	}

    	// displace textures
    	ca.getTranslation().y += 0.001f;
    	if(ca.getTranslation().y > 1)
    		ca.getTranslation().y = 0;

    	// change the focus
    	if (focus.distance(destination) > camSpeed)
    		focus = focus.add(destination.subtract(focus).normalize().mult(camSpeed));
    	else
    		focus = destination;
    	
    	// change zoom
    	if (camOffset.distance(cameraLevel[zoom]) > zoomSpeed)
    		camOffset = camOffset.add(cameraLevel[zoom].subtract(camOffset).normalize().mult(zoomSpeed));
    	else
    		camOffset = cameraLevel[zoom];
    	
    	// update the camera
    	cam.setLocation(focus.add(camOffset));
    	
    	// update rings
    	prevRing.setLocalTranslation(prevNode.vect.mult(scale).add(0, 0, -.01f));
    	currentRing.setLocalTranslation(currentNode.vect.mult(scale).add(0, (FastMath.sin(time*4)+1)/6, -.01f));
    	
    	// shift bg
    	bgNode.setLocalTranslation(cam.getLocation().add(bgOffset));
    	for (int i=0; i<clouds.length; i++) {
    		if (clouds[i].getLocalTranslation().x + 1 >= 2000)
    			clouds[i].getLocalTranslation().x = -1200;
    		else
    			clouds[i].getLocalTranslation().x += 1;
    	}
    	
    	// update node display info
    	if (focus == destination) {
    		findCost(currentNode);
    		burstOverlay.updateDescriptors(info, currentNode, currentCost);
    	}
    	
    	rootNode.updateGeometricState(tpf, true);
    	rootNode.updateRenderState();
    	
    	time += tpf;
    }
	
	/**
	 * Adds a node to the traversal stack and optimizes it.
	 */
	private void stackNode(BurstNode b) {
		int ind = nodeStack.indexOf(b);
		if (ind != -1)
			nodeStack.subList(0, ind);
		else
			nodeStack.add(b);
	}
	
	 /**
     * Returns a Texture loaded from {@code str}.
     */
    private Texture createTexture(String str) { 	
        Texture tex = TextureManager.loadTexture(
        		getClass().getResource("../../resources/icons/cosmos/"+str+".png"),
                  Texture.MinificationFilter.Trilinear, Texture.MagnificationFilter.Bilinear);  
        tex.setWrap(Texture.WrapMode.Repeat);
        tex.setTranslation(new Vector3f());
        return tex;
        
    }
    
    /**
	 * Creates the background Quad.
	 * @param str
	 */
	private Node createBg() {
		Node bg = new Node("bgNode");
		
		Quad sky = new Quad("", 800, 600);
        TextureState ts = DisplaySystem.getDisplaySystem().getRenderer().createTextureState();
        ts.setEnabled(true);
		ts.setTexture(createTexture("bg"));
		sky.setRenderState(ts);
		
		for (int i=0; i<clouds.length; i++) {
			clouds[i] = new Quad("", 1600, 232);
			ts = DisplaySystem.getDisplaySystem().getRenderer().createTextureState();
	        ts.setEnabled(true);
			ts.setTexture(createTexture("clouds"));
			clouds[i].setRenderState(ts);
			clouds[i].setLocalTranslation(new Vector3f(1600*i, -184, .001f));
		}
		
		Quad buildings = new Quad("", 800, 600);
		ts = DisplaySystem.getDisplaySystem().getRenderer().createTextureState();
        ts.setEnabled(true);
		ts.setTexture(createTexture("bg-buildings"));
		buildings.setRenderState(ts);
		buildings.setLocalTranslation(new Vector3f(0, 0, .002f));
		
		bg.attachChild(sky);
		bg.attachChild(clouds[0]);
		bg.attachChild(clouds[1]);
		bg.attachChild(buildings);

		Matrix3f m = new Matrix3f();
		m.fromAngleAxis(FastMath.PI, new Vector3f(0, 1, 0));
		bg.setLocalRotation(m);
		
        return bg;
	}
    
	/**
	 * Determine the cost of the current node.
	 */
	private void findCost(BurstNode b) {
		currentCost = Integer.MAX_VALUE;
		// find the lowest cost from an activated node
		for (int i=0; i<b.nodes.size(); i++) {
			if (b.nodes.get(i).activated && b.costs.get(i) < currentCost)
				currentCost = b.costs.get(i);
		}
	}
	
	/**
	 * Attempt to purchase the current node.
	 */
	private void buyNode(BurstNode b) {
		if (!b.activated && currentCost <= info.exp) {
			b.activated = true;
			info.exp -= currentCost;
			info.readStatsFromGrid();
			
			// update display
			burstOverlay.updateStats(info);
			burstOverlay.updateCount(grid);
			
			// redraw nodes
			gridNode.detachAllChildren();
			readNodes();
		}
	}
	
    /**
	 * Creates nodes from Burstgrid.
	 */
	private void readNodes(){
		ArrayList<BurstNode> bg = grid.getNodes();
		for(BurstNode b:bg){
			// create node
			if(b instanceof HPNode)
				createNode(b.activated, "hp", b.vect);
			else if(b instanceof StrengthNode)
				createNode(b.activated, "strength", b.vect);
			else if(b instanceof MinGaugeNode)
				createNode(b.activated, "gauge1", b.vect);
			else if(b instanceof MaxGaugeNode)
				createNode(b.activated, "gauge2", b.vect);
			else if(b instanceof RateNode)
				createNode(b.activated, "rate", b.vect);
			else if(b instanceof AbilityNode)
				createNode(b.activated, "ability", b.vect);
			else if(b instanceof AttackNode)
				createNode(b.activated, "attack", b.vect);
			
			// create connections
	        for(BurstNode c:b.nodes)
				createConnector(b.activated && c.activated, b.vect, c.vect);
		}
	}
	
	/**
	 * Creates the node of type str at position vec.
	 * @param str
	 */
	private void createNode(boolean active, String str, Vector3f vec) {
		Quad q = new Quad("", 1, 1);

        TextureState ts = DisplaySystem.getDisplaySystem().getRenderer().createTextureState();
        ts.setEnabled(true);
		ts.setTexture(createTexture(str + (active?"":"-i") ));
		q.setRenderState(ts);

        gridNode.attachChild(q);
        q.setLocalTranslation(vec.mult(scale));
        
        if (active)
        	createGlow(vec);
	}
	
	/**
     * Create ring indicators. 
     */
	private void createRings() {
		prevRing = new Quad("", 2, 2);
    	TextureState gs = DisplaySystem.getDisplaySystem().getRenderer().createTextureState();
		gs.setTexture(createTexture("root-locator"));
		prevRing.setRenderState(gs);
        cursorNode.attachChild(prevRing);
        
        currentRing = new Quad("", 2, 2);
    	TextureState gs2 = DisplaySystem.getDisplaySystem().getRenderer().createTextureState();
		gs2.setTexture(createTexture("current-locator"));
		currentRing.setRenderState(gs2);
        cursorNode.attachChild(currentRing);
	}
	
    /**
     * Create a glow at vec.    
     */
	private void createGlow(Vector3f vec) {
		Quad g = new Quad("", 3, 3);
    	TextureState gs = DisplaySystem.getDisplaySystem().getRenderer().createTextureState();
		gs.setTexture(createTexture("shine"));
		g.setRenderState(gs);

        gridNode.attachChild(g);
        g.setLocalTranslation(vec.mult(scale).add(new Vector3f(0, 0, -.01f)));
	}
	
	/**
	 * Create a connector between positions from and to.
	 */
	private void createConnector(boolean active, Vector3f from, Vector3f to) {
		TextureState ts = DisplaySystem.getDisplaySystem().getRenderer().createTextureState();
    	if (active)
    		ts.setTexture(ca);
    	else
    		ts.setTexture(ci);
        
    	Quad q = new Quad("", .1f, from.distance(to)*scale);
    	q.setLocalTranslation(from.add(to).divide(2).mult(scale));
    	Matrix3f m = new Matrix3f();
    	m.fromStartEndVectors(new Vector3f(0, 1, 0), to.subtract(from).normalize());
    	q.setLocalRotation(m);
    	
        q.setRenderState(ts);
        gridNode.attachChild(q);
	}
	
}
