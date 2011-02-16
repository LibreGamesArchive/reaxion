package com.googlecode.reaxion.tools.filters;

import java.io.File;

public class DialogueEditorFilter extends ToolFileFilter {

	@Override
	public boolean accept(File f) {
		String ext = getExtension(f);
		return ext != null && (f.isDirectory() || ext.equals(".scene"));
	}

	@Override
	public String getDescription() {
		return "Cutcenes (*.scene)";
	}

}
