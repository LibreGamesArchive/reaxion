package com.googlecode.reaxion.test;

import com.jme.app.SimpleGame;
import com.jme.image.Texture;
import com.jme.math.Vector3f;
import com.jme.renderer.ColorRGBA;
import com.jme.renderer.Renderer;
import com.jme.scene.Node;
import com.jme.scene.Spatial;
import com.jme.scene.shape.Box;
import com.jme.scene.shape.Pyramid;
import com.jme.scene.shape.Quad;
import com.jme.scene.shape.Sphere;
import com.jme.scene.state.BlendState;
import com.jme.scene.state.MaterialState;
import com.jme.scene.state.TextureState;
import com.jme.scene.state.ZBufferState;
import com.jme.util.TextureManager;

public class HelloTransparentWorld extends SimpleGame {

	/* Starting up a standard SimpleGame... */
	public static void main(String[] args) {
		HelloTransparentWorld app = new HelloTransparentWorld();
		app.setConfigShowMode(ConfigShowMode.ShowIfNoConfig);
		app.start();
	}

	/* I create custom nodes that inherit custom BlendStates to their children. */
	private final Node rootNodeOpaque = new Node("Opaque");
	private final Node rootNodeTranslucent = new Node("Translucent");
	private final Node rootNodeTransparent = new Node("Transparent");

	protected void simpleInitGame() {

		/* Spatials attached to this default rootnode will appear opaque (normal). */
		rootNode.attachChild(rootNodeOpaque);
		rootNodeOpaque.setRenderQueueMode(Renderer.QUEUE_OPAQUE);

		/* Spatials attached to the transparency rootnode will be invisible except for 
		 * an alpha texture pattern that you specify.  */
		rootNode.attachChild(rootNodeTransparent);
		BlendState tpState = display.getRenderer().createBlendState();
		tpState.setEnabled(true);
		tpState.setSourceFunction(BlendState.SourceFunction.SourceAlpha);
		tpState.setDestinationFunction(BlendState.DestinationFunction.OneMinusSourceAlpha);
		tpState.setBlendEnabled(true);
		tpState.setTestEnabled(true);
		tpState.setTestFunction(BlendState.TestFunction.GreaterThan);
		tpState.setReference(0.1f);
		rootNodeTransparent.setRenderState(tpState);
		rootNodeTransparent.setRenderQueueMode(Renderer.QUEUE_TRANSPARENT);
		rootNodeTransparent.setLightCombineMode(Spatial.LightCombineMode.Replace);

		/* Spatials attached to the translucent rootnode will blend with the background
		 * if you give them an alpha color or texture. */
		rootNode.attachChild(rootNodeTranslucent);
		BlendState tlState = display.getRenderer().createBlendState();
		tlState.setEnabled(true);
		tlState.setBlendEnabled(true);
		tlState.setSourceFunction(BlendState.SourceFunction.SourceAlpha);
		tlState.setDestinationFunction(BlendState.DestinationFunction.SourceColor);
		rootNodeTranslucent.setRenderState(tlState);
		rootNodeTranslucent.setRenderQueueMode(Renderer.QUEUE_TRANSPARENT);

		/* We activate two-sided transparency (?) */
		display.getRenderer().getQueue().setTwoPassTransparency(true);
		ZBufferState zstate = display.getRenderer().createZBufferState(); 
		zstate.setWritable(false);
		zstate.setEnabled(false); 

		/* Now we add our especially prepared objects to the rootnode */
		testOpacity();
		testTranslucency();
		testTransparency();
	}

	protected void testOpacity() {
		Box box = new Box("opaque box", new Vector3f(-5, 0, 0), new Vector3f(10, 3, 4));
		setColor(box, ColorRGBA.green); // opaque green
		rootNodeOpaque.attachChild(box);
	}

	protected void testTranslucency() {
		Pyramid cyl = new Pyramid("translucent pyramid", 4, 5);
		cyl.setLocalTranslation(new Vector3f(0, 1, 10)); // move it a bit
		setColor(cyl, new ColorRGBA(1, 0, 1, 0.3f)); // translucent purple
		rootNodeTranslucent.attachChild(cyl);

		Quad square = new Quad("translucent square",4,6);
		square.setLocalTranslation(new Vector3f(0, 1, 5)); // move it a bit
		setTexture(square, "as_ex_colors.png");
		rootNodeTranslucent.attachChild(square);

	}

	protected void testTransparency() {
		/* A faded cube - the alpha texture */
		Box cube = new Box("faded cube", new Vector3f(5, 0, 5), 2, 2, 2);
		setColor(cube, ColorRGBA.cyan); // cyan
		setTexture(cube, "as_ex_edgealpha2.png");
		rootNodeTransparent.attachChild(cube);

		/* A blue orb with a transparent alpha-textured outer layer */
		Sphere orbOutside = new Sphere("Alpha'ed outer orb", new Vector3f(10, 0, 10), 20, 20, 5f);
		Sphere orbInside = new Sphere("Blue inner Orb", new Vector3f(10, 0, 10), 20, 20, 4f);
		setTexture(orbOutside, "as_ex_lightning.png");
		setColor(orbInside, ColorRGBA.blue); // blue
		rootNodeOpaque.attachChild(orbInside);
		rootNodeTransparent.attachChild(orbOutside);
	}

	public void setTexture(Spatial spatial, String path) {
		TextureState ts = display.getRenderer().createTextureState();
		ts.setEnabled(true);
		Texture t1 = TextureManager.loadTexture(HelloTransparentWorld.class.getClassLoader().getResource("com/googlecode/reaxion/test/"+path));
		t1.setApply(Texture.ApplyMode.Replace);
		ts.setTexture(t1, 0);
		spatial.setRenderState(ts);
	}

	public void setColor(Spatial spatial, ColorRGBA color) {
		final MaterialState materialState = display.getRenderer().createMaterialState();
		materialState.setDiffuse(color); // must set diffuseColor to something with alpha
		materialState.setShininess(128);
		materialState.setMaterialFace(MaterialState.MaterialFace.FrontAndBack);
		spatial.setRenderState(materialState);
	}
}