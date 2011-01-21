package com.googlecode.reaxion.game.util;

import java.util.ArrayList;

import com.googlecode.reaxion.game.ability.Ability;
import com.googlecode.reaxion.game.model.Model;
import com.googlecode.reaxion.game.model.character.Character;
import com.googlecode.reaxion.game.model.character.MajorCharacter;
import com.jme.math.Vector3f;

public class ClientBattle extends Battle {

	public ClientBattle() {
	}

	public ClientBattle(Battle b) {
		setPlayers(b.getPlayerString());

		clientPreload();
		
		loadStage(b.nextStageString);
	}

	private void clientPreload() {
		try {
			try {
				p1 = (MajorCharacter) LoadingQueue.push((MajorCharacter) (Class.forName(
						baseURL + p1s).getConstructors()[1].newInstance(false)));
				p2 = (MajorCharacter) LoadingQueue.push((MajorCharacter) (Class.forName(
						baseURL + p2s).getConstructors()[1].newInstance(false)));
			} catch (Exception e) {
				e.printStackTrace();
			}

			p1Attacks = new Class[6];
			p2Attacks = new Class[6];
			op1Attacks = new Class[6];
			op2Attacks = new Class[6];

			Thread.sleep(1);
			
			try {

				String[] t1 = p1.info.getAttacks();
				for (int i = 0; i < t1.length; i++)
					p1Attacks[i] = Class.forName(attackBaseLocation + t1[i]);

				String[] t2 = p2.info.getAttacks();
				for (int i = 0; i < t2.length; i++) {
					System.out.println(attackBaseLocation + t2[i]);
					p2Attacks[i] = Class.forName(attackBaseLocation + t2[i]);
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
			
			Thread.sleep(1);
			
			try {
				if (op == null)
					op = new ArrayList<MajorCharacter>(2);
				op.add((MajorCharacter) LoadingQueue.push((MajorCharacter) (Class.forName(
						baseURL + o1s).getConstructors()[1].newInstance(false))));
				op.add((MajorCharacter) LoadingQueue.push((MajorCharacter) (Class.forName(
						baseURL + o2s).getConstructors()[1].newInstance(false))));
			} catch (Exception e) {
				e.printStackTrace();
			}

			Thread.sleep(1);
			
			try {

				String[] t1 = op.get(0).info.getAttacks();
				for (int j = 0; j < t1.length; j++)
					op1Attacks[j] = Class.forName(attackBaseLocation + t1[j]);

				t1 = op.get(1).info.getAttacks();
				for (int j = 0; j < t1.length; j++)
					op2Attacks[j] = Class.forName(attackBaseLocation + t1[j]);

			} catch (Exception e) {
				e.printStackTrace();
			}

			Thread.sleep(1);
			
			// try to preload player characters' attacks
			for (int i = 0; i < p1Attacks.length; i++)
				if (p1Attacks[i] != null)
					p1Attacks[i].getMethod("load").invoke(null);
			Thread.sleep(1);
			for (int i = 0; i < p2Attacks.length; i++)
				if (p2Attacks[i] != null)
					p2Attacks[i].getMethod("load").invoke(null);
			Thread.sleep(1);
			for (int i = 0; i < op1Attacks.length; i++)
				if (op1Attacks[i] != null)
					op1Attacks[i].getMethod("load").invoke(null);
			Thread.sleep(1);
			for (int i = 0; i < op2Attacks.length; i++)
				if (op2Attacks[i] != null)
					op2Attacks[i].getMethod("load").invoke(null);

			// try to preload common resources
			LoadingQueue.push(new Model("glow-ring"));

			// why is this line here...? it makes the state null. that is rather
			// odd.
			// possibly because it preloads into memory? hmm
			LoadingQueue.execute(null);
		} catch (Exception e) {
			System.out.println("Error occured during preloading.");
			e.printStackTrace();
		}
	}

	@Override
	public void assignPositions() {
		return;
	}

	public MajorCharacter getP1() {
		return null;
	}

	public MajorCharacter getP2() {
		return null;
	}

	public Character[] getOps() {
		return null;
	}

	public MajorCharacter getOp1() {
		return null;
	}

	public MajorCharacter getOp2() {
		return null;
	}

	public Class[] getP1Attacks() {
		return null;
	}

	public Class[] getP2Attacks() {
		return null;
	}

	public Ability[] getP1Abilities() {
		return null;
	}

	public Ability[] getP2Abilities() {
		return null;
	}

	public ArrayList<Ability[]> getOpAbilities() {
		return null;
	}

	public Vector3f getPlayerPosition() {
		return null;
	}

	public Class[] getOp1Attacks() {
		return null;
	}

	public Class[] getOp2Attacks() {
		return null;
	}

}
