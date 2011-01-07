package com.googlecode.reaxion.game.model.enemies;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

import com.googlecode.reaxion.game.model.Model;
import com.googlecode.reaxion.game.model.attackobject.CyberBeam;
import com.googlecode.reaxion.game.model.character.Character;
import com.googlecode.reaxion.game.state.StageGameState;
import com.googlecode.reaxion.game.util.LoadingQueue;
import com.jme.math.FastMath;
import com.jme.math.Vector3f;

/**
 * Physical attack prop for {@code FloralFallal}.
 */
public class SatelliteFlower extends Character {

	public int lifespan = 720;
	protected int lifeCount = 0;
	
	private final int riseTime = 40;
	private final float riseHeight = 10;
	
	private final double[] reloadTime = {1/10, 2};
	private final float dTheta = FastMath.PI/180;
	
	private float radius;
	private float maxDist;
	
	public float angle;
	private double lastReload = 0;
	
	private CyberBeam beam;
	
	public SatelliteFlower(Model[] m) {
    	// Load model
    	super("satellite-flower");
    	users = new ArrayList<Model>();
    	users.addAll(Arrays.asList(m));
    	trackable = false;
    	mass = 2;
		boundRadius = .1f;
		boundHeight = .2f;
    }

	@ Override
	public void act(StageGameState b) {
		// define radius
		if (lifeCount == 0)
			b.getStage().contain(this);
		else if (lifeCount == 1) {
			radius = model.getWorldTranslation().length();
			maxDist = radius * 2/3f;
		}
		
		// find target
		Model target = nearestTarget(b);
		Vector3f pos = model.getWorldTranslation();
		Vector3f tPos = target.model.getWorldTranslation();
        
        // rise or sink
        if (lifeCount < riseTime)
        	velocity.y = riseHeight/(float)riseTime;
        else if (lifespan - lifeCount < riseTime)
        	velocity.y = -riseHeight/(float)riseTime;
        else {
        	if (target != null && tPos.distance(pos) <= maxDist) {
        		// check distance
        		double load = (tPos.distance(pos)/maxDist)*(reloadTime[1]-reloadTime[0]) + reloadTime[0];
        		if (b.getTotalTime() - load >= lastReload && (beam == null || beam.model.getParent() == null)) {
        			// fire!
        			users.add(this);
        			beam = (CyberBeam)LoadingQueue.quickLoad(new CyberBeam(users.toArray(new Model[] {}), this, tPos, (int)load*60/2), b);
    				b.getRootNode().updateRenderState();
        			lastReload = b.getTotalTime();
        		}
        	}
            
        	velocity = new Vector3f(FastMath.sin(angle + dTheta), 6f/radius, FastMath.cos(angle + dTheta)).mult(radius).subtract(pos);
        	
        	angle = (angle + dTheta) % (2*FastMath.PI);       	
        }
        
        // rotate
        if (target != null)
        	rotate(tPos.subtract(pos));
        
		// actually move
		Vector3f loc = model.getLocalTranslation();
		loc.addLocal(velocity);
		model.setLocalTranslation(loc);
		
		//check lifespan
        if (lifeCount == lifespan)
        	b.removeModel(this);
        lifeCount++;
	}
	
	/**
     * Returns the nearest target model
     */
    private Model nearestTarget(StageGameState b) {
    	
    	ArrayList<Object[]> o = new ArrayList<Object[]>();
    	
    	// Add models and distances to 2D ArrayList
    	for (int i=0; i<b.getModels().size(); i++) {
    		Model m = b.getModels().get(i);
    		if (m != this && m != users.get(users.size()-1) && m.trackable) {
    			Object[] a = new Object[2];
    			a[0] = new Float(model.getWorldTranslation().distance(m.model.getWorldTranslation()));
    			a[1] = m;
    			o.add(a);
    		}
    	}
    	
    	// do nothing if there's nothing to sort
    	if (o.size() == 0)
    		return null;
    	
    	// Make it an array
    	Object[] t = o.toArray();
    	
    	// Sort 2D array by distances
    	Arrays.sort(t, new Comparator<Object>() {
        	public int compare(Object one, Object two){
        		Object[] first = (Object[]) one;
        		Object[] secnd = (Object[]) two;
        		//System.out.println((Float)(first[0])+" - "+(Float)(secnd[0]));
        		return (int)((Float)(first[0]) - (Float)(secnd[0]));
        	}
        });

    	// Send back closest model
    	return (Model)(((Object[])t[0])[1]);
    }

}
