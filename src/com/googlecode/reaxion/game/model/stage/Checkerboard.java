package com.googlecode.reaxion.game.model.stage;

import java.awt.geom.Point2D;
import java.util.ArrayList;

import com.googlecode.reaxion.game.audio.BackgroundMusic;
import com.jme.light.PointLight;
import com.jme.math.FastMath;
import com.jme.math.Vector3f;
import com.jme.renderer.ColorRGBA;
import com.jme.scene.state.LightState;
import com.jme.system.DisplaySystem;

/**
 * Test stage.
 */
public class Checkerboard extends Stage {
    
	public static final String NAME = "Checkerboard";
	
	private static final String filename = "checkerPlane";
	
    public Checkerboard() {
    	super(filename, NAME);
    	bgm = new BackgroundMusic[] {BackgroundMusic.OLIVE};
    	bgmOdds = new float[] {1};
    }

    @Override
    public Point2D.Float snapToBounds(Point2D.Float center) {
    	// introduce offset as correct in case center is already out of bounds
    	Point2D.Float offset = new Point2D.Float(0, 0);
    	if (center.x <= -64)
    		offset.x = center.x + 63.9f;
    	else if (center.x >= 64)
    		offset.x = center.x - 63.9f;
    	if (center.y <= -64)
    		offset.y = center.y + 63.9f;
    	else if (center.y >= 64)
    		offset.y = center.y - 63.9f;
    	return offset;
    }
    
    @Override
    public Point2D.Float[] bound(Point2D.Float center, float radius) {
    	ArrayList<Point2D.Float> p = new ArrayList<Point2D.Float>();
    	
    	// left bound
    	if (center.x + 64 <= radius) {
    		p.add(new Point2D.Float(-64f, FastMath.sqrt(radius*radius - FastMath.pow(-64 - center.x, 2)) + center.y ));
    		if (center.x + 64 < radius)
    			p.add(new Point2D.Float(-64f, -FastMath.sqrt(radius*radius - FastMath.pow(-64 - center.x, 2)) + center.y ));
    	}
    	// right bound
    	if (64 - center.x <= radius) {
    		p.add(new Point2D.Float(64f, FastMath.sqrt(radius*radius - FastMath.pow(64 - center.x, 2)) + center.y ));
    		if (64 - center.x < radius)
    			p.add(new Point2D.Float(64f, -FastMath.sqrt(radius*radius - FastMath.pow(64 - center.x, 2)) + center.y ));
    	}
    	// lower bound
    	if (center.y + 64 <= radius) {
    		p.add(new Point2D.Float(FastMath.sqrt(radius*radius - FastMath.pow(-64 - center.y, 2)) + center.x, -64f ));
    		if (center.y + 64 < radius)
    			p.add(new Point2D.Float(-FastMath.sqrt(radius*radius - FastMath.pow(-64 - center.y, 2)) + center.x, -64f ));
    	}
    	// upper bound
    	if (64 - center.y <= radius) {
    		p.add(new Point2D.Float(FastMath.sqrt(radius*radius - FastMath.pow(64 - center.y, 2)) + center.x, 64f ));
    		if (64 - center.y < radius)
    			p.add(new Point2D.Float(-FastMath.sqrt(radius*radius - FastMath.pow(64 - center.y, 2)) + center.x, 64f ));
    	}
    	
    	return (p.toArray(new Point2D.Float[0]));
    }
    
    @Override
    public LightState createLights() {
    	// Set up a basic, default light.
    	LightState lightState;
    	
        PointLight light = new PointLight();
        light.setDiffuse( new ColorRGBA( 0.75f, 0.75f, 0.75f, 0.75f ) );
        light.setAmbient( new ColorRGBA( 0.5f, 0.5f, 0.5f, 1.0f ) );
        light.setLocation( new Vector3f( 100, 100, 100 ) );
        light.setEnabled( true );
        
        lightState = DisplaySystem.getDisplaySystem().getRenderer().createLightState();
        lightState.setEnabled( true );
        lightState.attach( light );
        
        return lightState;
    }
}
