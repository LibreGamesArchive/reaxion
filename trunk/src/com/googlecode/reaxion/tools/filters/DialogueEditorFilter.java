package com.googlecode.reaxion.tools.filters;

import java.io.File;

import javax.swing.filechooser.FileFilter;

public class DialogueEditorFilter extends FileFilter {

	@Override
	public boolean accept(File f) {
		return f.isDirectory() || getExtension(f).equals(".scene");
	}

	@Override
	public String getDescription() {
		return "Cutcenes (*.scene)";
	}
	
	private String getExtension(File f) {
		String name = f.getName();
		
		return name.substring(name.lastIndexOf('.'));
	}

}
