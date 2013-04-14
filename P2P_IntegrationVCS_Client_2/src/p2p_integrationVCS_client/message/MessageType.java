package p2p_integrationVCS_client.message;

import java.util.EnumMap;

/**
 *
 * @author podolak
 */
public enum MessageType {
    // no valid message
    NONE,
    
    // validity test for updating
    IS_UPDATE_VALID,
    UPDATE_IS_VALID,
    
    // commit
    COMMIT,
    COMMIT_SUCCESSFUL,
    RELEASE_LOCK,
    RELEASE_LOCK_RESPONSE,
    
    // errors
    DOCUMENT_VERSION_OUTDATED_ERROR,
    WRONG_NODE_ERROR;
    
    private static EnumMap<MessageType, p2p_vcs_client.message.MessageType> supertypes =
            new EnumMap<MessageType, p2p_vcs_client.message.MessageType>(MessageType.class);
    
    static {
        supertypes.put(NONE, p2p_vcs_client.message.MessageType.NONE);
        
        supertypes.put(IS_UPDATE_VALID, p2p_vcs_client.message.MessageType.IS_UPDATE_VALID);
        supertypes.put(UPDATE_IS_VALID, p2p_vcs_client.message.MessageType.UPDATE_IS_VALID);
        
        supertypes.put(COMMIT, p2p_vcs_client.message.MessageType.COMMIT);
        supertypes.put(COMMIT_SUCCESSFUL, p2p_vcs_client.message.MessageType.COMMIT_SUCCESSFUL);
        
        supertypes.put(DOCUMENT_VERSION_OUTDATED_ERROR, p2p_vcs_client.message.MessageType.DOCUMENT_VERSION_OUTDATED_ERROR);
        supertypes.put(WRONG_NODE_ERROR, p2p_vcs_client.message.MessageType.WRONG_NODE_ERROR);
        
        // all other get NONE
        for (MessageType type : values()) {
            if (type.getSupertype() == null) {
                supertypes.put(type, p2p_vcs_client.message.MessageType.NONE);
            }
        }
    }
    
    private p2p_vcs_client.message.MessageType supertype;
    
    public short getType() {
        return (short)this.ordinal();
    }
    
    public static MessageType fromType(short s) {
        MessageType messageType = MessageType.NONE;
        
        for (MessageType type : values()) {
            if (s == type.ordinal()) {
                messageType = type;
                break;
            }
        }
        
        return messageType;
    }
    
    public p2p_vcs_client.message.MessageType getSupertype() {
        return supertypes.get(this);
    }
}
