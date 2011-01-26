package com.googlecode.reaxion.game.model;

import java.util.ArrayList;

import com.googlecode.reaxion.game.state.StageGameState;
import com.googlecode.reaxion.game.util.ListFilter;
import com.googlecode.reaxion.game.util.ListFilter.Filter;
import com.jme.math.FastMath;
import com.jme.math.Quaternion;
import com.jme.math.Vector3f;
import com.jme.scene.Node;
import com.jme.scene.Spatial;
import com.radakan.jme.mxml.anim.MeshAnimationController;

/**
 * Wrapper class that contains the actual model, handles animation, etc. Imported
 * models should extend this class.
 * @author Khoa
 */
public class Model {
	
	public enum Billboard {
		/**
		 * Model will not be billboarded.
		 */
		None,
		/**
		 * Model will only rotate in the XZ plane.
		 */
		YLocked,
		/**
		 * Model will rotate about all axes.
		 */
		Free;
	}
	
	public String name;
    
	/**
	 * Node pointing to the actual model
	 */
    public Node model;
    
    /**
	 * Vector representing velocity
	 */
    protected Vector3f velocity = new Vector3f(0, 0, 0);
    
    /**
	 * Filename used to reference load files
	 */
    public String filename;
    
    /**
	 * Vector representing offset for locking point
	 */
    public Vector3f trackOffset = new Vector3f(0, 0, 0);
    
    /**
	 * Vector pointing in the facing direction
	 */
    public Vector3f rotationVector = new Vector3f(0, 0, 1);
    
    /**
	 * Rotation along the x-axis
	 */
    public float yaw = 0;
    
    /**
	 * Rotation along the y-axis
	 */
    public float roll = 0;
    
    /**
	 * Rotation along the z-axis
	 */
    public float pitch = 0;
    
    /**
     * Type of billboarding model exhibits.
     */
    public Billboard billboarding = Billboard.None;
    
    /**
	 * Whether animation is being locked
	 */
	public boolean animationLock = false;
    
    /**
	 * Damage this object inflicts per frame
	 */
	public float damagePerFrame = 0;
	
	/**
	 * Damage multiplier.
	 */
	public double damageMult = 1;
	
	/**
	 * Whether this object causes contacting objects to flinch
	 */
	public boolean flinch = false;
    
	/**
     * Whether or not this object gravitates
     */
    public boolean gravitate = false;
	
    /**
     * Personal gravity for this model
     */
    public float gravity = 0;
    
	/**
	 * Y-velocity while airborne
	 */
	public float gravVel = 0;
    
    /**
	 * Whether this model can be locked onto by the camera
	 */
    public boolean trackable = false;
    
    /**
     * Whether or not to check triangles in this model's collision checking.
     */
    protected boolean checkTriangles = true;
    
    /**
	 * Refers to creators of model, if needed
	 */
    public ArrayList<Model> users;
    
    /**
	 * Contains the names of all loaded animations
	 */
    protected ArrayList<String> animation = new ArrayList<String>();
    int index = 0;
    
    public Model() {
    	init();
    }
    
    public Model(String fn) {
    	filename = fn;
    	init();
    }
    
    /**
     * Method called by all constructors. Override to add functionality.
     */
    protected void init() {
    }
    
    /**
     * Checks for collision with models in {@code models} at each epsilon along the
     * vector path, assuming that movement with vector is linear, and returning other
     * models at the first instance of collision if collisions occur.
     */
    public Model[] getLinearModelCollisions(Vector3f v, float epsilon, ArrayList<Model> models) {
    	Model[] hit = new Model[0];
    	Vector3f step = v.normalize().mult(epsilon);
    	Vector3f start = new Vector3f(model.getLocalTranslation().x, model.getLocalTranslation().y, model.getLocalTranslation().z);
    	
    	for (int i=0; i<v.length()/epsilon+1; i++) {
    		hit = getModelCollisions(models);
    		if (hit.length > 0) {
    			model.setLocalTranslation(start);
    			return hit;
    		}
    		model.setLocalTranslation(model.getLocalTranslation().addLocal(step));
    		model.updateGeometricState(0, true);
    	}
    	
    	model.setLocalTranslation(start);
    	return hit;
    }
    
    /**
     * Checks for collision with models in current {@code BattleGameState} filtered
     * according to the provided {@code Filter} and excluding the provided exceptions at
     * each epsilon along the vector path, assuming that movement with vector is linear,
     * and returning other models at the first instance of collision if collisions occur.
     */
    public Model[] getLinearModelCollisions(StageGameState b, Vector3f v, float epsilon, Filter filter, ArrayList<Model> exceptions) {
    	return getLinearModelCollisions(v, epsilon, ListFilter.filter(b.getModels(), filter, exceptions));
    }
    
    /**
     * Checks for collision with all models in current {@code BattleGameState}, returning
     * other models if collisions occur.
     */
    public Model[] getModelCollisions(StageGameState b) {
    	return (getModelCollisions(b.getModels()));
    }
    
    /**
     * Checks for collision with all models in {@code models}, returning other models if
     * collisions occur.
     */
    public Model[] getModelCollisions(ArrayList<Model> models) {
    	ArrayList<Model> hit = new ArrayList<Model>();
    	for (int i=0; i < models.size(); i++) {
    		if (model.hasCollision(models.get(i).model, checkTriangles)) {
    			hit.add(models.get(i));
    		}
    	}
    	return (hit.toArray(new Model[0]));
    }
    
    /**
     * Checks for collision with models in current {@code BattleGameState} considering provided
     * {@code Filter} and excluding exceptions, returning other models if collisions occur.
     */
//    public Model[] getModelCollisions(StageGameState b, Filter filter, ArrayList<Model> exceptions) {
//    	ArrayList<Model> hit = new ArrayList<Model>();
//    	ArrayList<Model> models = ListFilter.filter(b.getModels(), filter, exceptions);
//    	for (int i=0; i < models.size(); i++) {
//    		if (model.hasCollision(models.get(i).model, checkTriangles)) {
//    			hit.add(models.get(i));
//    		}
//    	}
//    	return (hit.toArray(new Model[0]));
//    }
     
    /**
     * Checks for collision with all nodes in current {@code BattleGameState}, returning
     * other spatials if collisions occur.
     */
    public Spatial[] getCollisions(StageGameState b) {
    	ArrayList<Spatial> a = new ArrayList<Spatial>();
    	for (int i=0; i < b.getRootNode().getQuantity(); i++) {
    		Spatial n = b.getRootNode().getChild(i);
    		if (model.hasCollision(n, checkTriangles)) {
    			a.add(n);
    		}
    	}
    	return (a.toArray(new Spatial[0]));
    }
    
    /**
     * Checks for collision with ground, assuming all ground is y=0, and correct in vector
     * velocity so that Model never goes under the ground.
     */
    protected void contactGround() {
    	if (model.getWorldTranslation().y + velocity.y < 0)
    		velocity.y = -model.getWorldTranslation().y;
    }
    
    /**
	 * Called once the model is loaded and the {@code Model} is ready to be deployed
	 */
    public void initialize() {
        // Populate list with animation states
    	if (model.getControllerCount() > 0) {
    		for (String i : ((MeshAnimationController)(model.getController(0))).getAnimationNames())
    			animation.add(i);
    		System.out.println("Animations: "+animation);
    	}
    	
        // Create vector
        velocity = new Vector3f();
    }
    
    public boolean play(String state) {
    	return play(state, 0);
    }
    
    /**
	 * Play animation {@code state}. Returns false if playing and true if animation is
	 * done or is incompatible with character.
	 */
    public boolean play(String state, float tpf) {
    	try {
    		MeshAnimationController animControl = (MeshAnimationController) model.getController(0);

    		// If not playing, shift the animation to the new one
    		if (!animControl.getActiveAnimation().equals(state))
    			animControl.setAnimation(state);
    		else if (animControl.getCurTime() + tpf >= animControl.getAnimationLength(state))
    			return true;
    		return false;
    	} catch (Exception e) {
    		return true;
    	}
    }
    
    public Vector3f getVelocity() {
    	return velocity;
    }
    
    public void setVelocity(Vector3f v) {
    	velocity = v;
    }
    
    public Vector3f getTrackPoint() {
    	return model.getWorldTranslation().add(trackOffset);
    }
    
    public Vector3f getXZTranslation() {
    	Vector3f loc = model.getLocalTranslation();
    	return new Vector3f(loc.x, 0, loc.z);    	
    }
    
    /**
	 * Rotate the model according to {@code yaw}, {@code roll}, and {@code pitch}.
	 */
    public void rotate() {
    	Quaternion q = new Quaternion();
    	model.setLocalRotation(q.fromAngles(yaw, roll, pitch));
    }
    
    /**
	 * Rotate the model to point to {@code vector} before applying {@code yaw},
	 * {@code roll}, and {@code pitch}.
	 * @param vector Specifies the point which the model will point to
	 */
    public void rotate(Vector3f point) {
    	rotationVector = point.normalize();

    	float pointRoll = (float) Math.atan2(rotationVector.x, rotationVector.z);
    	float pointYaw = (float) -Math.atan2(rotationVector.y, FastMath.sqrt(FastMath.pow(rotationVector.x, 2) + FastMath.pow(rotationVector.z, 2)));
    	
    	//System.out.println(point.x+" "+point.y+" "+point.z+": "+yaw+" "+roll+" "+pitch);
    	//model.setLocalRotation(new Quaternion().fromAngles(yaw + pointYaw, roll + pointRoll, pitch));
    	model.setLocalRotation(rotateYXZ(yaw + pointYaw, roll + pointRoll, pitch));
    }
    
    /**
     * Convenience method that generates a rotation quaternion with Euler angles
     * applied in roll, yaw, pitch order, as consistant with spherical coordinates.
     * @param yaw rotation about x
     * @param roll rotation about y
     * @param pitch rotation about z
     * @return
     */
    private Quaternion rotateYXZ(float yaw, float roll, float pitch) {
    	Quaternion q = new Quaternion();
    	float angle;
        float sinRoll, sinPitch, sinYaw, cosRoll, cosPitch, cosYaw;
        angle = pitch * 0.5f;
        sinPitch = FastMath.sin(angle);
        cosPitch = FastMath.cos(angle);
        angle = roll * 0.5f;
        sinRoll = FastMath.sin(angle);
        cosRoll = FastMath.cos(angle);
        angle = yaw * 0.5f;
        sinYaw = FastMath.sin(angle);
        cosYaw = FastMath.cos(angle);

        // variables used to reduce multiplication calls.
        float cosPitchXcosYaw = cosPitch * cosYaw;
        float sinPitchXsinYaw = sinPitch * sinYaw;
        float cosPitchXsinYaw = cosPitch * sinYaw;
        float sinPitchXcosYaw = sinPitch * cosYaw;
        
        q.w = (cosPitchXcosYaw * cosRoll - sinPitchXsinYaw * sinRoll);
        q.y = (cosPitchXcosYaw * sinRoll + sinPitchXsinYaw * cosRoll);
        q.x = (cosPitchXsinYaw * cosRoll + sinPitchXcosYaw * sinRoll);
        q.z = (sinPitchXcosYaw * cosRoll - cosPitchXsinYaw * sinRoll);
        
        q.normalize();
    	return q;
    }
    
    /**
     * Convenience method to make model rotate to face the camera
     * @param c Camera to face
     * @param yFree Whether to face the camera completely or only rotate about Y
     */
//    public void billboard(Camera c, boolean yFree) {
//    	Vector3f face = c.getDirection().negate();
//    	if (!yFree)
//    		face.y = 0;
//    	rotate(face);
//    }
    
    public float getDamage() {
    	return (float)damageMult*damagePerFrame;
    }
    
    /**
	 * Needs to be called during updates, override to add functionality
	 */
    public void act(StageGameState b) {
    }
}
