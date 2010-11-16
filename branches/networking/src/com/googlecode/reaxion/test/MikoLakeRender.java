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
import com.jme.system.DisplaySystem;
import com.jme.util.resource.ResourceLocatorTool;
import com.jme.util.resource.SimpleResourceLocator;
import com.jmex.effects.water.WaterRenderPass;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Should actually work for people besides Khoa now, tell someone if it doesn't.
 * 
 * @author Khoa, Arsy, etc.
 */
public class MikoLakeRender extends SimpleGame {

	private static final Logger logger = Logger.getLogger(MikoLakeRender.class
			.getName());

	protected BasicPassManager pManager;

	private Node world;
	private Node dome;

	private WaterRenderPass waterEffectRenderPass;
	private Quad waterQuad;
	private float farPlane = 10000.0f;

	public static void main(String[] args) {
		MikoLakeRender app = new MikoLakeRender();
		app.setConfigShowMode(ConfigShowMode.AlwaysShow);
		app.start();
	}

	protected Node loadMeshModel(String str) {
		Node model = new Node();

		OgreLoader loader = new OgreLoader();
		MaterialLoader matLoader = new MaterialLoader();

		// Attempt to load material references and model geometry
		try {
			URL matURL = MikoLakeRender.class.getClassLoader().getResource(
					"com/googlecode/reaxion/resources/stages/" + str
							+ ".material");
			URL meshURL = MikoLakeRender.class.getClassLoader().getResource(
					"com/googlecode/reaxion/resources/stages/" + str
							+ ".mesh.xml");

			if (matURL != null) {
				matLoader.load(matURL.openStream());
				if (matLoader.getMaterials().size() > 0)
					loader.setMaterials(matLoader.getMaterials());
			}

			model = loader.loadModel(meshURL);
		} catch (IOException ex) {
			Logger.getLogger(MikoLakeRender.class.getName()).log(Level.SEVERE,
					null, ex);
		}

		return model;
	}

	@Override
	protected void simpleInitGame() {
		try {
			SimpleResourceLocator locator = new SimpleResourceLocator(
					MikoLakeRender.class.getClassLoader().getResource(
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

		dome = loadMeshModel("miko_lake-4_dome");
		reflectedNode.attachChild(dome);

		world = loadMeshModel("miko_lake-4_world");
		reflectedNode.attachChild(world);

		rootNode.attachChild(reflectedNode);

		// set up water
		waterEffectRenderPass = new WaterRenderPass(cam, 4, false, true);
		waterEffectRenderPass.setWaterPlane(new Plane(new Vector3f(0.0f, 1.0f,
				0.0f), 0.0f));

		waterQuad = new Quad("waterQuad", 580, 580);
		waterQuad.setLocalRotation(new Quaternion().fromAngles(
				-FastMath.PI / 2, 0, 0));

		waterEffectRenderPass.setWaterEffectOnSpatial(waterQuad);
		rootNode.attachChild(waterQuad);

		waterEffectRenderPass.setReflectedScene(reflectedNode);
		// waterEffectRenderPass.setSkybox(dome);
		pManager.add(waterEffectRenderPass);

		RenderPass rootPass = new RenderPass();
		rootPass.add(rootNode);
		pManager.add(rootPass);

		rootNode.setCullHint(Spatial.CullHint.Never);
		rootNode.setRenderQueueMode(Renderer.QUEUE_OPAQUE);

	}

	private Vector3f tmpVec = new Vector3f();

	// Update each frame to check for input
	@Override
	protected void simpleUpdate() {
		Vector3f playerPos = cam.getLocation();

		// make the sky move with the player
		dome.setLocalTranslation(playerPos);

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
}