package com.googlecode.reaxion.tools.components;

import java.awt.GridLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.googlecode.reaxion.tools.listeners.ValidationEventListener;

/**
 * Contains fields for {@code EditorNode} data.
 * 
 * @author Brian Clanton
 *
 */
public class AttributePanel extends JPanel {
	
	private JLabel descriptor, depthLabel, costsLabel;
	private ToolField field, depth;
	private JTextField costs;
	
	public AttributePanel(String attribute, boolean hasNumericalData, ValidationEventListener v) {
		super(new GridLayout(3, 3, 5, 5));
		
		descriptor = new JLabel(attribute + ": ");
		field = new ToolField(hasNumericalData ? ToolField.NUMBERS_ONLY : ToolField.TEXT_ONLY);
		field.addValidationEventListener(v);
		
		depthLabel = new JLabel("Depth: ");
		depth = new ToolField(ToolField.NUMBERS_ONLY);
		depth.addValidationEventListener(v);
		
		costsLabel = new JLabel("Costs: ");
		costs = new JTextField();
		costs.setEnabled(false);
		
		add(descriptor);
		add(field);
		add(depthLabel);
		add(depth);
		add(costsLabel);
		add(costs);
	}
	
	public String getData() {
		return field.getText();
	}
	
	public int getDepth() {
		return Integer.parseInt(depth.getText());
	}
	
	public void resetFields() {
		field.reset();
		depth.reset();
	}
	
	public void setFields(String data, int z) {
		field.setText(data);
		depth.setText("" + z);
	}
	
	/**
	 * Checks that both fields are valid.
	 * @return {@code true} if both fields are valid, {@code false} if one or both fields are invalid.
	 */
	public boolean hasValidInfo() {
		return field.isValid() && depth.isValid();
	}
	
}
