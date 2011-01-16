package com.googlecode.reaxion.tools.listeners;

import com.googlecode.reaxion.tools.events.NodeEvent;

public interface NodeEventListener {

	/**
	 * Called when an {@code EditorNode} is created and is ready to be positioned on the burst grid.
	 * @param e {@code NodeEvent} containing the created node
	 */
	public void nodeCreated(NodeEvent e);
	
	/**
	 * Called when an {@code EditorNode} is added to the burst grid.
	 * @param e {@code NodeEvent} containing the added node
	 */
	public void nodeAdded(NodeEvent e);
	
	/**
	 * Called when an {@code EditorNode} is removed from the burst grid.
	 * @param e {@code NodeEvent} containing the removed node
	 */
	public void nodeRemoved(NodeEvent e);
	
	/**
	 * Called when an {@code EditorNode} is selected after already being placed
	 * 
	 */
	
	public void nodeSelected(NodeEvent e);
}
