package com.googlecode.reaxion.game.model.stage;

import java.awt.geom.Point2D;
import java.util.ArrayList;

import com.googlecode.reaxion.game.model.Model;
import com.googlecode.reaxion.game.model.prop.Firework;
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
 * A fantastic port city where dreams come true.
 */
public class CityOfDreams extends Stage {

	public static final String name = "City Of Dreams";

	private static final String filename = "stages/dream_port";
	
	private static int r = 72;
	
	private Model dome;
	
	private WaterRenderPass waterEffectRenderPass;
    private ProjectedGrid projectedGrid;
	
	public CityOfDreams() {
		super(filename);
		bgm = new String[] {"gateway_colosseum.ogg" };
		bgmOdds = new float[] {1};
	}

	@Override
	public void loadComponents(StageGameState b) {
		dome = LoadingQueue.quickLoad(new Model("stages/dream_port-sky"), b);
		b.removeModel(dome);
		model.attachChild(dome.model);
		
		// create fog
		setupFog(b);
		
		// create fireworks
		Firework firework = new Firework();
        b.getContainerNode().attachChild(firework);
		
		// set up water
		waterEffectRenderPass = new WaterRenderPass(b.getCamera(), 4, false, true);
        waterEffectRenderPass.setWaterPlane(new Plane(new Vector3f(0.0f, 1.0f,
                0.0f), 0.0f));
        waterEffectRenderPass.setWaterMaxAmplitude(1.0f);
        
        projectedGrid = new ProjectedGrid("ProjectedGrid", b.getCamera(), 40, 40, 0.01f,
        		new HeightGenerator() {
        	public float getHeight( float x, float z, float time ) {
        		return FastMath.sin(x*0.05f+time*-2.0f)+FastMath.cos(z*0.1f+time*-4.0f) - 2;
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
        fogState.setColor(new ColorRGBA(0f, 0f, 0f, 1.0f));
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
