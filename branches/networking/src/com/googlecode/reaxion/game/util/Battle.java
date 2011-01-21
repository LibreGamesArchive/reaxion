package com.googlecode.reaxion.game.util;

import java.util.ArrayList;
import com.googlecode.reaxion.game.ability.*;
import com.googlecode.reaxion.game.model.character.Character;
import com.googlecode.reaxion.game.model.character.MajorCharacter;
import com.googlecode.reaxion.game.model.stage.Stage;
import com.googlecode.reaxion.game.networking.NetworkingObjects;
import com.googlecode.reaxion.game.state.BattleGameState;
import com.googlecode.reaxion.game.state.ClientBattleGameState;
import com.googlecode.reaxion.game.state.HubGameState;
import com.googlecode.reaxion.game.state.ServerBattleGameState;
import com.jme.math.Vector3f;

/**
 * Contains all parameters needed to initiate a battle.
 * 
 * @author Brian, Khoa
 */

public class Battle {

	protected static final String baseURL = "com.googlecode.reaxion.game.model.character.";
	protected static final String attackBaseLocation = "com.googlecode.reaxion.game.attack.";
	protected static final String abilityBaseLocation = "com.googlecode.reaxion.game.ability.";
	protected static final String stageClassURL = "com.googlecode.reaxion.game.model.stage.";

	protected static Battle currentBattle;
	protected static String nextP1s, nextP2s;
	protected static Stage nextStage;

	// TODO: For networking, ugly solution, plz fix
	protected boolean networkedOps;
	protected String nextStageString;
	protected String o1s, o2s;

	public void setPlayers(String[] plays) {
		this.p1s = plays[0];
		this.p2s = plays[1];
		this.o1s = plays[2];
		this.o2s = plays[3];
		networkedOps = true;
	}

	protected String p1s, p2s;
	protected MajorCharacter p1, p2;
	protected ArrayList<MajorCharacter> op = new ArrayList<MajorCharacter>(2);
	protected Class[] p1Attacks, p2Attacks, op1Attacks, op2Attacks;
	protected Ability[] p1Abilities, p2Abilities;
	protected Vector3f playerPosition = new Vector3f(0, 0, 0);
	protected ArrayList<Vector3f> opPositions = new ArrayList<Vector3f>();
	protected ArrayList<Ability[]> opAbilities = new ArrayList<Ability[]>();
	protected Stage stage;
	protected int targetTime, expYield;

	public boolean music = true;

	public Battle() {

	}

	/**
	 * Sets players and stage to Battle's globals.
	 */
	protected void loadDefaults() {
		if (p1s == null)
			p1s = nextP1s;
		if (p2s == null)
			p2s = nextP2s;
		if (stage == null)
			stage = nextStage;
	}

	/**
	 * Load all attacks, abilities, and stage from data.
	 */
	private void init() {
		// Load basics
		try {
			p1 = (MajorCharacter) LoadingQueue.push((MajorCharacter) (Class.forName(baseURL + p1s)
					.getConstructors()[1].newInstance(false)));
			p2 = (MajorCharacter) LoadingQueue.push((MajorCharacter) (Class.forName(baseURL + p2s)
					.getConstructors()[1].newInstance(false)));
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(nextStageString == null)
			LoadingQueue.push(stage);
		else
			loadStage(nextStageString);
		p1Attacks = new Class[6];
		p2Attacks = new Class[6];
		// op1Attacks = new Class[6];
		// op2Attacks = new Class[6];

		try {
			String[] b1 = p1.info.getAbilities();
			p1Abilities = new Ability[b1.length];
			for (int i = 0; i < b1.length; i++)
				p1Abilities[i] = (Ability) Class.forName(abilityBaseLocation + b1[i])
						.getConstructors()[0].newInstance();
			String[] t1 = p1.info.getAttacks();
			for (int i = 0; i < t1.length; i++)
				p1Attacks[i] = Class.forName(attackBaseLocation + t1[i]);

			String[] b2 = p2.info.getAbilities();
			p2Abilities = new Ability[b2.length];
			for (int i = 0; i < b2.length; i++)
				p2Abilities[i] = (Ability) Class.forName(abilityBaseLocation + b2[i])
						.getConstructors()[0].newInstance();
			String[] t2 = p2.info.getAttacks();
			for (int i = 0; i < t2.length; i++) {
				System.out.println(attackBaseLocation + t2[i]);
				p2Attacks[i] = Class.forName(attackBaseLocation + t2[i]);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		if (networkedOps) {
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
			// LoadingQueue.push(stage);
			op1Attacks = new Class[6];
			op2Attacks = new Class[6];

			try {
				// yep hardcoded 2, i'm so forward thinking :3
				for (int i = 0; i < 2; i++) {
					String[] b1 = op.get(i).info.getAbilities();
					opAbilities.add(new Ability[b1.length]);
					for (int j = 0; j < b1.length; j++)
						opAbilities.get(i)[j] = (Ability) Class
								.forName(abilityBaseLocation + b1[j]).getConstructors()[0]
								.newInstance();
				}

				String[] t1 = op.get(0).info.getAttacks();
				for (int j = 0; j < t1.length; j++) {
					// System.out.println(attackBaseLocation + t1[j]);
					op1Attacks[j] = Class.forName(attackBaseLocation + t1[j]);
				}

				t1 = op.get(1).info.getAttacks();
				for (int j = 0; j < t1.length; j++)
					op2Attacks[j] = Class.forName(attackBaseLocation + t1[j]);

			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		// set abilities
		p1.setAbilities(p1Abilities);
		p2.setAbilities(p2Abilities);
		for (int i = 0; i < op.size(); i++) {
			if (opAbilities.size() > i)
				op.get(i).setAbilities(opAbilities.get(i));
		}
	}

	public void addOponentPosition(Vector3f position) {
		opPositions.add(position);
	}

	public void assignPositions() {
		p1.model.setLocalTranslation(playerPosition);
		p2.model.setLocalTranslation(playerPosition);

		for (int i = 0; i < op.size(); i++) {
			Vector3f pos = new Vector3f();
			if (opPositions.size() > i && opPositions.get(i) != null)
				pos = opPositions.get(i);
			op.get(i).model.setLocalTranslation(pos);
		}
	}

	/**
	 * Sets the default players for all {@code Battle} objects.
	 */
	public static void setDefaultPlayers(String dp1, String dp2) {
		// set players
		nextP1s = dp1;
		nextP2s = dp2;
	}

	/**
	 * Sets the default stage for all {@code Battle} objects.
	 */
	public static void setDefaultStage(String name) {
		try {
			Class cl;
			cl = Class.forName(stageClassURL + name);
			nextStage = (Stage) cl.getConstructors()[0].newInstance();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Load the stage from a filename. ew.
	 * @param name
	 */
	protected void loadStage(String name) {
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
		if (currentBattle == null)
			currentBattle = new Battle();
		return currentBattle;
	}

	public static void setCurrentBattle(Battle currentBattle) {
		Battle.currentBattle = currentBattle;
	}

	public static BattleGameState createBattleGameState() {
		Battle b = currentBattle;
		b.loadDefaults();
		b.init();
		currentBattle = new Battle();
		return new BattleGameState(b);
	}

	public static HubGameState createHubGameState() {
		Battle b = getCurrentBattle();
		b.loadDefaults();
		b.init();
		currentBattle = new Battle();
		return new HubGameState(b);
	}

	public static BattleGameState createNetworkedBattleGameState() {
		Battle b = getCurrentBattle();
		currentBattle = new Battle();

		if (NetworkingObjects.isServer) {
			// these are only in here because the client only needs to load the
			// stage, which is already pushed because setStage is called.
			// Everything else is just models sync'd from the server.
			b.loadDefaults();
			b.init();

			ServerBattleGameState sbgs = new ServerBattleGameState(b);

			return sbgs;
		} else {
			System.out.println("Stage:\t" + b.getStage());

			ClientBattle cb = new ClientBattle(b);
			
			ClientBattleGameState QQQ = new ClientBattleGameState(cb);

			NetworkingObjects.cbgs = QQQ;

			return QQQ;
		}
	}

	public String[] getPlayerString(){
		return new String[]{p1s,p2s,o1s,o2s};
	}
	
	public MajorCharacter getP1() {
		return p1;
	}

	public MajorCharacter getP2() {
		return p2;
	}

	public Character[] getOps() {
		return op.toArray(new Character[0]);
	}

	public MajorCharacter getOp1() {
		return op.get(0);
	}

	public MajorCharacter getOp2() {
		return op.get(1);
	}

	public void setOps(Character[] o) {
		op = new ArrayList<MajorCharacter>();
		for (int i = 0; i < o.length; i++)
			op.add((MajorCharacter) o[i]);
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
		for (int i = 0; i < opAbilities.size(); i++)
			op.get(i).setAbilities(oA.get(i));
	}

	public Vector3f getPlayerPosition() {
		return playerPosition;
	}

	public void setPlayerPosition(Vector3f playerPosition) {
		this.playerPosition = playerPosition;
	}

	public Stage getStage() {
		return stage;
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

	public Class[] getOp1Attacks() {
		return op1Attacks;
	}

	public void setOp1Attacks(Class[] op1Attacks) {
		this.op1Attacks = op1Attacks;
	}

	public Class[] getOp2Attacks() {
		return op2Attacks;
	}

	public void setOp2Attacks(Class[] op2Attacks) {
		this.op2Attacks = op2Attacks;
	}

	public void setNextStageString(String nextStageString) {
		this.nextStageString = nextStageString;
	}

	public boolean isClient() {
		return this instanceof ClientBattle;
	}

}
