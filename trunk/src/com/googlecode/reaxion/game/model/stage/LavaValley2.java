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
 * Lava Valley with modified bounds.
 */
public class LavaValley2 extends LavaValley {
	
	public static final String NAME = "Lava Valley2";

	private static int[] r = {136, 140};
	
	public LavaValley2() {
		super();
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

		if (dist >= r[1]) {
			offset.x = (dist - r[1] + .01f) * FastMath.cos(angle);
			offset.y = (dist - r[1] + .01f) * FastMath.sin(angle);
		} else if (dist <= r[0]) {
			offset.x = (dist - r[0] - .01f) * FastMath.cos(angle);
			offset.y = (dist - r[0] - .01f) * FastMath.sin(angle);
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
		if (dist + radius > r[1]) {

			// find the angle from origin
			float angle = FastMath.atan2(center.y, center.x);

			// determine collisions from that distance
			if (dist - radius == r[1] || dist + radius == r[1]) {
				// add the one point of collision
				p.add(new Point2D.Float(r[1] * FastMath.cos(angle), r[1]
						* FastMath.sin(angle)));
			} else {
				// add the two points of collision
				float ptY = FastMath.sqrt(FastMath.pow(radius, 2)
						- FastMath.pow(r[1] - dist, 2));
				float minor = FastMath.atan2(ptY, r[1] - dist);
				p.add(new Point2D.Float(r[1] * FastMath.cos(angle) + radius
						* FastMath.cos(angle + minor), r[1] * FastMath.sin(angle)
						+ radius * FastMath.sin(angle + minor)));
				p.add(new Point2D.Float(r[1] * FastMath.cos(angle) + radius
						* FastMath.cos(angle - minor), r[1] * FastMath.sin(angle)
						+ radius * FastMath.sin(angle - minor)));
			}

		}
		
		// check if inside the circle
		else if (dist - radius < r[0]) {

			// find the angle from origin
			float angle = FastMath.atan2(center.y, center.x);

			// determine collisions from that distance
			if (dist - radius == r[0] || dist + radius == r[0]) {
				// add the one point of collision
				p.add(new Point2D.Float(r[0] * FastMath.cos(angle), r[0]
						* FastMath.sin(angle)));
			} else {
				// add the two points of collision
				float ptY = FastMath.sqrt(FastMath.pow(radius, 2)
						- FastMath.pow(dist - r[0], 2));
				float minor = FastMath.atan2(ptY, dist - r[0]);
				p.add(new Point2D.Float(r[0] * FastMath.cos(angle) - radius
						* FastMath.cos(angle + minor), r[0] * FastMath.sin(angle)
						- radius * FastMath.sin(angle + minor)));
				p.add(new Point2D.Float(r[0] * FastMath.cos(angle) - radius
						* FastMath.cos(angle - minor), r[0] * FastMath.sin(angle)
						- radius * FastMath.sin(angle - minor)));
			}

		}

		return (p.toArray(new Point2D.Float[0]));
	}
	
}
