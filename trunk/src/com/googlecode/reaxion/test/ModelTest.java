package com.googlecode.reaxion.test;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.concurrent.Callable;
import java.util.logging.Level;
import java.util.logging.Logger;

import jmetest.TutorialGuide.HelloModelLoading;
import jmetest.renderer.loader.TestColladaLoading;

import com.jme.animation.Bone;
import com.jme.animation.SkinNode;
import com.jme.app.SimpleGame;
import com.jme.app.AbstractGame.ConfigShowMode;
import com.jme.bounding.BoundingSphere;
import com.jme.math.Vector3f;
import com.jme.scene.Node;
import com.jme.scene.shape.Box;
import com.jme.system.DisplaySystem;
import com.jme.util.GameTaskQueueManager;
import com.jme.util.export.binary.BinaryImporter;
import com.jmex.editors.swing.settings.GameSettingsPanel;
import com.jmex.game.StandardGame;
import com.jmex.game.state.DebugGameState;
import com.jmex.game.state.GameStateManager;
import com.jmex.game.state.load.LoadingGameState;
import com.jmex.model.collada.ColladaImporter;
import com.jmex.model.collada.ColladaToJme;
import com.jmex.model.converters.FormatConverter;
import com.jmex.model.converters.ObjToJme;


public class ModelTest extends SimpleGame {
	
	private static final Logger logger = Logger
    .getLogger(ModelTest.class.getName());

	private static final float GAME_VERSION = 0.1f;
	
	/**
	 * Filepath to test model
	 */
	private static final String path = "C:/Users/Khoa Ha/Programming/Java/Workspace/reaxion/src/com/googlecode/reaxion/resources/cow.dae";

	/**
	 * Initialize the system
	 */

	public static void main(String[] args) {
		ModelTest main = new ModelTest();
		main.setConfigShowMode(ConfigShowMode.AlwaysShow);
		main.start();
	}
	
	/**
	 * Start up the system
	 * @throws IOException when StandardGame's settings are unavailable (?)	
	 * @throws InterruptedException 
	 */
	protected void simpleInitGame() {
		Vector3f center = new Vector3f(-1, -1, -1);
		rootNode.attachChild(new Box("box", center, .5f, .5f, .5f));
		rootNode.attachChild(getModel());
	}
	
	private Node getModel() {
		InputStream source;

		try {
			source = new FileInputStream(path);
		} catch (FileNotFoundException e) {
			throw new RuntimeException(e);
		}

		ColladaImporter.load(source, path);
		Node n = ColladaImporter.getModel();
		return(n);
	}
}
