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
public class SeasReposeRender extends SimpleGame {

	private static final Logger logger = Logger.getLogger(SeasReposeRender.class
			.getName());

	protected BasicPassManager pManager;
	
	private static Vector3f[] turbinePos = { new Vector3f(-16, -87, -136),
		new Vector3f(-44, -124, -96),
		new Vector3f(-42, -118, -54),
		new Vector3f(-118, -124, -107),
		new Vector3f(-88, -146, -59),
		new Vector3f(-96, -179, -5),
		new Vector3f(-162, -167, -15),
		
		new Vector3f(18, -94, -89),
		new Vector3f(67, -73, -129),
		new Vector3f(56, -114, -68),
		new Vector3f(94, -136, -41),
		new Vector3f(125, -32, -70),
		new Vector3f(146, -3, -5),
		
		new Vector3f(-145, -194, 27),
		new Vector3f(-86, -216, 90),
		new Vector3f(-47, -219, 49),
		new Vector3f(-53, -223, -107),
		new Vector3f(-118, -204, -110),
		new Vector3f(-2, -263, 156),
		
		new Vector3f(99, -214, 28),
		new Vector3f(44, -252, 44),
		new Vector3f(136, -335, 76),
		new Vector3f(25, -243, 98),
		new Vector3f(63, -298, 129),
		};

	private Node world;
	private Node dome;

	private WaterRenderPass waterEffectRenderPass;
	private ProjectedGrid projectedGrid;
	private float farPlane = 10000.0f;

	public static void main(String[] args) {
		SeasReposeRender app = new SeasReposeRender();
		app.setConfigShowMode(ConfigShowMode.AlwaysShow);
		app.start();
	}

	protected Node loadMeshModel(String str) {
		Node model = new Node();

		OgreLoader loader = new OgreLoader();
		MaterialLoader matLoader = new MaterialLoader();

		// Attempt to load material references and model geometry
		try {
			URL matURL = SeasReposeRender.class.getClassLoader().getResource(
					"com/googlecode/reaxion/resources/stages/" + str
							+ ".material");
			URL meshURL = SeasReposeRender.class.getClassLoader().getResource(
					"com/googlecode/reaxion/resources/stages/" + str
							+ ".mesh.xml");

			if (matURL != null) {
				matLoader.load(matURL.openStream());
				if (matLoader.getMaterials().size() > 0)
					loader.setMaterials(matLoader.getMaterials());
			}

			model = loader.loadModel(meshURL);
		} catch (IOException ex) {
			Logger.getLogger(SeasReposeRender.class.getName()).log(Level.SEVERE,
					null, ex);
		}

		return model;
	}

	@Override
	protected void simpleInitGame() {
		try {
			SimpleResourceLocator locator = new SimpleResourceLocator(
					SeasReposeRender.class.getClassLoader().getResource(
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

		dome = loadMeshModel("seas_repose-sky");
		reflectedNode.attachChild(dome);

		world = loadMeshModel("seas_repose-islands");
		reflectedNode.attachChild(world);
		
		// load turbines
		for (int i=0; i<turbinePos.length; i++) {
			Node t = loadMeshModel("seas_repose-turbine");
			reflectedNode.attachChild(t);
			t.setLocalTranslation(turbinePos[i].x, -1, turbinePos[i].z);
			t.setLocalScale(1.5f);
			
			Vector3f point = new Vector3f(FastMath.cos(turbinePos[i].y), 0, FastMath.sin(turbinePos[i].y));
	    	float pointRoll = (float) Math.atan2(point.x, point.z);
	    	float pointYaw = (float) Math.atan2(point.y, FastMath.sqrt(FastMath.pow(point.x, 2) + FastMath.pow(point.z, 2)));
	    	Quaternion q = new Quaternion();
	    	t.setLocalRotation(q.fromAngles(pointYaw, pointRoll, 0));
		}

		rootNode.attachChild(reflectedNode);
		
		// create fog
		setupFog();
		
		// set up water
		waterEffectRenderPass = new WaterRenderPass(cam, 4, false, true);
        waterEffectRenderPass.setWaterPlane(new Plane(new Vector3f(0.0f, 1.0f,
                0.0f), 0.0f));
        waterEffectRenderPass.setWaterMaxAmplitude(1.0f);
        
        projectedGrid = new ProjectedGrid("ProjectedGrid", cam, 100, 100, 0.01f,
        		new HeightGenerator() {
        	public float getHeight( float x, float z, float time ) {
        		return FastMath.sin(x*0.05f+time*-2.0f)+FastMath.cos(z*0.1f+time*-4.0f)*2 - 1;
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

	private Vector3f tmpVec = new Vector3f();

	// Update each frame to check for input
	@Override
	protected void simpleUpdate() {
		Vector3f playerPos = cam.getLocation();

		// make the sky move with the player
		dome.setLocalTranslation(playerPos.mult(.5f));

		pManager.updatePasses(tpf);
	}

	@Override
	protected void simpleRender() {
		pManager.renderPasses(display.getRenderer());
	}
	
	private void setupFog() {
		FogState fogState = display.getRenderer().createFogState();
		fogState.setDensity(1.0f);
		fogState.setEnabled(true);
		fogState.setColor(new ColorRGBA(1.0f, 1.0f, 1.0f, 1.0f));
		fogState.setEnd(farPlane);
		fogState.setStart(farPlane / 10.0f);
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

		for (float a = FastMath.PI; a < 2 * FastMath.PI; a += 2 * FastMath.PI / 8) {
			PointLight p = new PointLight();
			p.setDiffuse(new ColorRGBA(.5f, .5f, .5f, .5f));
			p.setAmbient(new ColorRGBA(.25f, .25f, .25f, .25f));
			p.setLocation(new Vector3f(600 * FastMath.cos(a), 0, 600 * FastMath
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