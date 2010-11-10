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
import com.radakan.jme.mxml.anim.MeshAnimationController;

/**
 * This is a basic implementation of the GraphicalControler for the
 * jME project.
 * 
 * @author Matthew D. Hicks
 */
public class JMEGraphicalController implements GraphicalController<Model> {
    public void applySynchronizationMessage(SynchronizeMessage message, Model model) {
    	SynchronizeModelMessage m = (SynchronizeModelMessage)message;
    	model.model.getLocalTranslation().x = m.getPositionX();
    	model.model.getLocalTranslation().y = m.getPositionY();
    	model.model.getLocalTranslation().z = m.getPositionZ();
    	model.model.getLocalRotation().x = m.getRotationX();
    	model.model.getLocalRotation().y = m.getRotationY();
        model.model.getLocalRotation().z = m.getRotationZ();
        model.model.getLocalRotation().w = m.getRotationW();
        model.model.getLocalScale().x = m.getScaleX();
        model.model.getLocalScale().y = m.getScaleY();
        model.model.getLocalScale().z = m.getScaleZ();
        ((MeshAnimationController) model.model.getController(0)).setAnimation(m.getAnimation());
    }

    public SynchronizeMessage createSynchronizationMessage(Model model) {
    	SynchronizeModelMessage message = new SynchronizeModelMessage();
        message.setPositionX(model.model.getLocalTranslation().x);
        message.setPositionY(model.model.getLocalTranslation().y);
        message.setPositionZ(model.model.getLocalTranslation().z);
        message.setRotationX(model.model.getLocalRotation().x);
        message.setRotationY(model.model.getLocalRotation().y);
        message.setRotationZ(model.model.getLocalRotation().z);
        message.setRotationW(model.model.getLocalRotation().w);
        message.setScaleX(model.model.getLocalScale().x);
        message.setScaleY(model.model.getLocalScale().y);
        message.setScaleZ(model.model.getLocalScale().z);
        message.setAnimation(((MeshAnimationController) model.model.getController(0)).getActiveAnimation());
        return message;
    }

    /**
     * This method will always return 1.0f. It is recommended to override this method
     * in games to provide better efficiency to synchronization.
     */
    public float proximity(Model model, short playerId) {
        return 1.0f;
    }

    /**
     * This method will always return true. It is recommended to override this method
     * in games to provide a layer of security.
     */
    public boolean validateMessage(SynchronizeMessage message, Model model) {
        return true;
    }

	public boolean validateCreate(SynchronizeCreateMessage message) {
		return true;
	}

	public boolean validateRemove(SynchronizeRemoveMessage message) {
		return true;
	}
}
