package com.googlecode.reaxion.game.util;

import java.util.ArrayList;

import com.googlecode.reaxion.game.model.Model;
import com.googlecode.reaxion.game.model.attackobject.AttackObject;
import com.googlecode.reaxion.game.model.character.Character;
import com.googlecode.reaxion.game.state.StageGameState;

public class ListFilter {
	
	public enum Filter {
		/**
		 * Do not filter list.
		 */
		None,
		/**
		 * Filter only {@code Character}s.
		 */
		Character,
		/**
		 * Filter only {@code AttackObject}s.
		 */
		AttackObject;
	}
	
	/**
	 * Checks the provided ArrayList and returns a new list containing only
	 * elements that follow the provided {@code Filter} and excluding the
	 * {@code Model}s provided in the exceptions list.
	 * @param list ArrayList to check
	 * @param filter Filter type to apply
	 * @param exceptions Models to exclude from checking
	 * @return ArrayList of filtered elements
	 */
	public static ArrayList<Model> filter(ArrayList<Model> list, Filter filter, ArrayList<Model> exceptions) {
		if (filter == Filter.None)
			return list;
		
		ArrayList<Model> result = new ArrayList<Model>();
		
		for (int i=0; i<list.size(); i++) {
			Model e = list.get(i);
			if (exceptions != null && !exceptions.contains(e)) {
				if (filter == Filter.Character && e instanceof Character)
					result.add(e);
				else if (filter == Filter.AttackObject && e instanceof AttackObject)
					result.add(e);
			}			
		}
		
		return result;
	}
	
	/**
	 * Checks the models in the provided ArrayList according to the provided
	 * users list and returns a resultant list.
	 * @param list ArrayList to check
	 * @param users List of users of concern
	 * @param keepHostile If true, return non-intersecting models, intersecting
	 * set if otherwise
	 * @return ArrayList of filtered elements
	 */
	public static ArrayList<Model> filterUsers(ArrayList<Model> list, ArrayList<Model> users, boolean keepHostile) {
		ArrayList<Model> result = new ArrayList<Model>();
		
		for (Model c : list) {
			// check if users include the object's users
			for (Model u : users) {
				boolean contains = false;
				if (c.users != null)
					for (Model o : c.users)
						if (u == o) {
							contains = true;
							break;
						}
				if (contains == !keepHostile)
					result.add(c);
			}
		}
		
		return result;
	}
	
	/**
	 * Convenience function to return all {@code Model}s that obey the provided
	 * {@code Filter} that are not the {@code Model} users or user's affiliations.
	 * @param b Current {@code StageGameState}
	 * @param m {@code Model} calling
	 * @param filter {@code Filter} to apply
	 * @return ArrayList of filtered elements
	 */
	public static ArrayList<Model> filterHostile(StageGameState b, Model m, Filter filter) {
		return filterUsers(filter(b.getModels(), filter, m.users), m.users, true);
	}
}
