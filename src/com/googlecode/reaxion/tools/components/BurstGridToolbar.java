package com.googlecode.reaxion.tools.components;

import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

import com.googlecode.reaxion.tools.BurstGridEditor;

public class BurstGridToolbar extends JPanel {
	
	private ArrayList<ToolButton> tools;
	private int selectedIndex = 0;
	
	public BurstGridToolbar(ActionListener a) {
		super(new GridLayout(1, BurstGridEditor.nodeTypes.length));
		
		tools = new ArrayList<ToolButton>();
		
		for (int i = 0; i < BurstGridEditor.nodeTypes.length; i++) {
			ImageIcon icon = new ImageIcon(BurstGridEditor.nodeIconDir + BurstGridEditor.nodeTypes[i] + ".png");
			icon.setDescription(BurstGridEditor.nodeTypes[i]);
			
			ToolButton b = new ToolButton(icon, a);
			
			if (i == selectedIndex)
				b.setEnabled(false);
			
			tools.add(b);
			add(tools.get(i));
		}
	}
	
	public void switchSelectedTool(ToolButton b) {
		if (tools.contains(b)) {
			tools.get(selectedIndex).setEnabled(true);
			selectedIndex = tools.indexOf(b);
			tools.get(selectedIndex).setEnabled(false);
		}
	}
	
}
