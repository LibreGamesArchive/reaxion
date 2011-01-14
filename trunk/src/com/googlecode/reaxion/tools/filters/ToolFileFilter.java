package com.googlecode.reaxion.tools.filters;

import java.io.File;

import javax.swing.filechooser.FileFilter;

public abstract class ToolFileFilter extends FileFilter {

	@Override
	public boolean accept(File f) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String getDescription() {
		// TODO Auto-generated method stub
		return null;
	}
	
	protected String getExtension(File f) {
		if (f == null)
			return null;
		
		String name = f.getName();
		int index = name.lastIndexOf('.');
		
		if (index == -1)
			return null;
		else
			return name.substring(index);
	}

}
