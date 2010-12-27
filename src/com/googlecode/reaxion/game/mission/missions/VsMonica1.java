package com.googlecode.reaxion.game.mission.missions;

import java.awt.Point;
import java.util.ArrayList;

import com.googlecode.reaxion.game.ability.Ability;
import com.googlecode.reaxion.game.ability.EvasiveStart;
import com.googlecode.reaxion.game.input.ai.MonicaAI;
import com.googlecode.reaxion.game.mission.Mission;
import com.googlecode.reaxion.game.mission.MissionID;
import com.googlecode.reaxion.game.model.character.Character;
import com.googlecode.reaxion.game.model.character.Monica;
import com.googlecode.reaxion.game.util.Actor;
import com.googlecode.reaxion.game.util.Battle;
import com.googlecode.reaxion.game.util.LoadingQueue;
import com.jme.math.Vector3f;

public class VsMonica1 extends Mission {
	
	public VsMonica1() {
		super("Ambush!", MissionID.VS_MONICA_1, 1, false, "Take down the gunner!", "", new int[] {130, 80});
	}
	
	public void init() {
		// dialogue code
		int[] times = new int[24];
		for(int i = 0; i < times.length; i++)
			times[i] = i * 20;
		
		Actor[] a = {new Actor(), new Actor()};
		a[0].setPortraits(0, new int[]{0}, new String[]{"khoa-test.png"});
		a[0].setPortraits(2, times, 
				new String[]{"cy-test1.png", "cy-test2.png", "cy-test1.png", "cy-test3.png",
				"cy-test1.png", "cy-test2.png", "cy-test1.png", "cy-test3.png",
				"cy-test1.png", "cy-test2.png", "cy-test1.png", "cy-test3.png",
				"cy-test1.png", "cy-test2.png", "cy-test1.png", "cy-test3.png",
				"cy-test1.png", "cy-test2.png", "cy-test1.png", "cy-test3.png",
				"cy-test1.png", "cy-test2.png", "cy-test1.png", "cy-test3.png"});
		a[1].setPortraits(0, new int[]{0}, new String[]{"monica-test.png"});
		a[0].setPositions(0, new int[]{0}, new Point[]{new Point(160, 323)});
		a[1].setPositions(0, new int[]{0}, new Point[]{new Point(520, 323)});
		a[0].setPositions(2, new int[]{0}, new Point[]{new Point(160, 331)});
		a[1].setPositions(1, new int[]{60, 120}, new Point[]{new Point(480, 323), new Point(520, 323)});
		String[] lines = {"Mickey:: Aw, we don't hate it. It's just kinda... scary. But the world's made of light AND darkness. You can't have one without the other, 'cause darkness is half of everything. Sorta makes ya wonder why we are scared of the dark...",
				"You accept darkness, yet choose to live in the light. So why is it that you loathe us who teeter on the edge of nothing? We who were turned away by both light and dark - never given a choice?",
				"I knew. But I was too stubborn to accept it. It's always the same. I try to wrap my mind around things my heart already knows, only to fail.",
				"Xehanort::Students do take after their teachers. Only a fool would be your apprentice. After all, none of this would have happened without you. YOU are the source of all Heartless. It was your research that inspired me to go further than you ever dared.",
				"Thinking of you, wherever you are. We pray for our sorrows to end, and hope that our hearts will blend. Now I will step forward to realize this wish. And who knows: starting a new journey may not be so hard, or maybe it has already begun. There are many worlds, and they share the same sky -\n one sky, one destiny."};
		//int[] durations = {0, -1, 230, -2, 600};
		int[] durations = {0, 0, 0, 0, 0};
		
		/*
		DialogueGameState dialogueState = new DialogueGameState(lines, durations, a, "bg_twilight-kingdom.png");
		dialogueState.setBgm("mitsu_no_yoake.ogg");
		dialogueState.setStartsBGM(true);
		dialogueState.setEndsBGM(true);
		addState(dialogueState);
		*/
		
		Battle b = Battle.getCurrentBattle();
		Character monica = (Character)LoadingQueue.push(new Monica());
		ArrayList<Ability[]> ab = new ArrayList<Ability[]>();
		//ab.add(new Ability[] {new EvasiveStart()});
		monica.assignAI(new MonicaAI(monica));
		b.setOps(new Character[] {monica});
		//b.setOpAbilities(ab);
		b.setTargetTime(60);
		b.setExpYield(getExpYield());
		b.setPlayerPosition(new Vector3f(0, 0, -20));
		b.addOponentPosition(new Vector3f(0, 0, 20));
		Battle.setCurrentBattle(b);
		
		addState(Battle.createBattleGameState());
	}

	public VsMonica1 clone() {
		return new VsMonica1();
	}	
	
}
