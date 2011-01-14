package com.googlecode.reaxion.tools.events;

import java.util.EventObject;

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
