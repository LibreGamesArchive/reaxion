package com.googlecode.reaxion.game.input.ai;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

import com.googlecode.reaxion.game.input.AIInput;
import com.googlecode.reaxion.game.model.Model;
import com.googlecode.reaxion.game.model.attackobject.AttackObject;
import com.googlecode.reaxion.game.model.character.Character;
import com.googlecode.reaxion.game.state.StageGameState;
import com.jme.math.FastMath;
import com.jme.math.Vector3f;

/**
 * AI superclass with additional "smart" functions.
 * @author Khoa Ha
 *
 */
public abstract class SmartAI extends AIInput {
	
	/**
	 * Previous position of character.
	 */
	protected Vector3f prevStep;	
	/**
	 * List of stat's models at last frame.
	 */
	private ArrayList<Model> prevModels;
	/**
	 * Positions of state's models at last frame.
	 */
	private ArrayList<Vector3f> prevPositions;
	
	/**
	 * Stores desired velocity to assign to character.
	 */
	protected Vector3f velocity;
	
    public SmartAI(Character c) {
    	super(c);
    }
    
    /**
     * Updates tracking values. Call {@code super()} at the end of this
     * function if overriden.
     */
    @Override
    public void makeCommands(StageGameState state) {
    	// try to move as indicated by velocity
    	if (velocity != null)
    		move(velocity.mult(new Vector3f(1, 0, 1)));
    	
    	// update previous info
    	prevStep = character.model.getWorldTranslation().clone();
    	prevModels = (ArrayList<Model>) state.getModels().clone();
    	prevPositions = new ArrayList<Vector3f>();
    	for (int i=0; i<prevModels.size(); i++)
    		prevPositions.add(prevModels.get(i).model.getWorldTranslation().clone());
    	
    }
    
    /**
     * Finds the vector of movement that will maximize the distance from
     * the trajectory lines of the provided objects, resolving any
     * equivalent solutions by finding the one closest to the bias vector.
     * @param state The {@code StageGameState} the object occupies
     * @param m The objects whose trajectory lines to avoid. The lines
     * 			will be considered regardless of direction, so objects
     * 			moving away from the character should not be passed in.
     * @param passes Number of passes with which to perform checks
     * @param tolerance Allowed difference between distances values which
     * 					will still be considered.
     * @param bias Vector indicating which direction would be preferred in
     * 				the case that there are multiple maximizing solutions.
     * @return Vector indicating the direction to sidestep.
     */
    protected Vector3f sidestep(StageGameState state, ArrayList<Model> m, int passes, float tolerance, Vector3f bias) {
    	// check if data exists
    	if (prevModels == null || prevPositions == null)
    		return null;
    	
    	Vector3f step = null;
    	Vector3f t = character.model.getWorldTranslation();
    	
    	float[][] list = new float[passes][2];
    	
    	// Iterate over unit circle
    	for (int n=0; n<passes; n++) {
    		float angle = 2*FastMath.PI/(float)passes*n;
    		float x = t.x + character.speed*FastMath.sin(angle);
    		float z = t.z + character.speed*FastMath.cos(angle);
    		
	    	// Find product of distances based on iterated angle changes
	    	float distProd = 1;
	    	for (int i=0; i<m.size(); i++) {
	    		int ind = prevModels.indexOf(m.get(i));
	    		if (ind >= 0) {
	    			Vector3f prevPos = prevPositions.get(ind);
	    			Vector3f pos = m.get(i).model.getWorldTranslation();
	    			
	    			//float dist = ((x - prevPos.x)*(pos.x - prevPos.x) + (z - prevPos.z)*(pos.z - prevPos.z)) / pos.distanceSquared(prevPos);
	    			float dist = Math.abs((pos.x - prevPos.x)*(prevPos.z - z) - (pos.z - prevPos.z)*(prevPos.x - x)) / pos.distanceSquared(prevPos);
	    			
	    			// check NaN
	    			if (Float.isNaN(dist))
	    				dist = 1;
	    			
	    			distProd *= dist;
	    		}
	    	}
	    	
	    	// add to the list in the order [distanceProduct, direction]
	    	list[n][0] = distProd;
	    	list[n][1] = angle;
    	}
    	
    	// Sort the list in descending order of distance products
    	Arrays.sort(list, new Comparator<float[]>() {
        	public int compare(float[] one, float[] two){
        		return Float.compare(two[0], one[0]);
        	}
        });    	
    	
    	// Dump all angles corresponding to highest distances within provided tolerance level into new list
    	ArrayList<Float> directions = new ArrayList<Float>();
    	float maxDist = list[0][0];
    	directions.add(list[0][1]);
    	for (int j=1; j<list.length; j++) {
    		// Check if value is tolerated
    		if (list[j][0] >= maxDist - tolerance)
    			directions.add(list[j][1]);
    	}  	
    	
    	// Compare vector similarities to bias vector using dot product method
    	float maxSimilarity = 0;
    	for (int p=0; p<directions.size(); p++) {
    		Vector3f vec = new Vector3f(FastMath.sin(directions.get(p)), 0, FastMath.cos(directions.get(p)));
    		float dot = vec.dot(bias);
    		// Check if vector is more similar
    		if (dot > maxSimilarity) {
    			maxSimilarity = dot;
    			step = vec;
    		}
    	}
    	
    	return step;
    }
    
    /**
     * Finds all objects that could pose a threat to jumping by evaluating
     * their predicted angle change with respect to jumping motion.
     * @param state The {@code StageGameState} the object occupies
     * @param velocityY Initial vertical velocity for the jump, assuming that
     * 					gravity acts on the character.
     * @param threshold Minimum y value above the character an object must be
     * 					to be considered a threat.
     * @param precision Amount specifying how significant an angle change
     * 					must be to merit consideration. The lowest, 0, allows
     * 					for all changes to be considered, while the highest, 1,
     * 					considers only objects whose centers go through the
     * 					character, that is, those with a 180 degree change.
     * @return A list of objects flagged as threats.
     */
    protected ArrayList<Model> checkJump(StageGameState state, float velocityY, float threshold, float precision) {
    	ArrayList<Model> list = new ArrayList<Model>();
    	Vector3f t = character.model.getWorldTranslation();
    	Vector3f apex = t.mult(new Vector3f(1, 0, 1)).add(0, 1.5f*FastMath.pow(velocityY, 2)/character.gravity, 0);
    	ArrayList<Model> m = state.getModels();
    	
    	// check if data exists
    	if (prevModels == null || prevPositions == null)
    		return list;
    	
    	for (int i=0; i<m.size(); i++) {

    		// don't consider self
    		if ((!(m.get(i) instanceof Character) && !(m.get(i) instanceof AttackObject)) || m.get(i) == character)
    			continue;
    		
    		Model o = m.get(i);
    		int ind = prevModels.indexOf(o);
    		
    		Vector3f threat;
    		Vector3f pos = o.model.getWorldTranslation();
    		
    		// check if object is above player
    		if (pos.y >= t.y + threshold) {
    			
    			//check if reference point exists
    			if (ind >= 0) {

    				// find the closer value of the two methods, velocity addition and position extrapolation
    				// it's better to err on the side of caution
    				Vector3f posPrediction = pos.add(pos.subtract(prevPositions.get(ind)));
    				Vector3f velPrediction = pos.add(o.getVelocity());
    				if (ind >= 0 && t.distance(posPrediction) < t.distance(velPrediction))
    					threat = posPrediction;
    				else
    					threat = velPrediction;

    				// find the change in angle of the object relative to the character
    				// a greater angle change corresponds to a faster/closer object, e.g. a greater threat
    				float angleX = (FastMath.atan2(pos.y-t.y, pos.x-t.x) - FastMath.atan2(threat.y-apex.y, threat.x-apex.x) + 4*FastMath.PI)
    				%(2*FastMath.PI);
    				float angleZ = (FastMath.atan2(pos.y-t.y, pos.z-t.z) - FastMath.atan2(threat.y-apex.y, threat.z-apex.z) + 4*FastMath.PI)
    				%(2*FastMath.PI);

    				// evaluate angle based on precision
    				float avgAngle = (angleX + angleZ)/2 %(2*FastMath.PI);
    				if (FastMath.sin(avgAngle) < 0 && avgAngle >= 1.57f * precision)
    					list.add(o);
    			}
    		}
    	}
    	
    	return list;
    }
    
    /**
     * Finds all objects within a certain area that could pose a threat by 
     * evaluating their predicted angle change with respect to the character.
     * @param state The {@code StageGameState} the object occupies
     * @param radius Radius within which objects will be considered, -1 to
     * 					consider all objects.
     * @param forecast Number of frames to extrapolate over. Should be large
     * 					to see if objects pass through the center.
     * @param precision Amount specifying how significant an angle change
     * 					must be to merit consideration. The lowest, 0, allows
     * 					for all changes to be considered, while the highest, 1,
     * 					considers only objects whose centers go through the
     * 					character, that is, those with a 180 degree change.
     * @return A list of objects flagged as threats.
     */
    protected ArrayList<Model> findThreats(StageGameState state, float radius, float forecast, float precision) {
    	ArrayList<Model> list = new ArrayList<Model>();
    	Vector3f t = character.model.getWorldTranslation();
    	ArrayList<Model> m = state.getModels();
    	
    	
    	// check if data exists
    	if (prevModels == null || prevPositions == null)
    		return list;
    	
    	for (int i=0; i<m.size(); i++) {

    		// don't consider self
    		if ((!(m.get(i) instanceof Character) && !(m.get(i) instanceof AttackObject)) || m.get(i) == character)
    			continue;
    		
    		Model o = m.get(i);
    		int ind = prevModels.indexOf(o);
    		
    		Vector3f threat;
    		Vector3f pos = o.model.getWorldTranslation();
    		
    		//check if reference point exists
    		if (ind >= 0) {

    			// check if object is within detection range
    			if (radius < 0 || t.distance(pos) <= radius) {

    				// find the closer value of the two methods, velocity addition and position extrapolation
    				// it's better to err on the side of caution
    				Vector3f posPrediction = pos.add(pos.subtract(prevPositions.get(ind)).mult(forecast));
    				Vector3f velPrediction = pos.add(o.getVelocity().mult(forecast));
    				if (ind >= 0 && t.distance(posPrediction) < t.distance(velPrediction))
    					threat = posPrediction;
    				else
    					threat = velPrediction;

    				// find the change in angle of the object relative to the character
    				// a greater angle change corresponds to a faster/closer object, e.g. a greater threat
    				float angle = Math.abs((FastMath.atan2(pos.z-t.z, pos.x-t.x)-FastMath.atan2(threat.z-t.z, threat.x-t.x)+4*FastMath.PI) % (2*FastMath.PI));

    				// evaluate angle based on precision
    				if (angle >= 1.57f*precision)
    					list.add(o);
    			}
    		}
    	}
    	
    	return list;
    }
    
    /**
     * Determine the optimal direction for fleeing from surrounding hazards
     * based on their distances, taking into consideration the magnitude and
     * direction of separation during calculations.
     * @param state The {@code StageGameState} the object occupies
     * @param radius Radius within which objects will be considered, -1 to
     * 					consider all objects.
     * @param immediate If true, only consider approaching objects
     * @param objects If true, consider non-character damaging objects 
     * @param characters If true, consider other non-affiliated characters
     * @return A vector sum of the distances to all threats, pointing in the
     * 			opposite direction.
     */
    protected Vector3f fleeDistance(StageGameState state, float radius, boolean immediate, boolean objects, boolean characters) {
    	// check if data exists
    	if (prevModels == null || prevPositions == null)
    		return null;
    	
    	Vector3f run = new Vector3f();
    	Vector3f t = character.model.getWorldTranslation();
    	ArrayList<Model> m = state.getModels();
    	
    	// iterate through Model list and compare distances
    	for (int i=0; i<m.size(); i++) {

    		// don't consider self
    		if ((!(m.get(i) instanceof Character) && !(m.get(i) instanceof AttackObject)) || m.get(i) == character)
    			continue;
    		
    		Model o = m.get(i);

    		// check if within radius
    		if (radius < 0 || o.model.getWorldTranslation().distance(t) <= radius) {
    			
    			// check if the Model poses a threat
    			if (((objects && o.damagePerFrame > 0) || (characters && o instanceof Character)) && (o.users== null || !o.users.contains(character))) {
    				
    				int ind = prevModels.indexOf(o);
    				
    				// check if object is approaching if immediate is flagged
    				if (!immediate || (ind >= 0 && o.model.getWorldTranslation().distance(t) < prevPositions.get(ind).distance(t))) {
    					// add the value to run, so as to weigh in the opposite direction
    					run = run.subtract(o.model.getWorldTranslation().subtract(t));
    				}
    			}
    		}
    	}
    	
    	return run;
    }
    
    /**
     * Determine the optimal direction for fleeing from surrounding hazards
     * based on their velocities, taking into consideration the speed and
     * direction of movement of objects during calculations.
     * @param state The {@code StageGameState} the object occupies
     * @param radius Radius within which objects will be considered, -1 to
     * 					consider all objects.
     * @param immediate If true, only consider approaching objects
     * @param relative If true, consider from a relative frame of reference
     * @param characters If true, consider other non-affiliated characters
     * @return A vector sum of the velocities of all threats, pointing in the
     * 			opposite direction.
     */
    protected Vector3f fleeVelocity(StageGameState state, float radius, boolean immediate, boolean relative, boolean characters) {
    	// check if data exists
    	if (prevModels == null || prevPositions == null)
    		return null;
    	
    	Vector3f run = new Vector3f();
    	Vector3f t = character.model.getWorldTranslation();
    	ArrayList<Model> m = state.getModels();
    	ArrayList<Model> pm = (ArrayList<Model>) prevModels.clone();
    	ArrayList<Vector3f> pp = (ArrayList<Vector3f>) prevPositions.clone();
    	
    	// iterate through Model list and compare previous positions
    	for (int i=0; i<m.size(); i++) {
    		
    		// don't consider self
    		if ((!(m.get(i) instanceof Character) && !(m.get(i) instanceof AttackObject)) || m.get(i) == character)
    			continue;
    		
    		for (int j=0; j<pm.size(); j++) {
    			
    			// check if they're the same Model
        		if (m.get(i).equals(pm.get(j))) {
        			// remove entries from arrays to optimize the rest of the search
        			Model o = pm.remove(j);
        			Vector3f p = pp.remove(j);
        			
        			// check if within radius
        			if (radius < 0 || o.model.getWorldTranslation().distance(t) <= radius) {
        				
        				// check if the Model poses a threat
        				if ((o.damagePerFrame > 0 || (characters && o instanceof Character)) && (o.users== null || !o.users.contains(character))) {
        					
        					// check if object is approaching if immediate is flagged
        					if (!immediate || (!relative && o.model.getWorldTranslation().distance(t) < p.distance(t)) ||
        							(relative && o.model.getWorldTranslation().distance(t) < p.distance(prevStep))) {
        						
        						// find the Model's change in position
        						Vector3f dif;
        						if (!relative)
        							dif = o.model.getWorldTranslation().subtract(p);
        						else
        							dif = o.model.getWorldTranslation().subtract(t).subtract(p.subtract(prevStep));

        						// add the value to run, so as to weigh in the opposite direction
        						run = run.add(dif);
        					}
        				}
        			}
        			
        			j--;
        		}
        	}
    	}
    	
    	return run;
    }
    
    /**
     * Determine the optimal direction for fleeing from surrounding hazards
     * based on their damage potentials, taking into consideration the 
     * estimated damage they can inflict over time and their positions during
     * calculations.
     * @param state The {@code StageGameState} the object occupies
     * @param radius Radius within which objects will be considered, -1 to
     * 					consider all objects.
     * @param immediate If true, only consider approaching objects
     * @param contactTime Time estimated to be in contact with each threat.
     * @param threshold Threshold estimate for {@code damagePerFrame}, above
     * 					which the object is assumed to not hit every frame.
     * @return A vector sum of relative threat positions with magnitudes equal
     * 			to estimated damage, pointing in the direction of least damage.
     */
    protected Vector3f fleeDamage(StageGameState state, float radius, boolean immediate, int contactTime, float threshold) {
    	// check if data exists
    	if (prevModels == null || prevPositions == null)
    		return null;
    	
    	Vector3f run = new Vector3f();
    	Vector3f t = character.model.getWorldTranslation();
    	ArrayList<Model> m = state.getModels();
    	
    	// iterate through Model list and compare damage potentials
    	for (int i=0; i<m.size(); i++) {

    		// don't consider self
    		if ((!(m.get(i) instanceof Character) && !(m.get(i) instanceof AttackObject)) || m.get(i) == character)
    			continue;
    		
    		Model o = m.get(i);

    		// check if within radius
    		if (radius < 0 || o.model.getWorldTranslation().distance(t) <= radius) {
    			int ind = prevModels.indexOf(o);
    			
    			// check if the Model poses a threat
    			if (o.damagePerFrame > 0 && (o.users== null || !o.users.contains(character))) {

    				// check if object is approaching if immediate is flagged
    				if (!immediate || (ind >= 0 && o.model.getWorldTranslation().distance(t) < prevPositions.get(ind).distance(t))) {

    					// calculate a vector in the same direction with magnitude equal to damage potential
    					Vector3f v = o.model.getWorldTranslation().subtract(t).normalize().mult(estimateDamage(o, contactTime, threshold));

    					// add the value to run, so as to weigh in the opposite direction
    					run = run.add(v);
    				}
    			}
    		}
    	}
    	
    	return run;
    }

    /**
     * Compares all supplied objects for the highest damage potential, and
     * returns all objects with the highest value.
     * @param m List of objects to compare.
     * @param contactTime Time estimated to be in contact with each threat.
     * @param threshold Threshold estimate for {@code damagePerFrame}, above
     * 					which the object is assumed to not hit every frame.
     * @return An ArrayList of the highest-damage objects
     */
    protected ArrayList<Model> compareDanger(ArrayList<Model> m, int contactTime, float threshold) {
    	ArrayList<Model> max = (ArrayList<Model>) m.clone();
    	float maxDamage = estimateDamage(max.get(0), contactTime, threshold);
    	
    	// go through all objects, comparing estimated damage levels
    	for (int i=1; i<max.size(); i++) {
    		Model o = m.get(i);
    		float objDamage = estimateDamage(o, contactTime, threshold);
    		// check if current object's damage potential exceeds previous object's
    		if (objDamage > maxDamage) {  			
    			// remove all previous elements; they have lesser values
    			for (int j=0; j<i; j++)
    				max.remove(0);
    			i = 1;
    			maxDamage = objDamage;
    		} else if (objDamage < maxDamage) {
    			// remove this element; it has lesser value
    			max.remove(i);
    			i--;
    		}
    	}
    	
    	return max;
    }
    
    /**
     * Estimates damage an object can inflict over {@code contactTime}.
     * @param m Model in question.
     * @param contactTime Time estimated to be in contact with each threat.
     * @param threshold Threshold estimate for {@code damagePerFrame}, above
     * 					which the object is assumed to not hit every frame.
     * @return
     */
    private float estimateDamage(Model m, int contactTime, float threshold) {
    	if (m.users.contains(character))
    		return 0;
    	if (m.damagePerFrame >= threshold)
    		return m.damagePerFrame;
    	else
    		return m.damagePerFrame*contactTime;
    }
    
    /**
     * Checks for and calculates a vector deflecting from a boundary collision.
     * @param state The {@code StageGameState} the object occupies
     * @param precision Amount specifying how much random variation to include
     * 					in calculations. The lowest, 0, allows for the maximum
     * 					range of values, while the highest, 1, considers the
     * 					average of all possible deflections.
     * @return A normalized vector representing the deflection path if there is
     * 			a collision. Otherwise, {@code null} is returned.
     */
    protected Vector3f avoidWall(StageGameState state, float precision) {
    	// gather information
    	Vector3f t = character.model.getWorldTranslation();
    	float baseAngle = FastMath.atan2(t.z, t.x);
		Point2D.Float[] p = state.getStage().bound(character);
		
		// check if collision exists
		if (p.length >= 2) {
			// find range of departure angles
			float minAngle = baseAngle;
			float maxAngle = baseAngle;
			for (Point2D.Float o : p) {
				float a = FastMath.atan2(o.y, o.x);
				// adjust for cyclic comparisons
				if (a - baseAngle > 2*FastMath.PI)
					a -= 2*FastMath.PI;
				else if (baseAngle - a > 2*FastMath.PI)
					a += 2*FastMath.PI;

				// compare the max and min collision values
				minAngle = Math.min(minAngle, a);
				maxAngle = Math.max(maxAngle, a);
			}

			// calculate departure angles
			float minDep;
			float maxDep;

			// assume circular map, departure angles depend on angles swept out
			minDep = baseAngle + FastMath.PI - (FastMath.PI/2 - (maxAngle - baseAngle)/2);
			maxDep = baseAngle + FastMath.PI + (FastMath.PI/2 - (baseAngle - maxAngle)/2);

			// change angles to accommodate specified precision levels,
			// more precise values yield less random deflections
			minDep = minDep + (baseAngle + FastMath.PI - minDep) * precision;
			maxDep = maxDep + (baseAngle + FastMath.PI - maxDep) * precision;

			// set velocity to a random angle within range
			float angle = FastMath.nextRandomFloat()*(maxDep - minDep) + minDep;

			return new Vector3f(FastMath.cos(angle), 0, FastMath.sin(angle));
		}
		
		return null;
    }
    
}