package com.googlecode.reaxion.game.model.attackobject;

import com.googlecode.reaxion.game.attack.Attack;
import com.googlecode.reaxion.game.attack.AttackData;
import com.googlecode.reaxion.game.attack.Mirage;
import com.googlecode.reaxion.game.attack.Oblivion;
import com.googlecode.reaxion.game.attack.SheerCold;
import com.googlecode.reaxion.game.attack.ShieldBarrier;
import com.googlecode.reaxion.game.attack.ShieldHoly;
import com.googlecode.reaxion.game.attack.ShieldMediguard;
import com.googlecode.reaxion.game.attack.ShieldReflega;
import com.googlecode.reaxion.game.model.Model;
import com.googlecode.reaxion.game.model.character.Character;
import com.googlecode.reaxion.game.model.enemies.Specter;
import com.googlecode.reaxion.game.state.StageGameState;
import com.googlecode.reaxion.game.util.LoadingQueue;
import com.jme.math.Matrix3f;
import com.jme.math.Plane;
import com.jme.math.Vector3f;
import com.jme.scene.shape.Quad;
import com.jmex.effects.water.WaterRenderPass;

public class Mirror extends AttackObject {
	
	public static final String filename = "bound-sphere";
	protected static final int span = 440;
	protected static final float dpf = 3;
	
	public float growTime = 10;
	
	public Model target;
	private Model[] users;
	private Character user;
	
	private Quad mirror;
	private WaterRenderPass mirrorRenderPass;
	
	private Specter specter;
	
	public Mirror(Model m) {
    	super(filename, dpf, m);
    	flinch = true;
    	lifespan = span;
    	users = new Model[] {m};
    	user = ((Character)m);
    }
	
	public Mirror(Model[] m) {
    	super(filename, dpf, m);
    	flinch = true;
    	lifespan = span;
    	users = m;
    	user = ((Character)m[m.length - 1]);
    }
	
	public void setUpMirror(StageGameState b) {
		// set up mirror
		mirrorRenderPass = new WaterRenderPass(b.getCamera(), 4, false, true);
        mirrorRenderPass.setWaterPlane(new Plane(new Vector3f(0, 0, 1), 0));

        mirror = new Quad("mirror", 1, 1);

        mirrorRenderPass.setWaterEffectOnSpatial(mirror);
        b.getRootNode().attachChild(mirror);
        
        mirror.setLocalTranslation(model.getLocalTranslation().add(0, 3, 0));
        
        mirrorRenderPass.setReflectedScene(b.getContainerNode());
        b.getPassManager().add(mirrorRenderPass);
		b.getRootNode().updateRenderState();
	}
	
	@Override
	public void hit(StageGameState b, Character other) {
		finish(b);
    }
	
	@ Override
    public void act(StageGameState b) {
		// set up glow if not already done
		if (mirror == null)
			setUpMirror(b);
		
		// rotate to face target
		Vector3f toTarget = target.model.getLocalTranslation().subtract(model.getLocalTranslation()).normalize();
		Matrix3f m = new Matrix3f();
    	m.fromStartEndVectors(new Vector3f(0, 1, 0), toTarget);
    	mirror.setLocalRotation(m);
		
    	// grow mirror
		if (lifeCount <= growTime)
			mirror.setLocalScale(3*(float)(lifeCount+1)/(float)(growTime+1));
		else {
			
			// check user's attack
			Attack a;
			if (user != b.getPlayer() && user != b.getPartner())
				a = user.currentAttack;
			else
				// can also mirror teammate's attack
				a = b.getPlayer().currentAttack;
			
			if (a != null) {
				if (a instanceof Mirage || a instanceof Oblivion || a instanceof SheerCold /*|| a instanceof ShieldBarrier ||
						a instanceof ShieldHoly || a instanceof ShieldMediguard || a instanceof ShieldReflega*/) {
					// do nothing
				} else {
					// mimic the attack with a specter
					specter = (Specter)LoadingQueue.quickLoad(new Specter(users, target), b);
					specter.rotate(toTarget.mult(new Vector3f(1, 0, 1)));
					specter.model.setLocalTranslation(model.getLocalTranslation());
					b.getRootNode().updateRenderState();
					
					try {
						AttackData ad = a.getAttackData();
						// create new list of users
						Character[] users = new Character[ad.friends.length+1];
						for(int i = 0; i < ad.friends.length; i++)
							users[i] = ad.friends[i];
						users[users.length-1] = ad.character;
						a.getClass().getConstructors()[1].newInstance(new AttackData(specter, users, ad.target));
					} catch (Exception e) {
						e.printStackTrace();
					}
					
					// break mirror
					shatter(b);
				}
			}
			
		}
		
		// check if a hit by another attack
    	Model[] collisions = getModelCollisions(b);
        for (Model c : collisions) {
        	if (c instanceof AttackObject) {
        		// check if users include the other attack's users
        		boolean flag = false;
    			for (Model u : users) {
    				boolean flag2 = false;
    				for (Model o : c.users)
    					if (u == o) {
    						flag2 = true;
    						break;
    					}
    				if (!flag2)
    					flag = true;
    				break;
    			}
        		// shatter
        		if (flag)
        			shatter(b);
        	}
        }
		
		//check lifespan
        if (lifeCount == lifespan)
        	shatter(b);
        lifeCount++;
    }
	
	private void shatter(StageGameState b) {
		b.getRootNode().detachChild(mirror);
		b.getPassManager().remove(mirrorRenderPass);
		finish(b);
	}
	
}