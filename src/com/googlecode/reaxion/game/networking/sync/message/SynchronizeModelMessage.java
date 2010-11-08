package com.googlecode.reaxion.game.networking.sync.message;

import com.captiveimagination.jgn.synchronization.message.SynchronizeMessage;
import com.jme.math.Quaternion;
import com.jme.math.Vector3f;

public class SynchronizeModelMessage extends SynchronizeMessage {
	Vector3f pos, scale;
	Quaternion rot;
}
