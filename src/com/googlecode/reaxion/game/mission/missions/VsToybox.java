package com.googlecode.reaxion.game.mission.missions;

import java.awt.Point;
import java.util.ArrayList;

import com.googlecode.reaxion.game.ability.*;
import com.googlecode.reaxion.game.audio.AudioPlayer;
import com.googlecode.reaxion.game.input.ai.TestAI;
import com.googlecode.reaxion.game.mission.Mission;
import com.googlecode.reaxion.game.model.character.Character;
import com.googlecode.reaxion.game.model.character.Khoa;
import com.googlecode.reaxion.game.model.enemies.Toybox;
import com.googlecode.reaxion.game.state.DialogueGameState;
import com.googlecode.reaxion.game.state.StageGameState;
import com.googlecode.reaxion.game.util.Actor;
import com.googlecode.reaxion.game.util.Battle;
import com.googlecode.reaxion.game.util.LoadingQueue;
import com.jme.math.Vector3f;
import com.jmex.game.state.GameState;

public class VsToybox extends Mission {
	
	public VsToybox() {
		super("The Toybox Attacks!", 1, 4, true, "A powerful Animation has appeared on Cloud Nine! Defeat the mysterious opponent!", "");
	}
	
	@Override
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
		
		String[] lines = {"Cy:: It's the end of the line! Your reign of terror ends here!",
				"Andrew:: Fufufufu... Do you really think you've won?",
				"Andrew:: Tell me, have you ever wondered where all the Animations you've fought came from?",
				"Cy:: Animations are just objects that have been brought to life after being infused with Anima.",
				"Andrew:: How very observant of you.",
				"Andrew:: However, what you may not know is that people who have mastered control over their Anima can create Animations out of thin air.",
				"Andrew:: Observe!",
				"Cy:: What-! What is that?!",
				"Andrew:: Now I'd hate to chat and run, but it looks like you've got plenty to deal with at the moment. Ciao!",
				"Cy:: Alright! You're going down!"};
		int[] durations = {32, 32, 0, 0, 0, 0, 0, 48, 0, 48};
		
		DialogueGameState dialogueState = new DialogueGameState(lines, durations, a, "bg_cloud-nine.png");
		addState(dialogueState);
		
		Battle b = new Battle();
		b.music = false;
		b.setP1Position(new Vector3f(0, 0, 50));
		Character tb = (Character)LoadingQueue.push(new Toybox());
		b.setOps(new Character[] {tb});
		b.setStage("CloudNine");
		b.setTargetTime(180);
		b.setExpYield(4000);
		Battle.setCurrentBattle(b);
		
		addState(Battle.createBattleGameState());
		
		// play music
		AudioPlayer.queueBGM("unversed_boss.ogg");
		AudioPlayer.startBGM();
	}
}
