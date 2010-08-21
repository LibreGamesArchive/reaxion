package com.googlecode.reaxion.game;

import java.util.EventListener;

public interface ModelLoadEventListener extends EventListener {
	public void modelLoaded(ModelLoadEvent evt);
}
