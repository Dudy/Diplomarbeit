/*
 * AplicationSharingAbstractMessage.java
 *
 * Created on 08.10.2007, 11:44:47
 *
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package applicationSharing;

import rice.p2p.commonapi.Id;
import rice.p2p.commonapi.Message;
import rice.p2p.commonapi.rawserialization.RawMessage;

/**
 *
 * @author podolak
 */
public abstract class ApplicationSharingAbstractMessage implements RawMessage {
    private Id sender;
    
    public ApplicationSharingAbstractMessage(Id sender) {
        this.sender = sender;
    }
    
    public int getPriority() {
        return Message.LOW_PRIORITY;
    }

    public Id getSender() {
        return sender;
    }

    public void setSender(Id sender) {
        this.sender = sender;
    }
}