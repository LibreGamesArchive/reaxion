package com.googlecode.reaxion.tools.events;

import java.util.EventObject;

import com.googlecode.reaxion.tools.vo.EditorNode;

public class NodeEvent extends EventObject {

	public static final String ADDED = "added", REMOVED = "removed", CREATED = "created", SELECTED = "selected";
	
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
