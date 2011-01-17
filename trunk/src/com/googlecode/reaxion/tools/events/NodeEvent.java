package com.googlecode.reaxion.tools.events;

import java.util.EventObject;

import com.googlecode.reaxion.tools.vo.EditorNode;

/**
 * {@code NodeEvent} extends the {@code EventObject} class and is fired whenever nodes are modified in 
 * {@code BurstGridPanel} in order to send node information to {@code BurstGridEditor} for GUI updating
 * purposes.
 * 
 * @author Brian Clanton
 *
 */
public class NodeEvent extends EventObject {

	public static final String ADDED = "added", REMOVED = "removed", CREATED = "created", SELECTED = "selected",
		DESELECTED = "deselected", EDITED = "edited";
	
	private EditorNode node;
	private String type;
	
	public NodeEvent(Object source, EditorNode node, String type) {
		super(source);
		this.node = node;
		this.type = type;
	}

	public EditorNode getNode() {
		return node;
	}
	
	public String getType() {
		return type;
	}	

}
