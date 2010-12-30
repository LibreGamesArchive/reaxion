package com.googlecode.reaxion.game.model.stage;

import java.awt.geom.Point2D;
import java.util.ArrayList;

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
 * An unassailable castle, frozen in the depths of time.
 */
public class CrystalPalace extends Stage {

	public static final String NAME = "Crystal Palace";

	private static final String filename = "stages/crystal-palace";

	private static int s = 80;
	private final float pitchInc = FastMath.PI / 720;

	private Model sky;
	private Model warp;

	public CrystalPalace() {
		super(filename, NAME);
		bgm = new String[] { "forgotten_challenge.ogg", "via_purifico.ogg" };
		bgmOdds = new float[] { 1, 2 };
	}

	public void loadComponents(StageGameState b) {
		sky = LoadingQueue.quickLoad(new Model("stages/crystal-palace_sky"), null);
		model.attachChild(sky.model);
		warp = LoadingQueue.quickLoad(new Model("stages/crystal-palace_warp"), null);
		model.attachChild(warp.model);
		
		warp.model.setLocalTranslation(new Vector3f(0, 50, 0));
	}

	@Override
	public void act(StageGameState b) {
		Vector3f playerPos = b.getPlayer().model.getLocalTranslation();

		// make the sky move with the player
		sky.model.setLocalTranslation(playerPos);

		// rotate the warp
		warp.pitch += pitchInc;
		warp.rotate();
	}

	@Override
    public Point2D.Float snapToBounds(Point2D.Float center) {
    	// introduce offset as correct in case center is already out of bounds
    	Point2D.Float offset = new Point2D.Float(0, 0);
    	if (center.x <= -s)
    		offset.x = center.x + s - .1f;
    	else if (center.x >= s)
    		offset.x = center.x - s + .1f;
    	if (center.y <= -s)
    		offset.y = center.y + s - .1f;
    	else if (center.y >= s)
    		offset.y = center.y - s + .1f;
    	
    	return offset;
    }
    
    @Override
    public Point2D.Float[] bound(Point2D.Float center, float radius) {
    	ArrayList<Point2D.Float> p = new ArrayList<Point2D.Float>();
    	
    	// left bound
    	if (center.x + s <= radius) {
    		p.add(new Point2D.Float(-s, FastMath.sqrt(radius*radius - FastMath.pow(-s - center.x, 2)) + center.y ));
    		if (center.x + s < radius)
    			p.add(new Point2D.Float(-s, -FastMath.sqrt(radius*radius - FastMath.pow(-s - center.x, 2)) + center.y ));
    	}
    	// right bound
    	if (s - center.x <= radius) {
    		p.add(new Point2D.Float(s, FastMath.sqrt(radius*radius - FastMath.pow(s - center.x, 2)) + center.y ));
    		if (s - center.x < radius)
    			p.add(new Point2D.Float(s, -FastMath.sqrt(radius*radius - FastMath.pow(s - center.x, 2)) + center.y ));
    	}
    	// lower bound
    	if (center.y + s <= radius) {
    		p.add(new Point2D.Float(FastMath.sqrt(radius*radius - FastMath.pow(-s - center.y, 2)) + center.x, -s ));
    		if (center.y + s < radius)
    			p.add(new Point2D.Float(-FastMath.sqrt(radius*radius - FastMath.pow(-s - center.y, 2)) + center.x, -s ));
    	}
    	// upper bound
    	if (s - center.y <= radius) {
    		p.add(new Point2D.Float(FastMath.sqrt(radius*radius - FastMath.pow(s - center.y, 2)) + center.x, s ));
    		if (s - center.y < radius)
    			p.add(new Point2D.Float(-FastMath.sqrt(radius*radius - FastMath.pow(s - center.y, 2)) + center.x, s ));
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
