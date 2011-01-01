package com.googlecode.reaxion.game.mission.missions;

import java.awt.Point;

import com.googlecode.reaxion.game.audio.BackgroundMusic;
import com.googlecode.reaxion.game.mission.Mission;
import com.googlecode.reaxion.game.mission.MissionID;
import com.googlecode.reaxion.game.model.character.Character;
import com.googlecode.reaxion.game.model.enemies.Remnant;
import com.googlecode.reaxion.game.state.BattleGameState;
import com.googlecode.reaxion.game.state.DialogueGameState;
import com.googlecode.reaxion.game.util.Actor;
import com.googlecode.reaxion.game.util.Battle;
import com.googlecode.reaxion.game.util.LoadingQueue;
import com.jme.math.Vector3f;

public class VsRemnant extends Mission {
	
	public VsRemnant() {
		super("The Last Remnant", MissionID.VS_REMNANT, 4, true, "The lost Anima of the ruined world mount their last attack!", "", new int[] {800, 340});
	}
	
	public void init() {
		Actor[] a = {new Actor(), new Actor(), new Actor()};
		a[0].setPortraits(0, new int[]{0}, new String[]{"remnant.png"});
		a[1].setPortraits(0, new int[]{0}, new String[]{"cy-test1.png"});
		
		a[1].setPositions(0, new int[]{0, 32}, new Point[]{new Point(-160, 331), new Point(160, 331)});
		a[0].setPositions(0, new int[]{0}, new Point[]{new Point(1280, 388)});
		
		a[0].setPositions(4, new int[]{0, 48}, new Point[]{new Point(1280, 388), new Point(600, 388)});
		
		String[] lines = {"Cy:: It looks like this world stretches on forever...",
				"Cy:: How are we supposed to get out of here?",
				"???:: you have intruded upon this world. you will not be allowed to leave.",
				"Cy:: Where'd that come from?!",
				"???:: before this world was overrun by darkness, we inhabited it. even with our world in ruins, our Anima is bound to this place.",
				"???:: now we wander this plane with the sole purpose of avenging our world.",
				"Cy:: Wait! The one who destroyed your world is gone!",
				"???:: when our world fell, time stopped. we lost our humanity - all we have left is our fading Anima. our sole reason for being is to seek revenge.",
				"Cy:: We have to pass through here! We mean no harm to this place!",
				"???:: all we have left to feel is hate. you will bear the full front of our despair!",
				"Cy:: Looks like there's no choice. We have to fight our way through!"};
		int[] durations = {32, 0, 0, 0, 48, 0, 0, 0, 0, 0, 0};
		
		DialogueGameState dialogueState = new DialogueGameState(lines, durations, a, "bg_data-core.png");
		dialogueState.setBgm(BackgroundMusic.ZERO_TAIL);
		dialogueState.setStartsBGM(true);
		dialogueState.setEndsBGM(true);
		addState(dialogueState);
		
		Battle b = Battle.getCurrentBattle();
		b.setPlayerPosition(new Vector3f(0, 0, 100));
		Character r = (Character)LoadingQueue.push(new Remnant());
		b.setOps(new Character[] {r});
		b.addOponentPosition(new Vector3f(0, 0, -300));
		b.setStage("DataCore");
		b.setTargetTime(210);
		b.setExpYield(getExpYield());
		Battle.setCurrentBattle(b);
		
		BattleGameState bgs = Battle.createBattleGameState();
		bgs.setBgm(BackgroundMusic.NEBULA_GRAY);
		bgs.setStartsBGM(true);
		bgs.setEndsBGM(false);
		addState(bgs);
	}
	
	public VsRemnant clone() {
		return new VsRemnant();
	}
	
}
