package com.googlecode.reaxion.tools.components;

import java.awt.GridLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class AttributePanel extends JPanel {
	
	private JLabel descriptor;
	private JTextField field;
	private JLabel depthLabel;
	private JTextField depth;
	
	public AttributePanel(String attribute) {
		super(new GridLayout(2, 2));
		descriptor = new JLabel(attribute + ": ");
		field = new JTextField();
		
		depthLabel = new JLabel("Depth: ");
		depth = new JTextField("1");
		
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
	
}
