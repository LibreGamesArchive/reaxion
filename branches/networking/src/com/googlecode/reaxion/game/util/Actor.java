package com.googlecode.reaxion.game.util;

import java.awt.Point;
import java.util.HashMap;

/**
 * Represents a visual object during a dialogue cutscene.
 * Contains portrait and position data as a function of time.
 * @author Khoa Ha
 *
 */
public class Actor {
	
	/**
	 * Maps indices to position sub-times.
	 */
	private HashMap<Integer, int[]> actorPosTimes = new HashMap<Integer, int[]>();
	/**
	 * Maps indices to position points.
	 */
	private HashMap<Integer, Point[]> actorPosPoints = new HashMap<Integer, Point[]>();
	/**
	 * Maps indices to portraits sub-times.
	 */
	private HashMap<Integer, int[]> actorImgTimes = new HashMap<Integer, int[]>();
	/**
	 * Maps indices to portraits strings.
	 */
	private HashMap<Integer, String[]> actorImgFiles = new HashMap<Integer, String[]>();
	
	public Actor() {
		
	}
	
	public void setPositions(int index, int[] times, Point[] positions) {
		actorPosTimes.put(index, times);
		actorPosPoints.put(index, positions);
	}
	
	public void setPortraits(int index, int[] times, String[] portraits) {
		actorImgTimes.put(index, times);
		actorImgFiles.put(index, portraits);
	}
	
	/**
	 * Returns a tweened position for {@code index} at sub-time
	 * {@code time} based on the {@code Actor}'s {@code currentPosition}.
	 * @param index index of text
	 * @param time sub-time for text
	 * @param currentPosition actor's position at an instance less than {@code time}
	 * @return Point representing {@code Actor}'s position 
	 */
	public Point getPosition(int index, int time, Point currentPosition) {
		Point p = currentPosition;
		
		if (actorPosTimes.get(index) != null) {
			// search for the sub-time's index
			int timeInd = -1;
			for (int i=0; i<actorPosTimes.get(index).length; i++) {
				// check if already at sub-time
				if (actorPosTimes.get(index)[i] == time) {
					return actorPosPoints.get(index)[i];
				} else if (actorPosTimes.get(index)[i] > time) {
					timeInd = i;
					break;
				}		
			}
			
			// if time was found
			if (timeInd != -1) {
				p.x += (actorPosPoints.get(index)[timeInd].x - p.x)/(actorPosTimes.get(index)[timeInd] - time);
				p.y += (actorPosPoints.get(index)[timeInd].y - p.y)/(actorPosTimes.get(index)[timeInd] - time);
			}
		}
		return p;
	}
	
	/**
	 * Returns an image string for {@code index} at sub-time {@code time}.
	 * Returns {@code null} if there is no change at that time.
	 * @param index index of text
	 * @param time sub-time for text
	 * @return String representing {@code Actor}'s portrait
	 */
	public String getPortrait(int index, int time) {	
		if (actorImgTimes.get(index) != null) {
			// find the most recent index
			for (int i=0; i<actorImgTimes.get(index).length; i++)
				if (actorImgTimes.get(index)[i] == time)
					return actorImgFiles.get(index)[i];
		}
		return null;
	}
}
