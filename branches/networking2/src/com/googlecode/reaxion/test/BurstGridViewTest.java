package com.googlecode.reaxion.test;

import java.util.ArrayList;

import com.googlecode.reaxion.game.burstgrid.BurstGrid;
import com.googlecode.reaxion.game.burstgrid.node.AbilityNode;
import com.googlecode.reaxion.game.burstgrid.node.AttackNode;
import com.googlecode.reaxion.game.burstgrid.node.BurstNode;
import com.googlecode.reaxion.game.burstgrid.node.HPNode;
import com.googlecode.reaxion.game.burstgrid.node.MaxGaugeNode;
import com.googlecode.reaxion.game.burstgrid.node.MinGaugeNode;
import com.googlecode.reaxion.game.burstgrid.node.RateNode;
import com.googlecode.reaxion.game.burstgrid.node.StrengthNode;
import com.jme.app.SimpleGame;
import com.jme.image.Texture;
import com.jme.image.Texture.MagnificationFilter;
import com.jme.image.Texture.MinificationFilter;
import com.jme.image.Texture.WrapMode;
import com.jme.math.Matrix3f;
import com.jme.math.Vector3f;
import com.jme.renderer.Renderer;
import com.jme.scene.Spatial;
import com.jme.scene.shape.Quad;
import com.jme.scene.state.BlendState;
import com.jme.scene.state.TextureState;
import com.jme.system.DisplaySystem;
import com.jme.util.TextureManager;

public class BurstGridViewTest extends SimpleGame {

	BurstGrid grid;
	float scale = 6;

	Texture ci;
	Texture ca;

	public static void main(String[] args) {
		BurstGridViewTest app = new BurstGridViewTest();
		app.setConfigShowMode(SimpleGame.ConfigShowMode.AlwaysShow);
		app.start();
	}
	protected void simpleInitGame() {
		display.setTitle("BurstGrid Test");

		// to handle texture transparency:
		// create a blend state
		final BlendState bs = display.getRenderer().createBlendState();
		// activate blending
		bs.setBlendEnabled(true);
		// set the source function
		bs.setSourceFunctionAlpha(BlendState.SourceFunction.OneMinusDestinationAlpha);
		// set the destination function
		bs.setDestinationFunctionAlpha(BlendState.DestinationFunction.DestinationAlpha);
		// activate the blend state
		bs.setEnabled(true);
		// assign the blender state to the quad
		rootNode.setRenderState(bs);

		rootNode.setRenderQueueMode(Renderer.QUEUE_TRANSPARENT);
		rootNode.setLightCombineMode(Spatial.LightCombineMode.Off);

		// create textures
		ci = createTexture("connector-inactive");
		ca = createTexture("connector-active");

		// read in burstgrid
		grid = new BurstGrid("src/com/googlecode/reaxion/resources/burstgrid/MonicaGrid.txt");

		// test activate
		grid.getNodes().get(0).activated = true;
		grid.getNodes().get(1).activated = true;

		readNodes();

		System.out.println(grid);
	}

	protected void simpleUpdate() {
		// displace textures
		ca.getTranslation().y += 0.001f;
		if(ca.getTranslation().y > 1)
			ca.getTranslation().y = 0;
	}

	/**
	 * Returns a Texture loaded from {@code str}.
	 */
	private Texture createTexture(String str) { 	
		Texture tex = TextureManager.loadTexture(
				getClass().getResource("../resources/icons/cosmos/"+str+".png"),
				Texture.MinificationFilter.Trilinear, Texture.MagnificationFilter.Bilinear);  
		tex.setWrap(Texture.WrapMode.Repeat);
		tex.setTranslation(new Vector3f());
		return tex;

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

		TextureState ts = display.getRenderer().createTextureState();
		ts.setEnabled(true);
		ts.setTexture(createTexture(str + (active?"":"-i") ));
		q.setRenderState(ts);

		rootNode.attachChild(q);
		q.setLocalTranslation(vec.mult(scale));

		if (active) {
			// create glow
			Quad g = new Quad("", 3, 3);
			TextureState gs = display.getRenderer().createTextureState();
			gs.setTexture(createTexture("shine"));
			g.setRenderState(gs);

			rootNode.attachChild(g);
			g.setLocalTranslation(vec.mult(scale).add(new Vector3f(0, 0, -.01f)));
		}
	}

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
		rootNode.attachChild(q);
	}

}
