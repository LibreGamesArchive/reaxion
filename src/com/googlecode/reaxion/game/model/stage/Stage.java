package com.googlecode.reaxion.game.model.stage;

import java.awt.geom.Point2D;
import com.googlecode.reaxion.game.model.Model;
import com.googlecode.reaxion.game.model.character.Character;
import com.googlecode.reaxion.game.state.StageGameState;
import com.jme.math.FastMath;
import com.jme.math.Vector3f;
import com.jme.scene.state.LightState;

/**
 * Stages used during the {@code BattleGameState} should extend this class.
 * @author Khoa
 */
public class Stage extends Model {
	
	protected String[] bgm;
	protected float[] bgmOdds;
	
	protected Vector3f terminalPos = new Vector3f(0, 4, 0);
    
    public Stage() {
    	init();
    }
    
    public Stage(String fn) {
    	filename = fn;
    	init();
    }
    
    /**
     * Called when stage is set by the {@code BattleGameState}
     */
    public void loadComponents(StageGameState b) {
    }

    /**
     * Repositions character to ensure that it remains within the bounds of
     * the stage.
     */
    public void contain(Character c) {
    	Vector3f v = c.model.getWorldTranslation();
    	Point2D.Float p = new Point2D.Float(v.x + c.getVelocity().x, v.z + c.getVelocity().z);
    	
    	// snap to boundaries if outside
    	Point2D.Float offset = snapToBounds(p);
    	c.setVelocity(c.getVelocity().subtract(new Vector3f(offset.x, 0, offset.y)));
    	
    	// recalculate p
    	p = new Point2D.Float(v.x + c.getVelocity().x, v.z + c.getVelocity().z);
    	
    	Point2D.Float[] hit = bound(c);
    	
    	// if hits occur at two or more points along the circumference
    	if (hit.length >= 2) {
    		
    		Point2D.Float midpoint = new Point2D.Float();
    		
    		// since hits penetrate in sets of two, the true midpoint is the midpoint of the midpoints
    		for (int i=0; i<hit.length - hit.length%2; i+=2) {
    			midpoint.x += hit[i+1].x+hit[i].x;
    			midpoint.y += hit[i+1].y+hit[i].y;
    		}
    		midpoint.x /= hit.length - hit.length%2;
    		midpoint.y /= hit.length - hit.length%2; 
    		
    		
    		//Point2D.Float midpoint = new Point2D.Float((hit[1].x+hit[0].x)/2, (hit[1].y+hit[0].y)/2);
    		
    		// find magnitude of displacement
    		float magnitude = Math.max(c.boundRadius - FastMath.sqrt(FastMath.pow(midpoint.x-p.x, 2) + FastMath.pow(midpoint.y-p.y, 2)), 0);
    		
    		// find angle to midpoint from center
    		float angle = FastMath.atan2(midpoint.y - p.y, midpoint.x - p.x);
    		
    		// find vector of penetration
    		Vector3f vector = new Vector3f(magnitude*FastMath.cos(angle), 0, magnitude*FastMath.sin(angle));
    		
    		//System.out.println(Arrays.toString(hit) +" -> "+ midpoint +": "+ v +" + "+ vector);
    		//System.out.println(magnitude +" "+ angle);
    		
    		// fix the velocity vector so that the collision doesn't occur
			c.setVelocity(c.getVelocity().subtract(vector));
			
			if (offset.x != 0 && offset.y != 0)
				System.out.println("Out of bounds correction -> "+v.add(c.getVelocity()));
    	}
    }
    
    /**
     * Return the center's offset out of bounds and return it to bound boundaries.
     * Override to add functionality.
     */
    public Point2D.Float snapToBounds(Point2D.Float center) {
    	return (new Point2D.Float());
    }
    
    /**
     * Calls {@code bound()} using a {@code Character}'s next position and
     * radius
     */
    public Point2D.Float[] bound(Character c) {
    	Vector3f t = c.model.getWorldTranslation();
    	return bound(new Point2D.Float(t.x + c.getVelocity().x, t.z + c.getVelocity().z), c.boundRadius);
    }
    
    /**
     * Returns up to two points of collision between the stage bounds and a
     * circle with center {@code center} and radius {@code radius}. Override
     * to add actual bounding function.
     */
    public Point2D.Float[] bound(Point2D.Float center, float radius) {
    	return (new Point2D.Float[0]);
    }
    
    /**
     * Creates lighting for the stage and returns the new {@code LightState}.
     * Override to add functionality.
     */
    public LightState createLights() {
    	return null;
    }
    
    /**
     * Gets the position at which to put the terminal in {@code HubGameState}.
     * @return Position vector of the terminal
     */
    public Vector3f getTerminalPosition() {
    	return terminalPos;
    }
    
    /**
     * Gets the bgm associated with index {@code i}. If {@code i} is negative,
     * a bgm will be chosen at random according to the odds distribution.
     * @param i index of bgm or -1 for random
     * @return String for bgm
     */
    public String getBgm(int i) {
    	String str = null;
    	if (i < 0) {
    		float total = 0;
    		for (float f : bgmOdds)
    			total += f;
    		float rand = FastMath.nextRandomFloat()*total;
    		float sum = 0;
    		for (int n=0; n<bgmOdds.length; n++) {
    			sum += bgmOdds[n];
    			if (rand <= sum)
    				return bgm[n];
    		}
    	} else {
    		return bgm[i];
    	}
    	return str;
    }
}
