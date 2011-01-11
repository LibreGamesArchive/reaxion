package com.googlecode.reaxion.tools.components;

import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;

public class ToolButton extends JButton {
	
	public ToolButton(String name) {
		super(name);
		setActionCommand(name);
	}
	
	public ToolButton(String name, ActionListener a) {
		super(name);
		setActionCommand(name);
		addActionListener(a);
	}
	
	public ToolButton(ImageIcon icon, ActionListener a) {
		super(icon);
		setActionCommand(icon.getDescription());
		addActionListener(a);
	}
	
}
