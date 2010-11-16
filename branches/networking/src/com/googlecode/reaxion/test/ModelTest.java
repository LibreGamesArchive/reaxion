/*
 * Copyright (c) 2008, OgreLoader
 *
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *     - Redistributions of source code must retain the above copyright
 *       notice, this list of conditions and the following disclaimer.
 *     - Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the
 *       documentation and/or other materials provided with the distribution.
 *     - Neither the name of the Gibbon Entertainment nor the
 *       names of its contributors may be used to endorse or promote products
 *       derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY 'Gibbon Entertainment' "AS IS" AND ANY
 * EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL 'Gibbon Entertainment' BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package com.googlecode.reaxion.test;

import com.radakan.jme.mxml.*;
import com.jme.app.SimpleGame;
import com.jme.input.FirstPersonHandler;
import com.jme.input.KeyInput;
import com.jme.renderer.ColorRGBA;
import com.jme.scene.Node;
import com.jme.system.DisplaySystem;
import com.jme.util.resource.ResourceLocatorTool;
import com.jme.util.resource.SimpleResourceLocator;
import com.radakan.jme.mxml.anim.*;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ModelTest extends SimpleGame {

    private static final Logger logger = Logger.getLogger(ModelTest.class.getName());
    
    private Node model;
    
    // holds all animation names
    private ArrayList<String> animation = new ArrayList<String>();
    int index = 0;
    
    public static void main(String[] args){
        ModelTest app = new ModelTest();
        app.setConfigShowMode(ConfigShowMode.AlwaysShow);
        app.start();
    }
    
    protected void loadMeshModel(){
        OgreLoader loader = new OgreLoader();
        MaterialLoader matLoader = new MaterialLoader();
        
        // Attempt to load material references and model geometry
        try {
            URL matURL = ModelTest.class.getClassLoader().getResource("com/googlecode/reaxion/resources/i_khoa6.material");
            URL meshURL = ModelTest.class.getClassLoader().getResource("com/googlecode/reaxion/resources/i_khoa6.mesh.xml");
            
            if (matURL != null){
                matLoader.load(matURL.openStream());
                if (matLoader.getMaterials().size() > 0)
                    loader.setMaterials(matLoader.getMaterials());
            }
            
            model = loader.loadModel(meshURL);
        } catch (IOException ex) {
            Logger.getLogger(ModelTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @Override
    protected void simpleInitGame() {
        try {
            SimpleResourceLocator locator = new SimpleResourceLocator(ModelTest.class
                                                    .getClassLoader()
                                                    .getResource("com/googlecode/reaxion/resources/"));
            ResourceLocatorTool.addResourceLocator(
                    ResourceLocatorTool.TYPE_TEXTURE, locator);
            ResourceLocatorTool.addResourceLocator(
                    ResourceLocatorTool.TYPE_MODEL, locator);
        } catch (URISyntaxException e1) {
            logger.log(Level.WARNING, "unable to setup texture directory.", e1);
        }
        
        Logger.getLogger("com.jme.scene.state.lwjgl").setLevel(Level.SEVERE);
        
        DisplaySystem.getDisplaySystem().setTitle("Test Mesh Instancing");
        display.getRenderer().setBackgroundColor(ColorRGBA.darkGray);
        ((FirstPersonHandler)input).getKeyboardLookHandler().setMoveSpeed(100);
        cam.setFrustumFar(20000f);
        loadMeshModel();
        
        // Populate list with animation states
        for (String i : ((MeshAnimationController)(model.getController(0))).getAnimationNames())
        	animation.add(i);
        System.out.println("Animations: "+animation);
        
        // add the model to the scene
        rootNode.attachChild(model);
        
        updateAnimation();
    }
    
    private void updateAnimation() {
        // Not quite sure what this does (bone traversal is my guess), but the animation referenced is a preset in the skeleton file
        //for (int x = 0; x < 1; x++){
            //for (int y = 0; y < 1; y++){
                
                if (model.getControllerCount() > 0){
                    MeshAnimationController animControl = (MeshAnimationController) model.getController(0);
                    animControl.setAnimation(animation.get(index));
                    //animControl.setTime(animControl.getAnimationLength(animation.get(index)) * FastMath.nextRandomFloat());
                }
            //}
        //}
        
        rootNode.updateGeometricState(0, true);
        rootNode.updateRenderState();
    }
    
    // Update each frame to check for input
    @Override
	protected void simpleUpdate(){
        // press 1 and 2 to cycle through states
        if (KeyInput.get().isKeyDown(KeyInput.KEY_1)){
        	index = (animation.size() + index - 1) % animation.size();
        	updateAnimation();
        }else if (KeyInput.get().isKeyDown(KeyInput.KEY_2)){
        	index = (animation.size() + index + 1) % animation.size();
        	updateAnimation();
        }
    }

    
    
}
