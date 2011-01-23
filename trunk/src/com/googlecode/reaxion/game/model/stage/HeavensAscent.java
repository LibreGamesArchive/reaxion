package com.googlecode.reaxion.game.model.stage;

import java.awt.geom.Point2D;
import java.util.ArrayList;

import com.googlecode.reaxion.game.audio.BackgroundMusic;
import com.googlecode.reaxion.game.model.Model;
import com.googlecode.reaxion.game.model.Model.Billboard;
import com.googlecode.reaxion.game.state.StageGameState;
import com.googlecode.reaxion.game.util.LoadingQueue;
import com.jme.light.DirectionalLight;
import com.jme.light.PointLight;
import com.jme.math.FastMath;
import com.jme.math.Vector3f;
import com.jme.renderer.ColorRGBA;
import com.jme.scene.state.BlendState;
import com.jme.scene.state.LightState;
import com.jme.scene.state.ZBufferState;
import com.jme.system.DisplaySystem;

/**
 * The final terminus on the way to the realm of the Eternal.
 */
public class HeavensAscent extends Stage {

	public static final String NAME = "Heaven's Ascent";

	private static final String filename = "stages/heavens-ascent";

	private static int r = 136;
	private final float angleInc = FastMath.PI / 1440;
	
	private static final Vector3f[] mistPos = {
		new Vector3f(-55, 0, 44.8f),
		new Vector3f(-72.5f, 0, 38.2f),
		new Vector3f(-67.3f, 3.9f, 50.3f),
		new Vector3f(-83, 2.5f, 43.8f),
		new Vector3f(-85.4f, 5.4f, 28),
		new Vector3f(-99.6f, 7.2f, 37.3f),
		new Vector3f(-99.6f, 10.9f, 24),
		new Vector3f(-95.1f, 1.4f, 13.9f),
		new Vector3f(-108.1f, 6.79f, 3.5f),
		new Vector3f(-115.4f, 1.5f, 12.6f),
		new Vector3f(-109.1f, 1.5f, -6.9f),
		new Vector3f(-95.4f, -.3f, -12.6f),
		new Vector3f(-116.4f, 2.8f, 29.4f),
		new Vector3f(-109.8f, .7f, 48.5f),
		new Vector3f(-93.5f, 2.5f, 51.8f),
		new Vector3f(-83.6f, .7f, 59),
		new Vector3f(-62.7f, 2.1f, 63.4f),
		new Vector3f(57, 2.1f, 63.4f),
		new Vector3f(76.2f, 5.6f, 63.4f),
		new Vector3f(76.2f, 1.3f, 47),
		new Vector3f(64.5f, 2.3f, 74),
		new Vector3f(41, .3f, 73.5f),
		new Vector3f(35, .3f, 89.8f),
		new Vector3f(50.9f, 5.9f, 91.9f),
		new Vector3f(79.7f, 2.5f, 80.7f),
		new Vector3f(64.5f, 2.3f, 85.6f),
		new Vector3f(79.7f, 11.2f, 41.4f),
		new Vector3f(70.2f, 5.7f, -82.8f),
		new Vector3f(65.1f, 3.2f, -65.3f),
		new Vector3f(85.2f, .2f, -77),
		new Vector3f(85.2f, .2f, -63.8f),
		new Vector3f(64, .2f, -97.4f),
		new Vector3f(57.6f, 7.7f, -109.8f),
		new Vector3f(43.9f, 3.2f, -102.7f),
		new Vector3f(41.4f, 8.8f, -77.2f),
		new Vector3f(33.8f, 7, -86.9f),
		new Vector3f(25.3f, 2.4f, -99.1f),
		new Vector3f(17.5f, .9f, -109.6f),
		new Vector3f(5.2f, .5f, -102.1f),
		new Vector3f(-38, 15.9f, -83.9f),
		new Vector3f(-40.4f, 20.6f, -95.9f),
		new Vector3f(-55.9f, 12.8f, -95.9f),
		new Vector3f(-66.2f, 12.8f, -85.4f),
		new Vector3f(-27.2f, 12.7f, -96),
		new Vector3f(-49.8f, 15.5f, -104.8f),
		new Vector3f(-43.6f, 19.9f, -78.5f)
	};

	private Model sky;
	private Model gear;
	private Model[] mist;

	public HeavensAscent() {
		super(filename, NAME);
		bgm = new BackgroundMusic[] { BackgroundMusic.DUST_TO_DUST };
		bgmOdds = new float[] { 1 };
	}

	public void loadComponents(StageGameState b) {
		sky = LoadingQueue.quickLoad(new Model("stages/heavens-ascent_sky"), null);
		model.attachChild(sky.model);
		gear = LoadingQueue.quickLoad(new Model("stages/heavens-ascent_gear"), null);
		model.attachChild(gear.model);     
		
		// load mist
		mist = new Model[mistPos.length];
		for (int i=0; i<mist.length; i++) {
			mist[i] = LoadingQueue.quickLoad(new Model("stages/heavens-ascent_mist"), b);
			mist[i].model.setLocalTranslation(mistPos[i].mult(1.5f));
			mist[i].billboarding = Billboard.Free;
		}
	}

	@Override
	public void act(StageGameState b) {
		Vector3f playerPos = b.getPlayer().model.getLocalTranslation();

		// make the sky move with the player
		sky.model.setLocalTranslation(playerPos);
		gear.model.setLocalTranslation(playerPos.add(0, 180, 0));

		// rotate the ring
		gear.pitch = (gear.pitch + (FastMath.PI*2 - angleInc)) % (FastMath.PI*2);
		gear.rotate();
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
			p.setLocation(new Vector3f(600 * FastMath.cos(a), 140, 600 * FastMath
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
