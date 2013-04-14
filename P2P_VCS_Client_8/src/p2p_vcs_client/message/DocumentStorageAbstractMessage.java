package p2p_vcs_client.message;

import rice.p2p.commonapi.Message;
import rice.p2p.commonapi.NodeHandle;
import rice.p2p.commonapi.rawserialization.RawMessage;

/**
 *
 * @author podolak
 */
public abstract class DocumentStorageAbstractMessage implements RawMessage {
    protected MessageType messageType = MessageType.NONE;
    private NodeHandle sender;
    
    public DocumentStorageAbstractMessage(NodeHandle sender) {
        this.sender = sender;
    }
    
    public int getPriority() {
        return Message.LOW_PRIORITY;
    }

    public NodeHandle getSender() {
        return sender;
    }

    public void setSender(NodeHandle sender) {
        this.sender = sender;
    }
    
    public short getType() {
        return messageType.getType();
    }
    
    public void setType(short type) {
        this.messageType = MessageType.fromType(type);
    }
    
    public MessageType getMessageType() {
        return messageType;
    }
    
    public void setType(MessageType messageType) {
        this.messageType = messageType;
    }
}