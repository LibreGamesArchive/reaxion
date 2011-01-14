package com.googlecode.reaxion.tools.listeners;

import com.googlecode.reaxion.tools.events.ValidationEvent;

public interface ValidationEventListener {

	public void fieldFoundValid(ValidationEvent e);
	
	public void fieldFoundInvalid(ValidationEvent e);
	
}
