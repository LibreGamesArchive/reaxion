package com.googlecode.reaxion.tools.listeners;

import com.googlecode.reaxion.tools.events.NodeEvent;

/**
 * Defines methods for the different {@code NodeEvent} types;
 * 
 * @see NodeEvent
 * 
 * @author Brian Clanton
 *
 */
public interface NodeEventListener {

	/**
	 * Called when an {@code EditorNode} is created and is ready to be positioned on the burst grid.
	 * @param e {@code NodeEvent} containing the created {@code EditorNode}
	 */
	public void nodeCreated(NodeEvent e);
	
	/**
	 * Called when an {@code EditorNode} is added to the burst grid.
	 * @param e {@code NodeEvent} containing the added {@code EditorNode}
	 */
	public void nodeAdded(NodeEvent e);
	
	/**
	 * Called when an {@code EditorNode} is removed from the burst grid.
	 * @param e {@code NodeEvent} containing the removed {@code EditorNode}
	 */
	public void nodeRemoved(NodeEvent e);
	
	/**
	 * Called when an {@code EditorNode} is selected after already being placed
	 * @param e {@code NodeEvent} containing the selected {@code EditorNode}
	 */
	public void nodeSelected(NodeEvent e);
	
	/**
	 * Called when the grid is clicked at a place with no {@code EditorNode} objects, and thus when
	 * the selected node becomes unselected.
	 * @param e {@code NodeEvent} containing the previously selected {@code EditorNode}
	 */
	public void nodeDeselected(NodeEvent e);
	
	/**
	 * Called when attributes of the selected {@code EditorNode} have been edited.
	 * @param e {@code NodeEvent} containing the edited {@code EditorNode}
	 */
	public void nodeEdited(NodeEvent e);
	
}
