package com.googlecode.reaxion.tools.events;

import java.util.EventObject;

/**
 * Dispatched by {@code ToolField} objects whenever the field is validated.
 * 
 * @author Brian Clanton
 *
 */

public class ValidationEvent extends EventObject {

	public static final String VALID = "valid", INVALID = "invalid";
	
	private String type;
	
	public ValidationEvent(Object source, String type) {
		super(source);
		this.type = type;
	}

	public String getType() {
		return type;
	}
	
}
