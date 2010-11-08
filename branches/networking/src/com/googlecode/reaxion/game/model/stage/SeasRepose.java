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
import com.jme.math.Vector3f;
import com.jme.renderer.ColorRGBA;
import com.jme.scene.state.FogState;
import com.jme.scene.state.LightState;
import com.jme.system.DisplaySystem;
import com.jmex.effects.water.HeightGenerator;
import com.jmex.effects.water.ProjectedGrid;
import com.jmex.effects.water.WaterRenderPass;

/**
 * Tropical seas set a lit by a beautiful sunset.
 */
public class SeasRepose extends Stage {

	public static final String name = "Sea's Repose";

	private static final String filename = "stages/seas_repose-islands";
	
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
	
	private static int r = 80;
	
	private Model dome;
	
	private WaterRenderPass waterEffectRenderPass;
    private ProjectedGrid projectedGrid;
	
	public SeasRepose() {
		super(filename);
		bgm = new String[] {"dj_got_us_fallin_in_love.ogg" };
		bgmOdds = new float[] {1};
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
			t.model.setLocalTranslation(turbinePos[i].x, -1, turbinePos[i].z);
			t.model.setLocalScale(1.5f);
			t.rotate(new Vector3f(FastMath.cos(turbinePos[i].y), 0, FastMath.sin(turbinePos[i].y)));
			// animate the turbines
			t.play("spin");
		}
		
		// create fog
		setupFog(b);
		
		// set up water
		waterEffectRenderPass = new WaterRenderPass(b.getCamera(), 4, false, true);
        waterEffectRenderPass.setWaterPlane(new Plane(new Vector3f(0.0f, 1.0f,
                0.0f), 0.0f));
        waterEffectRenderPass.setWaterMaxAmplitude(1.0f);
        
        projectedGrid = new ProjectedGrid("ProjectedGrid", b.getCamera(), 30, 30, 0.01f,
        		new HeightGenerator() {
        	public float getHeight( float x, float z, float time ) {
        		return FastMath.sin(x*0.05f+time*-2.0f)+FastMath.cos(z*0.1f+time*-4.0f)*2 - 1;
        	} } );

        waterEffectRenderPass.setWaterEffectOnSpatial(projectedGrid);
        b.getRootNode().attachChild(projectedGrid);
        
        waterEffectRenderPass.setReflectedScene(b.getContainerNode());
        waterEffectRenderPass.setSkybox(dome.model);
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
	
	private void setupFog(StageGameState b) {
        FogState fogState = DisplaySystem.getDisplaySystem().getRenderer().createFogState();
        fogState.setDensity(1.0f);
        fogState.setEnabled(true);
        fogState.setColor(new ColorRGBA(1.0f, 1.0f, 1.0f, 1.0f));
        fogState.setEnd(10000.0f);
        fogState.setStart(10000.0f / 10.0f);
        fogState.setDensityFunction(FogState.DensityFunction.Linear);
        fogState.setQuality(FogState.Quality.PerVertex);
        b.getRootNode().setRenderState(fogState);
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
