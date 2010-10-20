package com.googlecode.reaxion.game.util;

import com.googlecode.reaxion.game.ability.*;
import com.googlecode.reaxion.game.input.ai.TestAI;
import com.googlecode.reaxion.game.model.character.MajorCharacter;
import com.googlecode.reaxion.game.model.stage.Stage;
import com.googlecode.reaxion.game.state.BattleGameState;

public class Battle {

	private static final String baseURL = "com.googlecode.reaxion.game.model.character.";
	private static final String attackBaseLocation = "com.googlecode.reaxion.game.attack.";
	private static final String stageClassURL = "com.googlecode.reaxion.game.model.stage.";

	private static Battle currentBattle;

	private MajorCharacter p1, p2, op;
	private Class[] p1Attacks, p2Attacks;
	private Ability[] p1Abilities, p2Abilities, opAbilities;
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
			p1Attacks[2] = Class.forName(attackBaseLocation + "ShieldBarrier");
			p1Attacks[1] = Class.forName(attackBaseLocation + "ShootFireball");
			p1Attacks[3] = Class.forName(attackBaseLocation + "ShieldMediguard");
			p1Attacks[4] = Class.forName(attackBaseLocation + "ShieldReflega");
			p1Attacks[5] = Class.forName(attackBaseLocation + "ShieldHoly");

			p2Attacks[0] = Class.forName(attackBaseLocation + "SpawnBubble");
			p2Attacks[1] = Class.forName(attackBaseLocation + "SpinLance");
			p2Attacks[2] = Class.forName(attackBaseLocation + "LanceWheel");
			p2Attacks[3] = Class.forName(attackBaseLocation + "LanceArc");
			p2Attacks[4] = Class.forName(attackBaseLocation + "TriLance");
			p2Attacks[5] = Class.forName(attackBaseLocation + "ShadowTag");
		} catch (Exception e) {
			e.printStackTrace();
		}

		p1Abilities = new Ability[] { new Masochist() };
		p2Abilities = new Ability[] { new RapidGauge() };
		opAbilities = new Ability[] { new AfterImage() };
	}

	public void setPlayers(String[] chars) {
		try {
			Class temp1 = Class.forName(baseURL + chars[0]);
			p1 = (MajorCharacter) temp1.getConstructors()[1].newInstance(false);
			Class temp2 = Class.forName(baseURL + chars[1]);
			p2 = (MajorCharacter) temp2.getConstructors()[1].newInstance(false);
			Class tempO = Class.forName(baseURL + chars[2]);
			op = (MajorCharacter) tempO.getConstructors()[0].newInstance();
		} catch (Exception e) {
			e.printStackTrace();
		}

		p1 = (MajorCharacter) LoadingQueue.push(p1);
		p1.setAbilities(p1Abilities);
		p2 = (MajorCharacter) LoadingQueue.push(p2);
		p2.setAbilities(p2Abilities);
		op = (MajorCharacter) LoadingQueue.push(op);
		op.setAbilities(opAbilities);
		
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
		currentBattle.getOp().assignAI(new TestAI(currentBattle.getOp()));
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

	public MajorCharacter getOp() {
		return op;
	}

	public void setOp(MajorCharacter op) {
		this.op = op;
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

	public Ability[] getOpAbilities() {
		return opAbilities;
	}

	public void setOpAbilities(Ability[] opAbilities) {
		this.opAbilities = opAbilities;
		op.setAbilities(opAbilities);
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
