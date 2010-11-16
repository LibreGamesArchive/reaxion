package com.googlecode.reaxion.game.model.prop;

import java.util.Random;

import com.jme.image.Texture;
import com.jme.image.Texture.MagnificationFilter;
import com.jme.image.Texture.MinificationFilter;
import com.jme.math.FastMath;
import com.jme.math.Vector3f;
import com.jme.renderer.ColorRGBA;
import com.jme.renderer.Renderer;
import com.jme.scene.Controller;
import com.jme.scene.Node;
import com.jme.scene.shape.Sphere;
import com.jme.scene.state.BlendState;
import com.jme.scene.state.MaterialState;
import com.jme.scene.state.TextureState;
import com.jme.scene.state.ZBufferState;
import com.jme.system.DisplaySystem;
import com.jme.util.TextureManager;
import com.jmex.effects.particles.ParticleFactory;
import com.jmex.effects.particles.ParticleMesh;
import com.jmex.effects.particles.SimpleParticleInfluenceFactory;

@SuppressWarnings("serial")
public class Firework extends Node {

	private static final String	PATH_SPARK	= "src/com/googlecode/reaxion/resources/particle/spark.jpg";
	/** spawn area */
	private float maxRadius = 300;
	private float minRadius = 150;
	private float maxAngle = FastMath.PI/3;
	private float minAngle = -FastMath.PI/3;
	/** spawnrate of new fireworks */
	private float spawnRate	= 8f;
	private float timePassed;
	/** material state for the sphere */
	private final MaterialState	sphereMatState;
	private float minLifetime;
	private float maxLifetime;
	private float minSpeed;
	private float maxSpeed;
	private final ExplosionFactory factory;


	/**
	 * Set up all the necessary Stuff. Init the Explosionfactory, create the 3DText.
	 */
	public Firework() {

		// initialize the ExplosionFactory and some other stuff
		this.factory = new ExplosionFactory(this, PATH_SPARK);
		this.sphereMatState = DisplaySystem.getDisplaySystem().getRenderer().createMaterialState();
		this.sphereMatState.setAmbient(ColorRGBA.white);
		this.minLifetime = 5f;
		this.maxLifetime = 8f;
		this.minSpeed = 5;
		this.maxSpeed = 7;
	}
	
	
	public void setExplosionSize(int size) {
		this.factory.setExplosionSize(size);
	}


	public void setSpawnRate(float rate) {
		this.spawnRate = rate;
	}
	
	public void setLifetime(float min, float max) {
		this.minLifetime = min;
		this.maxLifetime = max;
	}
	
	public void setSpeed(float min, float max) {
		this.minSpeed = min;
		this.maxSpeed = max;
	}


	@Override
	public void updateWorldData(final float time) {
		super.updateWorldData(time);
		this.timePassed += time;
		if (this.timePassed > this.spawnRate) {
			spawnFirework();
			this.timePassed = 0;
		}
		this.factory.respawn();
	}


	/**
	 * Spawn a new firework.<br>
	 * Creates a new Sphere at a random position and attaches a controller to it.
	 * Location is determined by bounding radius and angle.
	 */
	private void spawnFirework() {
		final Random rand = FastMath.rand;
		final float lifetime = this.minLifetime + rand.nextFloat() * (this.maxLifetime - this.minLifetime);
		final float speed = this.minSpeed + rand.nextFloat() * (this.maxSpeed - this.minSpeed);
		// Define a new start location and create a new Sphere.
		float angle = (rand.nextInt(2))*FastMath.PI + (rand.nextFloat()*(maxAngle - minAngle) + minAngle);
		float radius = rand.nextFloat()*(maxRadius - minRadius) + minRadius;
		final Vector3f start = new Vector3f(radius*FastMath.cos(angle), 0, radius*FastMath.sin(angle));
		final Sphere sphere = new Sphere("s", 5, 5, 0.06f);
		final Node spNode = new Node("firesphere");
		spNode.attachChild(sphere);
		spNode.getLocalTranslation().set(start);
		// color the Sphere yellow
		sphere.setRenderState(this.sphereMatState);
		// add a controller to move the Sphere upwards
		spNode.addController(new FireworkController(spNode, lifetime, speed, this.factory));

		spNode.attachChild(createTrail());

		// attach the Sphere to the scene.
		this.attachChild(spNode);
		this.updateRenderState();
	}


	/**
	 * creates a particle trail.
	 * 
	 * @return particleMesh representing the trail of a firework
	 */
	private ParticleMesh createTrail() {
		final Renderer r = DisplaySystem.getDisplaySystem().getRenderer();
		final ParticleMesh particleGeom = ParticleFactory.buildParticles("trail", 10);
		// add a gravity effect to the particle effect
		particleGeom.addInfluence(SimpleParticleInfluenceFactory.createBasicGravity(new Vector3f(0, -0.15f, 0), true));
		particleGeom.setEmissionDirection(new Vector3f(0.0f, 1.0f, 0.0f));
		// allow to shoot only downwards in a 20° angle
		particleGeom.setMaximumAngle(FastMath.DEG_TO_RAD * 170);
		particleGeom.setMinimumAngle(FastMath.DEG_TO_RAD * 190);
		particleGeom.getParticleController().setSpeed(0.2f);
		particleGeom.setMinimumLifeTime(10.0f);
		particleGeom.setMaximumLifeTime(50.0f);
		particleGeom.setStartSize(0.3f);
		particleGeom.setEndSize(0.1f);
		particleGeom.getParticleController().setControlFlow(false);
		// set the repeat type to looping
		particleGeom.getParticleController().setRepeatType(Controller.RT_CYCLE);
		particleGeom.warmUp(10);
		particleGeom.setInitialVelocity(0.005f);
		// the trail should be a simple white
		particleGeom.setStartColor(ColorRGBA.white.clone());
		particleGeom.setEndColor(ColorRGBA.white.clone());

		// apply alpha, texture and ZBuffer renderstates
		final BlendState bs = r.createBlendState();
		particleGeom.setRenderState(bs);
		bs.setBlendEnabled(true);
		bs.setSourceFunction(BlendState.SourceFunction.SourceAlpha);
		bs.setDestinationFunction(BlendState.DestinationFunction.One);
		bs.setTestEnabled(true);
		bs.setTestFunction(BlendState.TestFunction.GreaterThan);

		TextureState texState = r.createTextureState();
		Texture tex = TextureManager.loadTexture(PATH_SPARK, MinificationFilter.NearestNeighborNoMipMaps, MagnificationFilter.NearestNeighbor);
		texState.setTexture(tex);
		particleGeom.setRenderState(texState);

		final ZBufferState zs = r.createZBufferState();
		particleGeom.setRenderState(zs);
		zs.setWritable(false);
		zs.setEnabled(true);
		particleGeom.updateRenderState();
		return particleGeom;
	}
}
