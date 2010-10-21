package com.googlecode.reaxion.game.model.stage;

import java.awt.geom.Point2D;
import java.util.ArrayList;

import com.googlecode.reaxion.game.model.Model;
import com.googlecode.reaxion.game.state.StageGameState;
import com.googlecode.reaxion.game.util.LoadingQueue;
import com.jme.light.DirectionalLight;
import com.jme.light.PointLight;
import com.jme.math.FastMath;
import com.jme.math.Plane;
import com.jme.math.Quaternion;
import com.jme.math.Vector3f;
import com.jme.renderer.ColorRGBA;
import com.jme.scene.shape.Quad;
import com.jme.scene.state.LightState;
import com.jme.system.DisplaySystem;
import com.jmex.effects.water.WaterRenderPass;

/**
 * Open seas set a lit by a beautiful sunset.
 */
public class SeasRepose extends Stage {

	public static final String name = "Sea's Repose";

	private static final String filename = "stages/seas_repose-turbine";
	
	private static Vector3f[] turbinePos = { new Vector3f(-21, -118, -36),
		new Vector3f(-34, -124, -72),
		new Vector3f(-65, -146, -42),
		new Vector3f(-64, -178, -5),
		new Vector3f(-21, -118, -37),
		new Vector3f(15, -94, -69),
		new Vector3f(41, -114, -43),
		new Vector3f(70, -122, -42),
		new Vector3f(-71, -216, 30),
		new Vector3f(-44, -223, 68),
		new Vector3f(-32, -219, 30),
		new Vector3f(14, -244, 65),
		new Vector3f(29, -252, 27),
		new Vector3f(65, -214, 29),
		};
	
	private static int r = 80;
	
	private Model dome;
	
	private WaterRenderPass waterEffectRenderPass;
    private Quad waterQuad;
	
	public SeasRepose() {
		super(filename);
		bgm = new String[] {"japanize_dream.ogg", "551 Depression (Legend of Hourai).ogg" };
		bgmOdds = new float[] {1, 1};
	}

	public void loadComponents(StageGameState b) {
		dome = LoadingQueue.quickLoad(new Model("stages/seas_repose-sky"), b);
		b.removeModel(dome);
		model.attachChild(dome.model);
		
		// load turbines
		for (int i=0; i<turbinePos.length; i++) {
			Model t = LoadingQueue.quickLoad(new Model("stages/seas_repose-turbine"), b);
			b.removeModel(t);
			model.attachChild(t.model);
			t.model.setLocalTranslation(turbinePos[i].x, 0, turbinePos[i].z);
			t.rotate(new Vector3f(FastMath.cos(turbinePos[i].y), 0, FastMath.sin(turbinePos[i].y)));
			// animate the turbines
			t.play("spin");
		}
		
		// set up water
		waterEffectRenderPass = new WaterRenderPass(b.getCamera(), 4, false, true);
        waterEffectRenderPass.setWaterPlane(new Plane(new Vector3f(0.0f, 1.0f,
                0.0f), 0.0f));

        waterQuad = new Quad("waterQuad", 580, 580);
        waterQuad.setLocalRotation(new Quaternion().fromAngles(-FastMath.PI/2, 0, 0));

        waterEffectRenderPass.setWaterEffectOnSpatial(waterQuad);
        b.getRootNode().attachChild(waterQuad);
        
        waterEffectRenderPass.setReflectedScene(b.getContainerNode());
        b.getPassManager().add(waterEffectRenderPass);
	}

	@Override
	public void act(StageGameState b) {
		Vector3f playerPos = b.getPlayer().model.getLocalTranslation();

		// make the sky move with the player
		dome.model.setLocalTranslation(playerPos);
	}

	@Override
	public Point2D.Float snapToBounds(Point2D.Float center) {
		// introduce offset as correct in case center is already out of bounds
		Point2D.Float offset = new Point2D.Float(0, 0);

		// find the distance from origin
		float dist = FastMath.sqrt(FastMath.pow(center.x, 2)
				+ FastMath.pow(center.y, 2));
		// find the angle from origin
		float angle = FastMath.atan2(center.y, center.x);

		if (dist >= r) {
			offset.x = (dist - r + .01f) * FastMath.cos(angle);
			offset.y = (dist - r + .01f) * FastMath.sin(angle);
		}

		return offset;
	}

	@Override
	public Point2D.Float[] bound(Point2D.Float center, float radius) {
		ArrayList<Point2D.Float> p = new ArrayList<Point2D.Float>();

		// find the distance from origin
		float dist = FastMath.sqrt(FastMath.pow(center.x, 2)
				+ FastMath.pow(center.y, 2));

		// check if outside the circle
		if (dist + radius > r) {

			// find the angle from origin
			float angle = FastMath.atan2(center.y, center.x);

			// determine collisions from that distance
			if (dist - radius == r || dist + radius == r) {
				// add the one point of collision
				p.add(new Point2D.Float(r * FastMath.cos(angle), r
						* FastMath.sin(angle)));
			} else {
				// add the two points of collision
				float ptY = FastMath.sqrt(FastMath.pow(radius, 2)
						- FastMath.pow(r - dist, 2));
				float minor = FastMath.atan2(ptY, r - dist);
				p.add(new Point2D.Float(r * FastMath.cos(angle) + radius
						* FastMath.cos(angle + minor), r * FastMath.sin(angle)
						+ radius * FastMath.sin(angle + minor)));
				p.add(new Point2D.Float(r * FastMath.cos(angle) + radius
						* FastMath.cos(angle - minor), r * FastMath.sin(angle)
						+ radius * FastMath.sin(angle - minor)));
			}

		}

		return (p.toArray(new Point2D.Float[0]));
	}

	@Override
	public LightState createLights() {
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
