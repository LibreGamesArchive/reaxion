package com.googlecode.reaxion.game.mission.missions;

import java.awt.Point;

import com.googlecode.reaxion.game.audio.BackgroundMusic;
import com.googlecode.reaxion.game.mission.Mission;
import com.googlecode.reaxion.game.mission.MissionID;
import com.googlecode.reaxion.game.model.character.Character;
import com.googlecode.reaxion.game.model.enemies.HavocSkytank;
import com.googlecode.reaxion.game.state.BattleGameState;
import com.googlecode.reaxion.game.state.DialogueGameState;
import com.googlecode.reaxion.game.util.Actor;
import com.googlecode.reaxion.game.util.Battle;
import com.googlecode.reaxion.game.util.LoadingQueue;
import com.jme.math.Vector3f;

public class VsSkytank extends Mission {
	
	public VsSkytank() {
		super("Imagine Breaker", MissionID.VS_SKYTANK, 2, true, "The quiet calm is shattered by devastation from the sky!", "", new int[] {120, 70});
	}
	
	public void init() {
		Actor[] a = {new Actor(), new Actor(), new Actor()};
		a[0].setPortraits(0, new int[]{0}, new String[]{"skytank.png"});
		a[1].setPortraits(0, new int[]{0}, new String[]{"shine1.png"});
		a[2].setPortraits(0, new int[]{0}, new String[]{"khoa-test.png"});
		
		a[2].setPositions(0, new int[]{0, 32}, new Point[]{new Point(-160, 321), new Point(160, 321)});
		a[1].setPositions(0, new int[]{0, 48}, new Point[]{new Point(-160, 321), new Point(320, 321)});
		a[0].setPositions(0, new int[]{0}, new Point[]{new Point(670, 797)});
		
		a[2].setPositions(4, new int[]{0, 32}, new Point[]{new Point(160, 321), new Point(280, 321)});
		a[1].setPositions(4, new int[]{0, 32}, new Point[]{new Point(320, 321), new Point(70, 321)});
		
		a[0].setPositions(5, new int[]{0, 64}, new Point[]{new Point(670, 797), new Point(600, 460)});
		
		a[2].setPositions(10, new int[]{0, 48}, new Point[]{new Point(280, 321), new Point(300, 321)});
		a[0].setPositions(10, new int[]{0, 48}, new Point[]{new Point(600, 460), new Point(600, 410)});
		
		String[] lines = {"Khoa:: This place... it's so peaceful.",
				"Shine:: Stay alert! Something's not right here...",
				"Khoa:: Huh? What's wrong?",
				"Shine:: I saw something here earlier... something that's not supposed to be here.",
				"Shine:: !!! Up in the sky!",
				"Khoa:: ?! What is that thing?!",
				"Shine:: That's the same thing I saw the other day!",
				"Shine:: Khoa, we have to stop it before it destroys this place!",
				"Khoa:: Are you sure you're up for a fight?",
				"Shine:: ...",
				"Khoa:: Well, ready or not here it comes!"};
		int[] durations = {48, 0, 0, 0, 32, 64, 0, 0, 0, 0, 48};
		
		DialogueGameState dialogueState = new DialogueGameState(lines, durations, a, "bg_flower-field.png");
		dialogueState.setBgm(BackgroundMusic.PACKAGED);
		dialogueState.setStartsBGM(true);
		dialogueState.setEndsBGM(true);
		addState(dialogueState);
		
		Battle b = Battle.getCurrentBattle();
		b.setPlayerPosition(new Vector3f(0, 0, 95));
		Character r = (Character)LoadingQueue.push(new HavocSkytank());
		b.setOps(new Character[] {r});
		b.addOponentPosition(new Vector3f(0, 0, -20));
		b.setStage("FlowerField");
		b.setTargetTime(90);
		b.setExpYield(getExpYield());
		Battle.setCurrentBattle(b);
		
		BattleGameState bgs = Battle.createBattleGameState();
		bgs.setBgm(BackgroundMusic.NIGHT_OF_FATE);
		bgs.setStartsBGM(true);
		bgs.setEndsBGM(false);
		addState(bgs);
	}
	
	public VsSkytank clone() {
		return new VsSkytank();
	}
	
}
