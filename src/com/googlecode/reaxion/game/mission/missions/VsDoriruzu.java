package com.googlecode.reaxion.game.mission.missions;

import java.awt.Point;

import com.googlecode.reaxion.game.mission.Mission;
import com.googlecode.reaxion.game.mission.MissionID;
import com.googlecode.reaxion.game.model.character.Character;
import com.googlecode.reaxion.game.model.enemies.Doriruzu;
import com.googlecode.reaxion.game.state.BattleGameState;
import com.googlecode.reaxion.game.state.DialogueGameState;
import com.googlecode.reaxion.game.util.Actor;
import com.googlecode.reaxion.game.util.Battle;
import com.googlecode.reaxion.game.util.LoadingQueue;
import com.jme.math.Vector3f;

public class VsDoriruzu extends Mission {
	
	public VsDoriruzu() {
		super("Doriruzu's Terror!", MissionID.VS_DORIRUZU, 4, true, "An animation has kidnapped the shrine maiden and is running rampant in Miko Lake!", "");
	}
	
	public void init() {
		Actor[] a = {new Actor(), new Actor(), new Actor()};
		a[0].setPortraits(0, new int[]{0}, new String[]{"toybox.png"});
		a[1].setPortraits(0, new int[]{0}, new String[]{"cy-test1.png"});
		a[2].setPortraits(0, new int[]{0}, new String[]{"andrew-test1.png"});
		a[2].setPortraits(6, new int[]{0}, new String[]{"andrew-test2.png"});
		a[2].setPortraits(8, new int[]{0}, new String[]{"andrew-test1.png"});
		
		a[1].setPositions(0, new int[]{0, 32}, new Point[]{new Point(-160, 331), new Point(160, 331)});
		a[0].setPositions(0, new int[]{0}, new Point[]{new Point(1060, 383)});
		a[2].setPositions(0, new int[]{0}, new Point[]{new Point(960, 327)});
		
		a[2].setPositions(1, new int[]{0, 32}, new Point[]{new Point(960, 327), new Point(620, 327)});
		a[0].setPositions(7, new int[]{0, 48}, new Point[]{new Point(1060, 383), new Point(600, 383)});
		a[1].setPositions(9, new int[]{48}, new Point[]{new Point(180, 331)});
		a[0].setPositions(9, new int[]{48}, new Point[]{new Point(580, 383)});
		a[2].setPositions(9, new int[]{0, 32}, new Point[]{new Point(620, 327), new Point(960, 327)});
		
		String[] lines = {"Cy:: Alright, fiend! Where is MY PUDDIPUDDIPUDDIPUDDIPUDDI?",
				"Andrew:: PUDDIPUDDIPUDDIPUDDIPUDDI... Do you really think PUDDIPUDDIPUDDIPUDDIPUDDI?",
				"Cy:: Wait, what? ....PUDDIPUDDIPUDDIPUDDIPUDDI",
				"Andrew:: How PUDDIPUDDIPUDDIPUDDIPUDDI.",
				"Andrew:: Yeah, Khoa is a PUDDIPUDDIPUDDIPUDDIPUDDI!",
				"Cy:: PUDDIPUDDIPUDDIPUDDIPUDDI, Khoa really can't write dialog.",
				"Andrew:: NO ME",
				"Cy:: PUDDIPUDDIPUDDIPUDDIPUDDIPUDDIPUDDIPUDDIPUDDIPUDDIPUDDIPUDDIPUDDIPUDDIPUDDIPUDDIPUDDIPUDDIPUDDIPUDDIPUDDIPUDDIPUDDIPUDDIPUDDI",
				"Andrew:: nani?",
				"Cy:: GIGA PUDDIPUDDIPUDDIPUDDIPUDDI" +
				""};
		int[] durations = {32, 32, 0, 0, 0, 0, 0, 48, 0, 48};
		
		DialogueGameState dialogueState = new DialogueGameState(lines, durations, a, "bg_miko-lake.png");
		dialogueState.setBgm("sakura_distortion.ogg");
		dialogueState.setStartsBGM(true);
		dialogueState.setEndsBGM(false);
		addState(dialogueState);
		
		Battle b = Battle.getCurrentBattle();
		b.setPlayerPosition(new Vector3f(0, 0, 250));
		Character tb = (Character)LoadingQueue.push(new Doriruzu());
		b.setOps(new Character[] {tb});
		b.setStage("MikoLake");
		b.setTargetTime(180);
		b.setExpYield(4000);
		Battle.setCurrentBattle(b);
		
		BattleGameState bgs = Battle.createBattleGameState();
		bgs.setStartsBGM(false);
		addState(bgs);
	}
	
	public VsDoriruzu clone() {
		return new VsDoriruzu();
	}
	
}
