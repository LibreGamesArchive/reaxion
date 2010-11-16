package com.googlecode.reaxion.test;

import com.jme.app.SimpleGame;
import com.jme.bounding.BoundingSphere;
import com.jme.image.Texture;
import com.jme.input.KeyBindingManager;
import com.jme.input.KeyInput;
import com.jme.math.FastMath;
import com.jme.math.Vector3f;
import com.jme.renderer.ColorRGBA;
import com.jme.scene.Controller;
import com.jme.scene.state.BlendState;
import com.jme.scene.state.TextureState;
import com.jme.scene.state.ZBufferState;
import com.jme.util.TextureManager;
import com.jmex.effects.particles.ParticleController;
import com.jmex.effects.particles.ParticleFactory;
import com.jmex.effects.particles.ParticleInfluence;
import com.jmex.effects.particles.ParticleMesh;
import com.jmex.effects.particles.SimpleParticleInfluenceFactory;

public class TestParticleEffects extends SimpleGame {

        private ParticleMesh pMesh;
        private ParticleInfluence wind;

        public static void main(String[] args) {
                TestParticleEffects app = new TestParticleEffects();
                app.setConfigShowMode(ConfigShowMode.AlwaysShow);
                app.start();
        }


        @Override
		protected void simpleUpdate() {
                KeyBindingManager manager = KeyBindingManager.getKeyBindingManager();
                if(manager.isValidCommand("fireball", false))
                        createFireball();
                else if(manager.isValidCommand("lightball"))
                        createLightball();
        }

        private void createFireball() {
                if(pMesh != null)
                        pMesh.removeFromParent();
                lightState.setEnabled(false);
                BlendState as1 = display.getRenderer().createBlendState();
                as1.setBlendEnabled(true);
                as1.setSourceFunction(BlendState.SourceFunction.SourceAlpha);
                as1.setDestinationFunction(BlendState.DestinationFunction.One);
                as1.setTestEnabled(true);
                as1.setTestFunction(BlendState.TestFunction.GreaterThan);
                as1.setEnabled(true);
                as1.setEnabled(true);
                TextureState ts = display.getRenderer().createTextureState();
                ts.setTexture(
                                TextureManager.loadTexture(
                                                TestParticleEffects.class.getClassLoader().getResource(
                                                "jmetest/data/texture/flaresmall.jpg"),
                                                Texture.MinificationFilter.Trilinear,
                                                Texture.MagnificationFilter.Bilinear));
                ts.setEnabled(true);
                pMesh = ParticleFactory.buildParticles("particles", 75);
                pMesh.setEmissionDirection(new Vector3f(0,1,0));
                pMesh.setInitialVelocity(.008f);
                pMesh.setStartSize(2f);
                pMesh.setEndSize(.5f);
                pMesh.setMinimumLifeTime(100f);
                pMesh.setMaximumLifeTime(400f);
                pMesh.setStartColor(new ColorRGBA(1, 0, 0, 1));
                pMesh.setEndColor(new ColorRGBA(1, 0.5f, 0, 0));
                pMesh.setMaximumAngle(360f * FastMath.DEG_TO_RAD);
                pMesh.getParticleController().setControlFlow(false);
                pMesh.setParticlesInWorldCoords(true);
                pMesh.warmUp(60);
                wind = SimpleParticleInfluenceFactory.createBasicWind(.3f, new Vector3f(-1, 0, 0), true, true);
                wind.setEnabled(true);
                pMesh.addInfluence(wind);
                pMesh.forceRespawn();
                rootNode.setRenderState(ts);
                rootNode.setRenderState(as1);
                ZBufferState zstate = display.getRenderer().createZBufferState();
                zstate.setEnabled(false);
                pMesh.setRenderState(zstate);
                pMesh.setModelBound(new BoundingSphere());
                pMesh.updateModelBound();
                rootNode.attachChild(pMesh);
                rootNode.updateRenderState();
        }

        private void createLightball() {
                if(pMesh != null)
                        pMesh.removeFromParent();
                lightState.setEnabled(false);
                BlendState as1 = display.getRenderer().createBlendState();
                as1.setBlendEnabled(true);
                as1.setSourceFunction(BlendState.SourceFunction.SourceAlpha);
                as1.setDestinationFunction(BlendState.DestinationFunction.One);
                as1.setTestEnabled(true);
                as1.setTestFunction(BlendState.TestFunction.GreaterThan);
                as1.setEnabled(true);
                as1.setEnabled(true);
                TextureState ts = display.getRenderer().createTextureState();
                ts.setTexture(
                                TextureManager.loadTexture(
                                                TestParticleEffects.class.getClassLoader().getResource(
                                                "jmetest/data/texture/flaresmall.jpg"),
                                                Texture.MinificationFilter.Trilinear,
                                                Texture.MagnificationFilter.Bilinear));
                ts.setEnabled(true);
                pMesh = ParticleFactory.buildParticles("particles", 75);
                pMesh.setInitialVelocity(0);
                pMesh.setReleaseRate(0);
                pMesh.setReleaseVariance(0.0f);
                pMesh.setStartSize(2f);
                pMesh.setEndSize(.5f);
                pMesh.setMinimumLifeTime(8000f);
                pMesh.setMaximumLifeTime(10000f);
                pMesh.setStartColor(new ColorRGBA((float) 175/255, 1, 1, 1));
                pMesh.setEndColor(new ColorRGBA(0, 0, 0, 0));
                pMesh.setMaximumAngle(360f * FastMath.DEG_TO_RAD);
                pMesh.getParticleController().setControlFlow(false);
                pMesh.setParticlesInWorldCoords(true);
                pMesh.warmUp(1);
                pMesh.setRepeatType(Controller.RT_CLAMP);
                if(wind != null) {
                        wind.setEnabled(false);
                        pMesh.removeInfluence(wind);
                        wind = null;
                }
                rootNode.setRenderState(ts);
                rootNode.setRenderState(as1);
                ZBufferState zstate = display.getRenderer().createZBufferState();
                zstate.setEnabled(false);
                pMesh.setRenderState(zstate);
                pMesh.setModelBound(new BoundingSphere());
                pMesh.updateModelBound();
                rootNode.attachChild(pMesh);
                rootNode.updateRenderState();
        }

        @Override
		protected void simpleInitGame() {
                display.setTitle("Particle Effects Test");
                KeyBindingManager manager = KeyBindingManager.getKeyBindingManager();
                manager.set("fireball", KeyInput.KEY_1);
                manager.set("lightball", KeyInput.KEY_2);
        }

}