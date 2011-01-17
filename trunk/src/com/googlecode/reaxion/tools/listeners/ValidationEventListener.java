package com.googlecode.reaxion.tools.listeners;

import com.googlecode.reaxion.tools.events.ValidationEvent;

/**
 * Contains methods signatures for each {@code ValidationEvent} type.
 * 
 * @see ValidationEvent
 * 
 * @author Brian Clanton
 *
 */
public interface ValidationEventListener {

	/**
	 * Called when a field is found valid.
	 * 
	 * @param e
	 */
	public void fieldFoundValid(ValidationEvent e);
	
	/**
	 * Called when a field is found invalid.
	 * 
	 * @param e
	 */
	public void fieldFoundInvalid(ValidationEvent e);
	
}
