package com.googlecode.reaxion.game.state;

import com.googlecode.reaxion.game.attack.AttackData;
import com.googlecode.reaxion.game.input.PlayerInput;
import com.googlecode.reaxion.game.model.Model;
import com.googlecode.reaxion.game.model.character.Character;
import com.googlecode.reaxion.game.model.character.MajorCharacter;
import com.googlecode.reaxion.game.networking.ClientData;
import com.googlecode.reaxion.game.networking.HudInfoContainer;
import com.googlecode.reaxion.game.networking.NetworkingObjects;
import com.googlecode.reaxion.game.networking.NetworkingObjects.PlayerNum;
import com.googlecode.reaxion.game.util.Battle;
import com.jme.math.Vector3f;

/**
 * {@code ServerGameState} extends {@code BattleGameState} with functionality
 * dedicated to server handling.
 * 
 * @author Khoa
 */
public class ServerBattleGameState extends BattleGameState {

	// deprectate EVERYTHING

	public static final String NAME = "serverBattleGameState";

	public double targetTime = Double.NaN;
	public int expYield = 0;

	// time between final kill and results display
	public int victoryTime = 72;
	public int defeatTime = 72;
	private int resultCount = 0;

	protected PlayerInput opPlayerInput;
	protected MajorCharacter opPlayer;
	protected Class[] opPlayerAttacks;
	protected MajorCharacter opPartner;
	protected Class[] opPartnerAttacks;

	public ServerBattleGameState() {
		super();
	}

	public ServerBattleGameState(Battle b) {
		super(b);
	}

	@Override
	protected void onActivate() {
		// do nothing at friggin all
	}

	@Override
	public void setActive(boolean arg0) {
		super.setActive(arg0);
	}

	@Override
	protected void act() {
		try {
			Thread.sleep(10);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		if (resultCount != 0)
			timing = false;

		// Check winning/losing conditions
		/*
		 * if (player.hp <= 0 && (partner == null || partner.hp <=0)) {
		 * System.out.println("You lose!"); if (resultCount >= defeatTime)
		 * goToGameOver(); else { hideOverlays(); resultCount++; } } else if
		 * (opPlayer.hp <= 0 && (opPartner == null || opPartner.hp <=0)) {
		 * System.out.println("You win!"); if (resultCount >= victoryTime)
		 * goToResults(); else { hideOverlays(); resultCount++; } }
		 */

	}

	@Override
	public void addModel(Model m) {
		models.add(m);
		containerNode.attachChild(m.model);
	}

	@Override
	public boolean removeModel(Model m) {
		NetworkingObjects.serverSyncManager.unregister(m);
		return super.removeModel(m);
	}

	/**
	 * Specifies the tag team for this game state.
	 * 
	 * @param op1
	 *            Character to be designated as the opponent
	 * @param oq1
	 *            Array of the attack classes for the opponent
	 * @param op2
	 *            Character to be designated as the opponent partner
	 * @param oq2
	 *            Array of the attack classes for the opponent partner
	 * @author Khoa
	 * 
	 */
	public void assignOpTeam(MajorCharacter op1, Class[] oq1, MajorCharacter op2, Class[] oq2) {
		opPlayer = op1;
		opPlayerAttacks = oq1;
		opPartner = op2;
		opPartnerAttacks = oq2;
		removeModel(opPartner);
	}

	/**
	 * Specifies the opponent character for this game state.
	 * 
	 * @param op
	 *            Character to be designated as the player
	 * @param oq
	 *            Array of the attack classes for the character
	 * @author Khoa
	 * 
	 */
	public void assignOpPlayer(MajorCharacter op, Class[] oq) {
		opPlayer = op;
		opPlayerAttacks = oq;
		// Create input system
		// playerInput = new PlayerInput(this);
		opponents = new Character[] { opPlayer };
	}

	private void tagSwitch(PlayerNum pn) {
		switch (pn) {
		case P1:
			tagSwitch();
			return;
		case P2:
		default:
			tagOpSwitch();
			return;
		}
	}

	/**
	 * Switches opPlayer with opPartner
	 * 
	 * @author Khoa
	 * 
	 */
	public void tagOpSwitch() {
		if (opPartner != null && opPartner.hp > 0) {
			MajorCharacter p = opPlayer;
			opPlayer = opPartner;
			opPartner = p;
			Class[] a = opPlayerAttacks;
			opPlayerAttacks = opPartnerAttacks;
			opPartnerAttacks = a;
			// Pass attack reference to HUD
			// opponent hudNode.passCharacterInfo(opPlayerAttacks,
			// opPlayer.minGauge);
			// Attach the active character
			addModel(opPlayer);
			// Synchronize position
			opPlayer.model.setLocalTranslation(opPartner.model.getLocalTranslation().clone());
			opPlayer.model.setLocalRotation(opPartner.model.getLocalRotation().clone());
			opPlayer.rotationVector = opPartner.rotationVector;
			opPlayer.gravVel = opPartner.gravVel;
			// Remove the inactive character
			removeModel(opPartner);

			rootNode.updateRenderState();
		}
	}

	protected void checkKeys(ClientData pinput, PlayerNum pn) {
		// KeyBindingManager keyboard =
		// KeyBindingManager.getKeyBindingManager();

		if (pinput.getTagOut())
			tagSwitch(pn);

		MajorCharacter play = getPlayer(pn);

		if (pinput.getAttack1()) {
			if (pinput.getAttackHold())
				executeAttack(3, pn);
			else
				executeAttack(0, pn);
		}
		if (pinput.getAttack2()) {
			if (pinput.getAttackHold())
				executeAttack(4, pn);
			else
				executeAttack(1, pn);
		}
		if (pinput.getAttack3()) {
			if (pinput.getAttackHold())
				executeAttack(5, pn);
			else
				executeAttack(2, pn);
		}

		// assign vector to player
		if (!play.moveLock)
			play.setVelocity(new Vector3f(pinput.getFacingX(), 0, pinput.getFacingZ())
					.mult(play.speed));
		// System.out.println(play + " -- "
		// +pinput.getForthOn()+pinput.getJumpOn()+pinput.getLeftOn());

		final int[] jumpLevels = { 2, 4 };
		int jumpCount = 0;

		// TODO: Check if key inputs are being reused (if sync and opengl
		// threads are not working right, and why should they be?)
		if (pinput.getJumpOn()) {
			if (!play.jumpLock && !play.flinching && play.model.getWorldTranslation().y <= 0) {
				if (!pinput.getJumping()) {
					pinput.setJumping(true);
					jumpCount = jumpLevels[1] - jumpLevels[0];
					play.gravVel = play.jump * jumpLevels[0] / jumpLevels[1];
				}
			} else if (jumpCount > 0) {
				play.gravVel += play.jump / jumpLevels[1];
				jumpCount--;
			}
		} else {
			pinput.setJumping(false);
		}
	}

	private void executeAttack(int ind, PlayerNum pn) {
		MajorCharacter play, part, opp;
		Class[] attacks;

		play = getPlayer(pn);
		part = getPartner(pn);
		opp = getOpPlayer(pn);
		attacks = getPlayerAttacks(pn);
		switch (pn) {
		case P1:
			play = player;
			attacks = playerAttacks;
			part = partner;
			opp = opPlayer;
			break;
		case P2:
		default:
			play = opPlayer;
			attacks = opPlayerAttacks;
			part = opPartner;
			opp = player;
		}

		if (!play.flinching && play.currentAttack == null) {
			try {
				if (attacks[ind] != null) {
					Character[] friends = new Character[1];
					friends[0] = part;
					attacks[ind].getConstructors()[1]
							.newInstance(new AttackData(play, friends, opp));
				}
			} catch (Exception e) {
				System.out.println("Fatal error: Attack array parameter was not an Attack.");
				e.printStackTrace();
			}
		}
	}

	public MajorCharacter getPlayer(PlayerNum pn) {
		switch (pn) {
		case P1:
			return player;
		case P2:
		default:
			return opPlayer;
		}
	}

	public Class[] getPlayerAttacks(PlayerNum pn) {
		switch (pn) {
		case P1:
			return playerAttacks;
		case P2:
		default:
			return opPlayerAttacks;
		}
	}

	public MajorCharacter getPartner(PlayerNum pn) {
		switch (pn) {
		case P1:
			return partner;
		case P2:
		default:
			return opPartner;
		}
	}

	public MajorCharacter getOpPlayer(PlayerNum pn) {
		switch (pn) {
		case P1:
			return opPlayer;
		case P2:
		default:
			return player;
		}
	}

	private MajorCharacter getOpPartner(PlayerNum pn) {
		switch (pn) {
		case P1:
			return opPartner;
		case P2:
		default:
			return partner;
		}
	}

	/**
	 * Assumes P1
	 * 
	 * @return
	 */
	public MajorCharacter getOpPlayer() {
		return getOpPlayer(NetworkingObjects.PlayerNum.P1);
	}

	/**
	 * assumes p1
	 * 
	 * @param opPlayer
	 */
	public void setOpPlayer(MajorCharacter opPlayer) {
		this.opPlayer = opPlayer;
	}

	public Class[] getOpPlayerAttacks(PlayerNum pn) {
		switch (pn) {
		case P1:
			return opPlayerAttacks;
		case P2:
		default:
			return playerAttacks;
		}
	}

	public void setOpPlayerAttacks(Class[] opPlayerAttacks) {
		this.opPlayerAttacks = opPlayerAttacks;
	}

	public MajorCharacter getOpPartner() {
		return getOpPartner(NetworkingObjects.PlayerNum.P1);
	}

	public void setOpPartner(MajorCharacter opPartner) {
		this.opPartner = opPartner;
	}

	public Class[] getOpPartnerAttacks() {
		return opPartnerAttacks;
	}

	public void setOpPartnerAttacks(Class[] opPartnerAttacks) {
		this.opPartnerAttacks = opPartnerAttacks;
	}

	protected void sendHUDInfo(PlayerNum pn) {
		MajorCharacter p1 = getPlayer(pn);
		MajorCharacter p2 = getPartner(pn);
		Model target = getCurrentTarget();
		ClientData cd = getClientData(pn);
		
		HudInfoContainer play = cd.getPartner();
		HudInfoContainer part = cd.getPartner();
		HudInfoContainer targ = cd.getPartner();
		
		play.hp = p1.hp;
		play.maxHp = p1.maxHp;
		play.name = p1.name;
		
		part.hp = p2.hp;
		part.maxHp = p2.maxHp;
		part.name = p2.name;
		
		if(target instanceof Character) {
			Character t = (Character)target;
			targ.hp = t.hp;
			targ.maxHp = t.maxHp;
			targ.name = t.name;
		}
		
		cd.setMinGauge(p1.minGauge);
		cd.setGauge(p1.gauge);
		cd.setGaugecap(p1.maxGauge);
		cd.setPlayerAttacks(getPlayerAttacks(pn));
		cd.setCurrentAttackName(p1.currentAttack.name);
	}

	protected ClientData getClientData(PlayerNum pn) {
		switch (pn) {
		case P1:
			return NetworkingObjects.p1data;
		case P2:
		default:
			return NetworkingObjects.p2data;
		}
	}
}