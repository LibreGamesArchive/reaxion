package com.googlecode.reaxion.game.mission.missions;

import java.awt.Point;

import com.googlecode.reaxion.game.audio.BackgroundMusic;
import com.googlecode.reaxion.game.mission.Mission;
import com.googlecode.reaxion.game.mission.MissionID;
import com.googlecode.reaxion.game.model.character.Character;
import com.googlecode.reaxion.game.model.enemies.Pyroclast;
import com.googlecode.reaxion.game.state.BattleGameState;
import com.googlecode.reaxion.game.state.DialogueGameState;
import com.googlecode.reaxion.game.util.Actor;
import com.googlecode.reaxion.game.util.Battle;
import com.googlecode.reaxion.game.util.LoadingQueue;
import com.jme.math.Vector3f;

public class VsPyroclast extends Mission {
	
	public VsPyroclast() {
		super("Fiery Earth", MissionID.VS_PYROCLAST, 4, true, "An ancient evil has returned with a vengence!", "", new int[] {680, 310});
	}
	
	public void init() {
		Actor[] a = {new Actor(), new Actor(), new Actor()};
		a[0].setPortraits(0, new int[]{0}, new String[]{"pyroclast.png"});
		a[1].setPortraits(0, new int[]{0}, new String[]{"cy-test1.png"});
		
		a[1].setPositions(0, new int[]{0, 32}, new Point[]{new Point(-160, 331), new Point(160, 331)});
		a[0].setPositions(0, new int[]{0}, new Point[]{new Point(1280, 388)});
		
		a[0].setPositions(1, new int[]{0, 48}, new Point[]{new Point(1280, 367), new Point(700, 367)});
		
		String[] lines = {"Cy:: Something doesn't feel quite right...",
				"Cy:: Whoa!",
				"Cy:: This thing seems like it'll put up a strong fight.",
				"Cy:: Better stay on our toes!"};
		int[] durations = {32, 42, 0, 0};
		
		DialogueGameState dialogueState = new DialogueGameState(lines, durations, a, "bg_lava-valley.png");
		dialogueState.setBgm(BackgroundMusic.FIRIING_PREPARATION);
		dialogueState.setStartsBGM(true);
		dialogueState.setEndsBGM(true);
		addState(dialogueState);
		
		Battle b = Battle.getCurrentBattle();
		b.setPlayerPosition(new Vector3f(0, 0, 138));
		Character r = (Character)LoadingQueue.push(new Pyroclast());
		b.setOps(new Character[] {r});
		b.setStage("LavaValley2");
		b.setTargetTime(180);
		b.setExpYield(getExpYield());
		Battle.setCurrentBattle(b);
		
		BattleGameState bgs = Battle.createBattleGameState();
		bgs.setBgm(BackgroundMusic.VIOLATOR);
		bgs.setStartsBGM(true);
		bgs.setEndsBGM(false);
		addState(bgs);
	}
	
	public VsPyroclast clone() {
		return new VsPyroclast();
	}
	
}
