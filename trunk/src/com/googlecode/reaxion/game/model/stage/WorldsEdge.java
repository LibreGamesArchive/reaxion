package com.googlecode.reaxion.game.model.stage;

import java.awt.geom.Point2D;
import java.util.ArrayList;

import com.googlecode.reaxion.game.audio.BackgroundMusic;
import com.googlecode.reaxion.game.model.Model;
import com.googlecode.reaxion.game.state.StageGameState;
import com.googlecode.reaxion.game.util.LoadingQueue;
import com.jme.light.DirectionalLight;
import com.jme.light.PointLight;
import com.jme.math.FastMath;
import com.jme.math.Vector3f;
import com.jme.renderer.ColorRGBA;
import com.jme.scene.state.LightState;
import com.jme.system.DisplaySystem;

/**
 * Spacious wasteland at the brink of existence.
 */
public class WorldsEdge extends Stage {

	public static final String NAME = "World's Edge";

	private static final String filename = "stages/worlds_edge-ground";

	private static int r = 150;
	private final float yawInc = FastMath.PI / 360;

	private Model sky;
	private Model clouds;

	private Model[] shadow = new Model[2];
	private float shadowPos = 0;

	public WorldsEdge() {
		super(filename, NAME);
		bgm = new BackgroundMusic[] { BackgroundMusic.NO_THANK_YOU, BackgroundMusic.THIRTEENTH_ANTHOLOGY };
		bgmOdds = new float[] {1, 1};
	}

	public void loadComponents(StageGameState b) {
		sky = LoadingQueue.quickLoad(new Model("stages/worlds_edge-sky"), b);
		b.removeModel(sky);
		model.attachChild(sky.model);
		clouds = LoadingQueue.quickLoad(new Model("stages/worlds_edge-clouds"),
				b);
		b.removeModel(clouds);
		model.attachChild(clouds.model);
		shadow[0] = LoadingQueue.quickLoad(new Model(
				"stages/worlds_edge-shadow"), b);
		b.removeModel(shadow[0]);
		model.attachChild(shadow[0].model);
		shadow[1] = LoadingQueue.quickLoad(new Model(
				"stages/worlds_edge-shadow"), b);
		shadow[1].model.setLocalTranslation(new Vector3f(0, 0, -800));
		b.removeModel(shadow[1]);
		model.attachChild(shadow[1].model);
	}

	@Override
	public void act(StageGameState b) {
		Vector3f playerPos = b.getPlayer().model.getLocalTranslation();

		// make the sky and clouds move with the player
		sky.model.setLocalTranslation(playerPos);
		clouds.model.setLocalTranslation(playerPos);

		// rotate the clouds
		clouds.yaw += yawInc;
		clouds.rotate();
		clouds.model.setLocalTranslation(new Vector3f(0, -1100, 0));

		// move the shadows
		shadowPos += 64 * b.tpf;
		shadowPos %= 800;
		shadow[0].model.setLocalTranslation(new Vector3f(0, 0, shadowPos));
		shadow[1].model
				.setLocalTranslation(new Vector3f(0, 0, shadowPos - 800));
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

		for (float a = 0; a < 2 * FastMath.PI; a += 2 * FastMath.PI / 8) {
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
