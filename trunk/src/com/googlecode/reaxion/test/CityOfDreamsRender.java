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
import com.googlecode.reaxion.game.model.Model;
import com.googlecode.reaxion.game.state.StageGameState;
import com.googlecode.reaxion.game.util.LoadingQueue;
import com.jme.app.SimpleGame;
import com.jme.input.FirstPersonHandler;
import com.jme.light.DirectionalLight;
import com.jme.light.PointLight;
import com.jme.math.FastMath;
import com.jme.math.Plane;
import com.jme.math.Quaternion;
import com.jme.math.Vector3f;
import com.jme.renderer.ColorRGBA;
import com.jme.renderer.Renderer;
import com.jme.renderer.pass.BasicPassManager;
import com.jme.renderer.pass.RenderPass;
import com.jme.scene.Node;
import com.jme.scene.Spatial;
import com.jme.scene.shape.Quad;
import com.jme.scene.state.FogState;
import com.jme.scene.state.LightState;
import com.jme.system.DisplaySystem;
import com.jme.util.resource.ResourceLocatorTool;
import com.jme.util.resource.SimpleResourceLocator;
import com.jmex.effects.water.HeightGenerator;
import com.jmex.effects.water.ProjectedGrid;
import com.jmex.effects.water.WaterRenderPass;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Test render for Sea's Repose stage.
 * 
 * @author Khoa
 */
public class CityOfDreamsRender extends SimpleGame {

	private static final Logger logger = Logger.getLogger(CityOfDreamsRender.class
			.getName());

	protected BasicPassManager pManager;

	private Node world;
	private Node dome;

	private WaterRenderPass waterEffectRenderPass;
	private ProjectedGrid projectedGrid;
	private float farPlane = 10000.0f;

	public static void main(String[] args) {
		CityOfDreamsRender app = new CityOfDreamsRender();
		app.setConfigShowMode(ConfigShowMode.AlwaysShow);
		app.start();
	}

	protected Node loadMeshModel(String str) {
		Node model = new Node();

		OgreLoader loader = new OgreLoader();
		MaterialLoader matLoader = new MaterialLoader();

		// Attempt to load material references and model geometry
		try {
			URL matURL = CityOfDreamsRender.class.getClassLoader().getResource(
					"com/googlecode/reaxion/resources/stages/" + str
							+ ".material");
			URL meshURL = CityOfDreamsRender.class.getClassLoader().getResource(
					"com/googlecode/reaxion/resources/stages/" + str
							+ ".mesh.xml");

			if (matURL != null) {
				matLoader.load(matURL.openStream());
				if (matLoader.getMaterials().size() > 0)
					loader.setMaterials(matLoader.getMaterials());
			}

			model = (Node) loader.loadModel(meshURL);
		} catch (IOException ex) {
			Logger.getLogger(CityOfDreamsRender.class.getName()).log(Level.SEVERE,
					null, ex);
		}

		return model;
	}

	@Override
	protected void simpleInitGame() {
		try {
			SimpleResourceLocator locator = new SimpleResourceLocator(
					CityOfDreamsRender.class.getClassLoader().getResource(
							"com/googlecode/reaxion/resources/"));
			ResourceLocatorTool.addResourceLocator(
					ResourceLocatorTool.TYPE_TEXTURE, locator);
			ResourceLocatorTool.addResourceLocator(
					ResourceLocatorTool.TYPE_MODEL, locator);
		} catch (URISyntaxException e1) {
			logger.log(Level.WARNING, "unable to setup texture directory.", e1);
		}

		Logger.getLogger("com.jme.scene.state.lwjgl").setLevel(Level.SEVERE);

		pManager = new BasicPassManager();

		DisplaySystem.getDisplaySystem().setTitle("Test Mesh Instancing");
		display.getRenderer().setBackgroundColor(ColorRGBA.darkGray);
		((FirstPersonHandler) input).getKeyboardLookHandler().setMoveSpeed(100);
		cam.setFrustumFar(farPlane);

		// setupFog();

		// THIS PART MAKES IT WORK: It doesn't like reflecting the node it's a
		// child of for some reason (reflecting itself, maybe)
		Node reflectedNode = new Node();

		dome = loadMeshModel("dream_port-sky");
		reflectedNode.attachChild(dome);

		world = loadMeshModel("dream_port");
		reflectedNode.attachChild(world);

		rootNode.attachChild(reflectedNode);
		
		// create fog
		setupFog();
		
		// set up water
		waterEffectRenderPass = new WaterRenderPass(cam, 4, false, true);
        waterEffectRenderPass.setWaterPlane(new Plane(new Vector3f(0.0f, 1.0f,
                0.0f), 0.0f));
        waterEffectRenderPass.setWaterMaxAmplitude(1.0f);
        
        projectedGrid = new ProjectedGrid("ProjectedGrid", cam, 40, 40, 0.01f,
        		new HeightGenerator() {
        	public float getHeight( float x, float z, float time ) {
        		return FastMath.sin(x*0.05f+time*-2.0f)+FastMath.cos(z*0.1f+time*-4.0f) - 2;
        	} } );

        waterEffectRenderPass.setWaterEffectOnSpatial(projectedGrid);
        rootNode.attachChild(projectedGrid);
        
        waterEffectRenderPass.setReflectedScene(reflectedNode);
        waterEffectRenderPass.setSkybox(dome);
        pManager.add(waterEffectRenderPass);

		RenderPass rootPass = new RenderPass();
		rootPass.add(rootNode);
		pManager.add(rootPass);
		
		rootNode.setRenderState(createLights());

		rootNode.setCullHint(Spatial.CullHint.Never);
		rootNode.setRenderQueueMode(Renderer.QUEUE_OPAQUE);

	}

	// Update each frame to check for input
	protected void simpleUpdate() {
		Vector3f playerPos = cam.getLocation();

		// make the sky move with the player
		dome.setLocalTranslation(playerPos.mult(.5f));

		pManager.updatePasses(tpf);
	}

	protected void simpleRender() {
		pManager.renderPasses(display.getRenderer());
	}
	
	private void setupFog() {
        FogState fogState = DisplaySystem.getDisplaySystem().getRenderer().createFogState();
        fogState.setDensity(1.0f);
        fogState.setEnabled(true);
        fogState.setColor(new ColorRGBA(0f, 0f, 0f, 1.0f));
        fogState.setEnd(10000.0f);
        fogState.setStart(10000.0f / 10.0f);
        fogState.setDensityFunction(FogState.DensityFunction.Linear);
        fogState.setQuality(FogState.Quality.PerVertex);
        rootNode.setRenderState(fogState);
    }
	
	private LightState createLights() {
		// Set up lighting all around
		LightState lightState = DisplaySystem.getDisplaySystem().getRenderer()
				.createLightState();
		lightState.setEnabled(true);
		lightState.setGlobalAmbient(new ColorRGBA(1f, 1f, 1f, 1f));

		for (float a = 0; a < 2 * FastMath.PI; a += 2 * FastMath.PI / 8) {
			PointLight p = new PointLight();
			p.setDiffuse(new ColorRGBA(.5f, .5f, .5f, .5f));
			p.setAmbient(new ColorRGBA(.25f, .25f, .25f, .25f));
			p.setLocation(new Vector3f(600 * FastMath.cos(a), 100, 600 * FastMath
					.sin(a)));
			p.setEnabled(true);
			lightState.attach(p);
		}

		DirectionalLight dl = new DirectionalLight();
		dl.setDiffuse(new ColorRGBA(1.0f, 1.0f, 1.0f, 1.0f));
		dl.setAmbient(new ColorRGBA(.5f, .5f, .5f, .5f));
		dl.setDirection(new Vector3f(0, -1, 0));
		dl.setEnabled(true);

		lightState.attach(dl);

		return lightState;
	}
}