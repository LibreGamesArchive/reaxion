package com.googlecode.reaxion.tools.components;

import java.awt.GridLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class AttributePanel extends JPanel {
	
	private JLabel descriptor;
	private JTextField field;
	
	public AttributePanel(String attribute) {
		super(new GridLayout(1, 2));
		descriptor = new JLabel(attribute + ": ");
		field = new JTextField();
		
		add(descriptor);
		add(field);
	}
	
	public String getFieldText() {
		return field.getText();
	}
	
}
