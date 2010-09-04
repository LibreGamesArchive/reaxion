package com.googlecode.reaxion.game.model;

import java.util.ArrayList;

import com.googlecode.reaxion.game.state.BattleGameState;
import com.jme.math.Quaternion;
import com.jme.math.Vector3f;
import com.jme.renderer.Camera;
import com.jme.scene.Node;
import com.jme.scene.Spatial;
import com.radakan.jme.mxml.anim.MeshAnimationController;

/**
 * Wrapper class that contains the actual model, handles animation, etc. Imported
 * models should extend this class.
 * @author Khoa
 */
public class Model {
	
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
	 * Allow rotation along the x-axis
	 */
    protected Boolean allowYaw = true;
    
    /**
	 * Allow rotation along the y-axis
	 */
    protected Boolean allowRoll = true;
    
    /**
	 * Allow rotation along the z-axis
	 */
    protected Boolean allowPitch = true;
    
    /**
	 * Whether animation is being locked
	 */
	public Boolean animationLock = false;
    
    /**
	 * Damage this object inflicts per frame
	 */
	public float damagePerFrame = 0;
	
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
     * Checks for collision with all models in current {@code BattleGameState} at each
     * epsilon along the vector path, assuming that movement with vector is linear, and
     * returning other models at the first instance of collision if collisions occur.
     */
    public Model[] getLinearModelCollisions(BattleGameState b, Vector3f v, float epsilon) {
    	Model[] hit = new Model[0];
    	Vector3f step = v.normalize().mult(epsilon);
    	Vector3f start = new Vector3f(model.getLocalTranslation().x, model.getLocalTranslation().y, model.getLocalTranslation().z);
    	
    	for (int i=0; i<v.length()/epsilon+1; i++) {
    		hit = getModelCollisions(b);
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
     * Checks for collision with all models in current {@code BattleGameState}, returning
     * other models if collisions occur.
     */
    public Model[] getModelCollisions(BattleGameState b) {
    	ArrayList<Model> hit = new ArrayList<Model>();
    	ArrayList<Model> models = b.getModels();
    	for (int i=0; i < models.size(); i++) {
    		if (model.hasCollision(models.get(i).model, true)) {
    			hit.add(models.get(i));
    		}
    	}
    	return (hit.toArray(new Model[0]));
    }
     
    /**
     * Checks for collision with all nodes in current {@code BattleGameState}, returning
     * other spatials if collisions occur.
     */
    public Spatial[] getCollisions(BattleGameState b) {
    	ArrayList<Spatial> a = new ArrayList<Spatial>();
    	for (int i=0; i < b.getRootNode().getQuantity(); i++) {
    		Spatial n = b.getRootNode().getChild(i);
    		if (model.hasCollision(n, true)) {
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
    
    /**
	 * Rotate the model to point to {@code vector}
	 * @param vector Specifies the point which the model will point to
	 */
    public void rotate(Vector3f point) {
    	rotationVector = point.normalize();
    	
    	float pitch = 0f;
    	float roll = 0f;
    	float yaw = 0f;
    	if (allowYaw)
    		yaw = (float) Math.atan2(point.y, point.z);
    	if (allowRoll)
    		roll = (float) Math.atan2(point.x, point.z);
    	if (allowPitch)
    		pitch = (float) Math.atan2(point.y, point.x);
    	//System.out.println(point.x+" "+point.y+" "+point.z+": "+yaw+" "+roll+" "+pitch);
    	Quaternion q = new Quaternion();
    	model.setLocalRotation(q.fromAngles(yaw, roll, pitch));
    }
    
    /**
     * Convenience method to make model rotate about Y to face the camera
     */
    public void billboard(Camera c) {
    	Vector3f face = c.getDirection().negate();
    	face.y = 0;
    	rotate(face);
    }
    
    /**
	 * Needs to be called during updates, override to add functionality
	 */
    public void act(BattleGameState b) {
    }
}
