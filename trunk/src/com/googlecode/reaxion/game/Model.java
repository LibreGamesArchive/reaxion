package com.googlecode.reaxion.game;

import com.jme.math.Quaternion;
import com.jme.math.Vector3f;
import com.jme.scene.Node;
import com.radakan.jme.mxml.anim.*;
import java.util.ArrayList;

/**
 * Wrapper class that contains the actual model, handles animation, etc. Imported
 * models should extend this class.
 * @author Khoa
 */
public class Model {
    
	/**
	 * Node pointing to the actual model
	 */
    public Node model;
    
    /**
	 * Vector representing speed
	 */
    protected Vector3f vector;
    
    /**
	 * Vector representing offset for locking point
	 */
    public Vector3f trackOffset = new Vector3f(0, 0, 0);
    
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
	 * Whether this model can be locked onto by the camera
	 */
    public Boolean trackable = false;
    
    /**
	 * Marks model for damage collision-checking with player
	 */
    public Boolean dangerous = false;
    
    /**
	 * Contains the names of all loaded animations
	 */
    protected ArrayList<String> animation = new ArrayList<String>();
    int index = 0;
    
    public Model() {
    }
    
    public Model(String filename) {
    	ModelLoader.load(this, filename);
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
        vector = new Vector3f();
    }
    
    // TODO: Add tweening between states
    public void play(String state) {
        // Shift the animation to the new one
    	if (model.getControllerCount() > 0){
    		MeshAnimationController animControl = (MeshAnimationController) model.getController(0);
    		animControl.setAnimation(state);
    	}
    }
    
    public Vector3f getVector() {
    	return vector;
    }
    
    public void setVector(Vector3f v) {
    	vector = v;
    }
    
    public Vector3f getTrackPoint() {
    	return model.getWorldTranslation().add(trackOffset);
    }
    
    /**
	 * Rotate the model to point to {@code vector}
	 * @param vector Specifies the point which the model will point to
	 */
    public void rotate(Vector3f point) {
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
	 * Needs to be called during updates, override to add functionality
	 */
    public void act() {
    }
}
