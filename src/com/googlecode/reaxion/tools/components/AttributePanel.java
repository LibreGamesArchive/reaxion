package com.googlecode.reaxion.tools.components;

import java.awt.GridLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;

import com.googlecode.reaxion.tools.listeners.ValidationEventListener;

public class AttributePanel extends JPanel {
	
	private JLabel descriptor;
	private ToolField field;
	private JLabel depthLabel;
	private ToolField depth;
	
	public AttributePanel(String attribute, boolean hasNumericalData, ValidationEventListener v) {
		super(new GridLayout(2, 2, 5, 5));
		
		descriptor = new JLabel(attribute + ": ");
		field = new ToolField(hasNumericalData ? ToolField.NUMBERS_ONLY : ToolField.TEXT_ONLY);
		field.addValidationEventListener(v);
		
		depthLabel = new JLabel("Depth: ");
		depth = new ToolField(ToolField.NUMBERS_ONLY);
		depth.addValidationEventListener(v);
		
		add(descriptor);
		add(field);
		add(depthLabel);
		add(depth);
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
	
	public boolean hasValidInfo() {
		return field.isValid() && depth.isValid();
	}
	
}
