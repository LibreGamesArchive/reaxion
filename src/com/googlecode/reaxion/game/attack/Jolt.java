package com.googlecode.reaxion.game.attack;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import com.googlecode.reaxion.game.model.Model;
import com.googlecode.reaxion.game.model.attackobject.MagnetBeam;
import com.googlecode.reaxion.game.model.attackobject.MagnetBomb;
import com.googlecode.reaxion.game.state.StageGameState;
import com.googlecode.reaxion.game.util.LoadingQueue;
import com.jme.math.Vector3f;

public class Jolt extends Attack {
	
	private static final String n = "Jolt";
	private static final int gc = 6;
	
	private ArrayList<Object[]> magnetBombs;
	private MagnetBeam[] beams;
	
	public Jolt(AttackData ad) {
		super(ad, gc);
		name = n;
		validateGround();
	}

	public Jolt() {
		name = n;
		description = "Sends a burst of electric energy which can be channeled through magnet bombs.";
		gaugeCost = gc;
	}

	public static void load() {
		LoadingQueue.push(new Model(MagnetBeam.filename));
	}

	@Override
	public void firstFrame(StageGameState b) {
		character.jumpLock = true;
		character.animationLock = true;
		character.moveLock = true;
		character.tagLock = true;
		character.play("halt", b.tpf);
	}

	@Override
	public void nextFrame(StageGameState b) {
		if (phase == 0) {
			findMagnetBombs(b);
			
			if (magnetBombs != null) {
				if (beams == null) {
					beams = new MagnetBeam[magnetBombs.size()];
					
					for (int i = 0; i < beams.length; i++)
						beams[i] = (MagnetBeam) LoadingQueue.quickLoad(new MagnetBeam(getUsers()), b);
				}
				
				System.out.println("# no. of magnet bombs: " + magnetBombs.size());
				generateMagnetBeams(b);
				b.getRootNode().updateRenderState();
			} else {
				System.out.println("# no magnet bombs found");
			}
			
			if (frameCount >= 40) {
				phase++;
			}
		} else if (phase == 1) {
			finish();
		}	
	}
	
	@Override
	public void finish() {
		super.finish();
		character.jumpLock = false;
		character.animationLock = false;
		character.tagLock = false;
		character.moveLock = false;
	}
	
	private void generateMagnetBeams(StageGameState b) {
		System.out.println("# hello");
		
		if (beams.length != magnetBombs.size()) {
			MagnetBeam[] temp = new MagnetBeam[magnetBombs.size()];
			
			for (int i = 0; i < temp.length; i++)
				temp[i] = beams[i];
			
			beams = temp;
		}
		
		for (int i = 0; i < beams.length; i++) {
			System.out.println("hello again " + i);
			
			Vector3f pos;
			
			if (i == 0)
				pos = character.model.getLocalTranslation();
			else
				pos = ((MagnetBomb) magnetBombs.get(i - 1)[1]).model.getLocalTranslation();
			
			Vector3f targetBomb = ((MagnetBomb) magnetBombs.get(i)[1]).model.getLocalTranslation();
			
			Vector3f hit = pos.add(targetBomb.subtract(pos).mult(new Vector3f(1, 0, 1)).normalize().mult(pos.distance(targetBomb)));
			beams[i].model.setLocalScale(new Vector3f(1, 1, pos.distance(hit)));
			beams[i].model.setLocalTranslation(pos.add(hit).divide(2));
			if (i == 0)
				beams[i].model.setLocalTranslation(beams[i].model.getLocalTranslation().add(0, targetBomb.y + .5f, 0));
			beams[i].rotate(hit.subtract(pos));
			
			System.out.println("# hit: " + hit + " || pos: " + pos);
		}
	}
	
	private void findMagnetBombs(StageGameState b) {
		ArrayList<Object[]> mb = new ArrayList<Object[]>();
		
		for (Model m : b.getModels()) {
			if (m instanceof MagnetBomb) {
				Object[] temp = new Object[2];
				temp[0] = character.model.getLocalTranslation().distance(m.model.getLocalTranslation());
				temp[1] = m;
				mb.add(temp);
			}
		}
		
		if (mb.size() == 0)
			magnetBombs = null;
		else {
			Collections.sort(mb, new Comparator<Object>() {
	        	public int compare(Object one, Object two){
	        		Float first = (Float) ((Object[]) one)[0];
	        		Float secnd = (Float) ((Object[]) two)[0];
	        		return (int) first.compareTo(secnd);
	        	}
	        });
			
			magnetBombs = mb;
		}
	}
	
}
