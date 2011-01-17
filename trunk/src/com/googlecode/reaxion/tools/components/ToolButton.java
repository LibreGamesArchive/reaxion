package com.googlecode.reaxion.tools.components;

import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;

/**
 * Contains convenient constructors that assign action listeners to the button and set the action command
 * to the button name.
 * 
 * @author Brian Clanton
 *
 */
public class ToolButton extends JButton {
	
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

	@Override
	public void setText(String text) {
		super.setText(text);
		setActionCommand(text);
	}
	
}
