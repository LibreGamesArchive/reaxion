package com.googlecode.reaxion.game.model.prop;

import java.util.Random;

import com.jme.scene.Controller;
import com.jme.scene.Spatial;

/**
 * The FireworkController moves the sphere diagonally upwards.<br>
 * When the lifetime of the object is finished,<br>
 * the controller removes himself from the Sphere and removes the Sphere from the Scene.<br>
 */
@SuppressWarnings("serial")
class FireworkController extends Controller {
	/** lifetime in seconds */
	private float			lifetime;
	/** vertical speed */
	private final float		vSpeed;
	/** the object to be moved */
	private final Spatial	object;
	/** horizontal speed */
	private final float		hSpeed	= 1f;
	/** should the object move digonally to the left or right */
	private final boolean	leftright;
	private final Random	rand;
	private final ExplosionFactory	explosionFactory;


	/**
	 * The constructor, which takes the object to move, the lifetime and vertical speed.
	 * 
	 * @param object_
	 *            object to move
	 * @param lifetime_
	 *            lifetime in seconds
	 * @param vSpeed_
	 *            vertical speed
	 */
	FireworkController(final Spatial object_, final float lifetime_, final float vSpeed_, final ExplosionFactory factory) {
		this.explosionFactory = factory;
		this.lifetime = lifetime_;
		this.object = object_;
		this.vSpeed = vSpeed_;
		this.rand = new Random();
		// should the firework be moved diagonally to the left or right
		this.leftright = this.rand.nextBoolean();
	}


	/**
	 * The update Method gets called every frame.<br>
	 * Moves the object upwards and a bit to the left or right.<br>
	 * When the lifetime of the object is finished, it gets removed from the scene.<br>
	 * An explosion is spawned when the object dies.
	 */
	@Override
	public void update(final float time) {
		this.lifetime -= time;
		// add hSpeed to the X-Axis and speed to the Y-Axis
		this.object.getLocalTranslation().addLocal((this.leftright == true ? 1 : -1) * this.hSpeed * time,
				this.vSpeed * time, 0);

		if (this.lifetime <= 0) {
			setActive(false);
			this.explosionFactory.addDone(this.object);
		}
	}
}

