package com.googlecode.reaxion.tools.components;

import java.util.ArrayList;
import java.util.Collections;

import com.googlecode.reaxion.tools.events.ValidationEvent;
import com.googlecode.reaxion.tools.vo.EditorNode;

/**
 * Extends {@code ToolField} functionality to allow for validation of the field text to be checked against the ids of 
 * an {@code ArrayList<EditorNode>} to prevent users from creating two nodes with the same id.
 * 
 * @author Brian Clanton
 *
 */

public class NodeIDField extends ToolField {

	private static final String NODE_ID = "node_id";
	
	private ArrayList<EditorNode> nodes;
	
	public NodeIDField(String text) {
		super(text, NODE_ID);
		
		nodes = new ArrayList<EditorNode>();
	}
	
	public void updateNodes(ArrayList<EditorNode> nodes) {
		this.nodes = nodes;
	}
	
	public int getId() {
		return Integer.parseInt(getText());
	}
	
	@Override
	protected void validateField() {
		if (nodes != null) {
			if (getText().equals("")) {
				fireValidationMade(ValidationEvent.INVALID);
			} else {
				Collections.sort(nodes);

				try {
					Integer.parseInt(getText());

					if (nodes.size() == 0) {
						fireValidationMade(ValidationEvent.VALID);
					}

					boolean checked = false;
					
					for (EditorNode n : nodes) {
						if (Integer.parseInt(getText()) == n.getId() && !n.isSelected()) {
							fireValidationMade(ValidationEvent.INVALID);
							checked = true;
							break;
						}
					}
					
					if (!checked)
						fireValidationMade(ValidationEvent.VALID);
					
				} catch (NumberFormatException ex) {
					fireValidationMade(ValidationEvent.INVALID);
				}

			}
		}
	}

}
