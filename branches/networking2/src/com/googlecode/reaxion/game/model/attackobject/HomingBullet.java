package com.googlecode.reaxion.game.model.attackobject;

import com.googlecode.reaxion.game.model.Model;
import com.googlecode.reaxion.game.state.StageGameState;
import com.jme.math.Vector3f;

public class HomingBullet extends Bullet {

	public Model target;
	private final Vector3f offset = new Vector3f(0, 3, 0);
	
	private float speed = 4f;
	private float accelMax = .1f;
	
	public HomingBullet(Model m, Vector3f rotation) {
    	super(m);
    	velocity = rotation.mult(speed);
    }
	
	public HomingBullet(Model[] m, Vector3f rotation) {
    	super(m);
    	velocity = rotation.mult(speed);
    }
	
	
	@Override
	public void act(StageGameState b) {
		// home in, with a maximum acceleration of 3 natural units.
		Vector3f vel = target.model.getLocalTranslation().add(offset).subtract(model.getLocalTranslation()).normalize().mult(speed);
		Vector3f accel = vel.subtract(velocity);
		System.out.println(accel);
		if(accel.length() > accelMax)
			velocity = velocity.add(accel.normalize().mult(accelMax)).normalize().mult(speed);
		else
			velocity = velocity.add(accel).normalize().mult(speed);
		
		rotate(velocity.normalize());
		
		super.act(b);
	}
	
}
