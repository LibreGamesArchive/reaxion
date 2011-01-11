package com.googlecode.reaxion.tools.components;

import java.awt.event.ActionListener;

import javax.swing.Icon;
import javax.swing.JMenuItem;

public class ToolMenuItem extends JMenuItem {

	public ToolMenuItem(String name) {
		super(name);
		setActionCommand(name);
	}
	
	public ToolMenuItem(String name, ActionListener a) {
		super(name);
		setActionCommand(name);
		addActionListener(a);
	}
	
	public ToolMenuItem(String name, Icon icon, ActionListener a) {
		super(name, icon);
		setActionCommand(name);
		addActionListener(a);
	}
	
}
