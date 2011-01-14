package com.googlecode.reaxion.tools.filters;

import java.io.File;

public class TextFilter extends ToolFileFilter {

	@Override
	public boolean accept(File f) {
		String ext = getExtension(f);
		return ext != null && ext.equals(".txt");
	}

	@Override
	public String getDescription() {
		return "Burst Grid (*.txt)";
	}

}
