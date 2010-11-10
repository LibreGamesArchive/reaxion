/**
 * Copyright (c) 2005-2006 JavaGameNetworking
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 *
 * * Redistributions of source code must retain the above copyright
 *   notice, this list of conditions and the following disclaimer.
 *
 * * Redistributions in binary form must reproduce the above copyright
 *   notice, this list of conditions and the following disclaimer in the
 *   documentation and/or other materials provided with the distribution.
 *
 * * Neither the name of 'JavaGameNetworking' nor the names of its contributors 
 *   may be used to endorse or promote products derived from this software 
 *   without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED
 * TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 * Created: Jul 29, 2006
 */
package com.googlecode.reaxion.game.networking;

import com.captiveimagination.jgn.synchronization.*;
import com.captiveimagination.jgn.synchronization.message.*;
import com.googlecode.reaxion.game.model.Model;
import com.googlecode.reaxion.game.networking.sync.message.SynchronizeModelMessage;
import com.jme.scene.*;

/**
 * This is a basic implementation of the GraphicalControler for the
 * jME project.
 * 
 * @author Matthew D. Hicks
 */
public class JMEGraphicalController implements GraphicalController<Spatial> {
    public void applySynchronizationMessage(SynchronizeMessage message, Spatial spatial) {
    	SynchronizeModelMessage m = (SynchronizeModelMessage)message;
        spatial.getLocalTranslation().x = m.getPositionX();
        spatial.getLocalTranslation().y = m.getPositionY();
        spatial.getLocalTranslation().z = m.getPositionZ();
        spatial.getLocalRotation().x = m.getRotationX();
        spatial.getLocalRotation().y = m.getRotationY();
        spatial.getLocalRotation().z = m.getRotationZ();
        spatial.getLocalRotation().w = m.getRotationW();
        spatial.getLocalScale().x = m.getScaleX();
        spatial.getLocalScale().y = m.getScaleY();
        spatial.getLocalScale().z = m.getScaleZ();
    }

    public SynchronizeMessage createSynchronizationMessage(Spatial spatial) {
    	SynchronizeModelMessage message = new SynchronizeModelMessage();
        message.setPositionX(spatial.getLocalTranslation().x);
        message.setPositionY(spatial.getLocalTranslation().y);
        message.setPositionZ(spatial.getLocalTranslation().z);
        message.setRotationX(spatial.getLocalRotation().x);
        message.setRotationY(spatial.getLocalRotation().y);
        message.setRotationZ(spatial.getLocalRotation().z);
        message.setRotationW(spatial.getLocalRotation().w);
        message.setScaleX(spatial.getLocalScale().x);
        message.setScaleY(spatial.getLocalScale().y);
        message.setScaleZ(spatial.getLocalScale().z);
        return message;
    }

    /**
     * This method will always return 1.0f. It is recommended to override this method
     * in games to provide better efficiency to synchronization.
     */
    public float proximity(Spatial spatial, short playerId) {
        return 1.0f;
    }

    /**
     * This method will always return true. It is recommended to override this method
     * in games to provide a layer of security.
     */
    public boolean validateMessage(SynchronizeMessage message, Spatial spatial) {
        return true;
    }

	public boolean validateCreate(SynchronizeCreateMessage message) {
		return true;
	}

	public boolean validateRemove(SynchronizeRemoveMessage message) {
		return true;
	}
}
