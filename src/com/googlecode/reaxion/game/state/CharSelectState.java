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
    
    
    MajorCharacter[] selectedChars = new MajorCharacter[3];
    ArrayList <MajorCharacter> p1List = new ArrayList<MajorCharacter>();
    ArrayList <MajorCharacter> p2List = new ArrayList<MajorCharacter>();
    ArrayList <MajorCharacter> opList = new ArrayList<MajorCharacter>();
    
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
            
            
            MajorCharacter m = (MajorCharacter)LoadingQueue.push(new Monica(false));
	        MajorCharacter t = (MajorCharacter)LoadingQueue.push(new Khoa(false));        
	        MajorCharacter c = (MajorCharacter)LoadingQueue.push(new Cy(false));     
	        MajorCharacter n = (MajorCharacter)LoadingQueue.push(new Nilay(false));
	        
	        MajorCharacter m2 = (MajorCharacter)LoadingQueue.push(new Monica(false));
	        MajorCharacter t2 = (MajorCharacter)LoadingQueue.push(new Khoa(false));        
	        MajorCharacter c2 = (MajorCharacter)LoadingQueue.push(new Cy(false));     
	        MajorCharacter n2 = (MajorCharacter)LoadingQueue.push(new Nilay(false));
	        
	        MajorCharacter mo = (MajorCharacter)LoadingQueue.push(new Monica());
	        MajorCharacter to = (MajorCharacter)LoadingQueue.push(new Khoa());        
	        MajorCharacter co = (MajorCharacter)LoadingQueue.push(new Cy());     
	        MajorCharacter no = (MajorCharacter)LoadingQueue.push(new Nilay());
	        

	        
	        p1List.add(t);
	        p1List.add(c);
	        p1List.add(n);
	        p1List.add(m);
	        p2List.add(t2);
	        p2List.add(c2);
	        p2List.add(n2);
	        p2List.add(m2);
	        opList.add(to);
	        opList.add(co);
	        opList.add(no);
	        opList.add(mo);
	        
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
        	
        	/*{
        		if (KeyBindingManager.getKeyBindingManager().isValidCommand(
    	                "p11", false)) {
        			selectedChars[0] = p1List.get(0);
        		}
        		if (KeyBindingManager.getKeyBindingManager().isValidCommand(
    	                "p12", false)) {
        			selectedChars[0] = p1List.get(1);
        		}
        		if (KeyBindingManager.getKeyBindingManager().isValidCommand(
    	                "p13", false)) {
        			selectedChars[0] = p1List.get(2);
        		}
        		if (KeyBindingManager.getKeyBindingManager().isValidCommand(
    	                "p141", false)) {
        			selectedChars[0] = p1List.get(3);
        		}
        		if (KeyBindingManager.getKeyBindingManager().isValidCommand(
    	                "p15", false)) {
        			selectedChars[0] = p1List.get(4);
        		}
        		if (KeyBindingManager.getKeyBindingManager().isValidCommand(
    	                "p16", false)) {
        			selectedChars[0] = p1List.get(5);
        		}
        	}*/
        	
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
    		//String className = s.getstageOverlay().getSelectedStageClass();

    		
    		
			/*{
    		
    		BattleGameState battleState = new BattleGameState();

    		// Set the stage
    		try {
    			Class cl;
    			cl = Class.forName(stageClassURL + className);
    			Stage temp = (Stage) cl.getConstructors()[0].newInstance();
    			Stage cb = (Stage) LoadingQueue.push(temp);
    		} catch (Exception e) {
    			e.printStackTrace();
    		}

    		// Add some characters
    		ArrayList<MajorCharacter> p1 = new ArrayList<MajorCharacter>();
    		ArrayList<MajorCharacter> p2 = new ArrayList<MajorCharacter>();
    		ArrayList<MajorCharacter> op = new ArrayList<MajorCharacter>();
    		
    		p1.add(new Khoa(false));
    		p1.add(new Cy(false));
    		p1.add(new Nilay(false));
    		p1.add(new Monica(false));
    		
    		p2.add(new Khoa(false));
    		p2.add(new Cy(false));
    		p2.add(new Nilay(false));
    		p2.add(new Monica(false));
    		
    		op.add(new Khoa());
    		op.add(new Cy());
    		op.add(new Nilay());
    		op.add(new Monica());
    		
    		MajorCharacter PL1 = (MajorCharacter) LoadingQueue.push(p1.get(selected[0]));
    		MajorCharacter PL2 = (MajorCharacter) LoadingQueue.push(p2.get(selected[1]));
    		MajorCharacter PLO = (MajorCharacter) LoadingQueue.push(op.get(selected[2]));
    		
    		

    		// Load everything!
    		LoadingQueue.execute(battleState);

    		// Set up some abilities!
    		PL1.setAbilities(new Ability[] { new EvasiveStart() });
    		PL2.setAbilities(new Ability[] { new AfterImage() });
    		PLO.setAbilities(new Ability[] { new AfterImage() });

    		// Set up test attacks!
    		Class[] attacks1 = new Class[6];
    		Class[] attacks2 = new Class[6];
    		try {
    			attacks1[0] = Class.forName(attackBaseLocation + "ShootBullet");
    			attacks1[1] = Class.forName(attackBaseLocation + "ShieldBarrier");

    			attacks2[0] = Class.forName(attackBaseLocation + "SpinLance");
    			attacks2[1] = Class.forName(attackBaseLocation + "AngelRain");
    		} catch (Exception e) {
    			e.printStackTrace();
    		}

    		// Set up some AI!
    		PLO.assignAI(new TestAI(PLO));
    		//t.hp = 5;

    		// Set the opponent!
    		Character[] opponents = new Character[1];
    		opponents[0] = PLO;
    		battleState.assignOpponents(opponents);

    		// Set stuff in the battleState
    		battleState.assignTeam(PL1, attacks1, PL2, attacks2);
    		//c.model.setLocalTranslation(2, 5, -1);
    		//c2.model.setLocalTranslation(6, 5, 3);
    		//c2.gravity = 0;
    		//n.model.setLocalTranslation(-5, 0, -3);
    		battleState.nextTarget(0);

    		// Set up BGM
    		try {
    			BgmPlayer.prepare();
    			BgmPlayer.play(battleState.getStage().bgm[0]);
    		} catch (NullPointerException e) {
    			System.out.println("No BGM for " + className);
    		}

    		// test sounds, uncomment to test
    		// SfxPlayer.addSfx("test3.ogg", t, 50, true);

    		// Set some game conditions...
    		battleState.targetTime = 60;
    		battleState.expYield = 1000;

    		// reupdate due to added changes
    		battleState.getRootNode().updateRenderState();

    		PL1.play("stand");

    		GameStateManager.getInstance().attachChild(battleState);
    		battleState.setActive(true);
    		setActive(false);
    		
    		
			}*/
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