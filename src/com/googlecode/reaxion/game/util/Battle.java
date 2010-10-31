package com.googlecode.reaxion.game.util;

import java.util.ArrayList;

import com.googlecode.reaxion.game.ability.*;
import com.googlecode.reaxion.game.input.ai.*;
import com.googlecode.reaxion.game.model.character.Character;
import com.googlecode.reaxion.game.model.character.MajorCharacter;
import com.googlecode.reaxion.game.model.stage.Stage;
import com.googlecode.reaxion.game.state.BattleGameState;
import com.jme.math.Vector3f;

/**
 * Contains all parameters needed to initiate a battle.
 * @author Brian, Khoa
 */

public class Battle {

	private static final String baseURL = "com.googlecode.reaxion.game.model.character.";
	private static final String attackBaseLocation = "com.googlecode.reaxion.game.attack.";
	private static final String stageClassURL = "com.googlecode.reaxion.game.model.stage.";

	private static Battle currentBattle;

	private MajorCharacter p1, p2;
	private ArrayList<Character> op = new ArrayList<Character>();
	private Class[] p1Attacks, p2Attacks;
	private Ability[] p1Abilities, p2Abilities;
	private Vector3f p1Position, p2Position;
	private ArrayList<Vector3f> opPositions = new ArrayList<Vector3f>();
	private ArrayList<Ability[]> opAbilities = new ArrayList<Ability[]>();
	private Stage stage;	
	private int targetTime, expYield;

	public Battle() {
		testingInit();
	}

	private void testingInit() {
		targetTime = 60;
		expYield = 1000;
		
		p1Attacks = new Class[6];
		p2Attacks = new Class[6];

		try {
			p1Attacks[0] = Class.forName(attackBaseLocation + "ShootBullet");
			p1Attacks[1] = Class.forName(attackBaseLocation + "ShootFireball");
			p1Attacks[2] = Class.forName(attackBaseLocation + "LightningCloud");
			p1Attacks[3] = Class.forName(attackBaseLocation + "LightningStorm");
			p1Attacks[4] = Class.forName(attackBaseLocation + "BlackHole");
			p1Attacks[5] = Class.forName(attackBaseLocation + "ShieldHoly");

			p2Attacks[0] = Class.forName(attackBaseLocation + "Beacon");
			p2Attacks[1] = Class.forName(attackBaseLocation + "BombingMagnet");
			p2Attacks[2] = Class.forName(attackBaseLocation + "BubbleBath");
			p2Attacks[3] = Class.forName(attackBaseLocation + "TriLance");
			p2Attacks[4] = Class.forName(attackBaseLocation + "LanceGuard");
			p2Attacks[5] = Class.forName(attackBaseLocation + "ShadowTag");
		} catch (Exception e) {
			e.printStackTrace();
		}

		p1Position = new Vector3f(0, 0, 20);
		p2Position = p1Position;
		opPositions.add(new Vector3f(0, 0, -20));
		
		p1Abilities = new Ability[] { new MedusaEyes() };
		p2Abilities = new Ability[] { new FinalHour() };
		opAbilities.add(new Ability[] { new AfterImage() });
	}

	public void addOponentPosition(Vector3f position) {
		opPositions.add(position);
	}
	
	public void assignPositions() {
		p1.model.setLocalTranslation(p1Position);
		p2.model.setLocalTranslation(p2Position);
		
		for (int i = 0; i < op.size(); i++)
			op.get(i).model.setLocalTranslation(opPositions.get(i));
	}
	
	public void setPlayers(String[] chars) {
		try {
			Class temp1 = Class.forName(baseURL + chars[0]);
			Class temp2 = Class.forName(baseURL + chars[1]);
			
			// set players
			p1 = (MajorCharacter) LoadingQueue.push((MajorCharacter) temp1.getConstructors()[1].newInstance(false));
			p1.setAbilities(p1Abilities);
			p2 = (MajorCharacter) LoadingQueue.push((MajorCharacter) temp2.getConstructors()[1].newInstance(false));
			p2.setAbilities(p2Abilities);
			
			// set opponents
			op = new ArrayList<Character>();
			for (int i=2; i<chars.length; i++) {
				Class tempO = Class.forName(baseURL + chars[i]);
				op.add((MajorCharacter) LoadingQueue.push((Character) tempO.getConstructors()[0].newInstance()));
				if (opAbilities.size() > i-2)
					op.get(i-2).setAbilities(opAbilities.get(i-2));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		//op.hp = 5;
	}

	public void setStage(String name) {
		try {
			Class cl;
			cl = Class.forName(stageClassURL + name);
			Stage temp = (Stage) cl.getConstructors()[0].newInstance();
			stage = (Stage) LoadingQueue.push(temp);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static Battle getCurrentBattle() {
		if(currentBattle == null)
			currentBattle = new Battle();
		return currentBattle;
	}

	public static void setCurrentBattle(Battle currentBattle) {
		Battle.currentBattle = currentBattle;
	}

	public static BattleGameState createBattleGameState() {
		currentBattle.getOps()[0].assignAI(new TestAI(currentBattle.getOps()[0]));
		Battle b = currentBattle;
		currentBattle = new Battle();
		return new BattleGameState(b);
	}

	public MajorCharacter getP1() {
		return p1;
	}

	public void setP1(MajorCharacter p1) {
		this.p1 = p1;
	}

	public MajorCharacter getP2() {
		return p2;
	}

	public void setP2(MajorCharacter p2) {
		this.p2 = p2;
	}

	public Character[] getOps() {
		return op.toArray(new Character[0]);
	}

	public void setOps(Character[] o) {
		op = new ArrayList<Character>();
		for (int i=0; i<o.length; i++)
			op.add(o[i]);
	}

	public Class[] getP1Attacks() {
		return p1Attacks;
	}

	public void setP1Attacks(Class[] p1Attacks) {
		this.p1Attacks = p1Attacks;
	}

	public Class[] getP2Attacks() {
		return p2Attacks;
	}

	public void setP2Attacks(Class[] p2Attacks) {
		this.p2Attacks = p2Attacks;
	}

	public Ability[] getP1Abilities() {
		return p1Abilities;
	}

	public void setP1Abilities(Ability[] p1Abilities) {
		this.p1Abilities = p1Abilities;
		p1.setAbilities(p1Abilities);
	}

	public Ability[] getP2Abilities() {
		return p2Abilities;
	}

	public void setP2Abilities(Ability[] p2Abilities) {
		this.p2Abilities = p2Abilities;
		p2.setAbilities(p2Abilities);
	}

	public ArrayList<Ability[]> getOpAbilities() {
		return opAbilities;
	}

	public void setOpAbilities(ArrayList<Ability[]> oA) {
		this.opAbilities = oA;
		for (int i=0; i<opAbilities.size(); i++)
		op.get(i).setAbilities(oA.get(i));
	}

	public Vector3f getP1Position() {
		return p1Position;
	}

	public void setP1Position(Vector3f p1Position) {
		this.p1Position = p1Position;
	}

	public Vector3f getP2Position() {
		return p2Position;
	}

	public void setP2Position(Vector3f p2Position) {
		this.p2Position = p2Position;
	}

	public Stage getStage() {
		return stage;
	}

	public void setStage(Stage stage) {
		this.stage = stage;
	}

	public int getTargetTime() {
		return targetTime;
	}

	public void setTargetTime(int targetTime) {
		this.targetTime = targetTime;
	}

	public int getExpYield() {
		return expYield;
	}

	public void setExpYield(int expYield) {
		this.expYield = expYield;
	}

}
