package com.googlecode.reaxion.game.util;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.googlecode.reaxion.test.ModelTest;
import com.jme.scene.Node;
import com.jme.scene.Spatial;
import com.jme.scene.state.BlendState;
import com.jme.scene.state.ZBufferState;
import com.jme.system.DisplaySystem;
import com.radakan.jme.mxml.MaterialLoader;
import com.radakan.jme.mxml.MeshCloner;
import com.radakan.jme.mxml.OgreLoader;

/**
 * Stores model data into a map after first being loaded and serves up clones of the original
 * upon request to avoid having to load materials and geometry more than once.
 * @author Khoa
 */
public class ResourceLibrary {
	
	private static final String baseURL = "com/googlecode/reaxion/resources/";
	
	private static HashMap<String, Node> map = new HashMap<String, Node>();
	
	/**
	 * Loads the appropriate materials and geometry and stores the model for future reference. 
	 * @param filename
	 */
	public static void load(String filename) {
		OgreLoader loader = new OgreLoader();
        MaterialLoader matLoader = new MaterialLoader();
        
        // Attempt to load material references and model geometry
        try {
            URL matURL = ResourceLibrary.class.getClassLoader().getResource(baseURL+filename+".material");
            URL meshURL = ResourceLibrary.class.getClassLoader().getResource(baseURL+filename+".mesh.xml");
            
            if (matURL != null){
                matLoader.load(matURL.openStream());
                if (matLoader.getMaterials().size() > 0)
                    loader.setMaterials(matLoader.getMaterials());
            }
            
            // Store the loaded model to the map
            map.put(filename, (Node) loader.loadModel(meshURL));
            System.out.println(filename+" added to ResourceLibrary.");
            
            // Assign transparencies
//            List<Spatial> l = map.get(filename).getChildren();
//            for(Spatial s : l) {
//            	if (s.getRenderQueueMode() == 3)
//            		setTransparent(s);
//            		
//            }
        } catch (IOException ex) {
            Logger.getLogger(ModelTest.class.getName()).log(Level.SEVERE, null, ex);
        }
        
	}
	
	/**
	 * Returns the model associated with {@code filename} if it is defined, otherwise, it will be
	 * loaded and then returned.
	 */
	public static Node get(String filename) throws Exception {
		if (map.get(filename) == null)
			load(filename);
		return (MeshCloner.cloneMesh(map.get(filename)));
	}
	
	private static void setTransparent(Spatial s) {
		ZBufferState zbs = DisplaySystem.getDisplaySystem().getRenderer().createZBufferState();
        zbs.setWritable(false);
        zbs.setEnabled(true);
        zbs.setFunction(ZBufferState.TestFunction.LessThanOrEqualTo);
        s.setRenderState(zbs);
        
        BlendState alphaState = DisplaySystem.getDisplaySystem().getRenderer().createBlendState();
        alphaState.setBlendEnabled(true);
        alphaState.setSourceFunction(BlendState.SourceFunction.SourceAlpha);
        alphaState.setDestinationFunction(BlendState.DestinationFunction.OneMinusSourceAlpha);
        alphaState.setTestEnabled(true);
        alphaState.setTestFunction(BlendState.TestFunction.GreaterThan);
        alphaState.setEnabled(true);
//        alphaState.setReference(1);
 
        s.setRenderState(alphaState);
        s.updateRenderState();
	}

}
